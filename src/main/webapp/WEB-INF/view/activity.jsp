<%@ page import="java.util.List" %>
<%@ page import="codeu.model.data.Conversation" %>
<%@ page import="codeu.model.data.Message" %>
<%@ page import="codeu.model.data.User" %>

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
    
    <div id="activity">
        <ul>Hello</ul>
        <ul>glorious</ul>
        <ul>Hello</ul>
        <ul>glorious</ul>
        <ul>Hello</ul>
        <ul>glorious</ul>
        <ul>Hello</ul>
        <ul>glorious</ul>
        <ul>Hello</ul>
        <ul>glorious</ul>
        <ul>Hello</ul>
        <ul>glorious</ul>
        <ul>Hello</ul>
        <ul>glorious</ul>
        <ul>Hello</ul>
        <ul>glorious</ul>
        <ul>Hello</ul>
        <ul>glorious</ul>
    </div>
  </div>
</body>
</html>
