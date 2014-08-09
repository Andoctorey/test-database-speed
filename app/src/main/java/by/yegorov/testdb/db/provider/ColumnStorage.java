
package by.yegorov.testdb.db.provider;

import java.util.ArrayList;

public class ColumnStorage extends ArrayList<String> {

    public ColumnStorage(String[] arr) {
        for (String s : arr) {
            add(s);
        }
    }
}
