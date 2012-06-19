package org.celstec.arlearn2.gwt.client.ui.modal;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.HTMLPane;
import com.smartgwt.client.widgets.Window;


public class OpenQuestionAnswerWindow extends Window {

	
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);

	public OpenQuestionAnswerWindow(String json, String title) {
		setWidth(500);
		setHeight(400);
		setTitle(title);
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();
		JSONValue jsonValue = JSONParser.parseLenient(json);
		HTMLPane htmlPane = new HTMLPane();
		String html = "";
		if (jsonValue.isObject().get("imageUrl")!= null) {
			html += "<img align='center' src=\""+jsonValue.isObject().get("imageUrl").isString().stringValue()+"\"/>";
		}
		if (jsonValue.isObject().get("audioUrl")!= null) {
			String audioUrl = jsonValue.isObject().get("audioUrl").isString().stringValue();
//			html += "<audio controls=\"controls\">";
//			html += "<source src=\""+audioUrl+"\" type=\"audio/mpeg\" />";
//			html += "<embed height=\"50px\" width=\"100px\" src=\"song.mp3\" />";
//			html += "</audio>";
			html += "<embed src=\""+audioUrl+"\" autostart=\"false\" loop=\"false\" width=\"350\" height=\"50\">";
		}
//		html += "<pre>"+json+"</json>";
		htmlPane.setContents(html);
		
		
		
		addItem(htmlPane);
	}

}
