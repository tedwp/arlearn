package org.celstec.arlearn2.client;

import org.celstec.arlearn2.beans.AuthResponse;
import org.celstec.arlearn2.beans.account.Account;

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
public class CollaborationClient extends GenericClient{

    public static CollaborationClient instance;

    private CollaborationClient() {
        super("/collaboration");
    }

    public static CollaborationClient getAccountClient() {
        if (instance == null) {
            instance = new CollaborationClient();
        }
        return instance;
    }

    public Account getContact(String token, String fullId) {
        return (Account)  executeGet(getUrlPrefix()+"/getContact/"+fullId, token, Account.class);
    }
}