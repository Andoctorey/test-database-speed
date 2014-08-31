package by.yegorov.testdb.daogenerator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {
    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "by.yegorov.testdb.db.greendao");
        addDummy(schema);
        new DaoGenerator().generateAll(schema, "../app/src/main/java");
    }

    private static void addDummy(Schema schema) {
        Entity dummy = schema.addEntity("DummyModel");
        dummy.addIdProperty().autoincrement();
        dummy.addStringProperty("dummyString");
        dummy.addLongProperty("dummyLong");
    }
}
