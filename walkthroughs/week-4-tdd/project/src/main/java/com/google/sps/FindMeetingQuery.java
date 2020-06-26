// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Set;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return new ArrayList<>();
    } 
    
    PriorityQueue<TimeRange> busyTimeRanges = getBusyTimeRanges(events, request);
    ArrayList<TimeRange> combinedBusyTimeRanges = getCombinedBusyTimeRanges(busyTimeRanges);
    Collection<TimeRange> result = getAvailableTimeRanges(combinedBusyTimeRanges, request);
    return result;
  }

  private boolean hasCommonAttendees(Collection<String> requestAttendees, Collection<String> eventAttendees) {
    return !Collections.disjoint(requestAttendees, eventAttendees);
  }
  
  private PriorityQueue<TimeRange> getBusyTimeRanges(Collection<Event> events, MeetingRequest request) {
    PriorityQueue<TimeRange> busyTimeRanges = new PriorityQueue<>(TimeRange.ORDER_BY_START);
    Collection<String> requestAttendees = request.getAttendees();

    for (Event event : events) {
        Collection<String> eventAttendees = event.getAttendees();
      if (hasCommonAttendees(requestAttendees, eventAttendees)) {
        TimeRange eventTimeRange = event.getWhen();
        busyTimeRanges.add(eventTimeRange); 
      }
    }

    return busyTimeRanges;
  }

  private ArrayList<TimeRange> getCombinedBusyTimeRanges(PriorityQueue<TimeRange> busyTimeRanges) {
    ArrayList<TimeRange> combinedBusyTimeRanges = new ArrayList<>();

    while (!busyTimeRanges.isEmpty()) {
      TimeRange busyTimeRange = busyTimeRanges.poll();
      int startTime = busyTimeRange.start();
      int endTime = busyTimeRange.end();
      
      while (!busyTimeRanges.isEmpty() && busyTimeRanges.peek().overlaps(busyTimeRange)) {
        TimeRange nextOverlappingBusyTimeRange = busyTimeRanges.poll();
        endTime = Math.max(endTime, nextOverlappingBusyTimeRange.end());
      }
      
      TimeRange newTimeRange = TimeRange.fromStartEnd(startTime, endTime, false);
      combinedBusyTimeRanges.add(newTimeRange);
    }

    return combinedBusyTimeRanges;
  }

  private void processNewTimeRange(ArrayList<TimeRange> availableTimeRanges, TimeRange newTimeRange, MeetingRequest request) {
    if (newTimeRange.duration() >= request.getDuration()) {
      availableTimeRanges.add(newTimeRange);
    }
  }

  private Collection<TimeRange> getAvailableTimeRanges(ArrayList<TimeRange> combinedBusyTimeRanges, MeetingRequest request) {
    ArrayList<TimeRange> availableTimeRanges = new ArrayList<>();
    
    if (combinedBusyTimeRanges.size() == 0) {
      availableTimeRanges.add(TimeRange.WHOLE_DAY);
      return availableTimeRanges;
    }
    
    int endOfLastBusyTimeRange = TimeRange.START_OF_DAY;

    for (TimeRange busyTimeRange : combinedBusyTimeRanges) {
      int start = busyTimeRange.start();
      TimeRange newTimeRange = TimeRange.fromStartEnd(endOfLastBusyTimeRange, start, false);
      processNewTimeRange(availableTimeRanges, newTimeRange, request);
      endOfLastBusyTimeRange = busyTimeRange.end();
    }

    TimeRange newTimeRange = TimeRange.fromStartEnd(endOfLastBusyTimeRange, TimeRange.END_OF_DAY, true);
    processNewTimeRange(availableTimeRanges, newTimeRange, request);
    return availableTimeRanges;
  }
}
