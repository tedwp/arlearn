package org.celstec.arlearn2.resultDisplay.client;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.TeamModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.UserModel;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.ResponseDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.TeamDataSource;
import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.UserDataSource;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;

public class SearchForm extends DynamicForm {

	public SearchForm() {
		super();
		
		setPadding(10);		
		setGroupTitle("Search");
		setDataSource(ResponseDataSource.getInstance());
		setAutoFocus(false);
		setNumCols(8);
        
        setFields(addSelectTeamField(), addSelectUserField(), addSelectField("rol"), addSelectField("idItem"));
	}
	
	private SelectItem addSelectField(String name){
		final SelectItem select = new SelectItem(name);  
        select.setMultiple(true);  
        select.setWidth(150);
		return select; 
	}
	
	private SelectItem addSelectTeamField() {
		final SelectItem select = new SelectItem("team", "team -i18"); //TODO add internationalisation constant  
        select.setMultiple(true);  
        select.setWidth(150);
        select.setOptionDataSource(TeamDataSource.getInstance());
        select.setAutoFetchData(true);
        select.setDisplayField(TeamModel.NAME_FIELD);
		return select; 
	}

	private SelectItem addSelectUserField() {
		final SelectItem select = new SelectItem("user", "user -i18"); //TODO add internationalisation constant  
        select.setMultiple(true);  
        select.setWidth(150);
        select.setOptionDataSource(UserDataSource.getInstance());
        select.setAutoFetchData(true);
        select.setDisplayField(UserModel.NAME_FIELD);
		return select; 
	}
}
