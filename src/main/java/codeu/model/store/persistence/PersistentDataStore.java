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

package codeu.model.store.persistence;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.data.Activity;
//import codeu.model.store.persistence.PersistentDataStoreException;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This class handles all interactions with Google App Engine's Datastore service. On startup it
 * sets the state of the applications's data objects from the current contents of its Datastore. It
 * also performs writes of new of modified objects back to the Datastore.
 */
public class PersistentDataStore {

  // Handle to Google AppEngine's Datastore service.
  private DatastoreService datastore;

  /**
   * Constructs a new PersistentDataStore and sets up its state to begin loading objects from the
   * Datastore service.
   */
  public PersistentDataStore() {
    datastore = DatastoreServiceFactory.getDatastoreService();
  }

  /**
   * Loads all User objects from the Datastore service and returns them in a List.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<User> loadUsers() throws PersistentDataStoreException {

    List<User> users = new ArrayList<>();

    // Retrieve all users from the datastore.
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        String userName = (String) entity.getProperty("username");
        String password = (String) entity.getProperty("password");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String aboutMe = (String) entity.getProperty("aboutme");
        String imageUrl = (String) entity.getProperty("imageUrl");
        System.out.println("the imageUrl when loading all users is:" + imageUrl);
        System.out.println("the user is:" + userName);

        if (aboutMe == null){
          aboutMe = "AboutMe not set. If you're the owner of the page, you should see an edit button below.";
        }
        
        if (imageUrl == null) {
        	  imageUrl = "";
        }

        User user = new User(uuid, userName, password, creationTime, aboutMe, imageUrl);
        ArrayList<String> notifyList = (ArrayList<String>) entity.getProperty("notifyList");
        user.setNotify(notifyList);
        users.add(user);
        
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return users;
  }

  /**
   * Loads all Conversation objects from the Datastore service and returns them in a List.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Conversation> loadConversations() throws PersistentDataStoreException {

    List<Conversation> conversations = new ArrayList<>();

    // Retrieve all conversations from the datastore.
    Query query = new Query("chat-conversations");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID ownerUuid = UUID.fromString((String) entity.getProperty("owner_uuid"));
        String title = (String) entity.getProperty("title");
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        List<UUID> members = (List<UUID>)stringToUUID((String) entity.getProperty("members"));
        Boolean hidden = Boolean.valueOf((String) entity.getProperty("hidden"));
        Conversation conversation = new Conversation(uuid, ownerUuid, title, creationTime, members, hidden);
        conversations.add(conversation);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return conversations;
  }

  /**
   * Loads all Message objects from the Datastore service and returns them in a List.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Message> loadMessages() throws PersistentDataStoreException {

    List<Message> messages = new ArrayList<>();

    // Retrieve all messages from the datastore.
    Query query = new Query("chat-messages");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        UUID conversationUuid = UUID.fromString((String) entity.getProperty("conv_uuid"));
        UUID authorUuid = UUID.fromString((String) entity.getProperty("author_uuid"));
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String content = (String) entity.getProperty("content");
        String type = (String) entity.getProperty("type");
        Message message = new Message(uuid, conversationUuid, authorUuid, content, creationTime, type);
        messages.add(message);
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return messages;
  }

  /**
   * Loads all Activity objects from the Datastore service and returns them in a List.
   *
   * @throws PersistentDataStoreException if an error was detected during the load from the
   *     Datastore service
   */
  public List<Activity> loadActivities() throws PersistentDataStoreException {

    List<Activity> activities = new ArrayList<>();

    // Retrieve all messages from the datastore.
    Query query = new Query("chat-activities");
    PreparedQuery results = datastore.prepare(query);

    for (Entity entity : results.asIterable()) {
      try {
        Instant creationTime = Instant.parse((String) entity.getProperty("creation_time"));
        String activityType = (String) entity.getProperty("activity_type");
      } catch (Exception e) {
        // In a production environment, errors should be very rare. Errors which may
        // occur include network errors, Datastore service errors, authorization errors,
        // database entity definition mismatches, or service mismatches.
        throw new PersistentDataStoreException(e);
      }
    }

    return activities;
  }

