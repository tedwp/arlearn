package org.celstec.arlearn2.resultDisplay.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.media.client.Audio;
import com.google.gwt.media.client.Video;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.ImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.tile.TileGrid;

public class SlideShow extends PopupPanel {

	private static final int STATUS_BAR_ITEMS = 3;

	private static SlideShow instance;
	
	private VerticalPanel mainPanel;
	private HorizontalPanel statusBar;
	
	private static RecordList widgetsSelected;
	
	private static int currentPosition;
	private static Widget currentElement;
		
	public static SlideShow getInstance(TileGrid resultSelected) {
				
		widgetsSelected = resultSelected.getRecordList();
		
		if (instance == null){
			instance = new SlideShow();
		}else{
			instance.updateCurrentView(currentPosition);
		}
	
		return instance;
	}
	
	private SlideShow() {
		
		setTitle("SlideShow ARLearn");
		setAnimationEnabled(true);
		ensureDebugId("cwBasicPopup-imagePopup");
		
		getElement().getStyle().setZIndex(1000000);
		getElement().getStyle().setBorderWidth(0, Unit.PX);
		getElement().getStyle().setPadding(0, Unit.PX);
		getElement().getStyle().setProperty("marginLeft", "auto");
		getElement().getStyle().setProperty("marginRight", "auto");
		getElement().getStyle().setProperty("width", "90%");
		
		getElement().getStyle().setProperty("right", "0");
		getElement().setId("popup-main");
				
		mainPanel = new VerticalPanel();
		
		currentPosition = 1;
		
		updateCurrentView(currentPosition);
		mainPanel.add(statusBar);


		setAutoHideEnabled(true);
		setGlassEnabled(true);
		getGlassElement().getStyle().setZIndex(1000000);
		getGlassElement().getStyle().setOpacity(0.7);

		setWidget(mainPanel);		
	}
	
