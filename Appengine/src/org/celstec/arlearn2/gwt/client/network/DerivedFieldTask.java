package org.celstec.arlearn2.gwt.client.network;

public interface DerivedFieldTask {

	public String[] getSourceFieldName();
	public String getTargetFieldName();
	public String processValue(String... value);
}
