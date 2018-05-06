package codeu.model.store.basic;

import codeu.model.data.User;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/* provides way to compare users by date of creation*/

public class UserComparator implements Comparator<User> {

	@Override
	public int compare(User u1, User u2) {
		return u1.getCreationTime().compareTo(u2.getCreationTime());
	}
}