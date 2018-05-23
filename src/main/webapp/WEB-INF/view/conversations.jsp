<%--
  Copyright 2017 Google Inc.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--%>
<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>

<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="java.util.UUID" %>
<%@ page import="codeu.model.data.User" %>

<%
User viewer = (User) request.getAttribute("viewer");
%>

<%
String user = (String)request.getSession().getAttribute("user");
UUID userId = UserStore.getInstance().getUserId(user);
%>

<!DOCTYPE html>
<html>
<head>
  <title>Conversations</title>
  <link rel="stylesheet" href="/css/main.css">
    
  <style>
    #red {
      color: mediumvioletred;
      display: inline-block;
    }
  </style>
    
</head>
<body>

 <%@ include file="/navbar.html" %>

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if(request.getSession().getAttribute("user") != null){ %>
      <h1>New Conversation</h1>
      <form action="/conversations" method="POST">
          <div class="form-group">
            <label class="form-control-label">Title:</label>
          <input type="text" name="conversationTitle">
        </div>

        <button type="submit" name="buttonVal" value="create">Create</button>
        <button type="submit" name="buttonVal" value="hidden">Create Hidden Message</button>
      </form>

      <hr/>
    <% } %>

    <%
       if (viewer != null && viewer.getNotify() != null && !viewer.getNotify().isEmpty()){
         for (String message : viewer.getNotify()){
            %><div id = red><%= message %></div><%
         }
       } else{
       %><p>No notifications yet... </p><%                            
       }
       
    %>
    
    <h1>Conversations</h1>

    <%
    List<Conversation> conversations =
      (List<Conversation>) request.getAttribute("conversations");
    if(conversations == null || conversations.isEmpty()){
    %>
      <p>Create a conversation to get started.</p>
    <%
    }
      {
    %>
      <ul class="mdl-list">
    <%
      for(Conversation conversation : conversations) {
        if(conversation.getHidden() == false && (conversation.members).contains(userId)) {
    %>
          <li><a href="/chat/<%= conversation.getTitle() %>">
            <%= conversation.getTitle() %> (member) </a></li>
    <%
        }
        else if(conversation.getHidden() == false && !(conversation.members).contains(userId)) {
    %>
          <li><a href="/chat/<%= conversation.getTitle() %>">
            <%= conversation.getTitle() %></a></li>
    <%
        }
      }
    %>
      </ul>
    <%
    }
    %>

    <h1>Hidden Conversations</h1>
    <p>Only you and members of the conversation can see these</p>

    <%
    List<Conversation> conversations1 =
      (List<Conversation>) request.getAttribute("conversations");
    
    if(conversations1 == null || conversations1.isEmpty()){
    %>
      <p>Create a conversation to get started.</p>
    <%
    }
    else{
    %>
      <ul class="mdl-list">
    <%
      for(Conversation conversation1 : conversations1){
        if(conversation1.getHidden() == true){
          if((conversation1.members).contains(userId)) {
    %>
          <li><a href="/chat/<%= conversation1.getTitle() %>">
            <%= conversation1.getTitle() %></a></li>
    <%
          }
        }
      }
    %>
      </ul>
    <%
    }
    %>
    <hr/>
  </div>
</body>
</html>
