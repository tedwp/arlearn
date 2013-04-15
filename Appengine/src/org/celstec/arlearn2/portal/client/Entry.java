/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.portal.client;

import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthClient;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthFbClient;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthGoogleClient;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthLinkedIn;
import org.celstec.arlearn2.gwtcommonlib.client.network.JsonCallback;
import org.celstec.arlearn2.gwtcommonlib.client.network.OauthNetworkClient;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.widgets.layout.VLayout;

public class Entry implements EntryPoint {

	private VLayout vLayout;

	public Anchor anchorFacebook = new Anchor("Access with Facebook");
	public Anchor anchorGoogle = new Anchor("Access with Google");
	public Anchor anchorLinkedIn = new Anchor("Access with LinkedIn");

	@Override
	public void onModuleLoad() {
		// if (RootPanel.get("button-create") != null) {
		// secondMenu();
		// } else {

		OauthNetworkClient.getInstance().getOauthClientPackage(new JsonCallback() {
			public void onJsonReceived(JSONValue jsonValue) {
				for (int i = 0; i < jsonValue.isArray().size(); i++) {
					JSONObject object = jsonValue.isArray().get(i).isObject();
					System.out.println(object);
					switch ((int) object.get("provider_id").isNumber().doubleValue()) {
					case OauthClient.FBCLIENT:
						OauthFbClient.init(object.get("client_id").isString().stringValue(), object.get("redirect_uri").isString().stringValue());
						break;
					case OauthClient.GOOGLECLIENT:
						OauthGoogleClient.init(object.get("client_id").isString().stringValue(), object.get("redirect_uri").isString().stringValue());
						break;
					case OauthClient.LINKEDINCLIENT:
						OauthLinkedIn.init(object.get("client_id").isString().stringValue(), object.get("redirect_uri").isString().stringValue());
						break;

					default:
						break;
					}
				}
				System.out.println("received package");
				// mainMenu();
				// setOauthConfig();
				loadPage();
			}

		});
		// }

	}

	public void loadPage() {
		final OauthClient client = OauthClient.checkAuthentication();
		if (client != null) {
			// authenticated
			if (RootPanel.get("button-facebook") != null)
				(new OauthPage()).loadPage();
			if (RootPanel.get("button-create") != null)
				(new PortalPage()).loadPage();
		} else {
			// not authenticated
			if (RootPanel.get("button-facebook") == null)
				Window.open("/search.html", "_self", "");
			else {
				(new OauthPage()).loadPage();
			}
		}
	}

	public void setOauthConfig() {
		final OauthClient client = OauthClient.checkAuthentication();
		if (client != null) {
			// authenticated( client);
		} else {

			notauthenticated();
		}
	}

//	public void authenticated(final OauthClient client) {
//		RootPanel.get("sendButtonContainerFacebook").add(new Anchor("you are authenticated"));
//		RootPanel.get("sendButtonContainerGoogle").add(new Anchor("you are authenticated"));
//		RootPanel.get("sendButtonContainerLinkedIn").add(new Anchor("you are authenticated"));
//		Button b = new Button("disauthenticate");
//		b.addClickHandler(new com.smartgwt.client.widgets.events.ClickHandler() {
//
//			@Override
//			public void onClick(com.smartgwt.client.widgets.events.ClickEvent event) {
//				client.disAuthenticate();
//				Window.Location.reload();
//			}
//		});
//		vLayout = new VLayout();
//		vLayout.setMembersMargin(15);
//		Label accestoken = new Label(client.getAccessToken());
//		vLayout.addMember(b);
//		vLayout.addMember(accestoken);
//		RootPanel.get("container").add(vLayout);
//		System.out.println(Window.Location.getParameter("type"));
//		
//	}

	public void notauthenticated() {
		RootPanel.get("sendButtonContainerFacebook").add(anchorFacebook);
		RootPanel.get("sendButtonContainerGoogle").add(anchorGoogle);
		RootPanel.get("sendButtonContainerLinkedIn").add(anchorLinkedIn);
		anchorFacebook.setHref((new OauthFbClient()).getLoginRedirectURL());

		anchorGoogle.setHref((new OauthGoogleClient()).getLoginRedirectURL());
		anchorLinkedIn.setHref((new OauthLinkedIn()).getLoginRedirectURL());
	}

}
