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

package codeu.model.store.basic;

import codeu.model.data.Conversation;
import codeu.model.data.Activity;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
// Added
import java.util.UUID;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class ConversationStore {

  /** Singleton instance of ConversationStore. */
  private static ConversationStore instance;


  /**
   * Returns the singleton instance of ConversationStore that should be shared between all servlet
   * classes. Do not call this function from a test; use getTestInstance() instead.
   */
  public static ConversationStore getInstance() {
    if (instance == null) {
      instance = new ConversationStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static ConversationStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new ConversationStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Conversations from and saving Conversations
   * to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Conversations. */
  private List<Conversation> conversations;


  // private List<Activity> activities;
  // activities = new ArrayList<>();
  // activities = activities.getAllActivities();

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private ConversationStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    conversations = new ArrayList<>();
  }

  /**
   * Load a set of randomly-generated Conversation objects.
   *
   * @return false if a error occurs.
   */
  public boolean loadTestData() {
    boolean loaded = false;
    try {
      conversations.addAll(DefaultDataStore.getInstance().getAllConversations());
      loaded = true;
    } catch (Exception e) {
      loaded = false;
      System.err.println("ERROR: Unable to establish initial store (conversations).");
    }
    return loaded;
  }

  /** Access the current set of conversations known to the application. */
  public List<Conversation> getAllConversations() {
    Collections.sort(conversations, new ConversationComparator());
    Collections.reverse(conversations);
    return conversations;
  }

  /** Add a new conversation to the current set of conversations known to the application. */
  public void addConversation(Conversation conversation) {
    conversations.add(conversation);
    persistentStorageAgent.writeThrough(conversation);
  }

  /** Access the current set of members within the given Conversation. */
  public List<UUID> getMembersInConversation(Conversation conversation) {
    List<UUID> membersInConversation = new ArrayList<>();
    membersInConversation = conversation.getMembers();
    return membersInConversation;
  }

  /** Add a new member to the current set of members in the conversation. */
  public void addMember(Conversation conversation, UUID member) {
    conversation.members.add(member);
    List<UUID> membersInConversation = new ArrayList<>();
    membersInConversation = conversation.getMembers();
    persistentStorageAgent.updateConversationMembers(conversation, membersInConversation);
  }

  /** Check whether a Conversation title is already known to the application. */
  public boolean isTitleTaken(String title) {
    // This approach will be pretty slow if we have many Conversations.
    for (Conversation conversation : conversations) {
      if (conversation.getTitle().equals(title)) {
        return true;
      }
    }
    return false;
  }

  /** Find and return the Conversation with the given title. */
  public Conversation getConversationWithTitle(String title) {
    for (Conversation conversation : conversations) {
      if (conversation.getTitle().equals(title)) {
        return conversation;
      }
    }
    return null;
  }

  /** Sets the List of Conversations stored by this ConversationStore. */
  public void setConversations(List<Conversation> conversations) {
    this.conversations = conversations;
  }

  /** Find conversation with given UUID. Use in Activity Feed */
  public String findTitle(UUID ID) {
    for(Conversation conversation: conversations) {
      if(conversation.getId().equals(ID)) {
        return conversation.getTitle();
      }
    }
    return "No Conversation";
  }
}
