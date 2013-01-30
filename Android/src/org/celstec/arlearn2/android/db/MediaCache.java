package org.celstec.arlearn2.android.db;

import java.io.File;

import org.celstec.arlearn2.android.activities.ListMapItemsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.asynctasks.ActivityUpdater;
import org.celstec.arlearn2.android.db.beans.MediaCacheItem;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Message;
import android.util.Log;

public class MediaCache {// extends GenericDbTable {

//	public static final String MEDIACACHE_TABLE = "mediaCache";
//	public static final String ITEM_ID = "itemId";
//	public static final String RUN_ID = "runId";
//	public static final String ACCOUNT = "account";
//	public static final String LOCAL_FILE = "localFile";
//	public static final String URI = "localUri";
//	public static final String REMOTE_FILE = "remoteFile";
//	public static final String INCOMMING = "incomming";
//	public static final String REPLICATED = "replicated";
//	public static final String MIMETYPE = "mimetype";
//	public static final String BYTESTOTAL = "bytesTotal";
//	public static final String BYTESAVAILABLE = "bytesAvailable";
//	public static final String LASTBYTESDATE = "lastBytesDate";
//	public static final String APPENGINEFILEPATH = "appengineFilepath";
//
//	public static final int REP_STATUS_TODO = 0;
//	public static final int REP_STATUS_SYNCING = 1;
//	public static final int REP_STATUS_DONE = 2;
//
//	public MediaCache(DBAdapter db) {
//		super(db);
//	}
//
//	@Override
//	public String createStatement() {
//		return "create table " + MEDIACACHE_TABLE + " (" + 
//				ITEM_ID + " text primary key, " // 0
//				+ RUN_ID + " long, " + 
//				ACCOUNT + " text, " + 
//				LOCAL_FILE + " text, " // 3
//				+ REMOTE_FILE + " text, " + 
//				INCOMMING + " boolean not null, " + 
//				REPLICATED + " integer, "// 6
//				+ MIMETYPE + " text, " + 
//				URI + " text, "// 8
//				+ BYTESTOTAL + " long, " + 
//				BYTESAVAILABLE + " long, " + 
//				APPENGINEFILEPATH + " text, " + 
//				LASTBYTESDATE + " long);";
//	}
//
//	@Override
//	protected String getTableName() {
//		return MEDIACACHE_TABLE;
//	}
//
//	public boolean addIncommingObject(Long identifier, String remoteFile, Long runid) {
//		MediaCacheItem mciOld = queryById(identifier);
//		// try {
//		if (mciOld == null || !remoteFile.equals(mciOld.getRemoteFile())) {
//			ContentValues initialValues = new ContentValues();
//			initialValues.put(ITEM_ID, "" + identifier);
//			initialValues.put(REMOTE_FILE, remoteFile);
//			initialValues.put(RUN_ID, runid);
//			initialValues.put(INCOMMING, true);
//			initialValues.put(REPLICATED, REP_STATUS_TODO);
//			long i = db.getSQLiteDb().insert(getTableName(), null, initialValues);
//			return i != -1;
//		}
//		// } catch (android.database.sqlite.SQLiteConstraintException e) {
//		// e.printStackTrace();
//		// }
//		return false;
//	}
//
//	// public boolean addOutgoingObject(MediaCacheItem mci) {
//	// ContentValues initialValues = new ContentValues();
//	// initialValues.put(ITEM_ID, mci.getItemId());
//	// initialValues.put(LOCAL_FILE, mci.getLocalFile());
//	// initialValues.put(URI, mci.getUri().toString());
//	// initialValues.put(INCOMMING, false);
//	// initialValues.put(RUN_ID, mci.getRunId());
//	// initialValues.put(ACCOUNT, mci.getAccount());
//	// initialValues.put(REPLICATED, REP_STATUS_TODO);
//	// initialValues.put(MIMETYPE, mci.getMimetype());
//	// long i = db.getSQLiteDb().insert(getTableName(), null, initialValues);
//	// return i != -1;
//	// }
//
//	@Override
//	public int delete(Object o) {
//		return 0;
//	}
//
//	public Object[] query() {
//		return null;
//	}
//
//	public MediaCacheItem queryById(Object id) {
//		try {
//			return query(ITEM_ID + "= ?", new String[] { (String) id }, 1)[0];
//		} catch (Exception e) {
//			return null;
//		}
//	}
//
//	public MediaCacheItem getNextUnsyncedItem() {
//		MediaCacheItem[] results = query(REPLICATED + " = ? ", new String[] { "" + REP_STATUS_TODO }, 1);
//		if (results.length == 0)
//			return null;
//		return results[0];
//	}
//
//	public MediaCacheItem[] getNextUnsyncedItems() {
//		return query(REPLICATED + " = ? ", new String[] { "" + REP_STATUS_TODO }, 1);
//
//	}
//
//	private MediaCacheItem[] query(String selection, String[] selectionArgs, int amount) {
//		MediaCacheItem[] resultGenIt = null;
//		Cursor mCursor = null;
//		try {
//			mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
//			if (amount > mCursor.getCount())
//				amount = mCursor.getCount();
//			if (amount == 0)
//				amount = mCursor.getCount();
//
//			resultGenIt = new MediaCacheItem[amount];
//			int i = 0;
//
//			while (mCursor.moveToNext() && i < amount) {
//				
//				resultGenIt[i++] = cursorToMediaCacheItem(mCursor);
//			}
//
//		} catch (SQLException e) {
//			Log.e("sqlex", "ex", e);
//		} finally {
//			if (mCursor != null)
//				mCursor.close();
//		}
//		return resultGenIt;
//	}
//	
//	private MediaCacheItem cursorToMediaCacheItem(Cursor mCursor ) {
//		MediaCacheItem mci = new MediaCacheItem();
//		mci.setItemId(Long.parseLong(mCursor.getString(0))); //TODO this will make it crash
//		mci.setRunId(mCursor.getLong(1));
//		mci.setAccount(mCursor.getString(2));
//		mci.setLocalFile(mCursor.getString(3));
//		String uri = mCursor.getString(8);
//		if (uri != null && !"".equals(uri))
//			mci.setUri(Uri.parse(uri));
//		mci.setRemoteFile(mCursor.getString(4));
//		mci.setIncomming(mCursor.getInt(5) == 1);
//		mci.setReplicated(mCursor.getInt(6));
//		mci.setMimetype(mCursor.getString(7));
//		return mci;
//	}
//
//	public void updateLocalPath(String itemId, String localPath) {
//		ContentValues newValue = new ContentValues();
//		newValue.put(REPLICATED, REP_STATUS_DONE);
//		newValue.put(LOCAL_FILE, localPath);
//		db.getSQLiteDb().update(getTableName(), newValue, ITEM_ID + "= ?", new String[] { "" + itemId });
//	}
//
//	public void updateRemotePath(final String itemId, final String remotePath) {
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
//		m.obj = new DBAdapter.DatabaseTask() {
//			@Override
//			public void execute(DBAdapter db) {
//				ContentValues newValue = new ContentValues();
//				newValue.put(REMOTE_FILE, remotePath);
//				db.getSQLiteDb().update(getTableName(), newValue, ITEM_ID + "= ?", new String[] { "" + itemId });
//			}
//		};
//		m.sendToTarget();
//
//	}
//
//	public void setReplicationStatus(final String itemId, final int replicationStatus) {
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
//		m.obj = new DBAdapter.DatabaseTask() {
//			@Override
//			public void execute(DBAdapter db) {
//				setReplicationStatus(db, itemId, replicationStatus);
//
//			}
//		};
//		m.sendToTarget();
//	}
//	
//	public void setReplicationStatus(DBAdapter db, final String itemId, final int replicationStatus) {
//		ContentValues newValue = new ContentValues();
//		newValue.put(REPLICATED, replicationStatus);
//		db.getSQLiteDb().update(getTableName(), newValue, ITEM_ID + "= ?", new String[] { "" + itemId });
//		MediaCacheItem mci = queryById(itemId);
//		org.celstec.arlearn2.android.cache.MediaCache.getInstance().putReplicationstatus(mci.getRemoteFile(), replicationStatus);
//		ActivityUpdater.updateActivities(db.getContext(), NarratorItemActivity.class.getCanonicalName(), ListMessagesActivity.class.getCanonicalName(),
//				ListMapItemsActivity.class.getCanonicalName());
//	}
//
//	public void resetOnGoingSyncs() {
//		ContentValues newValue = new ContentValues();
//		newValue.put(REPLICATED, REP_STATUS_TODO);
//		db.getSQLiteDb().update(getTableName(), newValue, REPLICATED + "= ?", new String[] { "" + REP_STATUS_SYNCING });
//		
//	}
//	
//	public File getLocalFile(String audioFeed) {
//		MediaCacheItem[] results = query(REPLICATED + " = ? and " + REMOTE_FILE + " = ?", new String[] { "1", audioFeed }, 1);
//		if (results.length == 0)
//			return null;
//		File returnFile = new File(results[0].getLocalFile());
//		if (!returnFile.exists())
//			return null;
//		return returnFile;
//	}
//
//	public File getLocalFileFromId(String identifier) {
//		MediaCacheItem[] results = query(REPLICATED + " = ? and " + ITEM_ID + " = ?", new String[] { "" + REP_STATUS_DONE, identifier }, 1);
//		if (results.length == 0)
//			return null;
//		File returnFile = new File(results[0].getLocalFile());
//		if (!returnFile.exists())
//			return null;
//		return returnFile;
//	}
//
//	public File getLocalFileFromIdIgnoreReplication(String identifier) {
//		MediaCacheItem[] results = query(ITEM_ID + " = ?", new String[] { "" + identifier }, 1);
//		if (results.length == 0)
//			return null;
//
//		if (results[0].getLocalFile() == null)
//			return null;
//		File returnFile = new File(results[0].getLocalFile());
//		if (!returnFile.exists())
//			return null;
//		return returnFile;
//	}
//
//	public Uri getUriFromIdIgnoreReplication(String identifier) {
//		MediaCacheItem[] results = query(ITEM_ID + " = ?", new String[] { "" + identifier }, 1);
//		if (results.length == 0)
//			return null;
//		if (results[0].getUri() == null && results[0].getLocalFile() != null)
//			return Uri.fromFile(new File(results[0].getLocalFile()));
//		return results[0].getUri();
//	}
//
//	public int getReplicationStatus(String remoteFile) {
//		MediaCacheItem[] results = query(REMOTE_FILE + " = ?", new String[] { remoteFile }, 1);
//		if (results.length == 0)
//			return -1;
//		return (results[0].getReplicated());
//	}
//
//	public int deleteRun(long runId) {
//		int j = db.getSQLiteDb().delete(getTableName(), RUN_ID + " =  " + runId, null);
//		return j;
//
//	}
//
//	public void registerTotalAmountofBytes(final Uri uri, final int totalAmountOfBytes) {
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
//		m.obj = new DBAdapter.DatabaseTask() {
//			@Override
//			public void execute(DBAdapter db) {
//				ContentValues newValue = new ContentValues();
//				newValue.put(BYTESTOTAL, totalAmountOfBytes);
//				newValue.put(LASTBYTESDATE, System.currentTimeMillis());
//				db.getSQLiteDb().update(getTableName(), newValue, URI + "= ?", new String[] { uri.toString() });
//			}
//		};
//		m.sendToTarget();
//	}
//
//	public void registerTotalAmountofBytes(final String remoteFile, final long totalAmountOfBytes) {
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
//		m.obj = new DBAdapter.DatabaseTask() {
//			@Override
//			public void execute(DBAdapter db) {
//				ContentValues newValue = new ContentValues();
//				newValue.put(BYTESTOTAL, totalAmountOfBytes);
//				newValue.put(LASTBYTESDATE, System.currentTimeMillis());
//				db.getSQLiteDb().update(getTableName(), newValue, REMOTE_FILE + "= ?", new String[] { remoteFile });
//			}
//		};
//		m.sendToTarget();
//	}
//
//	public void registerBytesAvailable(final Uri uri, final long bytesAvailable) {
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
//		m.obj = new DBAdapter.DatabaseTask() {
//			@Override
//			public void execute(DBAdapter db) {
//				long bytesAvailableCopy = bytesAvailable;
//				if (bytesAvailableCopy < 0)
//					bytesAvailableCopy = 0;
//				ContentValues newValue = new ContentValues();
//				newValue.put(BYTESAVAILABLE, bytesAvailableCopy);
//				newValue.put(LASTBYTESDATE, System.currentTimeMillis());
//				db.getSQLiteDb().update(getTableName(), newValue, URI + "= ?", new String[] { uri.toString() });
//				MediaCacheItem mci = query(URI + "= ?", new String[] { uri.toString() }, 1)[0];
//				
//				Cursor mCursor = null;
//				try {
//					mCursor = db.getSQLiteDb().query(true, getTableName(), null, URI + "= ?", new String[] { uri.toString() }, null, null, null, null);
//					int i = 0;
//
//					while (mCursor.moveToNext()) {
//						long total = mCursor.getLong(9);
//						long available = mCursor.getLong(10);
//						org.celstec.arlearn2.android.cache.MediaCache.getInstance().putPercentageUploaded(mCursor.getString(4), getPercentageUpload(total, available));
//					}
//
//				} catch (SQLException e) {
//					Log.e("sqlex", "ex", e);
//				} finally {
//					if (mCursor != null)
//						mCursor.close();
//				}
//				
//				ActivityUpdater.updateActivities(db.getContext(), NarratorItemActivity.class.getCanonicalName());
//			}
//		};
//		m.sendToTarget();
//
//	}
//
//	public void registerBytesAvailable(final String remoteFile, final long bytesAvailable) {
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
//		m.obj = new DBAdapter.DatabaseTask() {
//			@Override
//			public void execute(DBAdapter db) {
//				long bytesAvailableCopy = bytesAvailable;
//				if (bytesAvailableCopy < 0)
//					bytesAvailableCopy = 0;
//				ContentValues newValue = new ContentValues();
//				newValue.put(BYTESAVAILABLE, bytesAvailableCopy);
//				newValue.put(LASTBYTESDATE, System.currentTimeMillis());
//				db.getSQLiteDb().update(getTableName(), newValue, REMOTE_FILE + "= ?", new String[] { remoteFile });
//				ActivityUpdater.updateActivities(db.getContext(), NarratorItemActivity.class.getCanonicalName(), ListMessagesActivity.class.getCanonicalName(),
//						ListMapItemsActivity.class.getCanonicalName());
//			}
//		};
//		m.sendToTarget();
//	}
//
//	public double getPercentageUploaded(String remoteFile) {
//		Cursor mCursor = null;
//		try {
//			mCursor = db.getSQLiteDb().query(true, getTableName(), null, REMOTE_FILE + " = ?", new String[] { remoteFile }, null, null, null, null);
//			int i = 0;
//
//			if (mCursor.moveToNext()) {
//				long total = mCursor.getLong(9);
//				long available = mCursor.getLong(10);
//
//				if (available == 0)
//					return 1;
//				return (((double) (total - available)) / total);
//			}
//
//		} catch (SQLException e) {
//			Log.e("sqlex", "ex", e);
//		} finally {
//			if (mCursor != null)
//				mCursor.close();
//		}
//		return 1;
//	}
//
//	public void reset() {
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
//
//		m.obj = new DBAdapter.DatabaseTask() {
//			@Override
//			public void execute(DBAdapter db) {
//				ContentValues newValue = new ContentValues();
//				newValue.put(BYTESTOTAL, 0);
//				newValue.put(BYTESAVAILABLE, 0);
//				newValue.put(LASTBYTESDATE, 0);
//				newValue.put(REPLICATED, REP_STATUS_TODO);
//				long date = System.currentTimeMillis() - 60000;
//				db.getSQLiteDb().update(getTableName(), newValue, LASTBYTESDATE + "< ? and " + REPLICATED + " = ?", new String[] { "" + date, "" + REP_STATUS_SYNCING });
//			}
//		};
//		m.sendToTarget();
//	}
//
//	public void addOutgoingObject(MediaCacheItem mci) {
//		AddOutgoingObject pa = new AddOutgoingObject();
//		pa.mci = mci;
//		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
//		m.obj = pa;
//		m.sendToTarget();
//	}
//
//	@Deprecated
//	public class AddOutgoingObject implements DBAdapter.DatabaseTask {
//		public MediaCacheItem mci;
//
//		@Override
//		public void execute(DBAdapter db) {
//			org.celstec.arlearn2.android.cache.MediaCache.getInstance().put(mci, 0);
//			ContentValues initialValues = new ContentValues();
//			initialValues.put(ITEM_ID, mci.getItemId());
//			initialValues.put(LOCAL_FILE, mci.getLocalFile());
//			initialValues.put(URI, mci.getUri().toString());
//			initialValues.put(INCOMMING, false);
//			initialValues.put(RUN_ID, mci.getRunId());
//			initialValues.put(ACCOUNT, mci.getAccount());
//			initialValues.put(REPLICATED, REP_STATUS_TODO);
//			initialValues.put(MIMETYPE, mci.getMimetype());
//			db.getSQLiteDb().insert(getTableName(), null, initialValues);
//		}
//	}
//
////	public void queryUploadStatus(final long runId, final UploadStatus uploadResult) {
////		Message m = Message.obtain(DBAdapter.getDatabaseThread(db.getContext()));
////		m.obj = new DBAdapter.DatabaseTask() {
////			@Override
////			public void execute(DBAdapter db) {
////				Cursor mCursor = null;
////				try {
////					mCursor = db.getSQLiteDb().query(true, getTableName(), null, RUN_ID + " = ?", new String[] { ""+runId }, null, null, null, null);
////					int i = 0;
////
////					while (mCursor.moveToNext()) {
////						long total = mCursor.getLong(9);
////						long available = mCursor.getLong(10);
////						
////						uploadResult.onResults(mCursor.getString(4), mCursor.getInt(6), getPercentageUpload(total, available));
////					}
////
////				} catch (SQLException e) {
////					Log.e("sqlex", "ex", e);
////				} finally {
////					if (mCursor != null)
////						mCursor.close();
////				}
////				
////				ActivityUpdater.updateActivities(db.getContext(), NarratorItemActivity.class.getCanonicalName());
////			}
////		};
////		m.sendToTarget();
////		
////	}
//	
//	public Double getPercentageUpload(long total, long available) {
//		double percentage;
//		if (total == 0) {
//			percentage = 1;
//		} else {
//			percentage = (((double) (total - available)) / total);
//		}
//		if (available == 0)
//			percentage = 1;
//		return percentage;
//	}
//	
////	public interface UploadStatus {
////		
////		public void onResults(String remoteFilePath, int repStatus, double percentage);
////	}
//
//	public void queryAll(DBAdapter db, Long runId) {
//		Cursor mCursor = null;
//		try {
//			mCursor = db.getSQLiteDb().query(true, getTableName(), null, RUN_ID + " = ?", new String[] { ""+runId }, null, null, null, null);
//			int i = 0;
//
//			while (mCursor.moveToNext()) {
//				long total = mCursor.getLong(9);
//				long available = mCursor.getLong(10);
//				org.celstec.arlearn2.android.cache.MediaCache.getInstance().put(cursorToMediaCacheItem(mCursor), getPercentageUpload(total, available));
//				
//			}
//
//		} catch (SQLException e) {
//			Log.e("sqlex", "ex", e);
//		} finally {
//			if (mCursor != null)
//				mCursor.close();
//		}
//		
//	}
//
//	

}
