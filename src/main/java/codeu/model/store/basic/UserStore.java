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

import codeu.model.data.User;
import codeu.model.store.persistence.PersistentDataStore;
import codeu.model.store.persistence.PersistentStorageAgent;
import codeu.controller.UserComparator;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Collections;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class UserStore {

  /** Singleton instance of UserStore. */
  private static UserStore instance;

  /**
   * Returns the singleton instance of UserStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static UserStore getInstance() {
    if (instance == null) {
      instance = new UserStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static UserStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new UserStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Users from and saving Users to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Users. */
  private List<User> users;

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private UserStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    users = new ArrayList<>();
  }

  /** Load a set of randomly-generated Message objects. */
  public void loadTestData() {
    users.addAll(DefaultDataStore.getInstance().getAllUsers());
  }

  /**
   * Access the User object with the given name.
   *
   * @return null if username does not match any existing User.
   */
  public User getUser(String username) {
    // This approach will be pretty slow if we have many users.
    for (User user : users) {
      if (user.getName().equals(username)) {
        return user;
      }
    }
    return null;
  }

  /**
   * Access the User object with the given UUID.
   *
   * @return null if the UUID does not match any existing User.
   */
  public User getUser(UUID id) {
    for (User user : users) {
      if (user.getId().equals(id)) {
        return user;
      }
    }
    return null;
  }

  /**
   * Access the user UUID object with the given username.
   *
   * @return null if the UUID does not match any existing User.
   */
  public UUID getUserId(String username) {
    for (User user : users) {
      if (user.getName().equals(username)) {
        return user.getId();
      }
    }
    return null;
  }

   public UUID getUserId(User u) {
    for (User user : users) {
      if (user.equals(u)) {
        return user.getId();
      }
    }
    return null;
  }

  public String getUsername(UUID id) {
    for (User user : users) {
      if (user.getId().equals(id)) {
        return user.getName();
      }
    }
    return null;
  }

  public String getUsername(User u) {
    for (User user : users) {
      if (user.equals(u)) {
        return user.getName();
      }
    }
    return null;
  }

  /** Add a new user to the current set of users known to the application. */
  public void addUser(User user) {
    users.add(user);
    persistentStorageAgent.writeThrough(user);
  }

  /** Update aboutme for a user*/
  public void updateAboutMe(User user, String aboutMe) {
    persistentStorageAgent.updateAboutMe(user, aboutMe);
  }
  
  /** Update notifyList for a user*/
  public void updateNotifyList(User user, ArrayList<String> notfiyList) {
    persistentStorageAgent.updateNotifyList(user, notfiyList);
  }
  
  /** Add a new user to the current set of users known to the application.*/
  public void updateImageUrl(User user, String imageUrl) {
    persistentStorageAgent.updateImageUrl(user, imageUrl);
  }
    
  /** Return true if the given username is known to the application. */
  public boolean isUserRegistered(String username) {
    for (User user : users) {
      if (user.getName().equals(username)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the List of Users stored by this UserStore. This should only be called once, when the data
   * is loaded from Datastore.
   */
  public void setUsers(List<User> users) {
    this.users = users;
  }


  /** Added. Return list of all users. Used in Activity Feed */
  /** sorts list by date, then reverses list so most recent is first*/
  public List<User> getAllUsers() {
    Collections.sort(users, new UserComparator());
    Collections.reverse(users);
    return users;
  }

  /** Update the request list for the given user */
  public void updateFriendRequests(User other_user, ArrayList<String> requests) {
    persistentStorageAgent.updateRequests(other_user, requests);
  }

  /** Update the friends list of this user */
  public void updateFriends(User this_user, ArrayList<String> friends) {
    persistentStorageAgent.updateFriends(this_user, friends);
  }
}
