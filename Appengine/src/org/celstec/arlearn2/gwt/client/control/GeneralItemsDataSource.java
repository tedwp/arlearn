package org.celstec.arlearn2.gwt.client.control;

import org.celstec.arlearn2.gwt.client.network.ActionClient;
import org.celstec.arlearn2.gwt.client.network.ActionsCallback;
import org.celstec.arlearn2.gwt.client.network.GeneralItemsCallback;
import org.celstec.arlearn2.gwt.client.network.GeneralItemsClient;
import org.celstec.arlearn2.gwt.client.network.ResponseCallback;
import org.celstec.arlearn2.gwt.client.network.ResponseClient;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GeneralItemsDataSource extends DataSource {

	public static GeneralItemsDataSource instance;

	private GeneralItemsDataSource() {
		DataSourceTextField pkField = new DataSourceTextField("pk",
				"Primary key");
		// pkField.setHidden(true);
		pkField.setPrimaryKey(true);

		DataSourceIntegerField itemIdField = new DataSourceIntegerField("id");
		itemIdField.setHidden(true);

		DataSourceTextField accountField = new DataSourceTextField("account");
		accountField.setHidden(true);
		accountField.setPrimaryKey(true);

		DataSourceTextField nameField = new DataSourceTextField("name",
				"Item Title");
		nameField.setRequired(true);

		DataSourceTextField typeField = new DataSourceTextField("type",
				"Item Type");
		typeField.setRequired(true);

		DataSourceIntegerField gameIdField = new DataSourceIntegerField(
				"gameId");
		itemIdField.setHidden(true);

		DataSourceIntegerField runIdField = new DataSourceIntegerField("runId");
		runIdField.setHidden(true);

		DataSourceBooleanField readField = new DataSourceBooleanField("read");
		DataSourceBooleanField correctField = new DataSourceBooleanField("correct");

		DataSourceTextField answerField = new DataSourceTextField("answer");

		setFields(pkField, itemIdField, accountField, nameField, typeField,
				gameIdField, runIdField, readField, correctField, answerField);

		setClientOnly(true);
	}

	public static GeneralItemsDataSource getInstance() {
		if (instance == null)
			instance = new GeneralItemsDataSource();
		return instance;
	}

	public void loadItemsGame(final long gameId, final long runId,
			final String account, final ReadyCallback rc) {
		GeneralItemsClient.getInstance().getGeneralItemsGame(gameId,
				new GeneralItemsCallback() {

					@Override
					public void onGeneralItemsReady() {
						for (int i = 0; i < giSize(); i++) {
							final ListGridRecord rec = new ListGridRecord();
							String pk = getItemId(i) + ":" + account + ":"
									+ runId;
							rec.setAttribute("pk", pk);
							rec.setAttribute("id", getItemId(i));
							rec.setAttribute("account", account);
							rec.setAttribute("name", getItemName(i));
							rec.setAttribute("type", getItemType(i));
							rec.setAttribute("gameId", getGameId(i));
							rec.setAttribute("runId", runId);
							rec.setAttribute("DataSourceTextField", runId);
							// removeData(rec);
							// addData(rec);
							final boolean last = i == giSize() - 1;
							fetchData(new Criteria("pk", pk), new DSCallback() {

								@Override
								public void execute(DSResponse response,
										Object rawData, DSRequest request) {
									if (response.getData().length == 0) {
										rec.setAttribute("read", false);
										addData(rec);
									} else {
										updateData(rec);
									}
									if (last) {
										System.out.println("executing last");
										updateResponses(runId, account, rc);
										updateActions(runId, account, rc);
									}
								}

							});
						}
						if (rc != null)
							rc.ready();

					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				});

	}

	private void updateActions(long runId, String account,
			final ReadyCallback rc) {
		ActionClient.getInstance().getActions(runId, new ActionsCallback() {

			@Override
			public void onError() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onActionsReady() {
				for (int i = 0; i < actionsSize(); i++) {
					if ("read".equals(getAction(i))) {
						// ListGridRecord rec = new ListGridRecord();
						// rec.setAttribute("id", getGeneralItemId(i));
						// rec.setAttribute("read", true);
						// updateData(rec);
						Criteria crit = new Criteria();
						crit.addCriteria("pk", getGeneralItemId(i) + ":"
								+ getUserEmail(i) + ":" + getRunId(i));
						fetchData(crit, new DSCallback() {

							@Override
							public void execute(DSResponse response,
									Object rawData, DSRequest request) {
								Record[] records = response.getData();
								for (Record record : records) {
									record.setAttribute("read", true);
									updateData(record);

								}
								if (rc != null)
									rc.ready();
							}
						});
					}
				}
			}
		});
	}

	private void updateResponses(long runId, String account,
			final ReadyCallback rc) {
		ResponseClient.getInstance().getResponses(runId, account,
				new ResponseCallback() {

					@Override
					public void onResponsesReady() {
						for (int i = 0; i < responsesSize(); i++) {

							final String responseValue = getResponseValue(i);
							Criteria crit = new Criteria();
							String pk = getGeneralItemId(i) + ":"
									+ getUserEmail(i) + ":" + getRunId(i);
							System.out.println("response "
									+ getResponseValue(i) + " " + pk);
							crit.addCriteria("pk", pk);
							fetchData(crit, new DSCallback() {

								@Override
								public void execute(DSResponse response,
										Object rawData, DSRequest request) {
									Record[] records = response.getData();
									for (Record record : records) {
										record.setAttribute("answer", processResponseValue(responseValue));
										if (processIsCorrect(responseValue)!=null) {
											record.setAttribute("correct", processResponseValue(responseValue));
											System.out.println("set correct to" +processResponseValue(responseValue));

										}
										updateData(record);

									}
									if (rc != null)
										rc.ready();
								}
							});
						}

					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				});

	}
	
	public String processResponseValue(String jsonIn) {
		try{
		JSONValue response = JSONParser.parseLenient(jsonIn);
		if (response.isObject().get("answer")!= null) return response.isObject().get("answer").isString().stringValue();
		} catch (Exception e) {
			return jsonIn;
		}
		return jsonIn;
	}
	
	public Boolean processIsCorrect(String jsonIn) {
		try{
		JSONValue response = JSONParser.parseLenient(jsonIn);
		if (response.isObject().get("isCorrect")!= null) return response.isObject().get("isCorrect").isBoolean().booleanValue();
		} catch (Exception e) {
			return null;
		}
		return null;
	}
}
