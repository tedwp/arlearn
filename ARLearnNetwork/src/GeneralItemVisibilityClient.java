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
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.client.GenericClient;


public class GeneralItemVisibilityClient  extends GenericClient{
	private static GeneralItemVisibilityClient instance;

	private GeneralItemVisibilityClient() {
		super("/generalItemsVisibility");
	}

	public static GeneralItemVisibilityClient getGeneralItemClient() {
		if (instance == null) {
			instance = new GeneralItemVisibilityClient();
		}
		return instance;
	}
	
	public GeneralItemList getGeneralItemVisibilities(String token, Long runId, Long from) {
		return (GeneralItemList) executeGet(getUrlPrefix()+"/runId/"+runId + "?from="+from, token, GeneralItemList.class);
	}
}
