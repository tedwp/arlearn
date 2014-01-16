package org.celstec.dao.gen;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import org.celstec.dao.gen.ActionLocalObject;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table ACTION_LOCAL_OBJECT.
*/
public class ActionLocalObjectDao extends AbstractDao<ActionLocalObject, Long> {

    public static final String TABLENAME = "ACTION_LOCAL_OBJECT";

    /**
     * Properties of entity ActionLocalObject.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Action = new Property(1, String.class, "action", false, "ACTION");
        public final static Property Time = new Property(2, Long.class, "time", false, "TIME");
        public final static Property RunId = new Property(3, long.class, "runId", false, "RUN_ID");
        public final static Property GeneralItem = new Property(4, long.class, "generalItem", false, "GENERAL_ITEM");
        public final static Property Account = new Property(5, long.class, "account", false, "ACCOUNT");
    };

    private DaoSession daoSession;

    private Query<ActionLocalObject> runLocalObject_ActionsQuery;
    private Query<ActionLocalObject> generalItemLocalObject_ActionsQuery;

    public ActionLocalObjectDao(DaoConfig config) {
        super(config);
    }
    
    public ActionLocalObjectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ACTION_LOCAL_OBJECT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'ACTION' TEXT NOT NULL ," + // 1: action
                "'TIME' INTEGER," + // 2: time
                "'RUN_ID' INTEGER NOT NULL ," + // 3: runId
                "'GENERAL_ITEM' INTEGER NOT NULL ," + // 4: generalItem
                "'ACCOUNT' INTEGER NOT NULL );"); // 5: account
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ACTION_LOCAL_OBJECT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ActionLocalObject entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getAction());
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(3, time);
        }
        stmt.bindLong(4, entity.getRunId());
        stmt.bindLong(5, entity.getGeneralItem());
        stmt.bindLong(6, entity.getAccount());
    }

    @Override
    protected void attachEntity(ActionLocalObject entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ActionLocalObject readEntity(Cursor cursor, int offset) {
        ActionLocalObject entity = new ActionLocalObject( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // action
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // time
            cursor.getLong(offset + 3), // runId
            cursor.getLong(offset + 4), // generalItem
            cursor.getLong(offset + 5) // account
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ActionLocalObject entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAction(cursor.getString(offset + 1));
        entity.setTime(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setRunId(cursor.getLong(offset + 3));
        entity.setGeneralItem(cursor.getLong(offset + 4));
        entity.setAccount(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ActionLocalObject entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ActionLocalObject entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "actions" to-many relationship of RunLocalObject. */
    public List<ActionLocalObject> _queryRunLocalObject_Actions(long runId) {
        synchronized (this) {
            if (runLocalObject_ActionsQuery == null) {
                QueryBuilder<ActionLocalObject> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.RunId.eq(null));
                runLocalObject_ActionsQuery = queryBuilder.build();
            }
        }
        Query<ActionLocalObject> query = runLocalObject_ActionsQuery.forCurrentThread();
        query.setParameter(0, runId);
        return query.list();
    }

    /** Internal query to resolve the "actions" to-many relationship of GeneralItemLocalObject. */
    public List<ActionLocalObject> _queryGeneralItemLocalObject_Actions(long generalItem) {
        synchronized (this) {
            if (generalItemLocalObject_ActionsQuery == null) {
                QueryBuilder<ActionLocalObject> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.GeneralItem.eq(null));
                generalItemLocalObject_ActionsQuery = queryBuilder.build();
            }
        }
        Query<ActionLocalObject> query = generalItemLocalObject_ActionsQuery.forCurrentThread();
        query.setParameter(0, generalItem);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getAccountLocalObjectDao().getAllColumns());
            builder.append(" FROM ACTION_LOCAL_OBJECT T");
            builder.append(" LEFT JOIN ACCOUNT_LOCAL_OBJECT T0 ON T.'ACCOUNT'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected ActionLocalObject loadCurrentDeep(Cursor cursor, boolean lock) {
        ActionLocalObject entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        AccountLocalObject accountLocalObject = loadCurrentOther(daoSession.getAccountLocalObjectDao(), cursor, offset);
         if(accountLocalObject != null) {
            entity.setAccountLocalObject(accountLocalObject);
        }

        return entity;    
    }

    public ActionLocalObject loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<ActionLocalObject> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<ActionLocalObject> list = new ArrayList<ActionLocalObject>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<ActionLocalObject> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<ActionLocalObject> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}