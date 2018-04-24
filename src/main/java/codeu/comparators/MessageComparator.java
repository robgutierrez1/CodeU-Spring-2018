package codeu.comparators;

import codeu.model.data.Message;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/* provides way to compare messages by date of creation*/

public class MessageComparator implements Comparator<Message> {

	@Override
	public int compare(Message m1, Message m2) {
		return m1.getCreationTime().compareTo(m2.getCreationTime());
	}
}