  /** Write a User object to the Datastore service. */
  public void writeThrough(User user) {
    Entity userEntity = new Entity("chat-users");
    userEntity.setProperty("uuid", user.getId().toString());
    userEntity.setProperty("username", user.getName());
    userEntity.setProperty("password", user.getPassword());
    userEntity.setProperty("creation_time", user.getCreationTime().toString());
    userEntity.setProperty("aboutme", user.getAboutMe().toString());
    userEntity.setProperty("notifyList", user.getNotify());
    datastore.put(userEntity);
  }
    
  /** Update a User object's aboutme to the Datastore service.*/
  public void updateAboutMe(User user, String aboutMe) {
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);
      
    for (Entity entity : results.asIterable()) {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        if (uuid.equals(user.getId())) {
          entity.setProperty("aboutme", aboutMe);
          datastore.put(entity);
        }
    }
  }
  
  /** Update a User object's notifyList to the Datastore service.*/
  public void updateNotifyList(User user, ArrayList<String> notifyList) {
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);
      
    for (Entity entity : results.asIterable()) {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        if (uuid.equals(user.getId())) {
          entity.setProperty("notifyList", notifyList);
          datastore.put(entity);
        }
    }
  }
  
  /** Update a User object's imageUrl to the Datastore service.*/
  public void updateImageUrl(User user, String imageUrl) {
    Query query = new Query("chat-users");
    PreparedQuery results = datastore.prepare(query);
      
    for (Entity entity : results.asIterable()) {
        UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
        if (uuid.equals(user.getId())) {
          entity.setProperty("imageUrl", imageUrl);
          datastore.put(entity);
        }
    }
  }

  /** Write a Message object to the Datastore service. */
  public void writeThrough(Message message) {
    Entity messageEntity = new Entity("chat-messages");
    messageEntity.setProperty("uuid", message.getId().toString());
    messageEntity.setProperty("conv_uuid", message.getConversationId().toString());
    messageEntity.setProperty("author_uuid", message.getAuthorId().toString());
    messageEntity.setProperty("content", message.getContent());
    messageEntity.setProperty("creation_time", message.getCreationTime().toString());
    messageEntity.setProperty("type", message.getType().toString());
    datastore.put(messageEntity);
  }

  /** Write a Conversation object to the Datastore service. */
  public void writeThrough(Conversation conversation) {
    Entity conversationEntity = new Entity("chat-conversations");
    conversationEntity.setProperty("uuid", conversation.getId().toString());
    conversationEntity.setProperty("owner_uuid", conversation.getOwnerId().toString());
    conversationEntity.setProperty("title", conversation.getTitle());
    conversationEntity.setProperty("creation_time", conversation.getCreationTime().toString());
    conversationEntity.setProperty("members", uuidtoString(conversation.getMembers())); 
    conversationEntity.setProperty("hidden", String.valueOf(conversation.getHidden())); 
    datastore.put(conversationEntity);
  }

  /** Update a Conversation members list to the Datastore service.*/
  public void updateConversationMembers(Conversation conversation, List<UUID> members) {
    Query query = new Query("chat-conversations");
    PreparedQuery results = datastore.prepare(query);
      
    for (Entity entity : results.asIterable()) {
      UUID uuid = UUID.fromString((String) entity.getProperty("uuid"));
      if(uuid.equals(conversation.getId())) {
        entity.setProperty("members", uuidtoString(members));
        datastore.put(entity);
      }
    }
  }

  /** Write a Conversation object to the Datastore service. */
  public void writeThrough(Activity activity) {
    Entity activityEntity = new Entity("chat-activities");
    activityEntity.setProperty("creation_time", activity.getCreationTime().toString());
    activityEntity.setProperty("title", activity.getType());
    datastore.put(activityEntity);
  }

  /** Converts list of UUIDs to string of UUIDs **/
  public String uuidtoString(List<UUID> members) {
    List<String> memberStrings = new ArrayList<>();
    for(UUID member : members) {
      memberStrings.add(member.toString());
    }
    String result = String.join(",", memberStrings);
    return result;
  }

  /** Conversts string of UUIDs to list */
  public List<UUID> stringToUUID(String members) {
    System.out.println("member string: " + members);
    if(members == null){
      ArrayList<UUID> result = new ArrayList<UUID>();
      return result;
    }
    ArrayList<String> stringResult = new ArrayList<String>();
    stringResult = new ArrayList<String>(Arrays.asList(members.split(",")));
    ArrayList<UUID> result = new ArrayList<UUID>();
    for(String id : stringResult) {;
      result.add(UUID.fromString(id));
    }
    return result; 
  }
}
