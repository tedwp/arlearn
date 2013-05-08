package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.datasource.GeneralItemModel;
import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.AdvancedDependenciesEditor;
import org.celstec.arlearn2.portal.client.author.ui.generic.canvas.RichTextCanvas;
import org.celstec.arlearn2.portal.client.author.ui.generic.canvas.RichTextCanvas.HtmlSaver;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class GeneralItemDetailEdit extends HLayout {
	
	private GeneralItem gi;
	
	VLayout infoLayout;
	DynamicForm form = new DynamicForm();
	RichTextCanvas rtCanvas;
	public GeneralItemDetailEdit() {
		final TextItem titleText = new TextItem(GeneralItemModel.NAME_FIELD, "Title");
		form.setFields(titleText);
		form.setWidth100();
		
		rtCanvas = new RichTextCanvas("", "", new HtmlSaver() {	
			@Override
			public void htmlReady(String html) {
				// TODO Auto-generated method stub
				
			}
		});
		setBorder("1px dashed blue");  
		infoLayout = new VLayout();
		infoLayout.setWidth(500);
		

		addMember(infoLayout);
		setAlign(Alignment.LEFT);
		
		infoLayout.addMember(form);
		infoLayout.addMember(rtCanvas);
		rtCanvas.setHeight("*");
		rtCanvas.setBorder("1px dashed blue");  
		
		
		SectionStack stack = new SectionStack();
	
		stack.addSection(new SectionStackSection("Video Object"));
		
		SectionStackSection appearStack = new SectionStackSection("Appear");
		appearStack.setItems(new AdvancedDependenciesEditor());
		stack.addSection(appearStack);
		
		addMember(stack);
	}
	
	public void loadGeneralItem(GeneralItem gi) {
		this.gi = gi;
		form.setValue(GeneralItemModel.NAME_FIELD, gi.getTitle());
		rtCanvas.updateHtml(gi.getRichText());
	}
}
