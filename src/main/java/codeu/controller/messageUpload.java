import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.Instant;
import codeu.model.data.User;
import codeu.model.data.Message;
import codeu.model.data.Conversation;
import codeu.model.store.basic.UserStore;
import codeu.model.store.basic.MessageStore;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class messageUpload extends HttpServlet {
    private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
	/** Store class that gives access to Users. */
  	private UserStore userStore;
  	
  	/**
   	 * Set up state for handling user-related and conversation-related requests. This method 
   	 * is only called when running in a server, not when running in a test.
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
   
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
		String username = request.getParameter("username");
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("myFile");
		String url = "";

        if (!(blobKeys == null || blobKeys.isEmpty())) {
        	url = "/messageServe?blob-key=" + blobKeys.get(0).getKeyString();
        }
        
        /**
    	if (username == null) {
      		// user is not logged in, don't let them create a conversation
      		response.sendRedirect("/");
      		return;
   		}

    	User user = userStore.getUser(username);
    	if (user == null) {
      		// user was not found, don't let them create a conversation
      		System.out.println("User not found: " + username);
      		response.sendRedirect("/");
      		return;
    	}
    	*/
    	
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

    	String requestUrl = request.getRequestURI();
    	String conversationTitle = requestUrl.substring("/chat/".length());

    	Conversation conversation = conversationStore.getConversationWithTitle(conversationTitle);
    	if (conversation == null) {
      		// couldn't find conversation, redirect to conversation list
      		response.sendRedirect("/conversations");
      		return;
    	}
    	
    	if(url != ""){
    		System.out.println("the url is:" + url);
    	}
    	
    	Message message =
        new Message(
            UUID.randomUUID(),
            conversation.getId(),
            user.getId(),
            url,
            Instant.now(),
            "image");

    	messageStore.addMessage(message);
		
		
    	// redirect to a GET request
    	response.sendRedirect("/chat/" + conversationTitle);
    }
}