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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import java.util.ArrayList;

/** An item on a comments list. */
public final class CommentsManager {
  private static DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

  public void storeComment(Comment comment) {
    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("comment", comment.getComment());
    commentEntity.setProperty("timestamp", comment.getTimeStamp());
    datastore.put(commentEntity);
  }

  public ArrayList<Comment> getStoredComments() {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    PreparedQuery results = datastore.prepare(query);
    ArrayList<Comment> comments = new ArrayList<>();

    for (Entity entity : results.asIterable()) {
      long id = entity.getKey().getId();
      String comment = (String) entity.getProperty("comment");
      Comment commentObj = new Comment(comment);
      comments.add(commentObj);
    }
    return comments;
  }
}
