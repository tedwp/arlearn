package org.celstec.dao.gen;

import java.util.List;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import org.celstec.dao.gen.ThreadLocalObject;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table THREAD_LOCAL_OBJECT.
*/
public class ThreadLocalObjectDao extends AbstractDao<ThreadLocalObject, Long> {

    public static final String TABLENAME = "THREAD_LOCAL_OBJECT";

    /**
     * Properties of entity ThreadLocalObject.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property LastModificationDate = new Property(2, Long.class, "lastModificationDate", false, "LAST_MODIFICATION_DATE");
        public final static Property RunId = new Property(3, long.class, "runId", false, "RUN_ID");
    };

    private DaoSession daoSession;

    private Query<ThreadLocalObject> runLocalObject_LastModificationDateQuery;

    public ThreadLocalObjectDao(DaoConfig config) {
        super(config);
    }
    
    public ThreadLocalObjectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'THREAD_LOCAL_OBJECT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'LAST_MODIFICATION_DATE' INTEGER," + // 2: lastModificationDate
                "'RUN_ID' INTEGER NOT NULL );"); // 3: runId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'THREAD_LOCAL_OBJECT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ThreadLocalObject entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        Long lastModificationDate = entity.getLastModificationDate();
        if (lastModificationDate != null) {
            stmt.bindLong(3, lastModificationDate);
        }
        stmt.bindLong(4, entity.getRunId());
    }

    @Override
    protected void attachEntity(ThreadLocalObject entity) {
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
    public ThreadLocalObject readEntity(Cursor cursor, int offset) {
        ThreadLocalObject entity = new ThreadLocalObject( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // lastModificationDate
            cursor.getLong(offset + 3) // runId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ThreadLocalObject entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLastModificationDate(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setRunId(cursor.getLong(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ThreadLocalObject entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ThreadLocalObject entity) {
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
    
    /** Internal query to resolve the "lastModificationDate" to-many relationship of RunLocalObject. */
    public List<ThreadLocalObject> _queryRunLocalObject_LastModificationDate(long runId) {
        synchronized (this) {
            if (runLocalObject_LastModificationDateQuery == null) {
                QueryBuilder<ThreadLocalObject> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.RunId.eq(null));
                runLocalObject_LastModificationDateQuery = queryBuilder.build();
            }
        }
        Query<ThreadLocalObject> query = runLocalObject_LastModificationDateQuery.forCurrentThread();
        query.setParameter(0, runId);
        return query.list();
    }

}
