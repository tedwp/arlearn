/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.db;

import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MediaCacheGeneralItems extends GenericDbTable {
	
	public static final String MEDIACACHE_TABLE = "mediaCacheGeneralItems";

	public static final String ITEM_ID = "itemId";
	public static final String LOCAL_ID = "localId";
	public static final String GAME_ID = "gameId";

	public static final String REMOTE_FILE = "remoteFile";
	public static final String URI = "localUri";
	public static final String REPLICATED = "replicated";
	public static final String MIMETYPE = "mimetype";
	public static final String BYTESTOTAL = "bytesTotal";
	public static final String BYTESAVAILABLE = "bytesAvailable";
    public static final String PREFFERED_FILE_NAME = "preferredFileName";
    public static final String MD5_HASH = "md5Hash";

	public static final int REP_STATUS_TODO = 0;
	public static final int REP_STATUS_SYNCING = 1;
	public static final int REP_STATUS_DONE = 2;
	public static final int REP_STATUS_FILE_NOT_FOUND = 3;
	
	public MediaCacheGeneralItems(DBAdapter db) {
		super(db);
	}

	@Override
	public String createStatement() {
		return "create table " + MEDIACACHE_TABLE + " (" +
				ITEM_ID + " long, " + //0
				LOCAL_ID + " text, " +
				GAME_ID + " long, " + 
				REMOTE_FILE + " text, " + //3 
				URI + " text, " +
				REPLICATED + " integer, " + //5
                MIMETYPE + " text, " + //5
                PREFFERED_FILE_NAME + " text, " + //5
                MD5_HASH + " text); " ;
				
	}

	@Override
	protected String getTableName() {
		return MEDIACACHE_TABLE;
	}
	

	
	private DownloadItem[] query(String selection, String[] selectionArgs, int amount) {
		DownloadItem[] resultGenIt = null;
		Cursor mCursor = null;
		try {
			mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
			if (amount > mCursor.getCount())
				amount = mCursor.getCount();
			if (amount == 0)
				amount = mCursor.getCount();

			resultGenIt = new DownloadItem[amount];
			int i = 0;

			while (mCursor.moveToNext() && i < amount) {
				
				resultGenIt[i++] = cursorToUploadItem(mCursor);
			}

		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		} finally {
			if (mCursor != null)
				mCursor.close();
		}
		return resultGenIt;
	}
	
	private DownloadItem cursorToUploadItem(Cursor mCursor ) {
		DownloadItem di = new DownloadItem();
		di.setItemId(mCursor.getLong(0));
		di.setLocalId(mCursor.getString(1));
		di.setGameId(mCursor.getLong(2));
		di.setRemoteUrl(mCursor.getString(3));
		String uri = mCursor.getString(4);
		if (uri != null && !"".equals(uri))
			di.setLocalPath(Uri.parse(uri));
		di.setReplicated(mCursor.getInt(5));
		di.setMimetype(mCursor.getString(6));
        di.setPreferredFileName(mCursor.getString(7));
        di.setMd5Hash(mCursor.getString(8));
		
		return di;
	}
	
	
	public void addToCache(long gameId) {
        JSONArray array = new JSONArray();
		for (DownloadItem di: query(GAME_ID + " = ?", new String[] { ""+gameId}, 0)) {
			switch (di.getReplicated()) {
			case REP_STATUS_DONE:
				MediaGeneralItemCache.getInstance(gameId).addDoneDownload(di);
                File md5File = new File(di.getLocalPath().getPath() + ".md5");
//                if (di.getItemId() == 4373001) {
//                    System.out.println("strange");
//                }
                if (md5File.exists()) {
                    try {
                        String md5String = deserializeString(md5File);
//                        Log.e("FILE", ""+di.getItemId()+ " " +di.getLocalId() + " " +md5String + "     -   " +di.getMd5Hash());
                        if (di.getMd5Hash() == null || !di.getMd5Hash().equals(md5String)) {
                            JSONObject description = new JSONObject();
                            description.put("itemId", di.getItemId());
                            description.put("md5Hash", md5String);
                            description.put("localId", di.getLocalId());
                            array.put(description);
                            if (array.length() == 20){
                                Log.e("FILE", ""+array.toString());
                                array = new JSONArray();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (JSONException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }


                }

				break;
			default:
				break;
			}
		}
        if (array.length() !=0) {
            Log.e("FILE", ""+array.toString());
        }

    }

    private String deserializeString(File file)
            throws IOException {
        int len;
        char[] chr = new char[4096];
        final StringBuffer buffer = new StringBuffer();
        final FileReader reader = new FileReader(file);
        try {
            while ((len = reader.read(chr)) > 0) {
                buffer.append(chr, 0, len);
            }
        } finally {
            reader.close();
        }
        return buffer.toString();
    }

    public void create(DownloadItem item) {
		db.getSQLiteDb().insert(getTableName(), null, getContentValues(item));
	}

	public void update(DownloadItem item) {
		db.getSQLiteDb().update(getTableName(), getContentValues(item),ITEM_ID + " = ? and "+LOCAL_ID +" = ?", new String[] { "" + item.getItemId(), item.getLocalId() });
	}
	
	private ContentValues getContentValues(DownloadItem item) {
		ContentValues values = new ContentValues();
		values.put(ITEM_ID, item.getItemId());
		values.put(LOCAL_ID, item.getLocalId());
		values.put(GAME_ID, item.getGameId());
		values.put(REMOTE_FILE, item.getRemoteUrl());
		if (item.getMimetype() != null) item.setMimetype(item.getMimetype());
		values.put(REPLICATED, item.getReplicated());
        if (item.getPreferredFileName() != null) values.put(PREFFERED_FILE_NAME, item.getPreferredFileName());
        if (item.getMd5Hash() != null) values.put(MD5_HASH, item.getMd5Hash());
		return values;
	}
	
	public DownloadItem getNextUnsyncedItem(Long gameId) {
		DownloadItem []  di = query(REPLICATED + " = ? and "+GAME_ID + " = ?", new String[] { "" + REP_STATUS_TODO, ""+gameId }, 0);
		MediaGeneralItemCache.getInstance(gameId).setAmountOfItemsToDownload(di.length);
		if (di.length == 0) return null;
		return di[0];
	}
	
	public DownloadItem get(Long itemId, String localId) {
		DownloadItem []  di = query(ITEM_ID + " = ? and "+LOCAL_ID +" = ?", new String[] { "" + itemId, localId }, 0);
		if (di.length == 0) return null;
		return di[0];
	}

    public MediaCacheGeneralItems.DownloadItem getReplicated(Long gameId, String remoteUrl) {
        DownloadItem []  di = query(REPLICATED + " = ? and "+GAME_ID + " = ? and "+REMOTE_FILE + " = ?", new String[] { "" + REP_STATUS_DONE, ""+gameId, remoteUrl }, 0);
        MediaGeneralItemCache.getInstance(gameId).setAmountOfItemsToDownload(di.length);
        if (di.length == 0) return null;
        return di[0];
    }
	
	public DownloadItem[] getDownloadItemsForGame(Long gameId) {
		return query(GAME_ID + " = ?", new String[] { ""+gameId }, 0);
	}

	public void setReplicationStatus(Long gameId, DownloadItem di, int status) {
		MediaGeneralItemCache.getInstance(gameId).putReplicationstatus(status, di);
		setReplicationStatus(di.getGameId(), di.getItemId(), di.getLocalId(), status, di.getLocalPath());
	}

	private void setReplicationStatus(final long gameId, final Long itemId, final String localId, final int replicationStatus, Uri localUri) {
		ContentValues newValue = new ContentValues();
		newValue.put(REPLICATED, replicationStatus);
		if (localUri != null) {
			newValue.put(URI, localUri.toString());
//			MediaCache.getInstance().putLocalURI(itemId, localId, localUri);
		}
		db.getSQLiteDb().update(getTableName(), newValue, ITEM_ID + "= ? and "+LOCAL_ID + " = ?", new String[] { "" + itemId, localId });
		
		
	}
	
	public static class DownloadItem {
		
		private long itemId;
		private String localId;
		private long gameId;
		private String remoteUrl;
		private Uri localPath;
		private int replicated;
		private  String mimetype;
        private String preferredFileName;
        private String md5Hash;

        public DownloadItem() {
			
		}
		public long getItemId() {
			return itemId;
		}
		public void setItemId(long itemId) {
			this.itemId = itemId;
		}
		public String getLocalId() {
			return localId;
		}
		public void setLocalId(String localId) {
			this.localId = localId;
		}
		public long getGameId() {
			return gameId;
		}
		public void setGameId(long gameId) {
			this.gameId = gameId;
		}
		public String getRemoteUrl() {
			return remoteUrl;
		}
		public void setRemoteUrl(String remoteUrl) {
			this.remoteUrl = remoteUrl;
		}
		public Uri getLocalPath() {
			return localPath;
		}
		public void setLocalPath(Uri localPath) {
			this.localPath = localPath;
		}
		public int getReplicated() {
			return replicated;
		}
		public void setReplicated(int replicated) {
			this.replicated = replicated;
		}
		public String getMimetype() {
			return mimetype;
		}
		public void setMimetype(String mimetype) {
			this.mimetype = mimetype;
		}
        public String getPreferredFileName() {
            return preferredFileName;
        }
        public void setPreferredFileName(String preferredFileName) {
            this.preferredFileName = preferredFileName;
        }

        public String getMd5Hash() {
            return md5Hash;
        }

        public void setMd5Hash(String md5Hash) {
            this.md5Hash = md5Hash;
        }
    }

	
}
