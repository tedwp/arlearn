package org.celstec.arlearn2.android.delegators;

import daoBase.DaoConfiguration;
import de.greenrobot.dao.query.QueryBuilder;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.client.AccountClient;
import org.celstec.arlearn2.client.CollaborationClient;
import org.celstec.dao.gen.AccountLocalObject;
import org.celstec.dao.gen.AccountLocalObjectDao;
import org.celstec.dao.gen.GameLocalObjectDao;

import java.util.List;

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
public class AccountDelegator {

    private static AccountDelegator instance;

    private AccountDelegator() {
//        ARL.eventBus.register(this);
    }

    public static AccountDelegator getInstance() {
        if (instance == null) {
            instance = new AccountDelegator();
        }
        return instance;
    }

    public AccountLocalObject getAccount(String accountFullId) {
        AccountLocalObjectDao dao = DaoConfiguration.getInstance().getAccountLocalObjectDao();
        List<AccountLocalObject> resultList = dao.queryBuilder().where(AccountLocalObjectDao.Properties.FullId.eq(accountFullId)).list();
        if (resultList.isEmpty()) return null;
        return resultList.get(0);
    }

    public AccountLocalObject syncAccount(String fullId) {
        Account account = CollaborationClient.getAccountClient().getContact(ARL.properties.getAuthToken(), fullId);
        AccountLocalObject localObject = getAccount(fullId);
        if (localObject != null) {
            //todo sync
            return localObject;
        }
        //Account did not yet locally exist, so create new account
        AccountLocalObject newAccount = toDaoLocalObject(account);
        DaoConfiguration.getInstance().getAccountLocalObjectDao().insertOrReplace(newAccount);
        return newAccount;
    }

    private AccountLocalObject toDaoLocalObject(Account aBean) {
        AccountLocalObject accountDao = new AccountLocalObject();
        accountDao.setName(aBean.getName());
        accountDao.setFamilyName(aBean.getFamilyName());
        accountDao.setGivenName(aBean.getGivenName());
        accountDao.setFullId(aBean.getFullId());
        accountDao.setAccountLevel(aBean.getAccountLevel());
        accountDao.setAccountType(aBean.getAccountType());
        accountDao.setLocalId(aBean.getLocalId());
        return accountDao;
    }

}
