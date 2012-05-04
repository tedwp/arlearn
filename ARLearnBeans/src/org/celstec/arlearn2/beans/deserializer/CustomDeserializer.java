package org.celstec.arlearn2.beans.deserializer;

import org.celstec.arlearn2.beans.Bean;
import org.codehaus.jettison.json.JSONObject;

public abstract class CustomDeserializer {
	public abstract Bean toBean(JSONObject object);
}
