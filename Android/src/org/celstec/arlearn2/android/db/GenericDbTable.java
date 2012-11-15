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
