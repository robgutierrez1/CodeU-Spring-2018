<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>
<%@ page import="codeu.model.store.basic.ConversationStore" %>
<%@ page import="java.time.Instant" %>
<%@ page import="java.util.UUID" %>

<!-- Get the necessary data that is going to be displayed on activity feed -->
<%
List<Conversation> conversations = (List<Conversation>) request.getAttribute("conversations");
List<User> users = (List<User>) request.getAttribute("users");
List<Message> messages = (List<Message>) request.getAttribute("messages");
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
