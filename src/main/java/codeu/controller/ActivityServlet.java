package codeu.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.ConversationStore;
import codeu.model.store.basic.MessageStore;
import codeu.model.store.basic.ActivityStore;
import codeu.model.data.User;
import codeu.model.data.Message;
import codeu.model.data.Conversation;
import codeu.model.data.Activity;
import java.time.Instant;
import java.util.UUID;
import java.util.List;

/** Servlet Class responsible for the activity feed */
public class ActivityServlet extends HttpServlet {

	/** Store class that gives access to Conversations */
  private ConversationStore conversationStore;

  /** Store class that gives access to Users */
  private UserStore userStore;

  /** Store class that gives access to Messages */
  private MessageStore messageStore;

  /** Store class that gives access to Activity */
  private ActivityStore activityStore;

  /**
    * Set up state for handling activity-related requests. This method is only
    * called when running in a server, not when running in a test.
    */
  @Override
  public void init() throws ServletException {
    super.init();
    setConversationStore(ConversationStore.getInstance());
    setUserStore(UserStore.getInstance());
    setMessageStore(MessageStore.getInstance());
    setActivityStore(ActivityStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setConversationStore(ConversationStore conversationStore) {
    this.conversationStore = conversationStore;
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setMessageStore(MessageStore messageStore) {
    this.messageStore = messageStore;
  }

  /**
   * Sets the MessageStore used by this servlet. This function provides a common setup method for
   * use by the test framework or the servlet's init() function.
   */
  void setActivityStore(ActivityStore activityStore) {
    this.activityStore = activityStore;
  }

  /**
    * This function fires when a user navigates to the activity page. It gets all of the 
    * conversations, messages, and users that will be displayed on the activity feed.
    * Forwards to activity.jsp for rendering the activity feed. The activity feed in this case
    * will act more like a log of everything that has occurred on the chat app.
    */
  @Override 
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        List<Conversation> conversations = conversationStore.getAllConversations();
        List<User> users = userStore.getAllUsers();
        List<Message> messages = messageStore.getAllMessages();
        List<Activity> activities = activityStore.getAllActivities();
        request.setAttribute("conversations", conversations);
        request.setAttribute("users", users);
        request.setAttribute("messages", messages);
        request.setAttribute("activities", activities);
        request.getRequestDispatcher("/WEB-INF/view/activity.jsp").forward(request, response);
    }

  /**
  * Currently not implemented as of right now there is no need accept any requests from
  * the user.
    
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {

    }  
  */

}

 