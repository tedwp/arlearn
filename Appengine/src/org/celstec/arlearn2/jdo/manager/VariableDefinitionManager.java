package org.celstec.arlearn2.jdo.manager;

import org.celstec.arlearn2.beans.game.VariableDefinition;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.VariableDefinitionJDO;

import javax.jdo.PersistenceManager;

/**
 * ****************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * <p/>
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * <p/>
 * Contributors: Stefaan Ternier
 * ****************************************************************************
 */

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
