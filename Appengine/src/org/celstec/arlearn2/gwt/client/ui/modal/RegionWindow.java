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
package org.celstec.arlearn2.gwt.client.ui.modal;

import org.celstec.arlearn2.gwt.client.AuthoringConstants;
import org.celstec.arlearn2.gwt.client.network.JsonCallback;
import org.celstec.arlearn2.gwt.client.network.game.GameClient;
import org.celstec.arlearn2.gwt.client.ui.ConfigForm;
import org.celstec.arlearn2.gwt.client.ui.GameTab;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SliderItem;
import com.smartgwt.client.widgets.form.fields.SpacerItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.smartgwt.client.widgets.form.validator.IsFloatValidator;
import com.smartgwt.client.widgets.layout.HLayout;

public class RegionWindow  extends Window {
	private AuthoringConstants constants = GWT.create(AuthoringConstants.class);
	IsFloatValidator floatValidator = new IsFloatValidator();
	
	private CustomValidator cv = new CustomValidator() {
		
		@Override
		protected boolean condition(Object value) {
			if (value == null) return false;
			if ((""+value).trim().equals("")) return false;
			return true;
		}
	};
	
	public RegionWindow(final ConfigForm cf) {
		setWidth(610);
		setHeight(400);
		setTitle(constants.defineOfflineRegion());
		setIsModal(true);
		setShowModalMask(true);
		centerInPage();

		final DynamicForm form = new DynamicForm();
		form.setHeight100();
		form.setWidth100();
		form.setPadding(5);
		form.setNumCols(4);
		
		SpacerItem si = new SpacerItem();
		TextItem latUp = new TextItem();
		latUp.setName("latUp");
		latUp.setTitle(constants.latUp());
		latUp.setValidators(floatValidator,cv);
		
		
		TextItem lngLeft = new TextItem();
		lngLeft.setName("lngLeft");
		lngLeft.setTitle(constants.lngLeft());
		lngLeft.setValidators(floatValidator,cv);
		
		TextItem lngRight = new TextItem();
		lngRight.setName("lngRight");
		lngRight.setTitle(constants.lngRight());
		lngRight.setValidators(floatValidator,cv);
		
		TextItem latDown = new TextItem();
		latDown.setName("latDown");
		latDown.setTitle(constants.latDown());
		latDown.setValidators(floatValidator,cv);
		
		final IButton submitButton = new IButton(constants.add());
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.validate()) {
//					ConfigForm.Region r = new cf.Region();
					ConfigForm.Region r = cf.new Region();
					r.setLatUp(Double.parseDouble(form.getValueAsString("latUp")));
					r.setLatDown(Double.parseDouble(form.getValueAsString("latDown")));
					r.setLngLeft(Double.parseDouble(form.getValueAsString("lngLeft")));
					r.setLngRight(Double.parseDouble(form.getValueAsString("lngRight")));
					r.setMinZoom(Integer.parseInt(form.getValueAsString("minZoom")));
					r.setMaxZoom(Integer.parseInt(form.getValueAsString("maxZoom")));
					cf.addRegion(r);
					RegionWindow.this.destroy();
				}
			}
		});

		SliderItem minZoom = new SliderItem();
	    minZoom.setName("minZoom");
	    minZoom.setTitle(constants.minZoom());
	    minZoom.setMinValue(0);
	    minZoom.setMaxValue(18);
	    minZoom.setColSpan(4);
	    minZoom.setWrapTitle(false);
	    minZoom.setRedrawOnChange(true);
	    minZoom.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if (((Integer) form.getValue("maxZoom"))< ((Integer) event.getValue())) {
					form.setValue("maxZoom", ((Integer) event.getValue()));
				}
			}
		});
	    
		SliderItem maxZoom = new SliderItem();
	    maxZoom.setName("maxZoom");
	    maxZoom.setTitle(constants.maxZoom());
	    maxZoom.setWrapTitle(false);
	    maxZoom.setMinValue(0);
	    maxZoom.setMaxValue(18);
	    maxZoom.setColSpan(4);
	    maxZoom.setRedrawOnChange(true);
	    maxZoom.addChangedHandler(new ChangedHandler() {
			@Override
			public void onChanged(ChangedEvent event) {
				if (((Integer) form.getValue("minZoom"))> ((Integer) event.getValue())) {
					form.setValue("minZoom", ((Integer) event.getValue()));
				}
			}
		});

		    
		    
		HLayout buttonLayout = new HLayout();
		buttonLayout.setAlign(Alignment.CENTER);
		buttonLayout.setLayoutMargin(6);
		buttonLayout.setMembersMargin(6);
		buttonLayout.addMember(submitButton);
		
		form.setFields(si, latUp, lngLeft, lngRight, si, latDown, minZoom, maxZoom);
		addItem(form);
		addItem(buttonLayout);

	}

}
