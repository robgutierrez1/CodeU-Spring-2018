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

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String password;
  private final Instant creation;
  private String aboutMe;
  private String imageUrl;

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param password the password of this User
   * @param creation the creation time of this User
   * @param creation the creation time of this User
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, String password, Instant creation) {
    this.id = id;
    this.name = name;
    this.password = password;
    this.creation = creation;
    
    // added aboutMe
    this.aboutMe = "AboutMe not set. If you're the owner of the page, you should see an edit button below.";
    this.imageUrl = "";
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
   public User(UUID id, String name, String password, Instant creation, String aboutMe, String imageUrl) {
     this.id = id;
     this.name = name;
     this.password = password;
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
    return password;
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
}
