package org.celstec.arlearn2.android.db;


import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
	public static final int GENERALTITEM_VISIBILITY_ADAPTER = 8;
	public static final int MEDIACACHE_GENERAL_ITEMS_ADAPTER = 9;
	
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
		allTables = new GenericDbTable[10];
		allTables[RUN_ADAPTER] = new RunAdapter(this);
		allTables[GENERALITEM_ADAPTER] = new GeneralItemAdapter(this);
		allTables[MYLOCATIONS_ADAPTER] = new MyLocations(this);
		allTables[MYRESPONSES_ADAPTER] = new MyResponses(this);
		allTables[MYACTIONS_ADAPTER] = new MyActions(this);
		allTables[MEDIA_CACHE] = new MediaCache(this);
		allTables[GAME_ADAPTER] = new GameAdapter(this);
		allTables[GENERIC_JSON_ADAPTER] = new GenericJsonAdapter(this);
		allTables[GENERALTITEM_VISIBILITY_ADAPTER] = new GeneralItemVisibility(this);
		allTables[MEDIACACHE_GENERAL_ITEMS_ADAPTER] = new MediaCacheGeneralItems(this);
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
	public GeneralItemVisibility getGeneralItemVisibility() {
		return (GeneralItemVisibility) allTables[GENERALTITEM_VISIBILITY_ADAPTER];
	}
	
	public GeneralItemAdapter getGeneralItemAdapter() {
		return (GeneralItemAdapter) allTables[GENERALITEM_ADAPTER];
	}
	
	public MyActions getMyActions() {
		return (MyActions) allTables[MYACTIONS_ADAPTER];
	}
	
	public MyResponses getMyResponses() {
		return (MyResponses) allTables[MYRESPONSES_ADAPTER];
	}
	
	public MediaCache getMediaCache() {
		return (MediaCache) allTables[MEDIA_CACHE];
	}
	
	public MediaCacheGeneralItems getMediaCacheGeneralItems() {
		return (MediaCacheGeneralItems) allTables[MEDIACACHE_GENERAL_ITEMS_ADAPTER];
	}
	
	public void deleteRun(long currentRunId) {
		((RunAdapter) allTables[RUN_ADAPTER]).delete(currentRunId);
		((GeneralItemVisibility) allTables[GENERALTITEM_VISIBILITY_ADAPTER]).deleteRun(currentRunId);
		
		((MyResponses) allTables[MYRESPONSES_ADAPTER]).deleteRun(currentRunId);
		((MyActions) allTables[MYACTIONS_ADAPTER]).deleteRun(currentRunId);
		// TODO locations?
		
	}
	
	public class DbOpenHelper extends SQLiteOpenHelper {
	    private static final int DATABASE_VERSION = 110;
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
	        db = DBHelper.getWritableDatabase();
	        return this;
    }
    
    public DBAdapter openForRead() {
        	db = DBHelper.getReadableDatabase();
            return this;
    }
    
    public void close() {
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
    
    
    public static final int DEPENDENCIES_MESSAGE = 1;
    
    private static DatabaseThread thread;
    public static DatabaseHandler getDatabaseThread(Context ctx) {
		startLatch = new CountDownLatch(1);
    	if (thread == null) {
    		thread = new DatabaseThread(ctx);
    		thread.start();
    	} else {
    		startLatch.countDown();
    	}
    	try {
			startLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	return thread.databaseHandler;
    	
    }
    
    private static CountDownLatch startLatch;
    public static DBAdapter getAdapter(Context ctx) {
    	if (staticDb != null) return staticDb;
    	getDatabaseThread(ctx);
    	return getAdapter(ctx);
    }
    
    private static DBAdapter staticDb;
    
    static class DatabaseThread extends Thread {
		
		
		private Context ctx;
		protected DatabaseHandler databaseHandler;
		
		
		public DatabaseThread(Context ctx) {
			this.ctx = ctx;
		}
		
		@Override
		public void run() {
			try {
				Looper.prepare();
				staticDb = new DBAdapter(ctx);
				staticDb.openForWrite();
				databaseHandler = new DatabaseHandler();
				startLatch.countDown();
				Looper.loop();
				staticDb.close();
			} catch (Throwable t) {
				Log.e("database", "database thread halted", t);
			}
		}
	}

    public static class DatabaseHandler extends Handler {
    	
		@Override
		public void handleMessage(Message message) {
			DatabaseTask task = (DatabaseTask) message.obj;
			long time = System.currentTimeMillis();
			try {
				task.execute(staticDb);
			} catch (Exception e) {
				Log.e("exception", "in databasehandler", e);
			}
			Log.i("DBTask", "end "+task.getClass().getCanonicalName()+" "+ (System.currentTimeMillis()-time));
		}
	}
    
    public interface DatabaseTask {
    	public void execute(DBAdapter db);

    }
	
}
