package org.celstec.arlearn2.resultDisplay.client;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.ResponseDataSource;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;

public class List extends ListGrid {

	private static List instance;

	private List() {
		super();
		setID("boundListGrid");
		setBackgroundColor("#f1f1f1");
		setWidth100();  
        setHeight100();  
        setShowAllRecords(true); 
        setAutoFetchData(true);
        setDataSource(ResponseDataSource.getInstance());
        
        ListGridField previewImageField = new ListGridField("picture", "Preview", 100);  
        previewImageField.setAlign(Alignment.CENTER);  
        previewImageField.setType(ListGridFieldType.IMAGE);  
        //previewImageField.setImageURLPrefix("flags/16/");  
        //previewImageField.setImageURLSuffix(".png");  
  
        ListGridField timestampField = new ListGridField("timestamp", "Date");  
        ListGridField informationField = new ListGridField("information", "Information");  
        ListGridField userField = new ListGridField("userEmail", "User");  
        ListGridField rolField = new ListGridField("rol", "Rol");  
  
        setFields(previewImageField, timestampField, informationField, userField, rolField);  
        setCanResizeFields(true);
	}

	public static List getInstance() {
		if (instance == null) {
			instance = new List();
		}
		
		return instance;
	}

}
