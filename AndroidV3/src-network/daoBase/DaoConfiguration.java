package daoBase;

import android.content.Context;
import org.celstec.dao.gen.*;

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
public class DaoConfiguration {

    private static DaoConfiguration instance;

    private static DaoSession session;

    private DaoConfiguration(Context ctx) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, "games-db", null);
        DaoMaster daoMaster = new DaoMaster(helper.getWritableDatabase());
        session = daoMaster.newSession();
        session.getGameLocalObjectDao();
    }

    public static DaoConfiguration getInstance(Context ctx) {
        if (instance == null) {
            instance = new DaoConfiguration(ctx);
        }
        return instance;
    }

    public DaoSession getSession() {
        return session;
    }

    public static DaoConfiguration getInstance() {
        return instance;
    }

    public DaoSession getDaoSession(){
        return session;
    }

    public GameLocalObjectDao getGameLocalObjectDao() {
        return session.getGameLocalObjectDao();
    }

    public InquiryLocalObjectDao getInquiryLocalObjectDao(){
        return session.getInquiryLocalObjectDao();
    }
    public RunLocalObjectDao getRunLocalObjectDao(){
        return session.getRunLocalObjectDao();
    }

    public AccountLocalObjectDao getAccountLocalObjectDao(){
        return session.getAccountLocalObjectDao();
    }

    public ActionLocalObjectDao getActionDependencyLocalObjectDao() {
        return session.getActionLocalObjectDao();
    }

    public ResponseLocalObjectDao getResponseLocalObjectDao() {
        return session.getResponseLocalObjectDao();
    }

    public BadgeLocalObjectDao getBadgesLocalObjectDao() {
        return session.getBadgeLocalObjectDao();
    }

    public GameContributorLocalObjectDao getGameContributorLocalObjectDao() {
        return session.getGameContributorLocalObjectDao();
    }

    public GeneralItemLocalObjectDao getGeneralItemLocalObjectDao() {
        return session.getGeneralItemLocalObjectDao();
    }
    public GeneralItemMediaLocalObjectDao getGeneralItemMediaLocalObject() {
        return session.getGeneralItemMediaLocalObjectDao();
    }

    public ThreadLocalObjectDao getThreadLocalObject() {
        return session.getThreadLocalObjectDao();
    }

    public MessageLocalObjectDao getMessageLocalObject() {
        return session.getMessageLocalObjectDao();
    }
//    public ActionDependencyLocalObjectDao getActionDependencyLocalObjectDao() {
//        return session.getActionDependencyLocalObjectDao();
//    }

    public DependencyLocalObjectDao getDependencyLocalObjectDao() {
        return session.getDependencyLocalObjectDao();
    }

    public CategoryLocalObjectDao getCategoryLocalObjectDao() {
        return session.getCategoryLocalObjectDao();
    }

    public GameCategoryLocalObjectDao getGameCategoryDao() {
        return session.getGameCategoryLocalObjectDao();
    }


}
