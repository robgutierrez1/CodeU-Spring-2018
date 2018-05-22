package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.data.Activity;
import codeu.model.data.User;
import codeu.model.data.Message;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ActivityStore;
import codeu.model.store.basic.MessageStore;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;  
import javax.servlet.*;  
import javax.servlet.http.*;  
import java.util.ArrayList;
import java.util.Arrays;


/** Servlet class responsible for the page to add people to a conversation. */
public class AccessServlet extends HttpServlet {
  /** Store class that gives access to Users. */
  private UserStore userStore;

  /** Store class that gives access to Conversations. */
  private ConversationStore conversationStore;

  /** Store class that gives access to Activities. */
  private ActivityStore activityStore;

  /** Store class that gives access to Messages. */
  private MessageStore messageStore;

  /**
   * Set up state for handling conversation-related requests. This method is only called when
   * running in a server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
    setConversationStore(ConversationStore.getInstance());
    setActivityStore(ActivityStore.getInstance());
    setMessageStore(MessageStore.getInstance());
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
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the ActivityStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setActivityStore(ActivityStore activityStore) {
    this.activityStore = activityStore;
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/access/".length());

    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    List<UUID> members = conversationStore.getMembersInConversation(conversation);
    String chatURL = "/chat/" + conversationTitle;

    request.setAttribute("conversation", conversation);
    request.setAttribute("members", members);
    request.setAttribute("chatURL", chatURL);
    request.setAttribute("conversationTitle", conversationTitle);
    request.getRequestDispatcher("/WEB-INF/view/access.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the form on the memebers page. It gets the
   * logged-in username from the session and the name of the user they want to add from the submitted form
   * data. It uses this to add the user to the list of members for the conversation.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        

    String username = (String) request.getSession().getAttribute("user");
    if (username == null) {
      // user is not logged in, don't let them create a conversation
      response.sendRedirect("/conversations");
      return;
    }

    User user = userStore.getUser(username);
    if (user == null) {
      // user was not found, don't let them create a conversation
      System.out.println("User not found: " + username);
      response.sendRedirect("/conversations");
      return;
    }

    String requestUrl = request.getRequestURI();
    String conversationTitle = requestUrl.substring("/access/".length());
    Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);


    String buttonVal = request.getParameter("buttonVal");

    if(buttonVal == null) {
      System.out.println("no button");
    }
    else if(buttonVal.equals("add")) { 
    System.out.println("add member");
          
          
      String userToAdd = request.getParameter("userToAdd");
      User newMember = userStore.getUser(userToAdd);

      if (newMember == null) {
        // couldn't find user with that username
        System.out.println("User not found: " + userToAdd);
      }
      else if (!(username.equals(userStore.getUsername(conversation.getOwnerId())))) {
        System.out.println("User not owner of this conversation");
        System.out.println("owner of conversation is " + userStore.getUsername(conversation.getOwnerId()));
        System.out.println("User is " + username);
      }
      else if ((conversation.members).contains(userStore.getUserId(userToAdd))) {
        System.out.println("User is already a member of this conversation");
      }
      else {
        UUID newMemberId = userStore.getUserId(userToAdd);
        conversationStore.addMember(conversation, newMemberId); 
    
        response.sendRedirect("/access/" + conversationTitle);
        return;
      }         
    }
    else if(buttonVal.equals("chat")) {
      System.out.println("go to chat");
      response.sendRedirect("/chat/" + conversationTitle);
      return;
    }
        
    else {
      // default case: shouldn't reach here
      System.out.println("Something went wrong...");
    }
  response.sendRedirect("/access/" + conversationTitle);
  }
}