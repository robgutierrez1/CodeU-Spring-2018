<%@ page import="java.util.ArrayList" %>
<%@ page import="codeu.model.data.User" %>

<%
    ArrayList<User> friends = (ArrayList<User>) request.getAttribute("friends");
    ArrayList<User> requests = (ArrayList<User>) request.getAttribute("requests");
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
        <form action="/friend" method="POST">
            <label for="username">Username: </label>
            <input type="text" name="username" id="username">
            <br/>
            <button type="submit" name="add">Add Friend</button>
        </form>
        <div id="friend">
            <ul>
                <%
                if(requests != null) {
                    for(User user: requests) {
                        String name = user.getName();
                %>
                    <form action="/friend" method="POST">
                        <li><strong><%= name %> wants to be your friend!</strong></li>
                        <input type="submit" name="add" value="Accept"/>
                        <input type="submit" name="remove" value="Decline"/>
                    </form>
                <%
                    }
                }
            </ul>
        </div>

        <div id="friend">
            <ul>
                <%
                    if(friends != null) {
                        for(User user: friends) {
                            String name = user.getName();
                %>
                        <li><strong><%= name %> is your friend!</strong></li>
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
