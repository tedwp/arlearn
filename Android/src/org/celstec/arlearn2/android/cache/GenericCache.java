package org.celstec.arlearn2.android.cache;



public class GenericCache {

	

	public static void emptyAllCaches() {
		ActionCache.getInstance().empty();

		GameCache.getInstance().empty();
		GeneralItemsCache.getInstance().empty();
		GeneralItemVisibilityCache.getInstance().empty();
		
		ResponseCache.getInstance().empty();
		RunCache.getInstance().empty();
	}
	
}
