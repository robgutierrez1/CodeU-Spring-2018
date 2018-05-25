<%@ page import="java.util.ArrayList" %>
<%@ page import="codeu.model.data.User" %>
<%@ page import="codeu.model.store.basic.UserStore" %>

<%
    ArrayList<String> friends = (ArrayList<String>) request.getAttribute("friends");
    ArrayList<String> requests = (ArrayList<String>) request.getAttribute("requests");
%>

<!DOCTYPE html>
<html>
<head>
  <title>Friends</title>
  <link rel="stylesheet" href="/css/main.css" type="text/css">
  <style>
        #friend {
          background-color: white;
          height: 500px;
          overflow-y: scroll
        }
  </style>
    
    <script>
      // scroll the chat div to the bottom
      function friendDisplay() {
        var friendDiv = document.getElementById('friend');
        friendDiv.scrollTop = friendDiv.scrollHeight;
      };
    </script>
</head>
<body onload="friendDisplay()">

    <%@ include file="/navbar.html" %>

    <div id="container">
        <h1>Friends</h1>

        <% if(request.getAttribute("error") != null){ %>
            <h2 style="color:red"><%= request.getAttribute("error") %></h2>
        <% } %>
        
        <!-- Friend Field -->
        <% if (request.getSession().getAttribute("user") != null) { %>
        <h3>Add a friend!</h3>
        <form action="/friend" method="POST">
            <label for="username">Username: </label>
            <input type="text" name="username" id="username">
            <br/>
            <button type="submit" name="add">Add Friend</button>
        </form>

        <h3>Accept a friend request!</h3>
        <form action="/friend" method="POST">
            <label for="request_accept">Accept Friend Request: </label>
            <input type="text" name="request_accept" id="request_accept">
            <br/>
            <button type="submit" name="accept">Accept</button>
        </form>

        <h3>Decline a friend request!</h3>
        <form action="/friend" method="POST">
            <label for="request_decline">Decline Friend Request: </label>
            <input type="text" name="request_decline" id="request_decline">
            <br/>
            <button type="submit" name="decline">Decline</button>
        </form>

        <h3>Unfriend somebody!</h3>
        <form action="/friend" method="POST">
            <label for="friend_remove">Username: </label>
            <input type="text" name="friend_remove" id="friend_remove">
            <br/>
            <button type="submit" name="remove">Unfriend</button>
        </form>

        <div id="friend">
            <ul>
                <%
                    if(friends != null) {
                        for(String user: friends) {
                            User me = UserStore.getInstance().getUser(user);
                %>
                        <li><strong><a href="/user/<%= user %>"><%= user %></a> is your friend!</strong></li>
                        
                <%
                        }
                    }
                %>
            </ul>
        </div>
        
        <div id="friend">
            <ul>
                <%
                    if(requests != null) {
                        for(int i = 0; i < requests.size(); i++) {
                            String username = requests.get(i);
                            User user = UserStore.getInstance().getUser(username);
                        
                %>
                    <li><strong><a href="/user/<%= username %>"><%= username %></a> wants to be your friend!</strong></li>
                <%
                        }
                    }
                %>
            </ul>
        </div>
        <% } else { %>
            <p><a href="/login">Login</a> to begin adding friends.</p>
        <% } %>
  </div>
</body>
</html>
