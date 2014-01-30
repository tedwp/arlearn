package org.celstec.arlearn2.android.delegators;

import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.events.CategoryEvent;
import org.celstec.arlearn2.android.events.GameCategoryEvent;
import org.celstec.arlearn2.beans.store.Category;
import org.celstec.arlearn2.beans.store.CategoryList;
import org.celstec.arlearn2.beans.store.GameCategory;
import org.celstec.arlearn2.client.StoreClient;
import org.celstec.dao.gen.CategoryLocalObject;
import org.celstec.dao.gen.GameCategoryLocalObject;

import java.util.HashMap;
import java.util.List;

import static daoBase.DaoConfiguration.*;

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
public class StoreDelegator extends AbstractDelegator{

    private static StoreDelegator instance;


    private StoreDelegator() {
        ARL.eventBus.register(this);
    }

    public static StoreDelegator getInstance() {
        if (instance == null) {
            instance = new StoreDelegator();
        }
        return instance;
    }

    public void synCategories() {
        ARL.eventBus.post(new SyncCategories());
    }

    public void syncGamesForCategory(Long categoryId) {
        ARL.eventBus.post(new SyncGames(categoryId));
    }

    private void onEventAsync(SyncCategories syncResponses) {
        String token = returnTokenIfOnline();
        if (token != null) {
            CategoryList list = StoreClient.getStoreClient().getCategoriesByLang(token, ARL.getContext().getResources().getConfiguration().locale.getLanguage());
            for (Category category:list.getCategoryList()) {
                CategoryLocalObject categoryLocalObject = DaoConfiguration.getInstance().getCategoryLocalObjectDao().load(category.getCategoryId());
                CategoryEvent event = null;
                if (categoryLocalObject == null) {
                    categoryLocalObject = new CategoryLocalObject();
                    event = new CategoryEvent(category.getCategoryId());
                }
                categoryLocalObject.setId(category.getCategoryId());
                categoryLocalObject.setLang(category.getLang());
                categoryLocalObject.setCategory(category.getTitle());
                categoryLocalObject.setDeleted(category.getDeleted());
                DaoConfiguration.getInstance().getCategoryLocalObjectDao().insertOrReplace(categoryLocalObject);
                if (event != null) {
                    ARL.eventBus.post(event);
                }
            }
        }
    }

    private void onEventAsync(SyncGames syncGames) {
        String token = returnTokenIfOnline();
        if (token != null) {
            for (GameCategory gameCategory: StoreClient.getStoreClient().getGameIdsForCategory(token, syncGames.getCategoryId()).getGameCategoryList()){
                GameCategoryLocalObject gameCategoryLocalObject = DaoConfiguration.getInstance().getGameCategoryDao().load(gameCategory.getId());
                GameCategoryEvent event = null;

                if (gameCategoryLocalObject == null) {
                    gameCategoryLocalObject = new GameCategoryLocalObject();
                    event = new GameCategoryEvent();
                    event.setCategoryId(gameCategory.getCategoryId());
                    event.setGameId(gameCategory.getGameId());
                }
                gameCategoryLocalObject.setId(gameCategory.getId());
                gameCategoryLocalObject.setDeleted(gameCategory.getDeleted());
                gameCategoryLocalObject.setGameId(gameCategory.getGameId());
                gameCategoryLocalObject.setCategoryId(gameCategory.getCategoryId());
                DaoConfiguration.getInstance().getGameCategoryDao().insertOrReplace(gameCategoryLocalObject);
                if (ARL.dao.getGameCategoryDao().load(gameCategory.getGameId())==null) {
                    ARL.games.syncGame(gameCategory.getGameId());
                }
                if (event != null) {
                    ARL.eventBus.post(event);
                }
            }
        }
    }



    private class SyncCategories{}

    private class SyncGames{
        private Long categoryId;

        private SyncGames(Long categoryId) {
            this.categoryId = categoryId;
        }

        public Long getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Long categoryId) {
            this.categoryId = categoryId;
        }
    }

    public List<CategoryLocalObject> getCategories() {
        return DaoConfiguration.getInstance().getCategoryLocalObjectDao().loadAll();
    }}
