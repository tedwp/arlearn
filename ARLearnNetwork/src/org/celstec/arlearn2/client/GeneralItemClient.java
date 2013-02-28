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
	
	public GeneralItemList getGameGeneralItems(String token, Long gameId, Long from) {
		return (GeneralItemList) executeGet(getUrlPrefix()+"/gameId/"+gameId + "?from="+from, token, GeneralItemList.class);
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
