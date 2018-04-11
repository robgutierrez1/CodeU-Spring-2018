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
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the conversations page. */
public class UserServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;
  
  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;
  
  /** Store class that gives access to Messages. */
  private MessageStore messageStore;
  
  /** Checks whether the user is editing about me*/
  private Boolean editAboutMe = false;

  /**
   * Set up state for handling user-related and conversation-related requests. This method 
   * is only called when running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setMessageStore(MessageStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }
  
  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
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
   * Gets the list conversation of a particular user
   */
   List getUserConversations(User user) {
     UUID userId = user.getId();
     List<Conversation> allConversations = conversationStore.getAllConversations();
     List<Conversation> userConversations = new ArrayList<Conversation>();   
     for (Conversation conversation: allConversations) {
       UUID conversationOwnerId = conversation.getOwnerId();
       if (conversationOwnerId.equals(userId)) {
         userConversations.add(conversation);
       }
     }
     return userConversations;
   }
    
  /**
   * This function fires when a user navigates to the user page. It checks whether the username
   * exist in the database and forwards to conversations.jsp for rendering the list.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
	  
	String requestUrl = request.getRequestURI();
	String userTitle = requestUrl.substring("/user/".length());
	  
	User user = userStore.getUser(userTitle);
    if (user == null) {
      // couldn't find user, redirect to home page (for now)
      System.out.println("Name was null: " + userTitle);
      response.sendRedirect("/");
      return;
    }

    String viewerName = (String) request.getSession().getAttribute("user");
    User viewer = userStore.getUser(viewerName);
    String aboutMe = user.getAboutMe();
	request.setAttribute("user", user);
	request.setAttribute("viewer", viewer);
	request.setAttribute("editAboutMe", editAboutMe);
    request.setAttribute("aboutMe", aboutMe);
    System.out.println("In doGet, aboutMe is:" + aboutMe);
	
	List<Conversation> userConversations = getUserConversations(user);
	List<Message> userMessages = new ArrayList<Message>();
	for (Conversation conversation: userConversations) {
	  List<Message> messages = messageStore.getMessagesInConversation(conversation.getId());
	  userMessages.addAll(messages);
	}
	request.setAttribute("messages", userMessages);
	
    request.getRequestDispatcher("/WEB-INF/view/user.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits a change in the about me section. It gets the
   * logged-in username from the session and the new conversation title from the submitted form
   * data. It uses this to create a new Conversation object that it adds to the model.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException{
	  
	String requestUrl = request.getRequestURI();
	String userTitle = requestUrl.substring("/user/".length());
	
	String buttonVal = request.getParameter("buttonVal");
    String enteredAboutMe = request.getParameter("enteredAboutMe");
	
	if (buttonVal.equals("edit")) {
		editAboutMe = true;
		System.out.println("Edit!");
	} else if (buttonVal.equals("cancel")) {
		editAboutMe = false;
		System.out.println("Cancel!");
	} else if (buttonVal.equals("submit")){
		editAboutMe = false; // finish editing
        User user = userStore.getUser(userTitle);
        user.setAboutMe(enteredAboutMe);
        //userStore.updateAboutMe(user, enteredAboutMe);
        System.out.println("entered is: " + enteredAboutMe);
		System.out.println("Submit!");
	} else {
		// default case: shouldn't reach here
		System.out.println("Something went wrong...");
	}
	  
	response.sendRedirect("/user/" + userTitle);
  }
}

