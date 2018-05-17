<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Activity" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="codeu.model.store.basic.ActivityStore" %>
<%@ page import="codeu.model.store.basic.MessageStore" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.util.ArrayList" %>

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

<% 
String conversationTitle = (String)request.getAttribute("conversationTitle");
String chatURL = (String)request.getAttribute("chatURL");
Conversation conversation = ConversationStore.getInstance().getConversationWithTitle(conversationTitle);
%>
<!DOCTYPE html>
<html>
<head>
  <title>Access</title>
  <link rel="stylesheet" href="/css/main.css">
</head>
<body>

  <%@ include file="/navbar.html" %>

  <div id="container">

    <% if(request.getAttribute("error") != null){ %>
        <h2 style="color:red"><%= request.getAttribute("error") %></h2>
    <% } %>

    <% if(request.getSession().getAttribute("user") != null){ %>
      <h1>Add Members</h1>
      <form action="/access/<%= conversation.getTitle() %>" method="POST">
          <div class="form-group">
            <label class="form-control-label">Add a user to your conversation:</label>
          <input type="text" name="userToAdd">
        </div>

        <button type="submit" name = "buttonVal" value = "add">Add user</button>
        <button type="submit" name = "buttonVal" value = "chat">Go to Chat</button>
      </form>

      <hr/>
    <% } %>

    <h1>Current Members</h1>

      <%
      List<UUID> members =
        (List<UUID>) request.getAttribute("members");
      if(members == null || members.isEmpty()){
      %>
        <p>Add some members to your conversation.</p>
      <%
      }
      else{
      %>
        <ul class="mdl-list">
      <%
        for(UUID member : members){
          String memberUsername = UserStore.getInstance().getUser(member).getName();
      %>
          <li><a><%= memberUsername %></a></li>
      <%
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
