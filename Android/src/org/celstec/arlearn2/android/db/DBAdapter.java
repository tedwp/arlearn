package org.celstec.arlearn2.android.db;


import java.util.concurrent.Semaphore;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
	
	static protected Semaphore semaphore = new Semaphore(1);

	
	public static final int RUN_ADAPTER = 0;
	public static final int GENERALITEM_ADAPTER = 1;
	public static final int MYLOCATIONS_ADAPTER = 2;
	public static final int MYRESPONSES_ADAPTER = 3;
	public static final int MYACTIONS_ADAPTER = 4;
	public static final int MEDIA_CACHE = 5;
	public static final int GAME_ADAPTER = 6;
	public static final int GENERIC_JSON_ADAPTER = 7;
	
	private GenericDbTable[] allTables;

	private final Context context;
    private DbOpenHelper DBHelper;
    private SQLiteDatabase db;


	public DBAdapter(Context ctx) {
        this.context = ctx;
        if (allTables == null) initAllTables();
        DBHelper = new DbOpenHelper(context);
    }
	
	private void initAllTables() {
		allTables = new GenericDbTable[8];
		allTables[RUN_ADAPTER] = new RunAdapter(this);
		allTables[GENERALITEM_ADAPTER] = new GeneralItemAdapter(this);
		allTables[MYLOCATIONS_ADAPTER] = new MyLocations(this);
		allTables[MYRESPONSES_ADAPTER] = new MyResponses(this);
		allTables[MYACTIONS_ADAPTER] = new MyActions(this);
		allTables[MEDIA_CACHE] = new MediaCache(this);
		allTables[GAME_ADAPTER] = new GameAdapter(this);
		allTables[GENERIC_JSON_ADAPTER] = new GenericJsonAdapter(this);
	}
	
	public RunAdapter getRunAdapter() {
		return (RunAdapter) allTables[RUN_ADAPTER];
	}
	
	public GameAdapter getGameAdapter() {
		return (GameAdapter) allTables[GAME_ADAPTER];
	}
	
	public GenericJsonAdapter getGenericJsonAdapter() {
		return (GenericJsonAdapter) allTables[GENERIC_JSON_ADAPTER];
	}
	
	public void deleteRun(long currentRunId) {
		((RunAdapter) allTables[RUN_ADAPTER]).delete(currentRunId);
		((GeneralItemAdapter) allTables[GENERALITEM_ADAPTER]).deleteRun(currentRunId);
		((MyResponses) allTables[MYRESPONSES_ADAPTER]).deleteRun(currentRunId);
		((MyActions) allTables[MYACTIONS_ADAPTER]).deleteRun(currentRunId);
		// TODO locations?
		
	}
	
	public class DbOpenHelper extends SQLiteOpenHelper {
	    private static final int DATABASE_VERSION = 98;
	    private static final String DATABASE_NAME = "arlearn2";
	   
	    DbOpenHelper(Context context) {
	        super(context, DATABASE_NAME, null, DATABASE_VERSION);
	    }

	    
	    public void onCreate(SQLiteDatabase db) {
	    	for (int i = 0; i < allTables.length; i++) {
	    		if (allTables[i].createStatement()!= null) db.execSQL(allTables[i].createStatement());
			}
	    }

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w("upgrade", "Upgrading database from version " + oldVersion 
	                  + " to "+ newVersion);
			for (int i = 0; i < allTables.length; i++) {
				if (allTables[i].dropStatement() != null) db.execSQL(allTables[i].dropStatement());
			}  
            onCreate(db);
		}

	}
	
    
	public DBAdapter openForWrite() throws SQLException {
		try {
			semaphore.acquire();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
//		try {
	        db = DBHelper.getWritableDatabase();
	        return this;
        
//		} catch (android.database.sqlite.SQLiteException e) {
//			return openForWrite(0);
//		}
    }
	
	public DBAdapter openForWrite(int trail) throws SQLException {
		
		try {
	        db = DBHelper.getWritableDatabase();
	        return this;
		} catch (android.database.sqlite.SQLiteException e) {
			if (trail > 20) {
				Log.e("error", "retried getting a connection more then 20 times", e);
				throw e;
			} else {
				try {
					Thread.sleep(100);
					return openForWrite(trail+1);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					Log.e("error", "got interruptedException", e1);
					throw e;
				}
			}
			
		}
    }
    
    public DBAdapter openForRead() {
    	try {
			semaphore.acquire();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		}
//        try {
        	db = DBHelper.getReadableDatabase();
            return this;
        
//		} catch (android.database.sqlite.SQLiteException e) {
//			return openForRead(0);
//		}
    }
    
    public DBAdapter openForRead(int trail) {
        try {
        	db = DBHelper.getReadableDatabase();
            return this;
		} catch (android.database.sqlite.SQLiteException e) {
			if (trail > 20) {
				Log.e("error", "retried getting a connection more then 20 times", e);
				throw e;
			} else {
				try {
					Thread.sleep(100);
					return openForWrite(trail+1);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
					Log.e("error", "got interruptedException", e1);
					throw e;
				}
			}
			
		}
    }
    
    public void close() {
		semaphore.release();
        DBHelper.close();
    }
    
    protected SQLiteDatabase getSQLiteDb() {
    	return db;
    }
    
    public void eraseAllData() {
    	for (int i = 0; i < allTables.length; i++) {
    		String statement =allTables[i].eraseAllStatement();
			if (statement != null) db.execSQL(statement);
		}  
    }
    
    public GenericDbTable table(int i) {
    	return allTables[i];
    }

    public Context getContext() {
    	return context;
    }
	
}
