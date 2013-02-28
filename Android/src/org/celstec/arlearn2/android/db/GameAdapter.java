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

import org.celstec.arlearn2.android.cache.GameCache;
import org.celstec.arlearn2.android.db.RunAdapter.RunQuery;
import org.celstec.arlearn2.android.db.RunAdapter.RunResults;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.game.Config;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.run.Run;
import org.codehaus.jettison.json.JSONException;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Message;
import android.util.Log;

public class GameAdapter extends GenericDbTable {

	public static final String GAME_TABLE = "game";
	public static final String ID = "id";
	public static final String TITLE = "title";
	public static final String SCORING = "scoring";
	public static final String MAP_AVAILABLE = "mapAvailable";
	public static final String BEAN = "bean";
	public static final String OWNER = "owner";
	
	public GameAdapter(DBAdapter db) {
		super(db);
	}
	
	@Override
	public String createStatement() {
		return "create table " + GAME_TABLE + " (" 
				+ ID + " long primary key, " 
				+ TITLE + " text not null, " 
				+ SCORING + " boolean not null, "
				+ BEAN + " text , "
				+ MAP_AVAILABLE + " boolean not null, "
				+ OWNER + " text);";
	}

	@Override
	protected String getTableName() {
		return GAME_TABLE;
	}

	public boolean insert(Game o) {
		return insertGame((Game) o);
	}
	
	public boolean insertGame(Game g) {
		Game oldGame =  (Game) queryById(g.getGameId());
		if (oldGame != null) {
			if (!oldGame.equals(g)) {
				return update(g);
			} else {
				return false;	
			}
		}    	
    	return db.getSQLiteDb().insert(getTableName(), null, getContentValues(g)) != -1;	
	}
	
	private ContentValues getContentValues(Game g) {
		ContentValues cValues = new ContentValues();
		cValues.put(ID, g.getGameId());
        cValues.put(TITLE, g.getTitle());
        cValues.put(OWNER, g.getOwner());
        if (g.getConfig() != null) {
        	Config c = g.getConfig();
        	if (c.getMapAvailable() == null) c.setMapAvailable(true);
        	cValues.put(MAP_AVAILABLE, c.getMapAvailable());
        	if (c.getScoring() == null) c.setScoring(true);
        	cValues.put(SCORING, c.getScoring());
        }	
        cValues.put(BEAN, g.toString());
        return cValues;
	}

	private boolean update(Game g) {
		if (g.getError() != null) return false;
		return db.getSQLiteDb().update(getTableName(), getContentValues(g), ID+" = "+g.getGameId(), null) != -1;
	}

	@Override
	public int delete(Object o) {
		Game g = (Game) o;
		return (int) g.getGameId().longValue();
	}
	
	public int delete(Long id) {
		return db.getSQLiteDb().delete(getTableName(), ID+" = "+id, null);
	}

	public Object queryById(Object id) {
		try {
			return query(ID + "= ?", new String[] { ""+ id })[0];
		} catch (Exception e) {
			return null;
		}
		
	}
	
	public Game[] queryByOwner(String owner) {
		try {
			return query(OWNER + "= ?", new String[] { owner });
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
	private Game[] query(String selection, String[] selectionArgs) {
		Game[] resultGame = null;
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, selection, selectionArgs, null, null, null, null);
			resultGame = new Game[mCursor.getCount()];
			int i = 0;
			
			while (mCursor.moveToNext()) {
				resultGame[i++] = cursorToGame(mCursor);
			}
			mCursor.close();
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
		return resultGame;
	}

	private Game cursorToGame(Cursor mCursor) {
		Game game = null;
		try {
			game = (Game) JsonBeanDeserializer.deserialize(mCursor.getString(3));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		if (game == null) game = new Game();
		game.setGameId(mCursor.getLong(0));
		game.setTitle(mCursor.getString(1));
//		Config c = new Config();
//		c.setScoring(1==mCursor.getInt(2));
//		c.setMapAvailable(1==mCursor.getInt(4));
//		game.setConfig(c);
		return game;
	}

	public void queryAll() {
		Game[] resultsGame = null;
		try {
			Cursor mCursor = db.getSQLiteDb().query(true, getTableName(), null, null, null, null, null, null, null);
			resultsGame = new Game[mCursor.getCount()];
			int i = 0;
			while (mCursor.moveToNext()) {
				GameCache.getInstance().putGame(cursorToGame(mCursor));
			}
			mCursor.close();
		} catch (SQLException e) {
			Log.e("sqlex", "ex", e);
		}
	}
}
