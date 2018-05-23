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
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%
User user = (User) request.getAttribute("user");
User viewer = (User) request.getAttribute("viewer");
Boolean editAboutMe = (Boolean) request.getAttribute("editAboutMe");
Boolean updateAboutMe = (Boolean) request.getAttribute("updateAboutMe");
List<Message> userMessages = (List<Message>) request.getAttribute("messages");
BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
%>

<!DOCTYPE html>
<html>
<head>
  <title><%= user.getName() %></title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">
  <style>
    #about {
      /*height: 150px;
      width: 250px;*/
    }
  </style>

</head>
<body>
  <%@ include file="/navbar.html" %>
      
  <div id="container">
    <div
      style="width:75%; margin-left:auto; margin-right:auto; margin-top: 50px;">

      <h1><%= user.getName() %>'s Profile Page</h1>
      
      <% if (user.getImageUrl() != null && (!user.getImageUrl().equals(""))) { %>
      	 <img src="<%= user.getImageUrl() %>" alt="profile image" width="500" height="377"> 
      <% } else { %>
      	 <p>Profile picture not set.</p>
      <% } %>
      
      <% if (user.equals(viewer)) { %>
      	<form action="<%= blobstoreService.createUploadUrl("/upload") %>" method="post" enctype="multipart/form-data">
      		<input type="hidden" name="username" value="<%= user.getName() %>">
      		<input type="file" name="myFile">
        	<input type="submit" value="Submit">
      	</form>
      <% } else { %>
            <!-- nothing -->
      <% } %>

      <h2>My profile :) </h2>

      <div>
          <p><%= user.getAboutMe() %></p>
          <% if (user.equals(viewer)) { %>
              <form action="/user/<%= user.getName() %>" method="POST">
                <br/>
                <% if (!editAboutMe) { %>
                    <button type="submit" name = "buttonVal" value = "edit">Edit</button>
                <% } else { %>
                    <textarea name = "enteredAboutMe" rows="4" cols="50" 
                    placeholder="Please enter your new About Me."></textarea>
                    <br/>
                    <button type="submit" name = "buttonVal" value = "cancel">Cancel</button>
                    <button type="submit" name = "buttonVal" value = "submit">Submit</button>
                <% } %>
            </form>
            <% } else { %>
              <!-- nothing -->
            <% } %>
      </div>
      
      <h2>Past messages: </h2>
      <div
      style="overflow: auto; height: 300px; background-color: white;">

      <ul style="margin: 10px;">
      
      <% int i = 0; 
         for (Message message: userMessages) { 
            if (message.getOpenToPublic()){%>
            
                    <%
                    if (message.getType() == null){
    				%>
      					<!-- nothing -->
    				<%
      				} else if (message.getType().equals("image")) {
    				%>
    					<li><b><%= message.getCreationTime()%></b> :  
    					<img src="<%= message.getContent() %>" alt="profile image" width=50% height=50%> </li>
    		
    				<%  
        			} else if (message.getType().equals("text")){ 
    				%>
    					<li><b><%= message.getCreationTime()%></b> : 
    					</strong> <%= message.getContent() %></li>
    				<%  
        			} else if (message.getType().equals("default")){ 
    				%>
    					<!-- nothing -->
    				<%
    				}
                   	%>
                   	 
                <% if (user.equals(viewer)) { %>
                        <form action="/user/<%= user.getName() %>" method="POST">
                        <button type="submit" name = "buttonVal" value = "hide<%=i%>">Hide</button>
                <% } %>
                    </li>
        <% } else {
                if (user.equals(viewer)) { %>
                        <form action="/user/<%= user.getName() %>" method="POST">
                        <li><button type="submit" name = "buttonVal" value = "show<%=i%>">show the hidden message</button></li>
                <% }
           }
           i++;
                
        }%>
        </ul>
        
      </div>
    </div>
  </div>
  

</body>
</html>
