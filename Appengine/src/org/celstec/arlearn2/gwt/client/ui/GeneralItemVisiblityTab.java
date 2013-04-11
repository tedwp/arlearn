package org.celstec.arlearn2.gwt.client.ui;

import java.util.Date;

import org.celstec.arlearn2.gwt.client.network.GeneralItemsVisiblityClient;
import org.celstec.arlearn2.gwt.client.network.generalItem.GeneralItemGameDataSource;
import org.celstec.arlearn2.gwt.client.network.run.RunDataSource;

import com.smartgwt.client.data.Criterion;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.TimeItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class GeneralItemVisiblityTab extends MasterDetailTab {

	public GeneralItemVisiblityTab() {
		super("Visibility tab");
		setMasterCanvas(getForm());
		setDetailCanvas(initDetailCanvas());
	}
	
	public VStack getForm() {
		VStack returnStack = new VStack();
		returnStack.setBorder("1px solid gray");
		returnStack.setWidth(300);

		DynamicForm selectForm = new DynamicForm();

		HeaderItem header = new HeaderItem();
		header.setDefaultValue("Make items Visible");

		final SelectItem selectRunItem = new SelectItem();
		selectRunItem.setName("selectRun");
		selectRunItem.setTitle("select run");
		selectRunItem.setOptionDataSource(RunDataSource.getInstance());
		selectRunItem.setDisplayField("title");
		selectRunItem.setValueField("runId");
		selectRunItem.setWrapTitle(false);
		
//
//		selectType.addChangeHandler(selectTypeChangeHandler);
		
		final SelectItem selectGiType = new SelectItem();
		selectGiType.setName("selectGi");
		selectGiType.setTitle("select gi");
		selectGiType.setOptionDataSource(GeneralItemGameDataSource.getInstance());
		selectGiType.setDisplayField("name");
		selectGiType.setValueField("id");
		selectGiType.setWrapTitle(false);
		
		
		selectRunItem.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println(event.getValue());
				System.out.println(selectRunItem.getSelectedRecord().getAttribute("gameId"));
				long gameId = selectRunItem.getSelectedRecord().getAttributeAsLong("gameId");
				GeneralItemGameDataSource.getInstance().loadDataGame(gameId);
				Criterion criterion = new Criterion();
				criterion.addCriteria("id", (int) gameId);
				selectGiType.setCriterion(criterion);
			}
		});
		final TextItem accountItem = new TextItem("account");
		accountItem.setTitle("account");
		accountItem.setSelectOnFocus(true);
		accountItem.setWrapTitle(false);
		
		final DateItem dateItem2 = new DateItem();  
        dateItem2.setTitle("Date");  
        dateItem2.setUseTextField(true);  
//        dateItem2.setDisplayFormat(DateDisplayFormat.TOEUROPEANSHORTDATETIME);  
        dateItem2.setHint("<nobr>Direct date input</nobr>");  
  
        final TimeItem timeItem = new TimeItem("timeItem", "Time");  

		ButtonItem button = new ButtonItem("submit", "create");
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);
		button.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
//				System.out.println("clicked button");
//				System.out.println("runId: "+selectRunItem.getSelectedRecord().getAttributeAsLong("runId"));
//				System.out.println("gi Id: "+selectGiType.getSelectedRecord().getAttributeAsLong("id"));
				Date d = dateItem2.getValueAsDate();
//				d.setHours(timeItem.getVal)
				System.out.println("date: "+dateItem2.getValueAsDate());
				System.out.println("date: "+dateItem2.getValueAsDate().getTime());
				long datetime = dateItem2.getValueAsDate().getTime();
				System.out.println("time: "+timeItem.getValue());
				System.out.println("time: "+timeItem.getValue().getClass());
				System.out.println("time: "+((Date)timeItem.getValue()).getTime());
				Date timeDate = (Date)timeItem.getValue();
				
				long timetime = timeDate.getTime();
				long addtime = (datetime+timetime)-((12*60+ timeDate.getTimezoneOffset())*60*1000l);// + timeDate.getTimezoneOffset()*3600000;
				System.out.println("add: "+addtime);
				
				System.out.println("now: "+System.currentTimeMillis());
				System.out.println("offset: "+timeDate.getTimezoneOffset());
				System.out.println("rel: "+(addtime - System.currentTimeMillis()));
				System.out.println("rel sec: "+((addtime - System.currentTimeMillis()) / 1000));
				System.out.println("rel min: "+((addtime - System.currentTimeMillis()) / 60000));
				System.out.println();
				System.out.println("rel: "+(datetime - System.currentTimeMillis()));
				System.out.println("rel sec: "+((datetime - System.currentTimeMillis()) / 1000));
				System.out.println("rel min: "+((datetime - System.currentTimeMillis()) / 60000));
				makeItemVisible(selectGiType.getSelectedRecord().getAttributeAsLong("id"), selectRunItem.getSelectedRecord().getAttributeAsLong("runId"), addtime, accountItem.getValueAsString());
				
			}
		});
		
		selectForm.setFields(header, selectRunItem, selectGiType,accountItem, dateItem2, timeItem, button);
		returnStack.addMember(selectForm);
		return returnStack;
	}
	
	private VLayout initDetailCanvas() {
		VLayout rightCanvas = new VLayout();
		rightCanvas.setHeight100();
//		rightCanvas.addMember(getListGeneralItemsCanvas());
		return rightCanvas;
	}
	
	private void makeItemVisible(long itemId, long runId, long atTime, String account) {
		GeneralItemsVisiblityClient.getInstance().makeItemVisible(itemId, runId, atTime, account, 1, null);
		
	}

}
