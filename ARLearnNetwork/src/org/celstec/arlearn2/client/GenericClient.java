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
package org.celstec.arlearn2.client;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.celstec.arlearn2.beans.Bean;
import org.celstec.arlearn2.beans.deserializer.json.JsonBeanDeserializer;
import org.celstec.arlearn2.beans.serializer.json.JsonBeanSerialiser;
import org.celstec.arlearn2.client.exception.ARLearnException;
import org.celstec.arlearn2.network.ConnectionFactory;
import org.celstec.arlearn2.network.HttpConnection;
import org.codehaus.jettison.json.JSONException;

public class GenericClient {
//		public static String urlPrefix = "http://localhost:9999";
//		public static String urlPrefix = "http://192.168.1.8:9999";
//		public static String urlPrefix = "http://145.20.132.154:9999";
//		public static String urlPrefix = "http://10.0.2.2:9999";
//		public static String urlPrefix = "http://10.0.1.2:9999";
//		public static String urlPrefix = "http://ambientdisplay.appspot.com/";
//		public static String urlPrefix = "http://ar-learn.appspot.com/";
		public static String urlPrefix = "http://streetlearn.appspot.com/";

		protected static HttpConnection	conn = ConnectionFactory.getConnection();

		private String path;
		
		public static void setUrlPrefix(String url) {
			urlPrefix = url;
		}
		
		public GenericClient(String path) {
			this.path = path;
		}
		
		public String getUrlPrefix() {
			return urlPrefix + "/rest" + path;
		}
		
		protected String toJson(Object bean) {
			if (bean instanceof String) return (String) bean;
			JsonBeanSerialiser jbs = new JsonBeanSerialiser(bean);
			return jbs.serialiseToJson().toString();
		}
		
		protected Object jsonDeserialise(String beanString, @SuppressWarnings("rawtypes") Class beanClass) {
			JsonBeanDeserializer jbd;
			try {
				jbd = new JsonBeanDeserializer(beanString);
				return (Bean) jbd.deserialize(beanClass);
			} catch (JSONException e) {
				return e.getMessage();
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		
		protected Object executeGet(String url, String token,@SuppressWarnings("rawtypes")  Class beanClass) {
			HttpResponse response = conn.executeGET(url, token, "application/json");
			try {
				if (beanClass.equals(String.class)) return EntityUtils.toString(response.getEntity());
				String result = EntityUtils.toString(response.getEntity(), "utf-8");
				if (result.trim().startsWith("<html><head>")) throw new ARLearnException(result);
				Object o = jsonDeserialise(result, beanClass);
				if (o instanceof Bean) {
					Bean b = (Bean) o;
					if (b.getErrorCode() != null) throw new ARLearnException(b.getErrorCode());
				}
				return o;
			} catch (Exception e) {
				if (e instanceof ARLearnException) throw (ARLearnException) e;
				return returnError(beanClass, e);	
			}
		}
		
		
		
		protected Object executePost(String url, String token, Object bean, @SuppressWarnings("rawtypes") Class beanClass) {
			HttpResponse response = ConnectionFactory.getConnection().executePOST(url, token, "application/json", toJson(bean), "application/json");
			try {
				if (beanClass != null) {
					return jsonDeserialise(EntityUtils.toString(response.getEntity(), "utf-8"), beanClass);
				} 
				return null;
			} catch (Exception e) {
				return returnError(beanClass, e);
			}
		}
		
		
		protected Object executeDelete(String url, String token, @SuppressWarnings("rawtypes") Class beanClass) {
			HttpResponse response =ConnectionFactory.getConnection().executeDELETE(url, token, "application/json");
			try {
				return jsonDeserialise(EntityUtils.toString(response.getEntity(), "utf-8"), beanClass);
			} catch (Exception e) {
				return returnError(beanClass, e);
			}
		}

		private Object returnError(Class beanClass, Exception e) {
			Bean b;
			try {
				b = (Bean) Class.forName(beanClass.getName()).getConstructor().newInstance();
				b.setError("exception "+e.getMessage());
				return b;
			} catch (Exception e1) {
				return null;
			} 	
		}
}
