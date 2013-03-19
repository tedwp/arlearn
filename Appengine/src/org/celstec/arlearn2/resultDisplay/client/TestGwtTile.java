package org.celstec.arlearn2.resultDisplay.client;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.desktop.ResponseDataSource;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionType;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ColumnTree;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tile.TileGrid;
import com.smartgwt.client.widgets.tile.events.SelectionChangedEvent;
import com.smartgwt.client.widgets.tile.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestGwtTile implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */

	private VLayout main = new VLayout(); 		// Main layout
	final TileGrid tileGridExtend = new TileGrid();
	final HTML breadcrumb = new HTML();
	private TileGrid tileGrid = null;
	private ListGrid listGrid = null;
	private ColumnTree columnTreeGrid = null;

	final IButton viewFiles = new IButton("Play");
	final PopupPanel imagePopup = new PopupPanel(true);
	int position = 0;
	
	private MyAlbum album = null;
	//private MyAlbumDialogBox album = null;

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		tileGridExtend.setTileWidth(158);
		tileGridExtend.setTileHeight(205);
		tileGridExtend.setHeight("100%");
		tileGridExtend.setID("boundListExtend");
		tileGridExtend.setDataSource(ResponseDataSource.getInstance());
		tileGridExtend.setCanReorderTiles(true);
		tileGridExtend.setAnimateTileChange(true);
		
		DetailViewerField pictureFieldExtend = new DetailViewerField("picture");
		tileGridExtend.setFields(pictureFieldExtend);
		
		/**
		 * Create grids 
		 * */
		tileGrid = createTileGrid();
		listGrid = createListGrid();
		columnTreeGrid = createColumnTreeGrid();
		
		/**
		 * 
		 * Layout's declaration
		 * 
		 * */		
		main.setWidth100();  
        main.setHeight100();
		HLayout hLayout = new HLayout(10); 	// Layout for visualization options, clear button and breadcrumb.
		hLayout.setHeight(22);
		hLayout.setPadding(10);
		
        /**
		 * Filter tilegrid forms setup
		 * */
		final DynamicForm filterForm = setSearchTileGridForm(tileGrid);
		filterForm.setPadding(10);
        


		/**
		 * DEPRECATED
		 * 
		 * Filter button
		 * */
		IButton filterButton = new IButton("Filter");
		filterButton.setAutoFit(true);
		filterForm.setBackgroundColor("#f1f1f1");
		filterButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tileGrid.fetchData(filterForm.getValuesAsCriteria());
			}
		});

		
		/**
		 * Clear button
		 * */	
		IButton clearButton = new IButton("Reset filter");
		clearButton.setAutoFit(true);
		clearButton.setWidth(150);
		clearButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tileGrid.fetchData();
				listGrid.fetchData();
				filterForm.clearValues();
				refreshBreadcrumbs(filterForm);
			}
		});
		
		/**
		 * Visualization toolbar
		 * */
		ToolStrip toolStrip = createVisualizationToolbar();
                
		hLayout.addMember(toolStrip);
		hLayout.addMember(clearButton);
		hLayout.addMember(breadcrumb);

		breadcrumb.setHTML("<b>Filters</b>");
		breadcrumb.setWidth("80%");
		breadcrumb.setHeight("30px");
		refreshBreadcrumbs(filterForm);
		
