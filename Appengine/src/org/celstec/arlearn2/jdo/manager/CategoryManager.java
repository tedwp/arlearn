package org.celstec.arlearn2.jdo.manager;

import com.google.appengine.api.datastore.KeyFactory;
import com.google.web.bindery.autobean.shared.AutoBeanFactory;
import org.celstec.arlearn2.beans.account.Account;
import org.celstec.arlearn2.beans.store.Category;
import org.celstec.arlearn2.jdo.PMF;
import org.celstec.arlearn2.jdo.classes.AccountJDO;
import org.celstec.arlearn2.jdo.classes.CategoryJDO;
import org.celstec.arlearn2.jdo.classes.RunAccessJDO;

import javax.jdo.PersistenceManager;
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
public class CategoryManager {

    public static CategoryJDO addCategory(String title, String lang) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        CategoryJDO categoryJDO = new CategoryJDO();
        categoryJDO.setTitle(title);
        categoryJDO.setLang(lang);
        try {
            pm.makePersistent(categoryJDO);
            return categoryJDO;
        } finally {
            pm.close();
        }
    }

    public static List<CategoryJDO> getCategoryList(String lang) {
        return null;
    }

    public Category getCategory(long id) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        try {
            return toBean(pm.getObjectById(CategoryJDO.class, KeyFactory
                    .createKey(CategoryJDO.class.getSimpleName(), id)));
        } catch (Exception e) {
            Category categoryError = new Category();
            categoryError.setError("Category does not exist");
            return categoryError;
        } finally {
            pm.close();
        }
    }

    public static Category toBean(CategoryJDO jdo){
        if (jdo == null)
            return null;
        Category category = new Category();
        category.setLang(jdo.getLang());
        category.setTitle(jdo.getTitle());
        category.setId(jdo.getId());
        return category;

    }
}
