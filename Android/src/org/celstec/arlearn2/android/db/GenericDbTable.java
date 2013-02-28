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

public abstract class GenericDbTable {

	protected DBAdapter db;
	
	public GenericDbTable(DBAdapter db) {
		this.db = db;
	}
	
	public abstract String createStatement();

	protected abstract String getTableName();

//	public abstract boolean insert(Object o);
	
	public int delete(Object o){ return -1;}
	
	
//	public abstract Object[] query();
	

	public String dropStatement() {
		return "DROP TABLE IF EXISTS "+getTableName();
	}
	
	public String eraseAllStatement() {
		return "DELETE FROM "+getTableName();
	}

}
