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
package org.celstec.arlearn2.gwt.client.network.game;

import org.celstec.arlearn2.gwt.client.network.DerivedFieldTask;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;
import org.celstec.arlearn2.gwt.client.notification.NotificationHandler;
import org.celstec.arlearn2.gwt.client.notification.NotificationSubscriber;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GameDataSource extends GenericDataSource {
	
	public static GameDataSource instance;

	public static GameDataSource getInstance() {
		if (instance == null)
			instance = new GameDataSource();
		return instance;
	}

	private GameDataSource() {
		super();
		NotificationSubscriber.getInstance().addNotificationHandler("org.celstec.arlearn2.beans.notification.GameModification", gameNotitificationHandler);

	}
	
	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "gameId", true, true);
		addField(STRING_DATA_TYPE, "title", false, true);
		addField(INTEGER_DATA_TYPE, "time", false, true);
		addDerivedField(new DerivedFieldTask() {
			
			private JSONObject jsonObject;
			
			@Override
			public String processValue(String... value) {
				if (jsonObject.containsKey("config")) {
					final JSONObject config = jsonObject.get("config").isObject();
					if (config != null && config.containsKey("mapAvailable")) {
						if (config.get("mapAvailable").isBoolean().booleanValue() == false) {
							return "list_icon";//System.out.println(config);
						}
					}
				}
				return "icon_maps";
			}
			
			@Override
			public String getTargetFieldName() {
				return "status_map";
			}
			
			@Override
			public String[] getSourceFieldName() {
				return new String[] {};
			}
			
			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}

			@Override
			public void setJsonSource(JSONObject jsonObject) {
				this.jsonObject = jsonObject;
				
			}
			
		}, false, false);
	}
	
	protected GenericClient getHttpClient() {
		return GameClient.getInstance();
	}

	@Override
	protected String getBeanType() {
		return "games";
	}

	public void delete(long gameId) {
//		ListGridRecord rec = new ListGridRecord();
//		rec.setAttribute("gameId", gameId);
//		removeData(rec);
		
		Criteria crit = new Criteria();
		crit.addCriteria("gameId", ""+gameId);
		deleteData(crit);
	}

	public NotificationHandler gameNotitificationHandler = new GameNotificationHandler() {
		

		@Override
		public void onNewGame(JSONObject jsonObject) {
			addBean(jsonObject);
		}

		@Override
		public void onDeleteGame(long gameId) {
			delete(gameId);
			
		}
	};

//	@Override
//	protected void processJson(JSONObject jsonObject) {
//		
//
//					Criteria crit = new Criteria();
//					int gameId = (int) jsonObject.get("gameId").isNumber().doubleValue();
//					System.out.println(gameId);
//					crit.setAttribute("gameId", gameId);
//					fetchData(crit, new DSCallback() {
//						@Override
//						public void execute(DSResponse response, Object rawData, DSRequest request) {
//							Record[] records = response.getData();
//							for (Record record : records) {
//								System.out.println("record foound");
//								record.setAttribute("status_map", "status_icon_green");
//								updateData(record);
//							}
//						}
//					});
//				}
//			}
//		}
//	}
}
