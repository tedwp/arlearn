package org.celstec.arlearn2.portal.client;

import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthFbClient;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthGoogleClient;
import org.celstec.arlearn2.gwtcommonlib.client.auth.OauthLinkedIn;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;

public class OauthPage {

	public void loadPage() {
		CustomButton facebook_button = new CustomButton();
		facebook_button.setText("Sign in with Facebook");
		facebook_button.setResource("images/facebook.png");
		facebook_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open((new OauthFbClient()).getLoginRedirectURL(), "_self", ""); 
			}
		});

		CustomButton twitter_button = new CustomButton();
		twitter_button.setText("Sign in with Google");
		twitter_button.setResource("images/google.png");
		twitter_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open((new OauthGoogleClient()).getLoginRedirectURL(), "_self", ""); 
			}
		});

		CustomButton linkedin_button = new CustomButton();
		linkedin_button.setText("Sign in with Linkedin");
		linkedin_button.setResource("images/linked-in.png");
		linkedin_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Window.open((new OauthLinkedIn()).getLoginRedirectURL(), "_self", ""); 
			}
		});

		RootPanel layout_button_facebook = RootPanel.get("button-facebook");
		RootPanel layout_button_twitter = RootPanel.get("button-twitter");
		RootPanel layout_button_linkedin = RootPanel.get("button-linkedin");

		layout_button_facebook.add(facebook_button);
		layout_button_twitter.add(twitter_button);
		layout_button_linkedin.add(linkedin_button);
		
	}
}
