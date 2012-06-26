package org.celstec.arlearn2.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class VisibleItemsList {

	private List<VisibleItem> visibleItems = new ArrayList<VisibleItem>();
	private HashMap<String, VisibleItem> map = new HashMap<String, VisibleItem>();
	
	
//	public void log(Logger log) {
//		
//for (Iterator iterator = visibleItems.iterator(); iterator.hasNext();) {
//	VisibleItem type = (VisibleItem) iterator.next();
//	log.severe("contains "+type.getGeneralItemId());
//}
//		
//	}
	
	public List<VisibleItem> getVisibleItems() {
		return visibleItems;
	}
	public void setVisibleItems(List<VisibleItem> visibleItems) {
		this.visibleItems = visibleItems;
		map = new HashMap<String, VisibleItem>();
		for(VisibleItem vi: visibleItems) {
			map.put(vi.getGeneralItemId(), vi);
		}
	}
	
	public VisibleItem getVisibleItem(String generalItemId){
		return map.get(generalItemId);
	}
	
	public void merge(VisibleItemsList vil) {
		visibleItems.addAll(vil.visibleItems);
		for(VisibleItem vi: vil.visibleItems) {
			map.put(vi.getGeneralItemId(), vi);
		}
	}
	
	
	
}
