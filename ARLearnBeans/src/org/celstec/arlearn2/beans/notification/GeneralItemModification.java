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
package org.celstec.arlearn2.beans.notification;

import org.celstec.arlearn2.beans.generalItem.GeneralItem;

public class GeneralItemModification extends NotificationBean {

	public final static int CREATED = GI_CREATED;
	public final static int DELETED = GI_DELETED;
	public final static int ALTERED = GI_ALTERED;
	public final static int VISIBLE = GI_VISIBLE;
	public final static int DISAPPEARED = GI_DISAPPEARED;

	private Integer modificationType;
	private Long runId;
	private Long gameId;
	private Long itemId;
	private GeneralItem generalItem;

	public Integer getModificationType() {
		return modificationType;
	}

	public void setModificationType(Integer modificationType) {
		this.modificationType = modificationType;
	}

	public Long getRunId() {
		return runId;
	}

	public void setRunId(Long runId) {
		this.runId = runId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public GeneralItem getGeneralItem() {
		return generalItem;
	}

	public void setGeneralItem(GeneralItem generalItem) {
		this.generalItem = generalItem;
	}

	public void retainOnlyIdentifier() {
		if (getGeneralItem() != null) {
			GeneralItem r = getGeneralItem();
			if (r.getGameId() != null)
				setGameId(r.getGameId());
			if (r.getId() != null)
				setItemId(r.getId());
			setGeneralItem(null);
		}
	}
}