	private HorizontalPanel setStatusBar(int newPos, int numberElem) {

		HTML counter = new HTML();
		counter.setHTML((newPos+1) + " of " + numberElem);
		counter.getElement().getStyle().setProperty("textAlign", "center");
		counter.getElement().getStyle().setProperty("position", "absolute");
		counter.getElement().getStyle().setProperty("bottom", "2px");
		
		if (statusBar == null || statusBar.getWidgetCount() < STATUS_BAR_ITEMS) {
			
			statusBar = new HorizontalPanel();
			
			statusBar.getElement().getStyle().setPosition(Position.ABSOLUTE);
			statusBar.getElement().getStyle().setProperty("width", "100%");
			statusBar.getElement().getStyle().setProperty("bottom", "0");
			statusBar.addStyleName("my-mouse-out");
			statusBar.getElement().setId("statusBar");

			/**
			 * TODO 
			 * Is using MyMouseEventHandler to manage over and out mouse event.
			 * 
			 * Not using it at this moment
			 * */
			//statusBar.addDomHandler(new MyMouseEventHandler(), MouseOverEvent.getType());
			//statusBar.addDomHandler(new MyMouseEventHandler(), MouseOutEvent.getType());
						
			ImgButton next = new ImgButton();
			
			next.setSrc("btn-next.gif");
			next.getElement().getStyle().setFloat(Float.RIGHT);
			
			
			ImgButton previous = new ImgButton();  
			previous.getElement().getStyle().setFloat(Float.LEFT);
			
			previous.setSrc("btn-previous.gif");
			statusBar.add(previous);
			statusBar.add(counter);
			statusBar.add(next);
			
			next.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					instance.updateCurrentView(currentPosition++);
				}
			});
			
			previous.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					instance.updateCurrentView(currentPosition--);
				}
			});
		}
		else{
			statusBar.remove(1);
			statusBar.insert(counter, 1);
		}
		
		return statusBar;
	}
	
	private void updateCurrentView(int position){
		
		sms("Len "+widgetsSelected.getLength()+" Pos"+currentPosition);
		
		if (currentPosition >= widgetsSelected.getLength()) {
			currentPosition = 0;
		} else if (currentPosition < 0) {
			currentPosition = widgetsSelected.getLength() - 1;
		}
		
		setStatusBar(currentPosition, widgetsSelected.getLength());

		sms("Num: "+mainPanel.getWidgetCount());
		
		if (mainPanel.getWidgetCount() > 1) {
			mainPanel.remove(0);
			mainPanel.insert(getNextElement(), 0);
		}
		else{
			mainPanel.add(getNextElement());
		}
	}
	
	private static Widget addFocusElement(Widget element) {
		FocusPanel pFocusPanel = new FocusPanel(element);
		
		pFocusPanel.addKeyDownHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent event) {
				switch (event.getNativeKeyCode()) {
                case KeyCodes.KEY_RIGHT:
                	System.out.println("Derecha");
					instance.updateCurrentView(currentPosition++);
                    break;
                case KeyCodes.KEY_LEFT:
                	System.out.println("Izquierda");
					instance.updateCurrentView(currentPosition--);
                    break;
                case KeyCodes.KEY_ESCAPE:
                	System.out.println("Escape");
                	instance.hide();
                    break;
                }
			}
        });
		
		final Element aux = element.getElement();
		
		/**
		 * TODO 
		 * Is using MyMouseEventHandler to manage over and out mouse event.
		 * 
		 * Not using it at this moment
		 * */
		//pFocusPanel.addDomHandler(new MyMouseEventHandler(), MouseOverEvent.getType());
		//pFocusPanel.addDomHandler(new MyMouseEventHandler(), MouseOutEvent.getType());
		
       
        pFocusPanel.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
			
			@Override
			public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
				if (event.getRelativeX(aux) >= aux.getOffsetWidth() / 2) {
					instance.updateCurrentView(currentPosition++);

				} else {
					instance.updateCurrentView(currentPosition--);
				}
			}
		});

		return pFocusPanel;

	}
	
	private Widget getNextElement() {
		Image image = null;
		Video video = null;
		Audio audio = null;
		
		
		String auxPic = widgetsSelected.get(currentPosition).getAttribute("picture");
		String auxAud = widgetsSelected.get(currentPosition).getAttribute("audio");
		String auxVid = widgetsSelected.get(currentPosition).getAttribute("video");
		String auxDoc = widgetsSelected.get(currentPosition).getAttribute("document");

		System.out.println("pic:"+auxPic+" aud:"+auxAud+" vid:"+auxVid+" doc:"+auxDoc);
		
		if (!auxPic.equals("") && auxVid.equals("") && auxAud.equals("") && auxDoc.equals("")) {

			//image = new Image("images/animals/" + auxPic);
			//image = new Image("http://www.hdwallpapersarena.com/wp-content/uploads/2013/02/ew1.jpeg");
			//image = new Image("http://www.peakprosperity.com/files/pictures/picture-758.jpg");
			image = new Image(auxPic);
			sms("This is Image: " + auxPic+" "+image.getWidth()+" "+image.getHeight()+ " "+image.getOffsetWidth());
			sms("d"+getElement().getStyle().getWidth()+getElement().getOffsetHeight()+"s");
			
			if (image.getWidth() >= image.getHeight()) {
				image.getElement().getStyle().setProperty("width", "100%");
			}else if(image.getWidth() < image.getHeight()){
				
				double scaleFactor = getScaleFactor(image.getWidth(),
						image.getHeight());
				
				final int width = (int) (image.getWidth() * scaleFactor);
				final int height = (int) (image.getHeight() * scaleFactor*0.90);
				image.setPixelSize(width, height);
				getElement().getStyle().setWidth(width, Unit.PX);
				image.getElement().getStyle().setProperty("width", "100%");
			}
			
			currentElement = image;
			
		} else if (!auxPic.equals("") && !auxVid.equals("") && auxAud.equals("") && auxDoc.equals("")) {
			System.out.println("This is video: " + auxVid);

			//String pathVideo = "video/" + auxVid;
			String pathVideo = auxVid;

			video = Video.createIfSupported();
			if (video == null) {
				return null;
			}

			video.load();

			video.addSource(pathVideo);
			video.setControls(true);
			video.setAutoplay(true);

			currentElement = video;

		} else if (!auxPic.equals("") && auxVid.equals("") && !auxAud.equals("") && auxDoc.equals("")) {
			System.out.println("This is Audio: " + auxAud);

			String pathAudio = "audio/" + auxAud;

			audio = Audio.createIfSupported();
			if (audio == null) {
				return null;
			}

			audio.load();

			audio.addSource(pathAudio);			
			audio.setControls(true);
			
			currentElement = audio;


		} else if (!auxPic.equals("") && auxVid.equals("") && auxAud.equals("") && !auxDoc.equals("")) {
			// Text
			System.out.println("This is Text: "
					+ auxDoc);
			
			HTML visorDocuments = new HTML();
			
			double scaleFactor = getScaleFactor(600,
					700);

			final int width = ((int) (600* scaleFactor))-20;
			final int height = ((int) (700 * scaleFactor))-20;
			visorDocuments.setPixelSize(width, height);
			
			visorDocuments.setHTML("<iframe src='http://docs.google.com/viewer?url="+auxDoc+"&embedded=true' width='"+width+"' height='"+height+"' style='border: none;'></iframe>");
			
			currentElement = visorDocuments;
			

		}
		
		sms(""+currentElement);
		
		return addFocusElement(currentElement);
	}

	
	private double getScaleFactor(int width, int height) {
		
		sms(width+" "+height);
		
		return Math.min(Window.getClientWidth() / (double) width,
				Window.getClientHeight() / (double) height);

		
	}
	
	public void sms(String msg){
		System.out.println(msg);
	}
	
	
	
}
