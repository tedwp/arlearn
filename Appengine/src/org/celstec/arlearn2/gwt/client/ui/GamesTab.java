package org.celstec.arlearn2.gwt.client.ui;

import org.celstec.arlearn2.gwt.client.Authoring;
import org.celstec.arlearn2.gwt.client.control.Authentication;
//import org.celstec.arlearn2.gwt.client.control.GameDataSource;
import org.celstec.arlearn2.gwt.client.control.ReadyCallback;
//import org.celstec.arlearn2.gwt.client.network.GameClient;
//import org.celstec.arlearn2.gwt.client.network.GamesCallback;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameClient;
import org.celstec.arlearn2.gwt.client.network.game.GameDataSource;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.Encoding;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.HeaderItem;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.RowSpacerItem;
import com.smartgwt.client.widgets.form.fields.SectionItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.events.TabSelectedEvent;
import com.smartgwt.client.widgets.tab.events.TabSelectedHandler;

public class GamesTab extends GenericTab {

	public static ListGrid listGrid;
	
	public GamesTab() {
		super("Games");		
		setLeft(getLeft());
		setRight(getGameCanvas());
		listGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {  
            public void onCellDoubleClick(CellDoubleClickEvent event) {  
            	GameTab tab = new GameTab("Game: "+event.getRecord().getAttribute("title"), 
            			Long.parseLong(event.getRecord().getAttribute("gameId")));  
                Authoring.addTab(tab);
               
            }  
        });  
	}
	
	@Override
	protected void tabSelect() {
//		listGrid.fetchData();
		GameDataSource.getInstance().loadData(new ReadyCallback() {
			
			@Override
			public void ready() {
				listGrid.fetchData();
				
			}
		});
	}
	
	public Canvas getLeft() {
		VLayout vLayout = new VLayout(10);
		vLayout.addMember(getNewGameForm());
		 vLayout.addMember(getExistingGameForm());
		return vLayout;
	}

	
	private Canvas getNewGameForm() {		

		HeaderItem header = new HeaderItem();
		header.setDefaultValue("Create new Game");

		final TextItem titleGame = new TextItem("titleGame");
		titleGame.setTitle("Game name");
		titleGame.setSelectOnFocus(true);
		titleGame.setWrapTitle(false);

		final TextItem creatorGame = new TextItem("creator");
		creatorGame.setTitle("Creator");

		creatorGame.setWrapTitle(false);

		ButtonItem button = new ButtonItem("submit", "Submit");
		// button.setStartRow(true);
		button.setWidth(80);
		button.setAlign(Alignment.RIGHT);
		final DynamicForm form = getForm("Create new game", titleGame, creatorGame, new RowSpacerItem(),
				button);
		form.setWidth(285);
		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
				GameClient.getInstance().createGame(
						form.getValueAsString("titleGame"),
						form.getValueAsString("creator"),
						new JsonCallback() {
							
							@Override
							public void onJsonReceived(JSONValue jsonValue) {
								//TODO merge method  with onload tab
								GameDataSource.getInstance().loadData(new ReadyCallback() {
									
									@Override
									public void ready() {
										listGrid.fetchData();
										
									}
								});
							}
							
							@Override
							public void onError() {
								// TODO Auto-generated method stub
								
							}
						});
				titleGame.setValue("");
				creatorGame.setValue("");
			}
		});
//		form.setFields(header, titleGame, creatorGame, button);
		
//		final DynamicForm uploadForm = new DynamicForm();
//		uploadForm.setWidth(100);
//		uploadForm.setEncoding(Encoding.MULTIPART);
//		uploadForm.setBorder("1px solid");
//
//		final UploadItem fileItem = new UploadItem("uploadRun");
//		fileItem.setTitle("Upload Game");
//		uploadForm.setTarget("hidden_frame");
//		uploadForm.setAction("/uploadGame/fileUpload.html");
//		
//		HiddenItem authItem = new HiddenItem("auth");
//		authItem.setValue("auth="+Authentication.getInstance().getAuthenticationToken());
//		
//		final HiddenItem callbackItem = new HiddenItem("callbackName");
//
//		uploadForm.setFields(fileItem, authItem, callbackItem);
//		
//		fileItem.addChangeHandler(new ChangeHandler() {
//			
//			@Override
//			public void onChange(ChangeEvent event) {
//			
//				String callbackName = JavaScriptMethodHelper.registerCallbackFunction(new JavaScriptMethodCallback() {
//					public void execute(JavaScriptObject obj) {
//						System.out.println("upload finished");
//					}
//							
//				});
//				callbackItem.setValue(callbackName);
//				uploadForm.submitForm();
//				
//				
//			}
//		});
//		vLayout.setMembers(form, uploadForm);
		return form;
	}
	
	private Canvas getExistingGameForm() {		
		

		final UploadItem fileItem = new UploadItem("uploadRun");
		fileItem.setTitle("Upload Run");
		fileItem.setWidth(150);
		fileItem.setWrapTitle(false);


		HiddenItem authItem = new HiddenItem("auth");
		authItem.setValue("auth="
				+ Authentication.getInstance().getAuthenticationToken());

		ButtonItem button = new ButtonItem("submit", "Upload");
		// button.setStartRow(true);
		button.setWidth(80);
		button.setStartRow(false);
		button.setEndRow(false);
		button.setColSpan(4);
		button.setAlign(Alignment.CENTER);
		
		
		
		final DynamicForm form = getForm("Upload game", fileItem, new RowSpacerItem(),
				button, authItem);
		form.setEncoding(Encoding.MULTIPART);


		button.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {
			public void onClick(
					com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
//				gameIdItem.setValue(form.getValue("itemID"));
				form.setCanSubmit(true);
				form.submit(new DSCallback() {
					
					@Override
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						System.out.println("in callback");
						
					}
				});
			}
		});

		form.setTarget("hidden_frame");
		form.setAction("/uploadGame/fileUpload.html");
		return form;
	}

	public Canvas getGameCanvas() {

		listGrid = new GenericListGrid(false, true) {
			protected void deleteItem(ListGridRecord rollOverRecord) {
				GamesTab.this.deleteGame(rollOverRecord.getAttributeAsInt("gameId"));
			}
		};
		listGrid.setShowRollOverCanvas(true);

		listGrid.setWidth(500);
		listGrid.setHeight(224);
		listGrid.setShowAllRecords(true);
		listGrid.setDataSource(GameDataSource.getInstance());
		
		ListGridField titleGameField = new ListGridField("title", "Game");  
	    ListGridField creatorGameField = new ListGridField("creator", "Creator");  
		
		listGrid.setFields(new ListGridField[] { titleGameField, creatorGameField });
		listGrid.setCanResizeFields(true);

		listGrid.setPadding(5);
		listGrid.setWidth("100%");
		listGrid.setHeight("100%");
		listGrid.fetchData();
		return listGrid;
	}

	protected void deleteGame(final int gameId) {
		System.out.println("delete game id "+gameId);
		GameClient.getInstance().deleteGame(gameId, new JsonCallback() {
			
			@Override
			public void onJsonReceived(JSONValue jsonValue) {
				tabSelect();
			}
			
		});
		
	}

	
}
