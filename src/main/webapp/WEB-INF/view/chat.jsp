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
<%@ page import="java.util.ArrayList" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%
User viewer = (User) request.getAttribute("viewer");
Conversation conversation = (Conversation) request.getAttribute("conversation");
List<Message> messages = (List<Message>) request.getAttribute("messages");
BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= conversation.getTitle() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <style>
    #chat {
      background-color: white;
      height: 500px;
      overflow-y: scroll
    }
    #orange {
      background-color: orange; 
      display: inline;
    }
  </style>

  <script>
    // scroll the chat div to the bottom
    function scrollChat() {
      var chatDiv = document.getElementById('chat');
      chatDiv.scrollTop = chatDiv.scrollHeight;
    };
  </script>
</head>
<body onload="scrollChat()">

  <%@ include file="/navbar.html" %>

  <div id="container">
      
    <%
       if (viewer != null && viewer.getNotify() != null && !viewer.getNotify().isEmpty()){
       %><p>You got a notification! </p><%
       } else{
       %><p>No notifications yet... </p><%                            
       }
       
    %>

    <h1><%= conversation.getTitle() %>
      <a href="" style="float: right">&#8635;</a></h1>

    <hr/>

    <div id="chat">
      <ul>
    <%
      // go through first time to see what uses
      ArrayList<String> texters = new ArrayList<String>();
      for (Message message : messages) {
         String author = UserStore.getInstance()
          .getUser(message.getAuthorId()).getName();
         if (!texters.contains(author)){
            texters.add(author);
          }
      }
    
      for (Message message : messages) {
        String author = UserStore.getInstance()
          .getUser(message.getAuthorId()).getName();
        if (message.getType() == null) {
    %>
      		<!-- nothing -->
    <%
      	} else if (message.getType().equals("image")) {
    %>
    		<li><strong><%= author %>:</strong> 
    		<img src="<%= message.getContent() %>" alt="profile image" width=50% height=50%> </li>
    <%  
        } else if (message.getType().equals("text")) { 
    %>
        <li><strong><%= author %>:</strong> 
    
    <%
        String rendered = message.getContent();                 
        String[] breakdown = rendered.split("@");
        if (breakdown != null && breakdown.length > 1){
    %>
        <%= breakdown[0] %>
    <% 
        for (int i = 1; i < breakdown.length; i++) {
          String[] atItem = breakdown[i].split(" ", 2);
          if (texters.contains(atItem[0])) {
    %> 
          <span id="orange">@<%= atItem[0] %></span>
      <%  } else { %>
            @<%= atItem[0] %>  
      <%  }   
          if (atItem.length >= 2) { 
      %>
            <%= atItem[1] %>
      <%
          }
        }                                   
      %>
        </li>
        <%
          } else { %>
            <%= rendered %>
        <% } 
        } else if (message.getType().equals("default")) {

        }
      }
    %><!-- nothing -->
      </ul>
    </div>

    <hr/>

    <% if (request.getSession().getAttribute("user") != null) { %>
    <form action="/chat/<%= conversation.getTitle() %>" method="POST">
        <input type="text" name="message">
        <br/>
        <button type="submit">Send</button>
    </form>
    <form action="<%= blobstoreService.createUploadUrl("/messageUpload") %>" method="post" enctype="multipart/form-data">
      		<input type="hidden" name="conversationTitle" value="<%= conversation.getTitle() %>">
      		<input type="file" name="myFile">
        	<input type="submit" value="Submit">
    </form>
    <% } else { %>
      <p><a href="/login">Login</a> to send a message.</p>
    <% } %>

    <hr/>

  </div>

</body>
</html>
