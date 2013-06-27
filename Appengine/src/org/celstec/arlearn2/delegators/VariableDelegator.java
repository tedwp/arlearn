package org.celstec.arlearn2.delegators;

import java.util.logging.Logger;

import org.celstec.arlearn2.api.Service;
import org.celstec.arlearn2.beans.game.VariableDefinition;

public class VariableDelegator extends GoogleDelegator {
	private static final Logger logger = Logger.getLogger(VariableDelegator.class.getName());

	
	public VariableDelegator(Service service) {
		super(service);
	}

	public VariableDefinition createVariableDefinition(VariableDefinition message) {
		return null;
	}

}
