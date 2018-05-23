package codeu.controller;

import codeu.model.data.Conversation;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
// Added
import java.util.UUID;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/* provides way to compare conversations by date of creation*/

public class ConversationComparator implements Comparator<Conversation> {

	@Override
	public int compare(Conversation c1, Conversation c2) {
		return c1.getCreationTime().compareTo(c2.getCreationTime());
	}
}