//		main.addMembers(filterForm, hLayout, tileGrid);		
		main.addMember(filterForm);
		main.addMember(hLayout);
		main.addMember(tileGrid);
		
		/**
		 * DEPRECATED
		 * 
		 * Modifications: AddClickHandler to play button. 
		 * */
		viewFiles.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				tileGridExtend.setData(tileGrid.getSelection());
				showPreview(tileGrid);	
			}			
		});

		main.draw();
	}

	private ToolStrip createVisualizationToolbar() {
		/**
		 * Toolstrip menu for visualization
		 * */		
		ToolStrip toolStrip = new ToolStrip();  
        toolStrip.setWidth(72);  
        toolStrip.setHeight(24);  
  
        ImgButton gridButton = new ImgButton();  
        gridButton.setSize(24);  
        gridButton.setShowRollOver(false);  
        gridButton.setSrc("grid.png");  
        //gridButton.setActionType(SelectionType.CHECKBOX);
        
        gridButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				main.removeMember(listGrid);
				main.removeMember(columnTreeGrid);
        		main.addMember(tileGrid);
        		//main.draw();
			}
		});
        
        toolStrip.addMember(gridButton);  
          
        ImgButton listButton = new ImgButton();  
        listButton.setSize(24);  
        listButton.setShowRollOver(false);  
        listButton.setSrc("list.png");  
        //listButton.setActionType(SelectionType.CHECKBOX);  
        toolStrip.addMember(listButton); 
        listButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				main.removeMember(tileGrid);
				main.removeMember(columnTreeGrid);
        		main.addMember(listGrid);
        		//main.draw();
			}
		});
          
        ImgButton mixedButton = new ImgButton();  
        mixedButton.setSize(24);  
        mixedButton.setShowRollOver(false);  
        mixedButton.setSrc("mixed.png");  
        //mixedButton.setActionType(SelectionType.CHECKBOX);  
        toolStrip.addMember(mixedButton);
        mixedButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				main.removeMember(tileGrid);
				main.removeMember(listGrid);
        		main.addMember(columnTreeGrid);
        		//main.draw();
			}
		});
		return toolStrip;
	}
	
	private ColumnTree createColumnTreeGrid() {
		
		ColumnTree columnTree = new ColumnTree();  
        columnTree.setWidth100();  
        columnTree.setHeight(205); 
        columnTree.setID("boundColumnTreeGrid");
        columnTree.setDataSource(ResponseDataSource.getInstance());  
        columnTree.setAutoFetchData(true);  
        columnTree.setNodeIcon("audio.png");  
        //columnTree.setFolderIcon("icons/16/person.png");  
        columnTree.setShowOpenIcons(false);  
        columnTree.setShowDropIcons(false);  
        columnTree.setClosedIconSuffix("");  
  
        columnTree.setShowHeaders(true);  
        columnTree.setShowNodeCount(true);          
        columnTree.setLoadDataOnDemand(false);   
		
		return columnTree;
	}

	private ListGrid createListGrid() {
		final ListGrid listGrid = new ListGrid();
	
		listGrid.setID("boundListGrid");
		listGrid.setBackgroundColor("#f1f1f1");
        listGrid.setWidth100();  
        listGrid.setHeight100();  
        listGrid.setShowAllRecords(true); 
        listGrid.setAutoFetchData(true);
        listGrid.setDataSource(ResponseDataSource.getInstance());
        
        ListGridField previewImageField = new ListGridField("picture", "Preview", 100);  
        previewImageField.setAlign(Alignment.CENTER);  
        previewImageField.setType(ListGridFieldType.IMAGE);  
        //previewImageField.setImageURLPrefix("flags/16/");  
        //previewImageField.setImageURLSuffix(".png");  
  
        ListGridField timestampField = new ListGridField("timestamp", "Date");  
        ListGridField informationField = new ListGridField("information", "Information");  
        ListGridField userField = new ListGridField("user", "User");  
        ListGridField rolField = new ListGridField("rol", "Rol");  
  
        listGrid.setFields(previewImageField, timestampField, informationField, userField, rolField);  
        listGrid.setCanResizeFields(true);
        
		return listGrid;
	}

	private TileGrid createTileGrid() {
		final TileGrid tileGrid = new TileGrid();
		tileGrid.setTileWidth(158);
		tileGrid.setTileHeight(158);
		tileGrid.setHeight100();
		tileGrid.setID("boundList");
		tileGrid.setBackgroundColor("#f1f1f1");
		tileGrid.setCanReorderTiles(true);
		tileGrid.setShowAllRecords(true);
		tileGrid.setDataSource(ResponseDataSource.getInstance());
		tileGrid.setAutoFetchData(true);
		tileGrid.setAnimateTileChange(true);
		tileGrid.setCanFocus(false);
		
		tileGrid.addSelectionChangedHandler(new SelectionChangedHandler() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent event) {
				tileGridExtend.setData(tileGrid.getRecordList() );
				if (tileGrid.getRecordList().getLength() > 0) {
					viewFiles.setDisabled(false);
				}
				else{
					viewFiles.setDisabled(true);
				}
			}			
		});
		
		tileGrid.addDoubleClickHandler(new DoubleClickHandler() {
			
			@Override
			public void onDoubleClick(DoubleClickEvent event) {
				tileGridExtend.setData(tileGrid.getRecordList());
				showPreview(tileGrid);

			}
		});
		
		/**
		 * Fields tilegrid setup
		 * */
		DetailViewerField pictureField = new DetailViewerField("picture");
		tileGrid.setFields(pictureField);
		
		return tileGrid;
	}
	
	private void showPreview(final TileGrid tileGrid) {
		album = new MyAlbum();
		//album = new MyAlbumDialogBox();
		
		if(tileGrid.getRecordList().getLength() == 1){
			album.setEnabledButtonPrevious(false);
			album.setEnabledButtonNext(false);
		}
		else{
			album.setEnabledButtonPrevious(true);
			album.setEnabledButtonNext(true);
		}
		
		album.refreshViewPopup(tileGrid.getRecordList(), 0);
		album.changeCounter();
		album.getButtonNext().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				album.refreshViewPopup(tileGrid.getRecordList(),1);
				album.changeCounter();
				album.show();
				
		        System.out.println("Contador de widget:"+album.getWidgetCount());
			}
		});
		
		album.getButtonPrevious().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				album.refreshViewPopup(tileGrid.getRecordList(),2);
				album.changeCounter();
				album.show();
			}
		});
		
		album.getButtonClose().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				album.hide();
			}
		});
		
		//album.center();
		album.show();
	}
	
	/**
	 * DEPRECATED
	 * */
	private DynamicForm setOrderTileGridForm(final TileGrid tileGrid) {
		final DynamicForm sortForm = new DynamicForm();
		sortForm.setIsGroup(true);
		sortForm.setGroupTitle("Sort");
		sortForm.setAutoFocus(false);
		sortForm.setID("sortForm");
		sortForm.setNumCols(6);

		SelectItem sortItem = new SelectItem();
		sortItem.setName("sortBy");
		sortItem.setTitle("Sort By");

		LinkedHashMap valueMap = new LinkedHashMap();
		valueMap.put("commonName", "Animal");
		valueMap.put("lifeSpan", "Life Span");
		valueMap.put("status", "Endangered Status");

		sortItem.setValueMap(valueMap);

		final CheckboxItem ascendingItem = new CheckboxItem("chkSortDir");
		ascendingItem.setTitle("Ascending");

		sortForm.setFields(sortItem, ascendingItem);

		sortForm.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				String sortVal = sortForm.getValueAsString("sortBy");
				Boolean sortDir = (Boolean) ascendingItem.getValue();
				if (sortDir == null)
					sortDir = false;
				if (sortVal != null) {
					tileGrid.sortByProperty(sortVal, sortDir);
				}
			}
		});
		return sortForm;
	}

	private DynamicForm setSearchTileGridForm(final TileGrid tileGrid) {
		final DynamicForm filterForm = new DynamicForm();
		
		filterForm.setGroupTitle("Search");
		//filterForm.setBorder("2px solid black");
		//filterForm.setHeight100();
		filterForm.setDataSource(ResponseDataSource.getInstance());
		filterForm.setAutoFocus(false);
		filterForm.setNumCols(8);
		
		final SelectItem selectMultiTeam = new SelectItem("team");  
        //selectMultiTeam.setTitle("");
		
        selectMultiTeam.setMultiple(true);  
        selectMultiTeam.setWidth(150); 
        //selectMultiTeam.setMultipleAppearance(MultipleAppearance.GRID);  
        
        final SelectItem selectMultiUser = new SelectItem("user");  
        //selectMultiUser.setTitle("Users");  
        selectMultiUser.setMultiple(true);  
        selectMultiUser.setWidth(150); 
        //selectMultiUser.setMultipleAppearance(MultipleAppearance.GRID); 
		
		/*SelectItem teams = new SelectItem("team");  
		teams.setOperator(OperatorId.EQUALS);
		teams.setAllowEmptyValue(true);
		
		SelectItem user = new SelectItem("user");
		user.setOperator(OperatorId.EQUALS);
		user.setAllowEmptyValue(true);*/

		/*SliderItem lifeSpanItem = new SliderItem("");
		lifeSpanItem.setTitle("Time Stamp");
		lifeSpanItem.setMinValue(1362665402);
		lifeSpanItem.setMaxValue(1362668611);
		//lifeSpanItem.setDefaultValue(60);
		lifeSpanItem.setWidth(170);
		lifeSpanItem.setOperator(OperatorId.LESS_THAN);*/
		
        final SelectItem selectMultiRole = new SelectItem("rol");  
        //selectMultiRole.setTitle("Rol");  
        selectMultiRole.setMultiple(true);  
        selectMultiRole.setWidth(150);
        //selectMultiRole.setMultipleAppearance(MultipleAppearance.GRID);
        
        final SelectItem selectMultiItem = new SelectItem("idItem");  
        //selectMultiUser.setTitle("Users");  
        selectMultiItem.setMultiple(true);  
        selectMultiItem.setWidth(150); 
        //selectMultiUser.setMultipleAppearance(MultipleAppearance.GRID); 
		

		filterForm.setFields(selectMultiTeam, selectMultiUser, selectMultiRole, selectMultiItem);

		filterForm.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				tileGrid.fetchData(filterForm.getValuesAsCriteria());
				listGrid.fetchData(filterForm.getValuesAsCriteria());
				//System.out.print(filterForm.getValues());
				
				refreshBreadcrumbs(filterForm);
				
				
			}
		});
		return filterForm;
	}
	
	private void refreshBreadcrumbs(final DynamicForm filterForm) {
		String bCrumb = "<b>Filtered by: </b>";
		
		@SuppressWarnings("rawtypes")
		Iterator it = filterForm.getValues().entrySet().iterator();
	    while (it.hasNext()) {
	        @SuppressWarnings("rawtypes")
			Map.Entry pairs = (Map.Entry)it.next();
	        System.out.println(pairs.getKey() + " = " + pairs.getValue());
	        
	        
	        if (pairs.getValue() != null) {
	        	bCrumb += " "+pairs.getKey();
		        bCrumb += " = "+pairs.getValue();
		        if (it.hasNext()) {
			        bCrumb += " >> ";
				}
			}
	        
	        it.remove(); // avoids a ConcurrentModificationException
	    }
	    
	    breadcrumb.setHTML(bCrumb);
	}
}

