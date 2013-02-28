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
 package org.celstec.arlearn2.beans.generalItem;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;

public class GeneralItemList extends Bean{

	public static String generalItemsType = "org.celstec.arlearn2.beans.generalItem.GeneralItem";

	private List<GeneralItem> generalItems = new ArrayList<GeneralItem>();

	public GeneralItemList() {

	}
	
	private Long serverTime;

	public Long getServerTime() {
		return serverTime;
	}

	public void setServerTime(Long serverTime) {
		this.serverTime = serverTime;
	}

	public List<GeneralItem> getGeneralItems() {
		return generalItems;
	}


	public void setGeneralItems(List<GeneralItem> giList) {
		this.generalItems = giList;
	}

	public void addGeneralItem(GeneralItem gi) {
		generalItems.add(gi);
	}
	
}
