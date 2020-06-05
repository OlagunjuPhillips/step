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

package com.google.sps.servlets;


import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Task;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.google.sps.data.Task;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  static final ArrayList<String> comments = new ArrayList<>();
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Task").addSort("timestamp", SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);

    List<Task> tasks = new ArrayList<>();
    for (Entity entity : results.asIterable()) {
        long id = entity.getKey().getId();
        if(entity.getProperty("comment") instanceof String){
             String comment = (String) entity.getProperty("comment");
        }
        if(entity.getProperty("timestamp") instanceof long){
            long timestamp = (long) entity.getProperty("timestamp");
        }
        
        Task task = new Task(id, comment, timestamp);
        tasks.add(task);
    }

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(tasks));
  }
  private String convertToJsonUsingGson(ArrayList<String> comments){
    String commentsJsonString = gson.toJson(comments);
    return commentsJsonString;
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = request.getParameter("comment");
    long timeStamp = System.currentTimeMillis();

    if(text.length() != 0) {
        comments.add(text);
    }

    Entity taskEntity = new Entity("Task");
    taskEntity.setProperty("comment", text);
    taskEntity.setProperty("timestamp", timeStamp);

    datastore.put(taskEntity);
    
    response.sendRedirect("/gallery.html");

    
  }
    


  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}
