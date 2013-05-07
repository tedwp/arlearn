package org.celstec.arlearn2.resultDisplay.client;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.ResponseModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.OwnerResponseDataSource;

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
        setDataSource(OwnerResponseDataSource.getInstance());
        
        ListGridField previewImageField = new ListGridField("picture", "Preview", 100);  
        previewImageField.setAlign(Alignment.CENTER);  
        previewImageField.setType(ListGridFieldType.IMAGE);  
        //previewImageField.setImageURLPrefix("flags/16/");  
        //previewImageField.setImageURLSuffix(".png");  
  
        ListGridField previewImageField2 = new ListGridField("picture", "Preview");  
        previewImageField.setAlign(Alignment.CENTER); 
       
        
        ListGridField timestampField = new ListGridField("timestamp", "Date");  
        ListGridField informationField = new ListGridField("information", "Information");  
        ListGridField userField = new ListGridField(ResponseModel.USEREMAIL_FIELD, "User");  
        ListGridField rolField = new ListGridField(ResponseModel.ROLE_VALUE_FIELD, "Rol"); 
        ListGridField teamField = new ListGridField(TeamModel.TEAMID_FIELD, "Team");
        teamField.setHidden(true);
  
        setFields(previewImageField/*,previewImageField2*/, timestampField, informationField, userField, rolField, teamField);  
        setCanResizeFields(true);
	}

	public static List getInstance() {
		if (instance == null) {
			instance = new List();
		}
		
		return instance;
	}

}