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
package org.celstec.arlearn2.tasks.beans;

import java.util.ArrayList;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.delegators.GameDelegator;
import org.celstec.arlearn2.delegators.GeneralItemDelegator;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.PutException;
import com.google.appengine.api.search.SearchServiceFactory;
import com.google.appengine.api.search.StatusCode;

public class GameSearchIndex extends GenericBean {

	private String gameTitle;
	private String gameAuthor;
	private Integer sharingType;
	private Long gameId;

	public GameSearchIndex() {
		super();
	}

	public GameSearchIndex(String gameTitle, String gameAuthor, Integer sharingType, Long gameId) {
		super();
		this.gameTitle = gameTitle;
		this.gameAuthor = gameAuthor;
		this.sharingType = sharingType;
		this.gameId = gameId;
	}

	public String getGameTitle() {
		return gameTitle;
	}

	public void setGameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}

	public String getGameAuthor() {
		return gameAuthor;
	}

	public void setGameAuthor(String gameAuthor) {
		this.gameAuthor = gameAuthor;
	}

	public Integer getSharingType() {
		return sharingType;
	}

	public void setSharingType(Integer sharingType) {
		this.sharingType = sharingType;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setGameId(Long gameId) {
		this.gameId = gameId;
	}

	@Override
	public void run() {
		try {

			if (sharingType != null && sharingType.equals(Game.PUBLIC)) {
				addToIndex();
			} else {
				removeFromIndex();
			}
			addGisToIndex();
		} catch (PutException e) {
			if (StatusCode.TRANSIENT_ERROR.equals(e.getOperationResult().getCode())) {
				// retry putting the document
				throw e;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addGisToIndex() {
		GameDelegator gd = new GameDelegator();
		GeneralItemDelegator gid = new GeneralItemDelegator(gd);
		for (GeneralItem gi :gid.getGeneralItems(getGameId()).getGeneralItems()){
			GeneralItemSearchIndex.scheduleGiTask(gi);
		}
	}

	private void removeFromIndex() {
		ArrayList<String> docIds = new ArrayList<String>();
		docIds.add("game:"+gameId);
		getIndex().delete(docIds);
	}

	private void addToIndex() throws PutException {
		Document doc = Document.newBuilder()
				.setId("game:" + gameId)
				.addField(Field.newBuilder().setName("gameId").setNumber(getGameId()))
				.addField(Field.newBuilder().setName("title").setText(getGameTitle()))
				.addField(Field.newBuilder().setName("author").setText(getGameAuthor())).build();
		getIndex().put(doc);
	}

	public Index getIndex() {
		IndexSpec indexSpec = IndexSpec.newBuilder().setName("game_index").build();
		return SearchServiceFactory.getSearchService().getIndex(indexSpec);
	}

}
