package org.celstec.arlearn2.android.delegators;

import android.util.Log;
import daoBase.DaoConfiguration;
import org.celstec.arlearn2.android.events.GeneralItemEvent;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.GeneralItemList;
import org.celstec.arlearn2.client.GeneralItemClient;
import org.celstec.dao.gen.*;

import java.util.HashMap;

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
public class GeneralItemDelegator extends AbstractDelegator{

    private static GeneralItemDelegator instance;
    private static HashMap<Long, Long> syncDates = new HashMap<Long, Long>();

    private GeneralItemDelegator() {
        ARL.eventBus.register(this);
    }

    public static GeneralItemDelegator getInstance() {
        if (instance == null) {
            instance = new GeneralItemDelegator();
        }
        return instance;
    }

    public void syncGeneralItems(Long gameId) {
        syncGeneralItems(DaoConfiguration.getInstance().getGameLocalObjectDao().load(gameId));
    }

    public void syncGeneralItems(GameLocalObject game) {
        if (game == null) {
            Log.e("ARLearn", "trying to sync game that does not exist ");
        } else {
            ARL.eventBus.post(new SyncGeneralItems(game));
        }
    }

    private class SyncGeneralItems {
        private GameLocalObject game;

        private SyncGeneralItems(GameLocalObject game) {
            this.game = game;
        }

        public GameLocalObject getGame() {
            return game;
        }

        public void setGame(GameLocalObject game) {
            this.game = game;
        }
    }

    private void onEventAsync(SyncGeneralItems sgi) {
        String token = returnTokenIfOnline();
        if (token != null) {
            long gameId = sgi.getGame().getId();
            GeneralItemList list = GeneralItemClient.getGeneralItemClient().getGameGeneralItems(token, gameId, getLastSyncDate(gameId));
            if (list.getError()== null) {
                process(list, sgi.getGame());
            } else {
                System.out.println("error "+list.getError());
                Log.e("ARLearn", "error returning list of gis"+list.getError());
            }
        } else {
            System.out.println("token was null ");
        }

    }

    public void process (GeneralItemList list, GameLocalObject gameLocalObject) {
        GeneralItemEvent iEvent = null;
        for (GeneralItem giBean: list.getGeneralItems()) {
            DaoConfiguration.getInstance().getGeneralItemLocalObjectDao().load(123l);
            GeneralItemLocalObject giDao = DaoConfiguration.getInstance().getGeneralItemLocalObjectDao().load(giBean.getId());
            if (giDao == null) {
                giDao = toDaoLocalObject(giBean);
            }
            giDao.setGameLocalObject(gameLocalObject);


            if (giBean.getDependsOn()!=null) {
                if (giDao.getDependsOn() != null && !giDao.getDependencyLocalObject().recursiveEquals(giBean.getDependsOn())) {
                    giDao.getDependencyLocalObject().recursiveDelete();
                    giDao.setDependencyLocalObject(null);
                }

                if (giDao.getDependsOn() == null) {
                    DependencyLocalObject dependsOn = DependencyLocalObject.createDependencyLocalObject(giBean.getDependsOn());
                    giDao.setDependencyLocalObject(dependsOn);
                }
            } else {
                if (giDao.getDependsOn()!= null) {
                    giDao.getDependencyLocalObject().recursiveDelete();
                    giDao.setDependencyLocalObject(null);
                }
            }

            DaoConfiguration.getInstance().getGeneralItemLocalObjectDao().insertOrReplace(giDao);

            GiFileReferenceDelegator.getInstance().createReference(giBean, giDao);
            iEvent = new GeneralItemEvent(giDao.getId());
        }
        if (iEvent != null) {
            ARL.eventBus.post(iEvent);
            syncDates.put(gameLocalObject.getId(), list.getServerTime());
        }
    }

    private GeneralItemLocalObject toDaoLocalObject (GeneralItem giBean) {
        GeneralItemLocalObject giDao = new GeneralItemLocalObject();
        giDao.setTitle(giBean.getName());
        giDao.setId(giBean.getId());
        giDao.setAutoLaunch(giBean.getAutoLaunch());
        giDao.setLastModificationDate(giBean.getLastModificationDate());
        giDao.setDescription(giBean.getDescription());
        return giDao;
    }

    private long getLastSyncDate(long gameId) {
        if (syncDates.containsKey(gameId)) {
            return syncDates.get(gameId);
        }
        return 0l;
    }


    private void GeneralItemLocalObject(GeneralItem giBean) {}

}
