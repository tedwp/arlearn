package org.celstec.arlearn2.portal.client.author.ui.gi;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.AdvancedDependenciesEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.dependency.DependencyEditor;
import org.celstec.arlearn2.portal.client.author.ui.gi.extensionEditors.ExtensionEditor;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;

public class GeneralItemEditStack extends SectionStack {
	
	private SectionStackSection extensionStackSection;
	private SectionStackSection appearStackSection;
	private SectionStackSection disappearStackSection;
	
	public GeneralItemEditStack(){
		
	}

	public void setExtensionStack(GeneralItem gi, ExtensionEditor extensionEditor){
		if (extensionStackSection != null) {
			removeSection(extensionStackSection.getID());
		}
		extensionStackSection = new SectionStackSection(gi.getHumanReadableName());
		extensionStackSection.setItems((Canvas)extensionEditor);
		addSection(extensionStackSection, 0);
	}
	
	public void setAppearStack(DependencyEditor dependencyEditor) {
		if (appearStackSection != null) {
			removeSection(appearStackSection.getID());
		}
		appearStackSection = new SectionStackSection("Appear");
		appearStackSection.setItems(dependencyEditor);
		addSection(appearStackSection);
	}
	
	public void setDisappearStack(DependencyEditor depEditor) {
		if (disappearStackSection != null) {
			removeSection(disappearStackSection.getID());
		}
		disappearStackSection = new SectionStackSection("Disappear");
		disappearStackSection.setItems(depEditor);
		addSection(disappearStackSection);
	}
}
