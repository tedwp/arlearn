package org.celstec.arlearn2.android.db;

import java.io.File;

import org.celstec.arlearn2.android.db.beans.MediaCacheItem;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class MediaCache extends GenericDbTable {

	public static final String MEDIACACHE_TABLE = "mediaCache";
	public static final String ITEM_ID = "itemId";
	public static final String RUN_ID = "runId";
	public static final String ACCOUNT = "account";
	public static final String LOCAL_FILE = "localFile";
	public static final String REMOTE_FILE = "remoteFile";
	public static final String INCOMMING = "incomming";
	public static final String REPLICATED = "replicated";
	public static final String MIMETYPE = "mimetype";

	public MediaCache(DBAdapter db) {
		super(db);
	}

	@Override
	public String createStatement() {
		return "create table " + MEDIACACHE_TABLE + " (" 
				+ ITEM_ID + " text primary key, " //0 
				+ RUN_ID + " long, " 
				+ ACCOUNT + " text, " 
				+ LOCAL_FILE + " text, " //3
				+ REMOTE_FILE + " text, " 
				+ INCOMMING + " boolean not null, " 
				+ REPLICATED + " boolean not null,"//6
				+ MIMETYPE + " text);";//6
	}

	@Override
	protected String getTableName() {
		return MEDIACACHE_TABLE;
	}

	@Override
	public boolean insert(Object o) {
		return false;
	}

	public boolean addIncommingObject(Long identifier, String remoteFile, Long runid) {
		MediaCacheItem mciOld = queryById(identifier);
//		try {
		if (mciOld == null || !remoteFile.equals(mciOld.getRemoteFile())) {
			ContentValues initialValues = new ContentValues();
			initialValues.put(ITEM_ID, ""+identifier);
			initialValues.put(REMOTE_FILE, remoteFile);
			initialValues.put(RUN_ID, runid);
			initialValues.put(INCOMMING, true);
			initialValues.put(REPLICATED, false);
			long i = db.getSQLiteDb().insert(getTableName(), null, initialValues);
			return i != -1;
		}
//		} catch (android.database.sqlite.SQLiteConstraintException e) {
//			e.printStackTrace();
//		}
		return false;
	}
	
	public boolean addOutgoingObject(MediaCacheItem mci) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(ITEM_ID, mci.getItemId());
		initialValues.put(LOCAL_FILE, mci.getLocalFile());
		initialValues.put(INCOMMING, false);
		initialValues.put(RUN_ID, mci.getRunId());
		initialValues.put(ACCOUNT, mci.getAccount());
		initialValues.put(REPLICATED, false);
		initialValues.put(MIMETYPE, mci.getMimetype());
		long i = db.getSQLiteDb().insert(getTableName(), null, initialValues);
		return i != -1;
	}

	@Override
	public int delete(Object o) {
		return 0;
	}

	
	public Object[] query() {
		return null;
	}

	
	public MediaCacheItem queryById(Object id) {
		try {
			return query(ITEM_ID + "= ?", new String[] { (String) id }, 1)[0];
		} catch (Exception e) {
			return null;
		}
	}

	public MediaCacheItem getNextUnsyncedItem() {
		MediaCacheItem[] results = query(REPLICATED + " = ? ", new String[] { "0" }, 1);
		if (results.length == 0)
			return null;
		return results[0];
	}

	private MediaCacheItem[] query(String selection, String[] selectionArgs, int amount) {
		MediaCacheItem[] resultGenIt = null;
		Cursor mCursor = null;
		try {
			mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
			if (amount > mCursor.getCount())
				amount = mCursor.getCount();
			if (amount == 0)
				amount = mCursor.getCount();

			resultGenIt = new MediaCacheItem[amount];
			int i = 0;

			while (mCursor.moveToNext() && i < amount) {
				MediaCacheItem gi = new MediaCacheItem();
				gi.setItemId(mCursor.getString(0));
				gi.setRunId(mCursor.getLong(1));
				gi.setAccount(mCursor.getString(2));
				gi.setLocalFile(mCursor.getString(3));
				gi.setRemoteFile(mCursor.getString(4));
				gi.setIncomming(mCursor.getInt(5) == 1);
				gi.setReplicated(mCursor.getInt(6) == 1);
				gi.setMimetype(mCursor.getString(7));
				resultGenIt[i++] = gi;
			}
			

		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		} finally {
			if (mCursor != null) mCursor.close();
		}
		return resultGenIt;
	}

	public void updateLocalPath(String itemId, String localPath) {
		ContentValues newValue = new ContentValues();
		newValue.put(REPLICATED, true);
		newValue.put(LOCAL_FILE, localPath);
		db.getSQLiteDb().update(getTableName(), newValue, ITEM_ID + "= ?", new String[] { "" + itemId });
	}
	
	public void updateRemotePath(String itemId, String remotePath) {
		ContentValues newValue = new ContentValues();
		newValue.put(REPLICATED, true);
		newValue.put(REMOTE_FILE, remotePath);
		db.getSQLiteDb().update(getTableName(), newValue, ITEM_ID + "= ?", new String[] { "" + itemId });
	}

	public File getLocalFile(String audioFeed) {
		MediaCacheItem[] results = query(REPLICATED + " = ? and " + REMOTE_FILE + " = ?", new String[] { "1", audioFeed }, 1);
		if (results.length == 0)
			return null;
		File returnFile = new File(results[0].getLocalFile());
		if (!returnFile.exists())
			return null;
		return returnFile;
	}
	
	public File getLocalFileFromId(String identifier) {
		MediaCacheItem[] results = query(REPLICATED + " = ? and " + ITEM_ID + " = ?", new String[] { "1", identifier }, 1);
		if (results.length == 0)
			return null;
		File returnFile = new File(results[0].getLocalFile());
		if (!returnFile.exists())
			return null;
		return returnFile;
	}
	
	public File getLocalFileFromIdIgnoreReplication(String identifier) {
		MediaCacheItem[] results = query(ITEM_ID + " = ?", new String[] {""+identifier }, 1);
		if (results.length == 0)
			return null;
		if (results[0].getLocalFile() == null) return null;
		File returnFile = new File(results[0].getLocalFile());
		if (!returnFile.exists())
			return null;
		return returnFile;
	}

	public int deleteRun(long runId) {
		int j = db.getSQLiteDb().delete(getTableName(), RUN_ID + " =  " + runId, null);
		return j; 
		
		
	}

}
