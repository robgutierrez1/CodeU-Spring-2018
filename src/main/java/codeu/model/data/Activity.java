package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
// Added for time/date
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/** Class representing an activity.
An activity can be a user, message, or conversation.  */
public class Activity implements Comparable<Activity> {
  public final Instant creation;
  public final String type;
  public final Object contents;

public Activity(Instant creation, String type, Object contents) {
    this.creation = creation;
    this.type = type;
    this.contents = contents;
  }

  /** Returns the creation time of this activity. */
  public Instant getCreationTime() {
    return creation;
  }

   /** Returns the type of activity. */
  public String getType() {
    return type;
  }

  public Object getContents() {
    return contents;
  }

   /** Accepts and instant and returns a string of the formatted date */
   public String getDate(Instant time) {
    LocalDateTime datetime = LocalDateTime.ofInstant(time, ZoneId.systemDefault());
    String formatted = DateTimeFormatter.ofPattern("E MMM d hh:mm:ss yyyy").format(datetime);
    return formatted;
  }


  /*public int compareTo(Object o) {
    if((o != null) && (o instanceof Activity)) {
      Activity otherActivity = (Activity) o;
      return(getCreationTime().compareTo(otherActivity.getCreationTime()));
    }
    return -1;
  }*/
   @Override
    public int compareTo(Activity a) {
        return creation.compareTo(a.getCreationTime());
    }
}