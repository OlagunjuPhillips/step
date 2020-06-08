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
import com.google.appengine.api.datastore.FetchOptions;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Task").addSort("timestamp", SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);
    int parameterValue = getNumberOfComments(request);


    List<Task> tasks = new ArrayList<>();

    List<Entity> comments = results.asList(FetchOptions.Builder.withLimit(parameterValue));

    for (Entity entity : comments) {
        long id = entity.getKey().getId();
        String comment = "";
        if(entity.getProperty("comment") instanceof String){
            comment = (String) entity.getProperty("comment");
        } else {
            System.err.println("Can't load comments");
        }

        long timestamp = 0;
        if(entity.getProperty("timestamp") instanceof Long){
            timestamp = (long) entity.getProperty("timestamp");
        } else {
            System.err.println("Can't load comments");
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
    

    Entity taskEntity = new Entity("Task");
    taskEntity.setProperty("comment", text);
    taskEntity.setProperty("timestamp", timeStamp);
    

    datastore.put(taskEntity);
    
    response.sendRedirect("/index.html");

    
  }

  private int getNumberOfComments(HttpServletRequest request){
    String parameterValue = request.getParameter("parameterValue");
    System.out.println(parameterValue);

    int parameter = 0;
      
    try {
        parameter = Integer.parseInt(parameterValue);
    } catch (NumberFormatException e){
        System.err.println("Can't convert to number");
    }
    return parameter;
  }


  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    if (value == null) {
      return defaultValue;
    }
    return value;
  }
}