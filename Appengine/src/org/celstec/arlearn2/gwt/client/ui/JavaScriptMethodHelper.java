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
