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

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String hashedPassword;
  private final Instant creation;
  // Added
  private String aboutMe;
  private String imageUrl;
  private final ArrayList<String> friends;
  private final ArrayList<String> requests;

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password the password of this User
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, String password, Instant creation) {
    //super(creation, "User");
    this.id = id;
    this.name = name;
    this.hashedPassword = password;
    this.creation = creation;
    
    // added aboutMe
    this.aboutMe = "AboutMe not set. If you're the owner of the page, you should see an edit button below.";
    this.imageUrl = "";
    
    // Initialize empty friends and requests
    this.friends = new ArrayList<String>();
    this.requests = new ArrayList<String>();
  }

   /**
   * Constructs a new User with passed in request and friends parameters.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password the password of this User
   * @param creation the creation time of this User
   * @param friends the friends list of this User
   * @param requests the friend requests of this User
   * @param aboutMe the about me/bio of this User
   */

   public User(UUID id, String name, String password, Instant creation, ArrayList<String> friends, 
                      ArrayList<String> requests, String aboutMe, String imageUrl) {
    this.id = id;
    this.name = name;
    this.hashedPassword = password;
    this.creation = creation;
    if(friends == null) {
      this.friends = new ArrayList<String>();
    } else {
      this.friends = friends;
    }
    if(requests == null) {
      this.requests = new ArrayList<String>();
    } else {
      this.requests = requests;
    }
    this.aboutMe = aboutMe;
    this.imageUrl = imageUrl;
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

   /**Accepts and instant and returns a string of the formatted date */
  public String getDate(Instant time) {
    LocalDateTime datetime = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
    String formatted = DateTimeFormatter.ofPattern("E MMM d hh:mm:ss yyyy").format(datetime);
    return formatted;
  }
  
  /** Returns the aboutMe of this User. */
  public String getAboutMe() {
    return aboutMe;
  }

  /**
   * Sets the aboutMe of this User.
   */
  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }
  
  /** Returns the imageUrl of this User. */
  public String getImageUrl() {
    return imageUrl;
  }

  /**
   * Sets the imageUrl of this User.
   */
  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  /** Return the list of friends for this user */
  public ArrayList<String> getFriends() {
    return this.friends;
  }

  /** Add a friend to this user's friend list */
  public void addFriend(String user) {
    this.friends.add(user);
  }

  /** Return the list of friend requests */
  public ArrayList<String> getRequests() {
    return this.requests;
  }
}
