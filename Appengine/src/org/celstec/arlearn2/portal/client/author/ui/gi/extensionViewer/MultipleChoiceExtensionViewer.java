package org.celstec.arlearn2.portal.client.author.ui.gi.extensionViewer;

import org.celstec.arlearn2.gwtcommonlib.client.objects.GeneralItem;
import org.celstec.arlearn2.gwtcommonlib.client.objects.MultipleChoiceTest;

import com.smartgwt.client.widgets.HTMLFlow;

public class MultipleChoiceExtensionViewer extends HTMLFlow implements ExtensionViewer {

	public MultipleChoiceExtensionViewer() {
		setWidth(230);

		String contents = "<hr><span class='exampleDropTitle'>Ajax  </span> " + "<b>A</b>synchronous <b>J</b>avaScript <b>A</b>nd <b>X</b>ML (AJAX) is a " + "Web development technique for creating interactive <b>web applications</b>.<hr>";
		setContents(contents);
	}

	@Override
	public void loadGeneralItem(GeneralItem gi) {
		MultipleChoiceTest yt = (MultipleChoiceTest) gi;
		String contents = "MultipleChoice choice test: todo work out viewing part";
		setContents(contents);
	}

}
