package org.celstec.arlearn2.gwt.client.network.generalItem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
import org.celstec.arlearn2.gwt.client.network.ActionClient;
import org.celstec.arlearn2.gwt.client.network.ActionsCallback;
import org.celstec.arlearn2.gwt.client.network.DerivedFieldTask;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;
import org.celstec.arlearn2.gwt.client.network.ResponseCallback;
import org.celstec.arlearn2.gwt.client.network.ResponseClient;
import org.celstec.arlearn2.gwt.client.network.user.UsersDataSource;
import org.celstec.arlearn2.gwt.client.ui.RunTab;

import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class GeneralItemRunDataSource extends GenericDataSource {
	
	public static final String MULTIPLECHOICE = "org.celstec.arlearn2.beans.generalItem.MultipleChoiceTest";
	public static final String NARRATORITEM = "org.celstec.arlearn2.beans.generalItem.NarratorItem";
	public static final String VIDEOOBJECT = "org.celstec.arlearn2.beans.generalItem.VideoObject";
	public static final String AUDIOOBJECT = "org.celstec.arlearn2.beans.generalItem.AudioObject";
	
	private RunTab runTab;

	public GeneralItemRunDataSource(RunTab rt) {
		super();
		this.runTab = rt;
	}
	
	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "id", true, true);
		addField(INTEGER_DATA_TYPE, "sortKey", false, true);
		addField(INTEGER_DATA_TYPE, "runId", false, true);
		addField(STRING_DATA_TYPE, "name", false, false);
		addField(STRING_DATA_TYPE, "type", false, false);
		addField(STRING_DATA_TYPE, "account", false, false);
		addField(STRING_DATA_TYPE, "answer", false, false);
		addField(STRING_AR_DATA_TYPE, "roles", false, false);
		addField(BOOLEAN_DATA_TYPE, "deleted", false, true);
		addField(BOOLEAN_DATA_TYPE, "read", false, false);
		addField(BOOLEAN_DATA_TYPE, "correct", false, false);
		addDerivedField(new DerivedFieldTask() {
			
			@Override
			public String processValue(String... value) {
				if (value == null || "".equals(value)) return "";
				String firstValue = value[0];
				if (firstValue.contains(AUDIOOBJECT)) return "Audio Object";
				if (firstValue.contains(VIDEOOBJECT)) return "Video Object";
				if (firstValue.contains(MULTIPLECHOICE)) return "Multiple Choice Test";
				if (firstValue.contains(NARRATORITEM)) return "Narrator Item";
				if (firstValue.contains("OpenUrl")) return "Open URL";
				return firstValue;
			}
			
			@Override
			public String getTargetFieldName() {
				return "simpleName";
			}
			
			@Override
			public String[] getSourceFieldName() {
				return new String[] {"type"};
			}
			@Override
			public int getType() {
				return STRING_DATA_TYPE;
			}
		}, false, false);
	}

	protected GenericClient getHttpClient() {
		return GeneralItemsClient.getInstance();
	}
	
	@Override
	protected String getBeanType() {
		return "generalItems";
	}
	
	protected boolean filterRecord(ListGridRecord rec) {
		String[] giRoles = new String[]{};
		String giRolesString = rec.getAttribute("roles");
		if (giRolesString != null) {
			giRoles = giRolesString.split(",");
		} else {
			return false;
		}
		String[] accountRoles = new String[]{};
		String accountRolesString = UsersDataSource.getInstance().getRole(rec.getAttribute("runId"), rec.getAttribute("teamId"), rec.getAttribute("account"));
		if (accountRolesString != null) {
			accountRoles = accountRolesString.split(",");
		}
		for (String giRole: giRoles) {
			for (String accountRole: accountRoles) {
				if (giRole.trim().equals(accountRole.trim())) return false;
			}
		}
		return true;
	}
	
	protected void allRecordsUpdated(HashMap<String,String> values) {
		updateResponses(runTab.getRunId(), values.get("account"), null);
		updateActions(runTab.getRunId(), values.get("account"), null);
	}
	
	private void updateActions(long runId, String account,
			final ReadyCallback rc) {
		ActionClient.getInstance().getActions(runId, new ActionsCallback() {

			@Override
			public void onError() {

			}

			@Override
			public void onActionsReady() {
				for (int i = 0; i < actionsSize(); i++) {
					if ("read".equals(getAction(i))) {
						updateAction(getGeneralItemId(i), getRunId(i), getUserEmail(i), rc);
					}
				}
			}
		});
	}
	
	public void updateAction(long itemId, long runId, String account, final ReadyCallback rc) {
		Criteria crit = new Criteria();
		crit.addCriteria(pkAttribute, ""+itemId );
		crit.addCriteria("runId", ""+runId);
		crit.addCriteria("account", account );
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
	
	private void updateResponses(long runId, String account, final ReadyCallback rc) {
		ResponseClient.getInstance().getResponses(runId, account,
				new ResponseCallback() {

					@Override
					public void onResponsesReady() {
						for (int i = 0; i < responsesSize(); i++) {
							updateResponse(getGeneralItemId(i), getRunId(i), getUserEmail(i), getResponseValue(i), rc);
						}

					}

					@Override
					public void onError() {
					}
				});

	}
	
	public void updateResponse(long itemId, long runId, String account, final String responseValue, final ReadyCallback rc) {
		Criteria crit = new Criteria();
		crit.addCriteria(pkAttribute, ""+itemId );
		crit.addCriteria("runId", ""+runId);
		crit.addCriteria("account", account );
		fetchData(crit, new DSCallback() {

			@Override
			public void execute(DSResponse response,
					Object rawData, DSRequest request) {
				Record[] records = response.getData();
				for (Record record : records) {
					record.setAttribute("answer", processResponseValue(responseValue));
					if (processIsCorrect(responseValue)!=null) {
						record.setAttribute("correct", processIsCorrect(responseValue));
					}
					updateData(record);

				}
				if (rc != null)
					rc.ready();
			}
		});
	}
	
	public String processResponseValue(String jsonIn) {
		try{
		JSONValue response = JSONParser.parseLenient(jsonIn);
		if (response.isObject().get("answer")!= null) return response.isObject().get("answer").isString().stringValue();
		return "*";
		} catch (Exception e) {
			return jsonIn;
		}
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
