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
import java.util.ArrayList;
import java.util.UUID;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String hashedPassword;
  private final Instant creation;
  private String aboutMe;
  private ArrayList<String> notifyList;

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password the password of this User
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, String password, Instant creation) {
    this.id = id;
    this.name = name;
    this.hashedPassword = password;
    this.creation = creation;
    this.notifyList = new ArrayList<String>();
    
    // added aboutMe
    this.aboutMe = "AboutMe not set. If you're the owner of the page, you should see an edit button below.";
  }
    
  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password the password of this User
   * @param creation the creation time of this User
   * @param aboutMe the about me/bio of this User
   */
   public User(UUID id, String name, String password, Instant creation, String aboutMe) {
     this.id = id;
     this.name = name;
     this.hashedPassword = password;
     this.creation = creation;
     this.aboutMe = aboutMe;
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
  
  /** Returns the notification list of this User. */
  public ArrayList<String> getNotify() {
	// this if loop is added to ensure backwards compatibility - some users are created 
	// with the old constructor, where the notifyList is not instantiated. Will find ways
	// to change it after persistence storage is set up.
	if (notifyList == null) {
		notifyList = new ArrayList<String>();
	}
    return notifyList;
  }
}
