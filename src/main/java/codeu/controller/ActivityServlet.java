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
import java.time.Instant;
import java.util.UUID;
import java.util.List;

/** Servlet Class responsible for the activity feed */
public class ActivityServlet extends HttpServlet {

	/** Store class that gives access to Activities. */
 	private ActivityStore activityStore;


  /**
    * Set up state for handling activity-related requests. This method is only
    * called when
    *  running in a server, not when running in a test.
    */
  @Override
  public void init() throws ServletException {
    super.init();
    setActivityStore(ActivityStore.getInstance());
  }

  /**
   * Sets the ConversationStore used by this servlet. This function provides a common setup method
   * for use by the test framework or the servlet's init() function.
   */
  void setActivityStore(ActivityStore activityStore) {
    this.activityStore = activityStore;
  }

  /**
    * This function fires when a user navigates to the activity page. It gets all of the 
    * activities from the model and forwards to activity.jsp for rendering the list.
    */
  @Override 
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
        List<User> users = activityStore.getAllUsers();
        List<Conversation> conversations = activityStore.getAllConversations();
        List<Message> messages = activityStore.getAllMessages();
        request.setAttribute("users", users);
        request.getRequestDispatcher("/WEB-INF/view/activity.jsp").forward(request, response);
    }

  /**
    * This function fires when a user navigates to activity page.
    */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    }  
}

 