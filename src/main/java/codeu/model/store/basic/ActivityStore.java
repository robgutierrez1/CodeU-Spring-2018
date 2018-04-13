package codeu.model.store.basic;


import codeu.model.data.User;
import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class ActivityStore {

  /** Singleton instance of UserStore. */
  private static ActivityStore instance;

  /**
   * Returns the singleton instance of UserStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static ActivityStore getInstance() {
    if (instance == null) {
      instance = new ActivityStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static ActivityStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new ActivityStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Users from and saving Users to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of new Users. */
  private List<User> new_users;

  // The in-memory list of new Conversations
  private List<Conversation> new_convos;

  // The in-memory list of new messages
  private List<Message> new_messages;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private ActivityStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    // Initialize a queue that holds new users. FIFO implementation.
    newUers = new ArrayList<>();
    newConversations = new ArrayList<>();
    newMessages = new ArrayList<>();
  }

  /** Add a new user to the queue */
  public void addUser(User user) {
    newUsers.add(user);
    // Not sure about adding to persistentStorageAgent
    // persistentStorageAgent.writeThrough(user);
  }

  /** Access the current set of new users known to the application. */
  public List<User> getAllUsers() {
    return newUsers;
  }

   /** Access the current set of new users known to the application. */
  public List<Conversation> getAllConversations() {
    return newConversations;
  }

   /** Access the current set of new users known to the application. */
  public List<Message> getAllMessages() {
    return newMessages;
  }


  /** Get the next new user to display 
  public User getUser() {
    return new_users.remove();
  }*/

  // Add a new conversation to the queue
  public void addConvo(Conversation conversation) {
    newConvos.add(conversation);
  }

  // Get the next new conversation to display
  /*public Conversation getConvo() {
    return new_convos.remove();
  }*/

  // Add a new message to the queue
  public void addMessage(Message message) {
    newMessages.add(message);
  }

  // Get a new message from the queue
  /*public Message getMess() {
    return new_message.remove();
  }*/
}
