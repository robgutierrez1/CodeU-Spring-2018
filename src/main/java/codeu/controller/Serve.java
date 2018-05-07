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

public class Serve extends HttpServlet {
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
	
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
	  		String username = request.getParameter("username");
    		
            BlobKey blobKey = new BlobKey(request.getParameter("blob-key"));
            blobstoreService.serve(blobKey, response);
        }
}