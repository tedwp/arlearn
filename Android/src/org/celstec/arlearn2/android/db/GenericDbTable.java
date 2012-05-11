package org.celstec.arlearn2.android.db;

public abstract class GenericDbTable {

	protected DBAdapter db;
	
	public GenericDbTable(DBAdapter db) {
		this.db = db;
	}
	
	public abstract String createStatement();

	protected abstract String getTableName();

	public abstract boolean insert(Object o);
	
	public abstract int delete(Object o);
	
//	public abstract Object[] query();
	
	public abstract Object queryById(Object id);
	
	public String dropStatement() {
		return "DROP TABLE IF EXISTS "+getTableName();
	}
	
	public String eraseAllStatement() {
		return "DELETE FROM "+getTableName();
	}

}
