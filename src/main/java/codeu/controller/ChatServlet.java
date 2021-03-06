// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

/** Servlet class responsible for the chat page. */
public class ChatServlet extends HttpServlet {

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Set up state for handling chat requests. */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when a user navigates to the chat page. It gets the conversation title from
   * the URL, finds the corresponding Conversation, and fetches the messages in that Conversation.
   * It then forwards to chat.jsp for rendering.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      System.out.println("Conversation was null: " + conversationTitle);
      response.sendRedirect("/conversations");
      return;
    }

    UUID conversationId = conversation.getId();

    List<Message> messages = messageStore.getMessagesInConversation(conversationId);

    request.setAttribute("conversation", conversation);
    request.setAttribute("messages", messages);
    request.setAttribute("viewer", userStore.getUser((String) request.getSession().getAttribute("user")));
    request.getRequestDispatcher("/WEB-INF/view/chat.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the chat page. It gets the logged-in
   * username from the session, the conversation title from the URL, and the chat message from the
   * submitted form data. It creates a new Message from that data, adds it to the model, and then
   * redirects back to the chat page.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/chat/".length());
    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them add a message
      response.sendRedirect("/login");
      return;
    }

    if (!(conversation.members).contains(userStore.getUserId(username))) {
      // user not in conversation
      System.out.println("User not a member of this conversation");
      response.sendRedirect("/conversations");
      return;
    }

    if (conversation == null) {
      // couldn't find conversation, redirect to conversation list
      response.sendRedirect("/conversations");
      return;
    }
    
    // deal with user hiding mentioning messages
    for (int i = 0; i < user.getNotify().size(); i++) {
    		String buttonVal = request.getParameter("buttonVal" + i);
    		
    		if (buttonVal != null && buttonVal.equals("hide")) {
    			user.getNotify().remove(i);
    			userStore.updateNotifyList(user, user.getNotify());
    			// leave after we done removing notifications (or messageContent will be null)
    			response.sendRedirect("/chat/" + conversationTitle);
    			return;
    		}
    }

    String buttonVal = request.getParameter("buttonVal");

    if(buttonVal == null) {
      System.out.println("no button");
    }
    else if (buttonVal.equals("submitMessage")) {
      String messageContent = request.getParameter("message");

      // this removes any HTML from the message content
      String cleanedMessageContent = Jsoup.clean(messageContent, Whitelist.none());
    
      // check whether the new message contain usernames that requires notification
      String[] breakdown = cleanedMessageContent.split("@");
      if (breakdown != null && breakdown.length > 1){
    		for (int i = 1; i < breakdown.length; i++){
            String[] atItem = breakdown[i].split(" ", 2);
            User notifiee = userStore.getUser(atItem[0]);
            if (notifiee != null){
            		// will be changed into something meaningful in the future

            		notifiee.getNotify().add("You were mentioned by \"" + user.getName() + "\" in chatroom: " + conversation.getTitle());
            		userStore.updateNotifyList(notifiee, notifiee.getNotify());

            }
    		 }
       }

      Message message =
        new Message(
            UUID.randomUUID(),
            conversation.getId(),
            user.getId(),
            cleanedMessageContent,
            Instant.now(),
            "text");

      messageStore.addMessage(message);

      // redirect to a GET request
      response.sendRedirect("/chat/" + conversationTitle);
    }
    else if (buttonVal.equals("leaveChat")){
      if ((userStore.getUserId(user).equals(conversation.getOwnerId()))) {
        System.out.println("You can't remove the owner");
      }
      else {
        UUID oldMemberId = userStore.getUserId(user);
        conversationStore.removeMember(conversation, oldMemberId); 
    
        response.sendRedirect("/conversations");
        return;
      }
    }
    else if (buttonVal.equals("seeMembers")) {
      response.sendRedirect("/access/" + conversationTitle);
    }
  }
}
