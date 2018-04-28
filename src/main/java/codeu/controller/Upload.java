import java.io.IOException;
import java.util.List;
import java.util.Map;
import codeu.model.data.User;
import codeu.model.store.basic.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class Upload extends HttpServlet {
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
		System.out.println("doPost, Upload");
		String requestUrl = request.getRequestURI();
		String userTitle = requestUrl.substring("/user/".length());
        Map<String, List<BlobKey>> blobs = blobstoreService.getUploads(request);
        List<BlobKey> blobKeys = blobs.get("myFile");
		String url = "";
		
        if (blobKeys == null || blobKeys.isEmpty()) {
            System.out.println("blob key is null");
        } else {
        	url = "/serve?blob-key=" + blobKeys.get(0).getKeyString();
        	System.out.println("!!!blob key is NOT null it is: " + url);
        }
        
    	User user = userStore.getUser(userTitle);
    	if (user == null) {
     		// couldn't find user, redirect to home page (for now)
     		System.out.println("in doPost, upload: Name was null: " + userTitle);
      		response.sendRedirect("/serve?blob-key=" + blobKeys.get(0).getKeyString());
      		return;
    	}
    	user.setImageUrl(url);
    	userStore.updateImageUrl(user, url);
    	// redirect to the profile page, because redirecting is refreshing
    	//response.sendRedirect("/user/" + userTitle);
    	response.sendRedirect("/serve?blob-key=" + blobKeys.get(0).getKeyString());
    }
}