package org.celstec.arlearn2.jdo.manager;

public class ManagerUtil {
	protected static String generateFilter(Object[] parameters, String[] param, String[] paramName) {
		String returnFilter = "";
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] != null) {
				if (!returnFilter.equals(""))
					returnFilter += " && ";
				returnFilter += param[i] + " == " + paramName[i];
			}
		}
		return returnFilter;
	}

	protected static String generateDeclareParameters(Object[] parameters,String[] types, String[] param, String[] paramName) {
		String returnFilter = "";
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] != null) {
				if (!returnFilter.equals(""))
					returnFilter += " , ";
				returnFilter += types[i]+" " + paramName[i];
			}
		}
		return returnFilter;
	}

	protected static Object[] filterOutNulls(Object[] parameters) {
		int counter = 0;
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] == null)
				counter++;
		}
		Object[] returnString = new Object[parameters.length - counter];
		counter = 0;
		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i] != null)
				returnString[counter++] = parameters[i];
		}
		return returnString;
	}
}
