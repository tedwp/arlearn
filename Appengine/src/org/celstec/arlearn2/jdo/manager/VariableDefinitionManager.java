package org.celstec.arlearn2.jdo.manager;

import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.VariableDefinitionJDO;

import javax.jdo.PersistenceManager;

public class VariableDefinitionManager {

	
	public static VariableDefinition createVariableDefinition(VariableDefinition variableDefinition) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        if (variableDefinition.getScope() == null) variableDefinition.setScope(0);
        VariableDefinitionJDO variableDefinitionJDO = new VariableDefinitionJDO();
        variableDefinitionJDO.setName(variableDefinition.getName());
        variableDefinitionJDO.setGameId(variableDefinition.getGameId());
        variableDefinitionJDO.setMinValue(variableDefinition.getMinValue());
        variableDefinitionJDO.setMaxValue(variableDefinition.getMaxValue());
        variableDefinitionJDO.setScope(variableDefinition.getScope());

        variableDefinitionJDO.setUniqueId();
        try {
            pm.makePersistent(variableDefinitionJDO);
            return toBean(variableDefinitionJDO);
        } finally {
            pm.close();
        }
	}

    private static VariableDefinition toBean(VariableDefinitionJDO jdo) {
        if (jdo == null)
            return null;
        VariableDefinition bean = new VariableDefinition();
        bean.setName(jdo.getName());
        if (jdo.getMinValue() != null) bean.setMinValue(jdo.getMinValue());
        if (jdo.getMaxValue() != null)bean.setMaxValue(jdo.getMaxValue());
        bean.setScope(jdo.getScope());
        bean.setGameId(jdo.getGameId());
        return bean;
    }
}
