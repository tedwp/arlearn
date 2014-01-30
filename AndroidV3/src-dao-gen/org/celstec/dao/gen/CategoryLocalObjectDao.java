package org.celstec.dao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import org.celstec.dao.gen.CategoryLocalObject;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table CATEGORY_LOCAL_OBJECT.
*/
public class CategoryLocalObjectDao extends AbstractDao<CategoryLocalObject, Long> {

    public static final String TABLENAME = "CATEGORY_LOCAL_OBJECT";

    /**
     * Properties of entity CategoryLocalObject.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Lang = new Property(1, String.class, "lang", false, "LANG");
        public final static Property Category = new Property(2, String.class, "category", false, "CATEGORY");
        public final static Property Deleted = new Property(3, Boolean.class, "deleted", false, "DELETED");
    };

    private DaoSession daoSession;


    public CategoryLocalObjectDao(DaoConfig config) {
        super(config);
    }
    
    public CategoryLocalObjectDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'CATEGORY_LOCAL_OBJECT' (" + //
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'LANG' TEXT," + // 1: lang
                "'CATEGORY' TEXT," + // 2: category
                "'DELETED' INTEGER);"); // 3: deleted
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'CATEGORY_LOCAL_OBJECT'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CategoryLocalObject entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String lang = entity.getLang();
        if (lang != null) {
            stmt.bindString(2, lang);
        }
 
        String category = entity.getCategory();
        if (category != null) {
            stmt.bindString(3, category);
        }
 
        Boolean deleted = entity.getDeleted();
        if (deleted != null) {
            stmt.bindLong(4, deleted ? 1l: 0l);
        }
    }

    @Override
    protected void attachEntity(CategoryLocalObject entity) {
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
    public CategoryLocalObject readEntity(Cursor cursor, int offset) {
        CategoryLocalObject entity = new CategoryLocalObject( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // lang
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // category
            cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0 // deleted
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CategoryLocalObject entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLang(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCategory(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDeleted(cursor.isNull(offset + 3) ? null : cursor.getShort(offset + 3) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CategoryLocalObject entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CategoryLocalObject entity) {
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
    
}