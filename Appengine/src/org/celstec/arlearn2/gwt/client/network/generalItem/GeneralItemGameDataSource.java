package org.celstec.arlearn2.gwt.client.network.generalItem;


import org.celstec.arlearn2.gwt.client.network.DerivedFieldTask;
import org.celstec.arlearn2.gwt.client.network.GenericClient;
import org.celstec.arlearn2.gwt.client.network.GenericDataSource;

public class GeneralItemGameDataSource extends GenericDataSource {
	
	public static GeneralItemGameDataSource instance;

	public static GeneralItemGameDataSource getInstance() {
		if (instance == null)
			instance = new GeneralItemGameDataSource();
		return instance;
	}

	private GeneralItemGameDataSource() {
		super();
	}

	@Override
	protected void initFields() {
		addField(INTEGER_DATA_TYPE, "id", true, true);
		addField(INTEGER_DATA_TYPE, "sortKey", false, true);
		addField(STRING_DATA_TYPE, "name", false, false);
		addField(STRING_DATA_TYPE, "type", false, false);
		addField(STRING_AR_DATA_TYPE, "roles", false, false);
		addField(BOOLEAN_DATA_TYPE, "deleted", false, true);
		addDerivedField(new DerivedFieldTask() {
			
			@Override
			public String processValue(String... value) {
				if (value == null || "".equals(value)) return "";
				String firstValue = value[0];
				if (firstValue.contains("AudioObject")) return "Audio Object";
				if (firstValue.contains("VideoObject")) return "Video Object";
				if (firstValue.contains("MultipleChoiceTest")) return "Multiple Choice Test";
				if (firstValue.contains("NarratorItem")) return "Narrator Item";
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
		}, false, false);
	}
	
	protected GenericClient getHttpClient() {
		return GeneralItemsClient.getInstance();
	}

	@Override
	protected String getBeanType() {
		return "generalItems";
	}
}
