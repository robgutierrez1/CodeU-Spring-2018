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
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class representing a conversation, which can be thought of as a chat room. Conversations are
 * created by a User and contain Messages.
 */
public class Conversation {
  public final UUID id;
  public final UUID owner;
  public final Instant creation;
  public final String title;
  public final List<UUID> members;
  public final Boolean hidden;

  /**
   * Constructs a new Conversation.
   *
   * @param id the ID of this Conversation
   * @param owner the ID of the User who created this Conversation
   * @param title the title of this Conversation
   * @param creation the creation time of this Conversation
   */
  public Conversation(UUID id, UUID owner, String title, Instant creation, Boolean hidden){ 
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
    this.members = new ArrayList<UUID>(Arrays.asList(owner));
    this.hidden = hidden;
  }

   public Conversation(UUID id, UUID owner, String title, Instant creation, List<UUID> members, Boolean hidden){ 
    this.id = id;
    this.owner = owner;
    this.creation = creation;
    this.title = title;
    this.hidden = hidden;
    if(members == null) {
      this.members = new ArrayList<UUID>(Arrays.asList(owner));
    }
    else {
      this.members = members;
    }
  }

  /** Returns the ID of this Conversation. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the User who created this Conversation. */
  public UUID getOwnerId() {
    return owner;
  }

  /** Returns the title of this Conversation. */
  public String getTitle() {
    return title;
  }

  /** Returns the creation time of this Conversation. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns list of members in this Conversation*/
  public List<UUID> getMembers() {
    return members;
  }

  /** Returns hidden status of conversation Conversation*/
  public Boolean getHidden() {
    return hidden;
  }

   /**Accepts and instant and returns a string of the formatted date */
   public String getDate(Instant time) {
    LocalDateTime datetime = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
    String formatted = DateTimeFormatter.ofPattern("E MMM d hh:mm:ss yyyy").format(datetime);
    return formatted;
  }
}
