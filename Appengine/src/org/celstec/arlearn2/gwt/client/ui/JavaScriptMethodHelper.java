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
package org.celstec.arlearn2.gwt.client.ui;

public class JavaScriptMethodHelper {
	private static int requestCounter = 0;

	public static String registerCallbackFunction(
			JavaScriptMethodCallback callback) {
		String callbackName = "callback" + (requestCounter++);
		createCallbackFunction(callback, callbackName);
		return callbackName;
	}

	private native static void createCallbackFunction(
			JavaScriptMethodCallback obj, String callbackName)/*-{
		tmpcallback = function(j) {													  
			obj.@org.celstec.arlearn2.gwt.client.ui.JavaScriptMethodCallback::execute(Lcom/google/gwt/core/client/JavaScriptObject;)( j );
		};
		$wnd[callbackName] = tmpcallback;
	}-*/;

}
