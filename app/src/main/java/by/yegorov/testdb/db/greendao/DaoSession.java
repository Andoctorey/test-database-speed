package by.yegorov.testdb.db.greendao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import by.yegorov.testdb.db.model.DummyModel;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 *
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig dummyModelDaoConfig;

    private final DummyModelDao dummyModelDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        dummyModelDaoConfig = daoConfigMap.get(DummyModelDao.class).clone();
        dummyModelDaoConfig.initIdentityScope(type);

        dummyModelDao = new DummyModelDao(dummyModelDaoConfig, this);

        registerDao(DummyModel.class, dummyModelDao);
    }

    public void clear() {
        dummyModelDaoConfig.getIdentityScope().clear();
    }

    public DummyModelDao getDummyModelDao() {
        return dummyModelDao;
    }

}
