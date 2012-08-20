package org.celstec.arlearn2.gwt.client.network;

import com.google.gwt.json.client.JSONObject;

public interface DerivedFieldTask {

	public String[] getSourceFieldName();
	public String getTargetFieldName();
	public String processValue(String... value);
	public int getType();
	public void setJsonSource(JSONObject jsonObject);
}
