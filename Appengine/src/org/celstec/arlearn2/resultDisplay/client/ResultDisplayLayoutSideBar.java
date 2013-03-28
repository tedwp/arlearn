package org.celstec.arlearn2.resultDisplay.client;

import java.util.Iterator;
import java.util.Map;

import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.events.ItemChangedEvent;
import com.smartgwt.client.widgets.form.events.ItemChangedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;


/**
 * Second approach of Result Display. This structure has a bar side in left part and on the other side has 
 * the toolbar and the results area.
 * 
 * @version
 * Left side bar
 * 
 * @author
 * Angel
 * 
 * @see
 * ResultDisplayLayout
 * 
 * */
public class ResultDisplayLayoutSideBar extends HLayout {
	
	private Grid grid = null;
	private List list = null;
	private Mixed mixed = null;
	private final HTML breadcrumb = new HTML();
	private SearchForm filter = null; 
	
	private VLayout rigthSide = null;
	

	public ResultDisplayLayoutSideBar(Grid tileGrid, List listGrid, Mixed mixedGrid) {
		super();
		setWidth100();  
        setHeight100();
        
        this.grid = tileGrid;
        this.list = listGrid;
        this.mixed = mixedGrid;
        this.filter = new SearchForm();
        
        HLayout toolbar = new HLayout(10); 	// Layout for visualization options, clear button and breadcrumb.
        toolbar.setHeight(22);
        toolbar.setPadding(10);
        
        breadcrumb.setHTML("<b>Filters</b>");
		breadcrumb.setWidth("80%");
		breadcrumb.setHeight("30px");
		refreshBreadcrumbs(filter);
        
        toolbar.addMember(createVisualizationToolbar());
        
        IButton clearButton = createClearButton("Reset filter");
        
        clearButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				grid.fetchData();
				list.fetchData();
				filter.clearValues();
				refreshBreadcrumbs(filter);
			}
		});
        
        toolbar.addMember(clearButton);
        toolbar.addMember(breadcrumb);
        
        rigthSide = new VLayout(); 	// Layout toolbar and results

        rigthSide.addMember(toolbar);
        rigthSide.addMember(tileGrid);
        
        addMember(filter);
		addMember(rigthSide);
		
		filter.addItemChangedHandler(new ItemChangedHandler() {
			public void onItemChanged(ItemChangedEvent event) {
				grid.fetchData(filter.getValuesAsCriteria());
				list.fetchData(filter.getValuesAsCriteria());
				refreshBreadcrumbs(filter);
				System.out.println(filter.getValues());
			}
		});
        
	}

	private IButton createClearButton(String name) {
		IButton clearButton = new IButton(name);
		clearButton.setAutoFit(true);
		clearButton.setWidth(150);
		
		return clearButton;
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
        
        gridButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (rigthSide.hasMember(list)) {
					rigthSide.removeMember(list);	
				}
				
				if (rigthSide.hasMember(mixed)) {
					rigthSide.removeMember(mixed);					
				}
				rigthSide.addMember(grid);
			}
		});
        

        
        toolStrip.addMember(gridButton);  
          
        ImgButton listButton = new ImgButton();  
        listButton.setSize(24);  
        listButton.setShowRollOver(false);  
        listButton.setSrc("list.png");  
        toolStrip.addMember(listButton); 
        listButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (rigthSide.hasMember(grid)) {
					rigthSide.removeMember(grid);					
				}
				
				if (rigthSide.hasMember(mixed)) {
					rigthSide.removeMember(mixed);					
				}
				rigthSide.addMember(list);
			}
		});
          
        ImgButton mixedButton = new ImgButton();  
        mixedButton.setSize(24);  
        mixedButton.setShowRollOver(false);  
        mixedButton.setSrc("mixed.png");  
        toolStrip.addMember(mixedButton);
        mixedButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (rigthSide.hasMember(grid)) {
					rigthSide.removeMember(grid);					
				}
				if (rigthSide.hasMember(list)) {
					rigthSide.removeMember(list);		
				}
				rigthSide.addMember(mixed);
			}
		});
		return toolStrip;
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
