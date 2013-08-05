package org.celstec.arlearn2.beans.oauth;

import java.util.ArrayList;
import java.util.List;

import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.BeanDeserializer;
import org.celstec.arlearn2.beans.deserializer.json.ListDeserializer;
import org.celstec.arlearn2.beans.serializer.json.BeanSerializer;
import org.celstec.arlearn2.beans.serializer.json.ListSerializer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class OauthInfoList extends Bean{

	public static String OauthInfoType = "org.celstec.arlearn2.beans.oauth.OauthInfo";

	private List<OauthInfo> oauthInfoList = new ArrayList<OauthInfo>();

	public List<OauthInfo> getOauthInfoList() {
		return oauthInfoList;
	}

	public void setOauthInfoList(List<OauthInfo> oauthList) {
		this.oauthInfoList = oauthList;
	}
	
	public void addOauthInfo(OauthInfo oauthInfo) {
		this.oauthInfoList.add(oauthInfo);
	}
	
	public static BeanSerializer serializer = new BeanSerializer() {

		@Override
		public JSONObject toJSON(Object bean) {
			OauthInfoList accountList = (OauthInfoList) bean;
			JSONObject returnObject = super.toJSON(bean);
			try {
				if (accountList.getOauthInfoList() != null)
					returnObject.put("oauthInfoList", ListSerializer.toJSON(accountList.getOauthInfoList()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return returnObject;
		}
	};

	public static BeanDeserializer deserializer = new BeanDeserializer() {

		@Override
		public OauthInfoList toBean(JSONObject object) {
			OauthInfoList al = new OauthInfoList();
			try {
				initBean(object, al);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return al;
		}

		public void initBean(JSONObject object, Bean genericBean) throws JSONException {
			super.initBean(object, genericBean);
			OauthInfoList oauthList = (OauthInfoList) genericBean;
			if (object.has("oauthInfoList"))
				oauthList.setOauthInfoList(ListDeserializer.toBean(object.getJSONArray("oauthInfoList"), OauthInfo.class));
		}
	};
	
}
