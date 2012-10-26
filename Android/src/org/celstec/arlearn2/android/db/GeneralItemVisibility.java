package org.celstec.arlearn2.android.db;

public class GeneralItemVisibility extends GenericDbTable {

	public static final String GENERALITEM_VISIBILITY_TABLE = "generalItemsVisibility";

	
	public GeneralItemVisibility(DBAdapter db) {
		super(db);
	}
	
	@Override
	public String createStatement() {
		 return "create table " + GENERALITEM_VISIBILITY_TABLE + " (" 
				+ ID + " text, " //0 
				+ SHOW_AT + " long , "  
				+ RUNID + " long ,"
//				+ DEPENDENCY_VISIBLE+" boolean, "
				+ VISIBILITY_STATUS+" int, " //11
				+ FIRST_READ + " long, "
				+ DELETED + " boolean, "
				+ LAST_MODIFICATION_DATE + " long, "
				+ SORTKEY + " long );";;
	}

	@Override
	protected String getTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insert(Object o) {
		// TODO Auto-generated method stub
		return false;
	}

}
