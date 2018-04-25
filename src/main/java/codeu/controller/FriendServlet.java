package codeu.controller;

import codeu.model.data.User;
import codeu.model.store.basic.UserStore;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet class responsible for the friend page. */
public class FriendServlet extends HttpServlet {

  /** Store class that gives access to Users. */
  private UserStore userStore;

  /**
   * Set up state for handling friend-related requests. This method is only called when running in a
   * server, not when running in a test.
   */
  @Override
  public void init() throws ServletException {
    super.init();
    setUserStore(UserStore.getInstance());
  }

  /**
   * Sets the UserStore used by this servlet. This function provides a common setup method for use
   * by the test framework or the servlet's init() function.
   */
  void setUserStore(UserStore userStore) {
    this.userStore = userStore;
  }

  /**
   * This function fires when a user requests the /friend URL. It simply forwards the request to
   * friend.jsp.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    if(request.getSession().getAttribute("user") != null) {
        String this_user_name = (String) request.getSession().getAttribute("user");
        User this_user = userStore.getUser(this_user_name);
        ArrayList<User> friends = this_user.getFriends();
        ArrayList<User> requests = this_user.getRequests();
        request.setAttribute("friends", friends);
        request.setAttribute("requests", requests);
    }
    request.getRequestDispatcher("/WEB-INF/view/friend.jsp").forward(request, response);
  }

  /**
   * This function fires when a user submits the friend form. It gets the username and password from
   * the submitted form data, checks that they're valid, and either sends a friend request to the user
   * or returns an error stating no such user exists.
   */
  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    if(request.getParameter("username") != null) {
        String username = request.getParameter("username");
        // For now we just add the user to the person's friend list
        // Later implement friend requests
        if (userStore.isUserRegistered(username)) {
            User user = userStore.getUser(username);
            // Find out which user sent the friend request
            String this_user_name = (String) request.getSession().getAttribute("user");
            User this_user = userStore.getUser(this_user_name);
            // Add user to this user's friend list
            ArrayList<User> friends = this_user.getFriends();
            friends.add(user);
            request.setAttribute("friends", friends);
        }
        // User not found
        else {
        request.setAttribute("error", "That username was not found.");
        }
    } else if(request.getParameter("add") != null) {

    } else if(request.getParameter("remove") != null) {

    }
    request.getRequestDispatcher("/WEB-INF/view/friend.jsp").forward(request, response);
  }
}
