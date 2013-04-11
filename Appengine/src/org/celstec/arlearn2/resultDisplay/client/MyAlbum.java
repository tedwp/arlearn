package org.celstec.arlearn2.resultDisplay.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.EndedEvent;
import com.google.gwt.event.dom.client.EndedHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.shared.HandlerRegistration;
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
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickEvent;

public class MyAlbum extends PopupPanel {

	private VerticalPanel pPanel;
	private Widget pElement = new Widget();
	private FocusPanel pFocusPanel;
	private HorizontalPanel hPanel;
	
	private Audio audio = null;

	private RecordList widgetsSelected;

	private Button next = new Button(">");
	private Button previous = new Button("<");
	private Button close = new Button("X");
	private int position = 0;

	private Context2d context;
	
	
	public MyAlbum() {
		super();
		setTitle("Demo Application");
		setAnimationEnabled(true);
		ensureDebugId("cwBasicPopup-imagePopup");
		//getElement().getStyle().setLeft(50, Unit.PCT);
		getElement().getStyle().setZIndex(1000000);
		getElement().getStyle().setBorderWidth(0, Unit.PX);
		
		getElement().getStyle().setPadding(0, Unit.PX);
		
		
		/*getElement().getStyle().setProperty("position", "relative");
		*/
		
		pPanel = new VerticalPanel();
		/*pPanel.getElement().getStyle().setProperty("position", "relative");
		pPanel.getElement().getStyle().setProperty("float", "left");
		pPanel.getElement().getStyle().setProperty("right", "50%");
		*/
		hPanel = new HorizontalPanel();

		HTML counter = new HTML();
		counter.setHTML(position + " of " + 0);

		hPanel.add(previous);
		hPanel.add(counter);
		hPanel.add(next);
		
		hPanel.add(close);
		
		pPanel.add(hPanel);

		setGlassEnabled(true);
		setWidget(pPanel);		
		
		//setWidget(pFocusPanel);		
		//setAutoHideEnabled(true);
		
		addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				// Fix bug: setCurrentTime audio y video to 0.0 otherwise 
				// in next playing it doesn't work
				System.out.println("Closing panel...");
				
				/*if (!audio.equals(null)) {
					audio.setCurrentTime(0.0);
					audio.pause();
					audio = null;
					System.out.println("Set current time to 0.0");
				}
				*/				
			}
		});
	}
	
	public void refreshViewPopup(RecordList recordList, int direction) {

		widgetsSelected = recordList;

		switch (direction) {
		case 1:
			position++;
			break;
		case 2:
			position--;
			break;

		default:
			break;
		}

		pElement = changeWidget();

		if (pPanel.getWidgetCount() > 1) {
			pPanel.remove(0);
		}
		pPanel.insert(pElement, 0);

	}

	public Button getButtonNext() {
		return next;
	}

	public Button getButtonPrevious() {
		return previous;
	}
	
	public Button getButtonClose() {
		return close;
	}
	
	public void setEnabledButtonNext(boolean a) {
		if (a) 
			next.setDisabled(false);
		else
			next.setDisabled(true);
	}
	
	public void setEnabledButtonPrevious(boolean a) {
		if (a) 
			previous.setDisabled(false);
		else
			previous.setDisabled(true);
	}

	public FocusPanel getpFocusPanel() {
		return pFocusPanel;
	}

	public void setpFocusPanel(FocusPanel pFocusPanel) {
		this.pFocusPanel = pFocusPanel;
	}

	private Widget changeWidget() {
		Image image = null;
		Video video = null;
		
		if (position >= widgetsSelected.getLength()) {
			position = 0;
		} else if (position < 0) {
			position = widgetsSelected.getLength() - 1;
		}
		
		System.out.println("record "+widgetsSelected.get(position));
		String auxPic = widgetsSelected.get(position).getAttribute("picture");
		String auxAud = widgetsSelected.get(position).getAttribute("audio");
		String auxVid = widgetsSelected.get(position).getAttribute("video");
		String auxDoc = widgetsSelected.get(position).getAttribute("document");

		if (!auxPic.equals("") && auxVid.equals("") && auxAud.equals("") && auxDoc.equals("")) {
			System.out.println("This is Image: " + auxPic);

			image = new Image("images/animals/" + auxPic);
			
			FocusPanel pFocusPanel = new FocusPanel(image);
			pFocusPanel.addKeyDownHandler(new KeyDownHandler() {
	            

				@Override
				public void onKeyDown(KeyDownEvent event) {
					// TODO Auto-generated method stub
					switch (event.getNativeKeyCode()) {
	                case KeyCodes.KEY_RIGHT:
	                	System.out.println("Derecha");
	                    break;
	                case KeyCodes.KEY_LEFT:
	                	System.out.println("Izquierda");
	                    break;
	                case KeyCodes.KEY_ESCAPE:
	                	System.out.println("Escape");
	                    break;
	                }
				}
	        });
	        
	         final Element aux = image.getElement();
	         final Image auxI = image;
	        
	         pFocusPanel.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
					if (event.getRelativeX(aux) >= auxI.getWidth() / 2) {
						refreshViewPopup(widgetsSelected, 1);

					} else {
						refreshViewPopup(widgetsSelected, 2);

					}
				}
	        });

			double scaleFactor = getScaleFactor(image.getWidth(),
					image.getHeight());
			pFocusPanel.setFocus(true);

			System.out.println(scaleFactor+" This is Image: " + auxPic);
			System.out.println(Window.getClientWidth()+" "+Window.getClientHeight()+" "+image.getWidth()+" "+image.getHeight());
			
		
			final int width = (int) (image.getWidth() * scaleFactor);
			final int height = (int) (image.getHeight() * scaleFactor);
			image.setPixelSize(width, height);

			
			
			return pFocusPanel;

		} else if (!auxPic.equals("") && !auxVid.equals("") && auxAud.equals("") && auxDoc.equals("")) {
			System.out.println("This is video: " + auxVid);

			String pathVideo = "video/" + auxVid;

			video = Video.createIfSupported();
			if (video == null) {
				return null;
			}

			video.load();

			video.addSource(pathVideo);
			video.setControls(true);
			video.setAutoplay(true);

			return video;


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
			
			return audio;


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
			
			return visorDocuments;
			

		} else {
			// Graphics
			System.out.println("This is Graphic: "
					+ widgetsSelected.get(position).getAttribute("graphic"));
			
			Canvas canvas = Canvas.createIfSupported();

			if (canvas == null) {
				return null;
			}

			canvas.setStyleName("mainCanvas");
			canvas.setWidth(300 + "px");
			canvas.setCoordinateSpaceWidth(400);

			canvas.setHeight(300 + "px");
			canvas.setCoordinateSpaceHeight(300);

			
			double scaleFactor = getScaleFactor(canvas.getCoordinateSpaceWidth(),
					canvas.getCoordinateSpaceHeight());

			final int width = ((int) (canvas.getCoordinateSpaceWidth() * scaleFactor))-20;
			final int height = ((int) (canvas.getCoordinateSpaceHeight() * scaleFactor))-20;
			canvas.setPixelSize(width, height);
			
			int[] anArray;
			String[] colour;
			String[] text;

	        // data
		    anArray = new int[6];
		    
	        anArray[0] = 75;
	        anArray[1] = 68;
	        anArray[2] = 32;
	        anArray[3] = 95;
	        anArray[4] = 20;
	        anArray[5] = 51;
	        
		    // colour
	        colour = new String[6];
	        
	        colour[0] = "#7E3817";
	        colour[1] = "#C35817";
	        colour[2] = "#EE9A4D";
	        colour[3] = "#A0C544";
	        colour[4] = "#348017";
	        colour[5] = "#307D7E";
	        
		    // text
	        text = new String[6];
	        
	        text[0] = "January";
	        text[1] = "Febrary";
	        text[2] = "April";
	        text[3] = "May";
	        text[4] = "September";
	        text[5] = "December";
	        
	        
	        int total = 0;
	        double lastend = 0;
	        
	        for(int i=0; i<anArray.length; i++){
	        	total += anArray[i];
	        }


			context = canvas.getContext2d();
			
			int rndX = (width/2)-70;
			int rndY = (height/2)-20;
			int rnRadio = Math.min(rndX, rndY) / 2;

			System.out.println("X:"+rndX+" Y:"+rndY+" radio:"+rnRadio+" "+width+" "+height+" "+scaleFactor+" "+canvas.getCoordinateSpaceHeight()+" "+canvas.getCoordinateSpaceWidth());

			for (int j = 0; j < anArray.length; j++) {
			      
				context.beginPath();
			    context.moveTo(rndX, rndY);
			      
			    double value = (double)(anArray[j])/(double)total;

				context.arc(rndX,rndY,rndY,lastend,lastend+(Math.PI*2*(value)),false);
				context.setFillStyle(colour[j]);
				context.fill();
				context.fillText(text[j], rndX+rnRadio+100, rndY-rnRadio/2+j*18);
				//context.stroke();
				lastend += Math.PI*2*(value);
				
				System.out.println(lastend+" "+anArray[j]+" "+total+" "+(value)+" "+Math.PI);
			}

			return canvas;
		}
	}

	private double getScaleFactor(int width, int height) {
		// TODO Auto-generated method stub
		return Math.min(Window.getClientWidth() / (double) width,
				Window.getClientHeight() / (double) height);
	}

	public int getWidgetCount() {
		// TODO Auto-generated method stub

		return pPanel.getWidgetCount();
	}

	public void changeCounter() {
		hPanel.remove(1);
		HTML aux = new HTML();
		aux.setHTML((position + 1) + " of " + widgetsSelected.getLength());
		hPanel.insert(aux, 1);

	}

}
