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
import com.google.sps.data.Comment;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import com.google.sps.data.Comment;
import com.google.appengine.api.datastore.FetchOptions;


/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  Gson gson = new Gson();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query query = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);

    PreparedQuery results = datastore.prepare(query);
    int parameterValue = getNumberOfComments(request);


    List<Comment> comments = new ArrayList<>();

    List<Entity> commentsList = results.asList(FetchOptions.Builder.withLimit(parameterValue));

    for (Entity entity : commentsList) {
        long id = entity.getKey().getId();
        String comment = "";
        if(entity.getProperty("comment") instanceof String){
            comment = (String) entity.getProperty("comment");
        } else {
            System.err.println("Can't load comments");
        }

        long timestamp = 0;
        if(entity.getProperty("timestamp") instanceof Long){
            timestamp = (Long) entity.getProperty("timestamp");
        } else {
            System.err.println("Can't load comments");
        }
        
        Comment commentTask = new Comment(id, comment, timestamp);
        comments.add(commentTask);
    }

    response.setContentType("application/json;");
    response.getWriter().println(gson.toJson(comments));
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String text = request.getParameter("comment");
    long timeStamp = System.currentTimeMillis();
    
    Entity commentEntity = new Entity("Comment");

    commentEntity.setProperty("comment", text);
    commentEntity.setProperty("timestamp", timeStamp);
    
    datastore.put(commentEntity);

    response.sendRedirect("/index.html"); 
  }

  private int getNumberOfComments(HttpServletRequest request){
    String parameterValue = request.getParameter("parameterValue");
    int parameter = 0;
      
    try {
        parameter = Integer.parseInt(parameterValue);
    } catch (NumberFormatException e){
        System.err.println("Can't convert to number");
        System.out.println(parameterValue);
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

  private String convertToJsonUsingGson(ArrayList<String> comments){
    String commentsJsonString = gson.toJson(comments);
    return commentsJsonString;
  }
}

  