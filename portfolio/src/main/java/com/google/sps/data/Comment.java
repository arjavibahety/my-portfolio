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

package com.google.sps.data;

/** An item on a comments list. */
public final class Comment {
  private final long ID;
  private final String COMMENT;
  private final long TIMESTAMP;
  private static long commentId = 0;

  public Comment(String comment) {
    this.ID = commentId;
    this.COMMENT = comment;
    this.TIMESTAMP = System.currentTimeMillis();
    commentId++;
  }

  public String getComment() {
      return COMMENT;
  }

  public long getTimeStamp() {
      return TIMESTAMP;
  }
}
