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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/** Servlet class responsible for the user page. */
public class UserServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;
  
  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;
  
  /** Store class that gives access to Messages. */
  private MessageStore messageStore;
  
  /** Checks whether the user is editing about me*/
  private Boolean editAboutMe = false;
  
  /** List of User Messages related to this user*/
  private List<Message> userMessages;
  
  /** Blobstore that stores a map of blob keys and the url for image storage*/
  private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

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
   * Gets the list messages of a particular user
   */
   List getUserMessages(User user) {
       UUID userId = user.getId();
       List<Message> allMessages = messageStore.getAllMessages();
       List<Message> userMessages = new ArrayList<Message>();   
       for (Message message: allMessages) {
           UUID messageAuthorId = message.getAuthorId();
           if (messageAuthorId.equals(userId)) {
               userMessages.add(message);
           }
       }
       return userMessages;
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
      response.sendRedirect("/conversations");
      return;
    }

    String viewerName = (String) request.getSession().getAttribute("user");
    User viewer = userStore.getUser(viewerName);
    String aboutMe = user.getAboutMe();
	request.setAttribute("user", user);
	request.setAttribute("viewer", viewer);
	request.setAttribute("editAboutMe", editAboutMe);
    request.setAttribute("aboutMe", aboutMe);
	
	userMessages = getUserMessages(user);
	
	// added to sort userMessages
	Collections.sort(userMessages, new Comparator<Message>() {
		@Override
		public int compare(Message o1, Message o2) {
			return o2.getCreationTime().compareTo(o1.getCreationTime());
		}
	});
	
	List<Message> topTenList = new ArrayList<Message>();
	for (int i = 0; i < userMessages.size() && i < 10; i++) {
		topTenList.add(userMessages.get(i));
	}
	
	request.setAttribute("messages", topTenList);
	
    request.getRequestDispatcher("/WEB-INF/view/user.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits a change in the about me section. It gets the
   * logged-in username from the session and the new conversation title from the submitted form
   * data. It uses this to update the about me that adds to the model.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException{
    String requestUrl = request.getRequestURI();
    String userTitle = requestUrl.substring("/user/".length());

      User user = userStore.getUser(userTitle);
      if (user == null) {
        // couldn't find user, redirect to home page (for now)
        response.sendRedirect("/");
        return;
      }
    String buttonVal = request.getParameter("buttonVal");
      String enteredAboutMe = request.getParameter("enteredAboutMe");

    if(buttonVal == null){
      System.out.println("not updating aboutme");
    }
    else if (buttonVal.equals("edit")) {
      editAboutMe = true;
      System.out.println("Edit!");
    } else if (buttonVal.equals("cancel")) {
      editAboutMe = false;
      System.out.println("Cancel!");
    } else if (buttonVal.equals("submit")){
      editAboutMe = false; // finish editing
          user.setAboutMe(enteredAboutMe);
          userStore.updateAboutMe(user, enteredAboutMe);
      System.out.println("Submit!");
    } else if(buttonVal.contains("hide")) {
      int orderOfMessage = Integer.parseInt(buttonVal.substring(4));
      userMessages.get(orderOfMessage).setOpenToPublic(false);
    } else if(buttonVal.contains("show")) {
      int orderOfMessage = Integer.parseInt(buttonVal.substring(4));
      userMessages.get(orderOfMessage).setOpenToPublic(true);
    } else {
      // default case: shouldn't reach here
      System.out.println("Something went wrong...");
    }

    response.sendRedirect("/user/" + userTitle);
  }
}

