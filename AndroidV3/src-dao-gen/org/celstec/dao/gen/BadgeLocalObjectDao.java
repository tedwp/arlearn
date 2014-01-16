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

import org.celstec.dao.gen.BadgeLocalObject;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table BADGE_LOCAL_OBJECT.
*/
public class BadgeLocalObjectDao extends AbstractDao<BadgeLocalObject, Long> {

    public static final String TABLENAME = "BADGE_LOCAL_OBJECT";

    /**
     * Properties of entity BadgeLocalObject.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Description = new Property(2, String.class, "description", false, "DESCRIPTION");
        public final static Property BadgeIcon = new Property(3, byte[].class, "badgeIcon", false, "BADGE_ICON");
        public final static Property InquiryId = new Property(4, Long.class, "inquiryId", false, "INQUIRY_ID");
        public final static Property AccountId = new Property(5, long.class, "accountId", false, "ACCOUNT_ID");
    };

    private DaoSession daoSession;

    private Query<BadgeLocalObject> inquiryLocalObject_BadgesQuery;

    public BadgeLocalObjectDao(DaoConfig config) {
        super(config);
    }
    
    public BadgeLocalObjectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'BADGE_LOCAL_OBJECT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'TITLE' TEXT," + // 1: title
                "'DESCRIPTION' TEXT," + // 2: description
                "'BADGE_ICON' BLOB," + // 3: badgeIcon
                "'INQUIRY_ID' INTEGER," + // 4: inquiryId
                "'ACCOUNT_ID' INTEGER NOT NULL );"); // 5: accountId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BADGE_LOCAL_OBJECT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, BadgeLocalObject entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String description = entity.getDescription();
        if (description != null) {
            stmt.bindString(3, description);
        }
 
        byte[] badgeIcon = entity.getBadgeIcon();
        if (badgeIcon != null) {
            stmt.bindBlob(4, badgeIcon);
        }
 
        Long inquiryId = entity.getInquiryId();
        if (inquiryId != null) {
            stmt.bindLong(5, inquiryId);
        }
        stmt.bindLong(6, entity.getAccountId());
    }

    @Override
    protected void attachEntity(BadgeLocalObject entity) {
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
    public BadgeLocalObject readEntity(Cursor cursor, int offset) {
        BadgeLocalObject entity = new BadgeLocalObject( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // description
            cursor.isNull(offset + 3) ? null : cursor.getBlob(offset + 3), // badgeIcon
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // inquiryId
            cursor.getLong(offset + 5) // accountId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, BadgeLocalObject entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setDescription(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setBadgeIcon(cursor.isNull(offset + 3) ? null : cursor.getBlob(offset + 3));
        entity.setInquiryId(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setAccountId(cursor.getLong(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(BadgeLocalObject entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(BadgeLocalObject entity) {
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
    
    /** Internal query to resolve the "badges" to-many relationship of InquiryLocalObject. */
    public List<BadgeLocalObject> _queryInquiryLocalObject_Badges(Long inquiryId) {
        synchronized (this) {
            if (inquiryLocalObject_BadgesQuery == null) {
                QueryBuilder<BadgeLocalObject> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.InquiryId.eq(null));
                inquiryLocalObject_BadgesQuery = queryBuilder.build();
            }
        }
        Query<BadgeLocalObject> query = inquiryLocalObject_BadgesQuery.forCurrentThread();
        query.setParameter(0, inquiryId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getInquiryLocalObjectDao().getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T1", daoSession.getAccountLocalObjectDao().getAllColumns());
            builder.append(" FROM BADGE_LOCAL_OBJECT T");
            builder.append(" LEFT JOIN INQUIRY_LOCAL_OBJECT T0 ON T.'INQUIRY_ID'=T0.'_id'");
            builder.append(" LEFT JOIN ACCOUNT_LOCAL_OBJECT T1 ON T.'ACCOUNT_ID'=T1.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected BadgeLocalObject loadCurrentDeep(Cursor cursor, boolean lock) {
        BadgeLocalObject entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        InquiryLocalObject inquiryLocalObject = loadCurrentOther(daoSession.getInquiryLocalObjectDao(), cursor, offset);
        entity.setInquiryLocalObject(inquiryLocalObject);
        offset += daoSession.getInquiryLocalObjectDao().getAllColumns().length;

        AccountLocalObject accountLocalObject = loadCurrentOther(daoSession.getAccountLocalObjectDao(), cursor, offset);
         if(accountLocalObject != null) {
            entity.setAccountLocalObject(accountLocalObject);
        }

        return entity;    
    }

    public BadgeLocalObject loadDeep(Long key) {
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
    public List<BadgeLocalObject> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<BadgeLocalObject> list = new ArrayList<BadgeLocalObject>(count);
        
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
    
    protected List<BadgeLocalObject> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<BadgeLocalObject> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}