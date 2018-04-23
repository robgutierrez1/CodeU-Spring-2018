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
          <!-- Need to figure out how to sort these initially-->
          <!-- Also this is extremely gacky! Need to figure out how to put code in functions! -->
          <!-- Lists have a get method so no need for arrays. Change it! -->
        <% 
            // Convert lists to arrays for easier iteration
            Conversation[] con_array = new Conversation[conversations.size()];
            con_array = conversations.toArray(con_array);
            User[] user_array = new User[users.size()];
            user_array = users.toArray(user_array);
            Message[] message_array = new Message[messages.size()];
            message_array = messages.toArray(message_array);
            
            // Figure out the smallest array size of the three
            int smallest_size = con_array.length;
            if(smallest_size > user_array.length) {
              smallest_size = user_array.length;
            }
            if(smallest_size > message_array.length) {
                smallest_size = message_array.length;
            }
            // Initialize indexes for traversal through the arrays
            int con_index = 0;
            int user_index = 0;
            int message_index = 0;

            // Booleans used to figure out what ended first
            boolean con_ended = con_index < smallest_size;
            boolean user_ended = user_index < smallest_size;
            boolean message_ended = message_index < smallest_size;

            // Iterate through data arrays to order activity log based on time
            while(con_ended && user_ended && message_ended) {
              Instant con_time = con_array[con_index].getCreationTime();
              Instant user_time = user_array[user_index].getCreationTime();
              Instant message_time = message_array[message_index].getCreationTime();

              // Conversation created before user and message
              if(con_time.compareTo(user_time) < 0 && con_time.compareTo(message_time) < 0) {
                Conversation con = con_array[con_index];
                String author = UserStore.getInstance()
                .getUser(con.getOwnerId()).getName();
                String date = con.getDate(con_time);
                String convo = con.getTitle();
        %>
              <li><strong><%= date %>: </strong><%= author %> created a new conversation: 
                <%= convo %></li>
        <%
                con_index++;
              } 
              // User created before message and conversation
              else if(user_time.compareTo(con_time) < 0 && user_time.compareTo(message_time) < 0) {
                User us = user_array[user_index];
                String author = us.getName();
                String date = us.getDate(user_time);
        %>
                <li><strong><%= date %>: </strong><%= author %> joined!</li>
        <%
                user_index++;
              }
              // Message created before conversation and user
              else if(message_time.compareTo(con_time) < 0 && message_time.compareTo(user_time) < 0) {
                Message mess = message_array[message_index];
                String author = UserStore.getInstance()
                .getUser(mess.getAuthorId()).getName();
                String date = mess.getDate(message_time);
                UUID ID = mess.getConversationId();
                String convo = ConversationStore.getInstance().findTitle(ID);
        %>
                <li><strong><%= date %>: </strong><%= author %> sent a message in <%= convo %>: 
                "<%= mess.getContent() %>"</li>
        <%
                message_index++;
              } 
              // Some of the data types were created at the same time. Order appropriately
              else {
                // All created at the same time. Standard output to activity log
                if(con_time.compareTo(user_time) == 0 && con_time.compareTo(message_time) == 0) {
                  con_index++;
                  user_index++;
                  message_index++;
                } 
                // Conversation created at the same time as user
                else if(con_time.compareTo(user_time) == 0) {
                  con_index++;
                  user_index++;
                }
                // Conversation created at the same time as message
                else if(con_time.compareTo(message_time) == 0) {
                  con_index++;
                  message_index++;
                } 
                // Conversation not created at the same time as message and user
                else {
                  // User created at the same time as message
                  if(user_time.compareTo(message_time) == 0) {
                    user_index++;
                    message_index++;
                  }
                }
              }
              con_ended = con_index < smallest_size;
              user_ended = user_index < smallest_size;
              message_ended = message_index < smallest_size;
            }

            // Still need to iterate through what's left
            while(con_ended || user_ended || message_ended) {
              // Out of conversations. Iterate through users and messages
              if(!con_ended) {
                // Users and messages left
                if(user_ended && message_ended) {
                  while(user_ended && message_ended) {
                    Instant user_time = user_array[user_index].getCreationTime();
                    Instant message_time = message_array[message_index].getCreationTime();
                    // User created before message
                    if(user_time.compareTo(message_time) < 0) {
                      User us = user_array[user_index];
                      String author = us.getName();
                      String date = us.getDate(user_time);
            %>
                      <li><strong><%= date %>: </strong><%= author %> joined!</li>
            <%
                      user_index++;
                      user_ended = user_index < user_array.length;
                    }
                    // User created after message
                    else if(user_time.compareTo(message_time) > 0) {
                        Message mess = message_array[message_index];
                        String author = UserStore.getInstance()
                        .getUser(mess.getAuthorId()).getName();
                        String date = mess.getDate(message_time);
                        UUID ID = mess.getConversationId();
                        String convo = ConversationStore.getInstance().findTitle(ID);
            %>
                        <li><strong><%= date %>: </strong><%= author %> sent a message in <%= convo %>: 
                        "<%= mess.getContent() %>"</li>
            <%
                        message_index++;
                        message_ended = message_index < message_array.length;
                    } 
                    // Message and user created at the same time
                    else {
                        // User Display
                        User us = user_array[user_index];
                        String author = us.getName();
                        String date = us.getDate(user_time);
            %>
                        <li><strong><%= date %>: </strong><%= author %> joined!</li>
            <%
                        user_index++;
                        user_ended = user_index < user_array.length;
                        
                        // Message Display
                        Message mess = message_array[message_index];
                        author = UserStore.getInstance()
                        .getUser(mess.getAuthorId()).getName();
                        date = mess.getDate(message_time);
                        UUID ID = mess.getConversationId();
                        String convo = ConversationStore.getInstance().findTitle(ID);
            %>
                        <li><strong><%= date %>: </strong><%= author %> sent a message in <%= convo %>: 
                        "<%= mess.getContent() %>"</li>
            <%
                        message_index++;
                        message_ended = message_index < message_array.length;
                    }
                  }
                }
                // Also out of users. May have messages left
                else if(!user_ended) {
                  while(message_ended) {
                    Instant message_time = message_array[message_index].getCreationTime();
                    Message mess = message_array[message_index];
                    String author = UserStore.getInstance()
                    .getUser(mess.getAuthorId()).getName();
                    String date = mess.getDate(message_time);
                    UUID ID = mess.getConversationId();
                    String convo = ConversationStore.getInstance().findTitle(ID);
          %>
                    <li><strong><%= date %>: </strong><%= author %> sent a message in <%= convo %>: 
                    "<%= mess.getContent() %>"</li>
          <%
                    message_index++;
                    message_ended = message_index < message_array.length;
                  }
                  // No conversations, users, or messages left at this point
                } 
                // Also out of messages. May have users left
                else if(!message_ended) {
                  while(user_ended) {
                    Instant user_time = user_array[user_index].getCreationTime();
                    User us = user_array[user_index];
                    String author = us.getName();
                    String date = us.getDate(user_time);
          %>
                    <li><strong><%= date %>: </strong><%= author %> joined!</li>
          <%
                    user_index++;
                    user_ended = user_index < user_array.length;
                  }
                  // No conversations, users, or messages left at this point
                }
              }
              // Out of users. Iterate through conversations and messages
              else if(!user_ended) {
                // Conversations and messages left
                if(con_ended && message_ended) {
                  while(con_ended && message_ended) {
                    Instant con_time = con_array[con_index].getCreationTime();
                    Instant message_time = message_array[message_index].getCreationTime();
                    if(con_time.compareTo(message_time) < 0) {
                      Conversation con = con_array[con_index];
                      String author = UserStore.getInstance()
                      .getUser(con.getOwnerId()).getName();
                      String date = con.getDate(con_time);
                      String convo = con.getTitle();
            %>
                      <li><strong><%= date %>: </strong><%= author %> created a new conversation: 
                      <%= convo %></li>
            <%
                      con_index++;
                      con_ended = con_index < con_array.length;
                    } else if(con_time.compareTo(message_time) > 0) {
                      Message mess = message_array[message_index];
                      String author = UserStore.getInstance()
                      .getUser(mess.getAuthorId()).getName();
                      String date = mess.getDate(message_time);
                      UUID ID = mess.getConversationId();
                      String convo = ConversationStore.getInstance().findTitle(ID);
            %>
                      <li><strong><%= date %>: </strong><%= author %> sent a message in <%= convo %>: 
                      "<%= mess.getContent() %>"</li>
            <%
                      message_index++;
                      message_ended = message_index < message_array.length;
                    } else {
                      // Conversation Display
                      Conversation con = con_array[con_index];
                      String author = UserStore.getInstance()
                      .getUser(con.getOwnerId()).getName();
                      String date = con.getDate(con_time);
                      String convo = con.getTitle();
            %>
                      <li><strong><%= date %>: </strong><%= author %> created a new conversation: 
                      <%= convo %></li>
            <%
                      con_index++;
                      con_ended = con_index < con_array.length;
                      
                      // Message Display
                      Message mess = message_array[message_index];
                      author = UserStore.getInstance()
                      .getUser(mess.getAuthorId()).getName();
                      date = mess.getDate(message_time);
                      UUID ID = mess.getConversationId();
                      convo = ConversationStore.getInstance().findTitle(ID);
            %>
                      <li><strong><%= date %>: </strong><%= author %> sent a message in <%= convo %>: 
                      "<%= mess.getContent() %>"</li>
            <%
                      message_index++;
                      message_ended = message_index < message_array.length;
                    }
                  }
                }
                // Also out of conversations. May have messages left
                else if(!con_ended) {
                  // Figure out how to get rid of repeated code!
                  while(message_ended) {
                    Instant message_time = message_array[message_index].getCreationTime();
                    Message mess = message_array[message_index];
                    String author = UserStore.getInstance()
                    .getUser(mess.getAuthorId()).getName();
                    String date = mess.getDate(message_time);
                    UUID ID = mess.getConversationId();
                    String convo = ConversationStore.getInstance().findTitle(ID);
          %>
                    <li><strong><%= date %>: </strong><%= author %> sent a message in <%= convo %>: 
                    "<%= mess.getContent() %>"</li>
          <%
                    message_index++;
                    message_ended = message_index < message_array.length;
                  }
                  // No conversations, users, or messages left at this point
                }
                // Also out of messages. May have conversations left
                else if(!message_ended) {
                  while(con_ended) {
                    Instant con_time = con_array[con_index].getCreationTime();
                    Conversation con = con_array[con_index];
                    String author = UserStore.getInstance()
                    .getUser(con.getOwnerId()).getName();
                    String date = con.getDate(con_time);
                    String convo = con.getTitle();
          %>
                    <li><strong><%= date %>: </strong><%= author %> created a new conversation: 
                    <%= convo %></li>
          <%
                    con_index++;
                    con_ended = con_index < con_array.length;
                  }
                  // No conversations, users, or messages left at this point
                }
              } 
              // Out of messages. Iterate through conversations and users
              else {
                // Conversations and users left
                if(con_ended && user_ended) {
                  while(con_ended && user_ended) {
                    Instant con_time = con_array[con_index].getCreationTime();
                    Instant user_time = user_array[user_index].getCreationTime();
                    if(con_time.compareTo(user_time) < 0) {
                      Conversation con = con_array[con_index];
                      String author = UserStore.getInstance()
                      .getUser(con.getOwnerId()).getName();
                      String date = con.getDate(con_time);
                      String convo = con.getTitle();
            %>
                      <li><strong><%= date %>: </strong><%= author %> created a new conversation: 
                      <%= convo %></li>
            <%
                      con_index++;
                      con_ended = con_index < con_array.length;
                    } else if(con_time.compareTo(user_time) > 0) {
                        User us = user_array[user_index];
                        String author = us.getName();
                        String date = us.getDate(user_time);
            %>
                        <li><strong><%= date %>: </strong><%= author %> joined!</li>
            <%
                        user_index++;
                        user_ended = user_index < user_array.length;
                    } else {
                        // Conversation Display
                        Conversation con = con_array[con_index];
                        String author = UserStore.getInstance()
                        .getUser(con.getOwnerId()).getName();
                        String date = con.getDate(con_time);
                        String convo = con.getTitle();
            %>
                        <li><strong><%= date %>: </strong><%= author %> created a new conversation: 
                        <%= convo %></li>
            <%
                        con_index++;
                        con_ended = con_index < con_array.length;
                        
                        // User Display
                        User us = user_array[user_index];
                        author = us.getName();
                        date = us.getDate(user_time);
            %>
                        <li><strong><%= date %>: </strong><%= author %> joined!</li>
            <%
                        user_index++;
                        user_ended = user_index < user_array.length;
                    }
                  }
                }
                // Also out of conversations. Users may be left
                else if(!con_ended) {
                  while(user_ended) {
                    Instant user_time = user_array[user_index].getCreationTime();
                    User us = user_array[user_index];
                    String author = us.getName();
                    String date = us.getDate(user_time);
          %>
                    <li><strong><%= date %>: </strong><%= author %> joined!</li>
          <%
                    user_index++;
                    user_ended = user_index < user_array.length;
                  }
                  // No conversations, users, or messages left at this point
                }
                // Also out of users. May have conversations left
                else if(!user_ended) {
                  while(con_ended) {
                    Instant con_time = con_array[con_index].getCreationTime();
                    Conversation con = con_array[con_index];
                    String author = UserStore.getInstance()
                    .getUser(con.getOwnerId()).getName();
                    String date = con.getDate(con_time);
                    String convo = con.getTitle();
          %>
                    <li><strong><%= date %>: </strong><%= author %> created a new conversation: 
                    <%= convo %></li>
          <%
                    con_index++;
                    con_ended = con_index < con_array.length;
                  }
                  // No conversations, users, or messages left at this point
                }
              }
            }
        %>
        </ul>
    </div>
  </div>
</body>
</html>
