package codeu.model.data;

public class Mention {

	/** The person who mentions you in the notification */
	private String mentioner;
	
	/** The entire message that mentioned you */
	private String message;
	
	/** Location of the chatroom */
	private String chatroom;
	
	public String getMentioner() {
		return mentioner;
	}
	
	public void setMentioner(String mentioner) {
		this.mentioner = mentioner;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getChatroom() {
		return chatroom;
	}
	
	public void setChatroom(String chatroom) {
		this.chatroom = chatroom;
	}
}
