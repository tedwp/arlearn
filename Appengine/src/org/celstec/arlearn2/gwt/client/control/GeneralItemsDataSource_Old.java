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
package org.celstec.arlearn2.gwt.client.control;

import com.smartgwt.client.data.DataSource;

public class GeneralItemsDataSource_Old extends DataSource {
//
//	public static GeneralItemsDataSource_Old instance;
//
//	private GeneralItemsDataSource_Old() {
//		DataSourceTextField pkField = new DataSourceTextField("pk",
//				"Primary key");
//		// pkField.setHidden(true);
//		pkField.setPrimaryKey(true);
//
//		DataSourceIntegerField itemIdField = new DataSourceIntegerField("id");
//		itemIdField.setHidden(true);
//
//		DataSourceTextField accountField = new DataSourceTextField("account");
//		accountField.setHidden(true);
//		accountField.setPrimaryKey(true);
//
//		DataSourceTextField sortField = new DataSourceTextField("sortKey", "sortKey");
//		DataSourceTextField nameField = new DataSourceTextField("name", "Item Title");
//		nameField.setRequired(true);
//
//		DataSourceTextField typeField = new DataSourceTextField("type","Item Type");
//		typeField.setRequired(true);
//
//		DataSourceIntegerField gameIdField = new DataSourceIntegerField(
//				"gameId");
//		itemIdField.setHidden(true);
//
//		DataSourceIntegerField runIdField = new DataSourceIntegerField("runId");
//		runIdField.setHidden(true);
//
//		DataSourceBooleanField readField = new DataSourceBooleanField("read");
//		DataSourceBooleanField correctField = new DataSourceBooleanField("correct");
//
//		DataSourceTextField answerField = new DataSourceTextField("answer");
//		DataSourceBooleanField deletedField = new DataSourceBooleanField("deleted");
//
//		setFields(pkField, sortField, itemIdField, accountField, nameField, typeField,
//				gameIdField, runIdField, readField, correctField, answerField, deletedField);
//
//		setClientOnly(true);
//	}
//
//	public static GeneralItemsDataSource_Old getInstance() {
//		if (instance == null)
//			instance = new GeneralItemsDataSource_Old();
//		return instance;
//	}
//
//	public void loadItemsGame(final long gameId, final long runId,
//			final String account, final ReadyCallback rc) {
//		GeneralItemsClient.getInstance().getGeneralItemsGame(gameId,
//				new GeneralItemsCallback() {
//
//					@Override
//					public void onGeneralItemsReady() {
//						for (int i = 0; i < giSize(); i++) {
//							final ListGridRecord rec = new ListGridRecord();
//							String pk = getItemId(i) + ":" + account + ":"
//									+ runId;
//							rec.setAttribute("pk", pk);
//							rec.setAttribute("id", getItemId(i));
//							rec.setAttribute("sortKey", getSortKey(i));
//							rec.setAttribute("account", account);
//							rec.setAttribute("name", getItemName(i));
//							rec.setAttribute("type", getItemType(i));
//							rec.setAttribute("gameId", getGameId(i));
//							rec.setAttribute("runId", runId);
//							rec.setAttribute("DataSourceTextField", runId);
//							// removeData(rec);
//							 addData(rec);
//							final boolean last = i == giSize() - 1;
//							if (!getDeleted(i)) {
//								fetchData(new Criteria("pk", pk), new DSCallback() {
//							
//
//								@Override
//								public void execute(DSResponse response,
//										Object rawData, DSRequest request) {
//									if (response.getData().length == 0) {
//										rec.setAttribute("read", false);
//										addData(rec);
//									} else {
//										updateData(rec);
//									}
//									if (last) {
//										updateResponses(runId, account, rc);
//										updateActions(runId, account, rc);
//									}
//								}
//
//							});
//							}
//						}
//						if (rc != null)
//							rc.ready();
//
//					}
//
//					@Override
//					public void onError() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//
//	}
//
//	private void updateActions(long runId, String account,
//			final ReadyCallback rc) {
//		ActionClient.getInstance().getActions(runId, new ActionsCallback() {
//
//			@Override
//			public void onError() {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onActionsReady() {
//				for (int i = 0; i < actionsSize(); i++) {
//					if ("read".equals(getAction(i))) {
//						// ListGridRecord rec = new ListGridRecord();
//						// rec.setAttribute("id", getGeneralItemId(i));
//						// rec.setAttribute("read", true);
//						// updateData(rec);
//						Criteria crit = new Criteria();
//						crit.addCriteria("pk", getGeneralItemId(i) + ":"
//								+ getUserEmail(i) + ":" + getRunId(i));
//						fetchData(crit, new DSCallback() {
//
//							@Override
//							public void execute(DSResponse response,
//									Object rawData, DSRequest request) {
//								Record[] records = response.getData();
//								for (Record record : records) {
//									record.setAttribute("read", true);
//									updateData(record);
//
//								}
//								if (rc != null)
//									rc.ready();
//							}
//						});
//					}
//				}
//			}
//		});
//	}
//
//	private void updateResponses(long runId, String account,
//			final ReadyCallback rc) {
//		ResponseClient.getInstance().getResponses(runId, account,
//				new ResponseCallback() {
//
//					@Override
//					public void onResponsesReady() {
//						for (int i = 0; i < responsesSize(); i++) {
//
//							final String responseValue = getResponseValue(i);
////							final String responseValue = "{\"answer\":\"Notify the hostage family\",\"isCorrect\":false}";
//							Criteria crit = new Criteria();
//							String pk = getGeneralItemId(i) + ":"+ getUserEmail(i) + ":" + getRunId(i);
////							String pk = "829:test:866" ;
////							System.out.println("response "+ getResponseValue(i) + " " + pk);
//							crit.addCriteria("pk", pk);
//							fetchData(crit, new DSCallback() {
//
//								@Override
//								public void execute(DSResponse response,
//										Object rawData, DSRequest request) {
//									Record[] records = response.getData();
//									for (Record record : records) {
//										record.setAttribute("answer", processResponseValue(responseValue));
//										if (processIsCorrect(responseValue)!=null) {
//											record.setAttribute("correct", processIsCorrect(responseValue));
//										}
//										updateData(record);
//
//									}
//									if (rc != null)
//										rc.ready();
//								}
//							});
//						}
//
//					}
//
//					@Override
//					public void onError() {
//						// TODO Auto-generated method stub
//
//					}
//				});
//
//	}
//	
//	public String processResponseValue(String jsonIn) {
//		try{
//		JSONValue response = JSONParser.parseLenient(jsonIn);
//		if (response.isObject().get("answer")!= null) return response.isObject().get("answer").isString().stringValue();
//		} catch (Exception e) {
//			return jsonIn;
//		}
//		return jsonIn;
//	}
//	
//	public Boolean processIsCorrect(String jsonIn) {
//		try{
//		JSONValue response = JSONParser.parseLenient(jsonIn);
//		if (response.isObject().get("isCorrect")!= null) return response.isObject().get("isCorrect").isBoolean().booleanValue();
//		} catch (Exception e) {
//			return null;
//		}
//		return null;
//	}
}
