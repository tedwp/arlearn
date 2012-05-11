package org.celstec.arlearn2.android.db.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;




public class MultipleChoiceTest extends OpenQuestion {
	public static String answersType = "org.celstec.arlearn2.android.db.beans.MultipleChoiceAnswerItem";

	public static final String TYPE = "MultipleChoiceTest";
	
	private List<MultipleChoiceAnswerItem> answers = new Vector();;

	public MultipleChoiceTest() {
		super();
	}
	
	public MultipleChoiceTest(GeneralItem gi) {
		super(gi);
		if (gi.getPayload() != null) {
			try {
				JSONObject object = new JSONObject(gi.getPayload());
				if (object != null) {
					if (object.has("answers")) {
						JSONArray array = object.getJSONArray("answers");
						ArrayList<MultipleChoiceAnswerItem> list = new ArrayList<MultipleChoiceAnswerItem>();
						for (int i = 0; i < array.length(); i++) {
							MultipleChoiceAnswerItem answer = new MultipleChoiceAnswerItem(array.getJSONObject(i));
							list.add(answer);
						}
						setAnswers(list);
					}
				}
			} catch (JSONException e) {
				// in case of a JSON exception, the fields are not filled in
			}
		}
	}

	public List<MultipleChoiceAnswerItem> getAnswers() {
		return answers;
	}

	public void setAnswers(List<MultipleChoiceAnswerItem> answers) {
		this.answers = answers;
	}
	
	
	public JSONObject getSpecificPartAsJson() {
		JSONObject json = super.getSpecificPartAsJson();
		try {
			
			JSONArray array = new JSONArray();
			Iterator<MultipleChoiceAnswerItem> it = getAnswers().iterator();
			while (it.hasNext()) {
				MultipleChoiceAnswerItem multipleChoiceAnswerItem = (MultipleChoiceAnswerItem) it.next();
				array.put(new JSONObject(multipleChoiceAnswerItem.getSpecificPartAsJson()));
			}
			json.put("answers", array);
		} catch (JSONException e) {
			// can't happen
			e.printStackTrace();
		}
		return json;
	}

}
