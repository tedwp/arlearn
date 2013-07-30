package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.i18.GeneralItemConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONString;
import com.google.maps.gwt.client.LatLng;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;

public class BasicMetadataEditorPlus extends BasicMetadataEditor {

	private static GeneralItemConstants constants = GWT.create(GeneralItemConstants.class);

	protected CheckboxItem drawOnMap;
	protected CheckboxItem automaticallyLaunch;
	protected CheckboxItem toggleRichtText;

	protected TextItem latText;
	protected TextItem lngText;

	public BasicMetadataEditorPlus(boolean showTitle, boolean showDescription) {
		super(showTitle, showDescription);
	}

	protected void createForm() {
		titleText = new TextItem(GeneralItemModel.NAME_FIELD, constants.title());
		titleText.setStartRow(true);
        orderText = new TextItem(GeneralItemModel.SORTKEY_FIELD, constants.order());

		drawOnMap = new CheckboxItem("drawOnMap", constants.drawOnMap());
		drawOnMap.setStartRow(true);
		drawOnMap.setRedrawOnChange(true);
		automaticallyLaunch = new CheckboxItem(GeneralItemModel.AUTO_LAUNCH, constants.automaticallyLaunch());

		latText = new TextItem(GeneralItemModel.LAT_FIELD, constants.latitude());
		latText.setStartRow(true);
		lngText = new TextItem(GeneralItemModel.LNG_FIELD, constants.longitude());
		lngText.setStartRow(true);

		toggleRichtText = new CheckboxItem("toggleRichtText", constants.richTextEditing());
		toggleRichtText.setValue(true);
		toggleRichtText.addChangedHandler(new ChangedHandler() {

			@Override
			public void onChanged(ChangedEvent event) {
				boolean richTextBool = (Boolean) event.getValue();
				BasicMetadataEditorPlus.this.showRichtText = richTextBool;
				if (showRichtText) {
					richTextEditor.setVisibility(Visibility.INHERIT);
					richTextEditor.setValue(textAreaItem.getValueAsString());

				} else {
					richTextEditor.setVisibility(Visibility.HIDDEN);
					textAreaItem.setValue(richTextEditor.getValue());

				}
				form.redraw();
			}
		});

		FormItemIfFunction drawOnMapFunction = new FormItemIfFunction() {
			public boolean execute(FormItem item, Object value, DynamicForm form) {
				if (form.getValue("drawOnMap") == null)
					return false;
				return form.getValue("drawOnMap").equals(Boolean.TRUE);
			}
		};
		latText.setShowIfCondition(drawOnMapFunction);
		lngText.setShowIfCondition(drawOnMapFunction);

		form.setFields(titleText, orderText, drawOnMap, automaticallyLaunch, latText, toggleRichtText, lngText, textAreaItem);
		form.setNumCols(4);
		form.setWidth100();

		addMember(form);
	}

	public void saveToBean(GeneralItem gi) {
		gi.getJsonRep().put(GeneralItemModel.SORTKEY_FIELD, new JSONNumber(Integer.parseInt(form.getValueAsString(GeneralItemModel.SORTKEY_FIELD))));
		if (form.getValue("drawOnMap") != null && (Boolean) form.getValue("drawOnMap")) {
			gi.setDouble(GeneralItemModel.LAT_FIELD, Double.parseDouble(form.getValueAsString(GeneralItemModel.LAT_FIELD)));
			gi.setDouble(GeneralItemModel.LNG_FIELD, Double.parseDouble(form.getValueAsString(GeneralItemModel.LNG_FIELD)));
		} else {
			gi.getJsonRep().put(GeneralItemModel.LAT_FIELD, null);
			gi.getJsonRep().put(GeneralItemModel.LNG_FIELD, null);
		}
		if (form.getValue(GeneralItemModel.AUTO_LAUNCH) != null && (Boolean) form.getValue(GeneralItemModel.AUTO_LAUNCH)) {
			gi.setBoolean(GeneralItemModel.AUTO_LAUNCH, true);
		} else {
			gi.setBoolean(GeneralItemModel.AUTO_LAUNCH, false);
		}
		super.saveToBean(gi);
	}

	public void loadGeneralItem(GeneralItem gi) {
		super.loadGeneralItem(gi);
		form.setValue(GeneralItemModel.SORTKEY_FIELD, gi.getInteger(GeneralItemModel.SORTKEY_FIELD));
		if (gi.getDouble(GeneralItemModel.LAT_FIELD) != null)
			form.setValue(GeneralItemModel.LAT_FIELD, gi.getDouble(GeneralItemModel.LAT_FIELD));
		if (gi.getDouble(GeneralItemModel.LNG_FIELD) != null)
			form.setValue(GeneralItemModel.LNG_FIELD, gi.getDouble(GeneralItemModel.LNG_FIELD));
		form.setValue("drawOnMap", gi.getDouble(GeneralItemModel.LNG_FIELD) != null);
		form.setValue(GeneralItemModel.AUTO_LAUNCH, gi.getBoolean(GeneralItemModel.AUTO_LAUNCH));
	}

	public void coordinatesChanged(LatLng newCoordinates) {
		form.setValue("drawOnMap", true);
		form.setValue(GeneralItemModel.LAT_FIELD, newCoordinates.lat());
		form.setValue(GeneralItemModel.LNG_FIELD, newCoordinates.lng());

	}

}
