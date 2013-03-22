package org.celstec.arlearn2.resultDisplay.client;

import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;

public class MyMouseEventHandler implements MouseOverHandler, MouseOutHandler {

//	private static UtilsSlideShow util;
	
	@Override
	public void onMouseOut(MouseOutEvent event) {
		Widget widget = (Widget) event.getSource();
	    
	    if (event.getRelativeX(widget.getElement()) >= widget.getOffsetWidth() / 2) {
		    //widget.addStyleName("my-mouse-out");
		    //widget.removeStyleName("my-mouse-over");
		    DOM.getElementById("statusBar").addClassName("my-mouse-out");
		    DOM.getElementById("statusBar").removeClassName("my-mouse-over");
//		    util._m("MyMouseEventHandler", "Sale de la parte derecha la imagen.");
		}
	    
	    
	}

	@Override
	public void onMouseOver(MouseOverEvent event) {
		Widget widget = (Widget) event.getSource();

//	    util = UtilsSlideShow.getInstance();
		
	    if (event.getRelativeX(widget.getElement()) >= widget.getOffsetWidth() / 2) {
		    //widget.addStyleName("my-mouse-over");
		    //widget.removeStyleName("my-mouse-out");
		    DOM.getElementById("statusBar").addClassName("my-mouse-over");
		    DOM.getElementById("statusBar").removeClassName("my-mouse-out");
//	    	util._m("MyMouseEventHandler", "Sobre la parte derecha la imagen.");
		}
	    
	}
}
