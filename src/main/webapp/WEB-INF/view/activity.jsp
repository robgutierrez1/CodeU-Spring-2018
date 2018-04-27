<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.data.Activity" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="codeu.model.store.basic.ActivityStore" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.UUID" %>
<%@page import="java.util.ArrayList" %>

<!-- Get the necessary data that is going to be displayed on activity feed -->
<%
List<Conversation> conversations = (List<Conversation>) request.getAttribute("conversations");
List<User> users = (List<User>) request.getAttribute("users");
List<Message> messages = (List<Message>) request.getAttribute("messages");
List<Activity> activities = (List<Activity>) request.getAttribute("activities");
%>

<%
ArrayList<Conversation> conversationsArray = new ArrayList<Conversation>();
ArrayList<User> usersArray = new ArrayList<User>();
ArrayList<Message> messagesArray = new ArrayList<Message>();
%>

<!DOCTYPE html>
<html>
<head>
  <title>Activity Feed</title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">

  <style>
        #activity {
          background-color: white;
          height: 500px;
          overflow-y: scroll
        }
      </style>
    
      <script>
        // scroll the chat div to the bottom
        function activityChat() {
          var activityDiv = document.getElementById('activity');
          activityDiv.scrollTop = activityDiv.scrollHeight;
        };
      </script>
    </head>
    <body onload="activityChat()">

  <%@ include file="/navbar.html" %>

  <div id="container">
    <h1>Activity Feed</h1>
    
    <hr/>

    <div id="activity">
        <ul>
        <!-- Iterate through message list and get data of every message
            to display on the activity feed -->
        <%
            for(Message message: messages) {
                String author = UserStore.getInstance()
                .getUser(message.getAuthorId()).getName();
                String date = message.getDate(message.getCreationTime());
                UUID ID = message.getConversationId();
                String convo = ConversationStore.getInstance().findTitle(ID);
                messagesArray.add(message);
                //activityStore.addActivity(message);
        %>  
            <li><strong><%= date %>: </strong><%= author %> sent a message in <%= convo %>: 
             "<%= message.getContent() %>"</li>
        <%
            }
        %>
        <!-- Iterate through conversation list and get data of every conversation to display -->
        <%
            for(Conversation conversation: conversations) {
              String author = UserStore.getInstance()
              .getUser(conversation.getOwnerId()).getName();
              String date = conversation.getDate(conversation.getCreationTime());
              String convo = conversation.getTitle();
              conversationsArray.add(conversation);
              //activityStore.addActivity(conversation);
        %>
              <li><strong><%= date %>: </strong><%= author %> created a new conversation: 
              <%= convo %></li>
        <%
          }
        %>
        <!-- Iterate through user list and get data of every user to display -->
        <%
            for(User user: users) {
              String author = user.getName();
              String date = user.getDate(user.getCreationTime());
              usersArray.add(user);
              //activityStore.addActivity(user);
        %>     
              <li><strong><%= date %>: </strong><%= author %> joined!</li>
        <%
          }
        %>


        

      
     

        </ul>
    </div>
  </div>
</body>
</html>
