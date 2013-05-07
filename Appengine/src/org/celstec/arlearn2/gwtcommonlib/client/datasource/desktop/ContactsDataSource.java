package org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.ContactModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.JsonResumptionListCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.CollaborationClient;
import org.celstec.arlearn2.gwtcommonlib.client.network.GenericClient;

import com.google.gwt.json.client.JSONObject;

public class ContactsDataSource extends GenericDataSource {

	public static ContactsDataSource instance;
	
	public static ContactsDataSource getInstance() {
		if (instance == null)
			instance = new ContactsDataSource();
		return instance;
	}
	
	private ContactsDataSource() {
		super();
		setDataSourceModel(new ContactModel(this));
	}
	
	public GenericClient getHttpClient() {
		return CollaborationClient.getInstance();
	}

	public void loadDataFromWeb() {
			JsonResumptionListCallback callback = new JsonResumptionListCallback(getBeanType(), this.getDataSourceModel(), 0l) {

				@Override
				public void nextCall() {
					((CollaborationClient) getHttpClient()).getContacts(ContactsDataSource.this.lastSyncDate, resumptionToken, this);

				}

			};
		((CollaborationClient) getHttpClient()).getContacts(ContactsDataSource.this.lastSyncDate, null, callback);
	}

	protected String getBeanType() {
		return "accountList";
	}

	@Override
	public void processNotification(JSONObject bean) {
		loadDataFromWeb();
	}

}
