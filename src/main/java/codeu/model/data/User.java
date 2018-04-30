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

package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
// Added
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import codeu.model.data.User;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String hashedPassword;
  private final Instant creation;
  private final ArrayList<User> friends;
  private final ArrayList<User> requests;

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password the password of this User
   * @param creation the creation time of this User
   * @param friends the friends list of this User
   * @param requests the friend requests of this User
   */
  public User(UUID id, String name, String password, Instant creation, ArrayList<User> friends, 
                                                          ArrayList<User> requests) {
    this.id = id;
    this.name = name;
    this.hashedPassword = password;
    this.creation = creation;
    if(friends == null) {
      this.friends = new ArrayList<User>();
    } else {
      this.friends = friends;
    }
    if(requests == null) {
      this.requests = new ArrayList<User>();
    } else {
      this.requests = requests;
    }
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }

  /**
   * Returns the password of this User.
   */
  public String getPassword() {
    return hashedPassword;
  }


  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Accepts and instant and returns a string of the formatted date */
  public String getDate(Instant time) {
    LocalDateTime datetime = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
    String formatted = DateTimeFormatter.ofPattern("E MMM d hh:mm:ss yyyy").format(datetime);
    return formatted;
  }

  /** Return the list of friends for this user */
  public ArrayList<User> getFriends() {
    return this.friends;
  }

  /** Add a friend to this user's friend list */
  public void addFriend(User user) {
    this.friends.add(user);
  }

  /** Return the list of friend requests */
  public ArrayList<User> getRequests() {
    return this.requests;
  }
}
