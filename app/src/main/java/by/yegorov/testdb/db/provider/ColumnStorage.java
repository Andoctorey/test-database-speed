
package by.yegorov.testdb.db.provider;

import java.util.ArrayList;

public class ColumnStorage extends ArrayList<String> {
	private static final long serialVersionUID = -2662074016427687701L;

	public ColumnStorage(String[] arr) {
		for (String s : arr) {
			add(s);
		}
	}
}
