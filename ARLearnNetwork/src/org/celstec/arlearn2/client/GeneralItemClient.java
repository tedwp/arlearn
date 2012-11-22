package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;

public class GeneralItemClient extends GenericClient{
	private static GeneralItemClient instance;

	private GeneralItemClient() {
		super("/generalItems");
	}

	public static GeneralItemClient getGeneralItemClient() {
		if (instance == null) {
			instance = new GeneralItemClient();
		}
		return instance;
	}
	
	public GeneralItemList getGameGeneralItems(String token, Long gameId) {
		return (GeneralItemList) executeGet(getUrlPrefix()+"/gameId/"+gameId, token, GeneralItemList.class);
	}
	
	public GeneralItemList getRunGeneralItemsAll(String token, Long runId) {
		return (GeneralItemList) executeGet(getUrlPrefix()+"/runId/"+runId+"/all", token, GeneralItemList.class);
	}
	
	public GeneralItemList getRunGeneralItemsVisible(String token, Long runId) {
		return (GeneralItemList) executeGet(getUrlPrefix()+"/runId/"+runId, token, GeneralItemList.class);
	}
	
	public GeneralItemList getRunGeneralItemsDisappeared(String token, Long runId) {
		return (GeneralItemList) executeGet(getUrlPrefix()+"/runId/"+runId +"/disappeared", token, GeneralItemList.class);
	}
	
	public GeneralItem getRunGeneralItem(String token, Long runId, long itemId) {
		Object o = executeGet(getUrlPrefix()+"/runId/"+runId+"/generalItem/"+itemId, token, GeneralItem.class);
		if (o instanceof String) {
			System.out.println(o);
		}
		return (GeneralItem)  o;
	}
	
	public GeneralItem postGeneralItem( String token, String generalItem) {
		return (GeneralItem) executePost(getUrlPrefix(), token, generalItem, GeneralItem.class);
	}
	

}
