package org.celstec.dao;

import de.greenrobot.daogenerator.*;

import java.io.IOException;

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
public class ARlearnDaoGenerator {

    private static Entity account;
    private static Entity game;
    private static Entity gameContributors;
    private static Entity generalItem;
    private static Entity generalItemMediaObjects;
    private static Entity dependency;
    private static Entity actionDependency;
    private static Entity andDependency;
    private static Entity orDependency;
    private static Entity timeDependency;
    private static Entity proximityDependency;

    private static Entity run;
    private static Entity action;
    private static Entity response;
    private static Entity thread;
    private static Entity message;
    private static Entity inquiry;
    private static Entity badge;

    private static Entity category;
    private static Entity gameCategory;

    private static Entity generalItemVisibility;

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema(1, "org.celstec.dao.gen");
        schema.enableKeepSectionsByDefault();
        account = createAccount(schema);
        game = createGame(schema);
        gameContributors = createGameContributor(schema);
        dependency = createDependency(schema);
        generalItem = createGeneralItem(schema);

        generalItemMediaObjects = createGeneralItemMediaObjects(schema);
        run = createRunItem(schema);

        action = createAction(schema);
        response = createResponse(schema);
        inquiry = createInquiryItem(schema);
        thread = createThread(schema);
        message = createMessage(schema);
        generalItemVisibility = createGeneralItemVisibility(schema);
        badge = createBadges(schema);

        category = createCategory(schema);
        gameCategory = createGameCategory(schema);
        new DaoGenerator().generateAll(schema, "src-dao-gen");

    }

    private static Entity createAccount(Schema schema) {
        Entity account = schema.addEntity("AccountLocalObject");
        account.addIdProperty();
        account.addStringProperty("email");
        account.addStringProperty("familyName");
        account.addStringProperty("givenName");
        account.addStringProperty("name");
        account.addIntProperty("accountLevel");
        account.addIntProperty("accountType");
        account.addStringProperty("localId");
        account.addStringProperty("fullId");
        account.addByteArrayProperty("picture");
        return account;
    }

    private static Entity createThread(Schema schema) {
        Entity threadLocalObject = schema.addEntity("ThreadLocalObject");
        threadLocalObject.addIdProperty();
        threadLocalObject.addStringProperty("name");
        threadLocalObject.addLongProperty("lastModificationDate");

        Property runId = threadLocalObject.addLongProperty("runId").notNull().getProperty();
        run.addToMany(threadLocalObject, runId, "lastModificationDate");

        return threadLocalObject;
    }

    private static Entity createMessage(Schema schema) {
        Entity messageLocalObject = schema.addEntity("MessageLocalObject");
        messageLocalObject.addIdProperty();
        messageLocalObject.addStringProperty("subject");
        messageLocalObject.addStringProperty("body");
        messageLocalObject.addLongProperty("time");
        messageLocalObject.addStringProperty("userIds");

        Property threadId = messageLocalObject.addLongProperty("threadId").notNull().getProperty();
        thread.addToMany(messageLocalObject, threadId, "messages");

        Property runId = messageLocalObject.addLongProperty("runId").notNull().getProperty();
        run.addToMany(messageLocalObject, runId, "messages");

        return messageLocalObject;
    }

    private static Entity createBadges(Schema schema) {
        Entity badge = schema.addEntity("BadgeLocalObject");
        badge.addIdProperty();
        badge.addStringProperty("title");
        badge.addStringProperty("description");
        badge.addByteArrayProperty("badgeIcon");

        Property inquiryId = badge.addLongProperty("inquiryId").getProperty();
        badge.addToOne(inquiry, inquiryId);

        Property accountId = badge.addLongProperty("accountId").notNull().getProperty();
        badge.addToOne(account, accountId);
        ToMany inqToBadges = inquiry.addToMany(badge, inquiryId);
        inqToBadges.setName("badges");

        return badge;
    }


    private static Entity createInquiryItem(Schema schema) {
        Entity inquiry = schema.addEntity("InquiryLocalObject");

        inquiry.addIdProperty();
        inquiry.addStringProperty("title");
        inquiry.addStringProperty("description");
        inquiry.addStringProperty("hypothesisTitle");
        inquiry.addStringProperty("hypothesisDescription");
        inquiry.addStringProperty("reflection");
        inquiry.addBooleanProperty("isSynchronized");
        inquiry.addByteArrayProperty("icon");


        Property runId = inquiry.addLongProperty("runId").notNull().getProperty();
        inquiry.addToOne(run, runId);




        return inquiry;
    }


    private static Entity createRunItem(Schema schema) {
        Entity run = schema.addEntity("RunLocalObject");
        run.addIdProperty();
        run.addStringProperty("title").notNull();
        run.addBooleanProperty("deleted");

        Property gameId = run.addLongProperty("gameId").notNull().getProperty();
        run.addToOne(game, gameId);
        ToMany gameToRuns = game.addToMany(run, gameId);
        gameToRuns.setName("runs");
        return run;
    }

    private static Entity createAction(Schema schema) {
        Entity action = schema.addEntity("ActionLocalObject");
        action.addIdProperty();
        action.addStringProperty("action").notNull();
        action.addLongProperty("time");

        Property runId = action.addLongProperty("runId").notNull().getProperty();
//        action.addToOne(run, runId);
        run.addToMany(action, runId, "actions");

        Property itemId = action.addLongProperty("generalItem").notNull().getProperty();
//        action.addToOne(generalItem, itemId, );
        generalItem.addToMany(action, itemId, "actions");

        Property accountId = action.addLongProperty("account").notNull().getProperty();
        action.addToOne(account, accountId);

        return action;
    }

    private static Entity createResponse(Schema schema) {
        Entity response = schema.addEntity("ResponseLocalObject");

        response.addIdProperty();
        response.addIntProperty("type");
        response.addStringProperty("contentType");
//        response.addStringProperty("fileName");
        response.addStringProperty("UriAsString");
        response.addStringProperty("value");
        response.addBooleanProperty("isSynchronized");
        response.addBooleanProperty("revoked");
        response.addLongProperty("nextSynchronisationTime");
        response.addIntProperty("amountOfSynchronisationAttempts");
        response.addLongProperty("timeStamp");
        response.addIntProperty("width");
        response.addIntProperty("height");
        response.addDoubleProperty("lat");
        response.addDoubleProperty("lng");

        Property runId = response.addLongProperty("runId").notNull().getProperty();
        run.addToMany(response, runId, "responses");

        Property itemId = response.addLongProperty("generalItem").notNull().getProperty();
        response.addToOne(generalItem, itemId);
        generalItem.addToMany(response, itemId, "responses");

        Property accountId = response.addLongProperty("account").getProperty();
        response.addToOne(account, accountId);

        return response;
    }

    private static Entity createGame(Schema schema) {
        Entity game = schema.addEntity("GameLocalObject");
        game.addIdProperty();
        game.addStringProperty("title").notNull();
        game.addStringProperty("description");
        game.addBooleanProperty("mapAvailable");
        game.addBooleanProperty("deleted");
        game.addLongProperty("lastModificationDate");

        return game;
    }

    private static Entity createGameContributor(Schema schema) {
        Entity gameContributor = schema.addEntity("GameContributorLocalObject");
        gameContributor.addIdProperty();
        gameContributor.addIntProperty("type");

        Property gameId = gameContributor.addLongProperty("gameId").notNull().getProperty();
        gameContributor.addToOne(game, gameId);

        ToMany gameToOwners = game.addToMany(gameContributor, gameId);
        gameToOwners.setName("contributors");

        Property accountId = gameContributor.addLongProperty("accountId").notNull().getProperty();
        gameContributor.addToOne(account, accountId);
        ToMany contributorToAccount = account.addToMany(gameContributor, accountId);
        contributorToAccount.setName("account");

        return gameContributor;
    }

    private static Entity createGeneralItem(Schema schema) {
        Entity generalItem = schema.addEntity("GeneralItemLocalObject");
        generalItem.addIdProperty();
        generalItem.addStringProperty("title");
        generalItem.addStringProperty("description");
        generalItem.addStringProperty("richText");
        generalItem.addBooleanProperty("autoLaunch");
        generalItem.addLongProperty("lastModificationDate");

        Property gameId = generalItem.addLongProperty("gameId").notNull().getProperty();
        generalItem.addToOne(game, gameId);

        ToMany gameToGeneralItems = game.addToMany(generalItem, gameId);
        gameToGeneralItems.setName("generalItems");

        Property dependsOnDependencyId = generalItem.addLongProperty("dependsOn").getProperty();
        generalItem.addToOne(dependency, dependsOnDependencyId);

        Property generalItemId = dependency.addLongProperty("generalItemId").getProperty();
        dependency.addToOne(generalItem, generalItemId);

        return generalItem;
    }

    private static Property depPkProperty = null;

    private static Entity createDependency(Schema schema) {
        Entity dependency = schema.addEntity("DependencyLocalObject");
        dependency.addIdProperty();
        dependency.addIntProperty("type");

        dependency.addStringProperty("action");
        dependency.addIntProperty("scope");

        dependency.addLongProperty("timeDelta");

        dependency.addLongProperty("radius");
        dependency.addDoubleProperty("lat");
        dependency.addDoubleProperty("lng");

        depPkProperty = dependency.addLongProperty("parentDependency").getProperty();
        ToMany parentDepToChildDep = dependency.addToMany(dependency, depPkProperty);
        parentDepToChildDep.setName("childDeps");



        return dependency;
    }

    private static Entity createActionDependency(Schema schema) {
        Entity dependency = schema.addEntity("ActionDependencyLocalObject");
        dependency.setSuperclass("DependencyLocalObject");
        dependency.addStringProperty("action");
        dependency.addIntProperty("scope");
        Property generalItemId = dependency.addLongProperty("generalItemId").getProperty();
        dependency.addToOne(generalItem, generalItemId);
        return dependency;
    }

    private static Entity createAndDependency(Schema schema) {
        Entity andDep = schema.addEntity("AndDependencyLocalObject");
        andDep.setSuperclass("DependencyLocalObject");
        andDep.addLongProperty("dummyProperty");
        return dependency;
    }

    private static Entity createOrDependency(Schema schema) {
        Entity andDep = schema.addEntity("OrDependencyLocalObject");
        andDep.setSuperclass("DependencyLocalObject");
        andDep.addLongProperty("dummyProperty");
        return dependency;
    }

    private static Entity createTimeDependency(Schema schema) {
        Entity andDep = schema.addEntity("TimeDependencyLocalObject");
        andDep.setSuperclass("DependencyLocalObject");
        andDep.addLongProperty("timeDelta");
        return dependency;
    }

    private static Entity createProximityDependency(Schema schema) {
        Entity andDep = schema.addEntity("ProximityDependencyLocalObject");
        andDep.setSuperclass("DependencyLocalObject");
        andDep.addLongProperty("radius");
        andDep.addDoubleProperty("lat");
        andDep.addDoubleProperty("lng");
        return dependency;
    }

    private static Entity createGeneralItemMediaObjects(Schema schema) {
        Entity generalItemMedia = schema.addEntity("GeneralItemMediaLocalObject");
        generalItemMedia.addIdProperty();
        generalItemMedia.addStringProperty("localId");
        generalItemMedia.addStringProperty("remoteFile");
        generalItemMedia.addStringProperty("localUri");
        generalItemMedia.addStringProperty("mimetype");
        generalItemMedia.addStringProperty("preferredFileName");
        generalItemMedia.addStringProperty("md5Hash");
        generalItemMedia.addBooleanProperty("replicated");

        Property generalItemId = generalItemMedia.addLongProperty("generalItem").notNull().getProperty();
        generalItemMedia.addToOne(generalItem, generalItemId);

        ToMany mediaToGeneralItems = generalItem.addToMany(generalItemMedia, generalItemId);
        mediaToGeneralItems.setName("generalItemMedia");

        return generalItemMedia;
    }

    private static Entity createGeneralItemVisibility(Schema schema) {
        Entity generalItemVisibility = schema.addEntity("GeneralItemVisibilityLocalObject");
        generalItemVisibility.addStringProperty("account");
        generalItemVisibility.addLongProperty("status");
        generalItemVisibility.addLongProperty("timeStamp");

        Property generalItemId = generalItemVisibility.addLongProperty("generalItemId").notNull().getProperty();
        generalItemVisibility.addToOne(generalItem, generalItemId);

        ToMany generalItemToGeneralItemVisibility = generalItem.addToMany(generalItemVisibility, generalItemId);
        generalItemToGeneralItemVisibility.setName("visibilities");

        Property runId = generalItemVisibility.addLongProperty("runId").notNull().getProperty();
        generalItemVisibility.addToOne(run, runId);

        ToMany runToGeneralItemVisibility = run.addToMany(generalItemVisibility, runId);
        runToGeneralItemVisibility.setName("visibilities");

        return generalItemVisibility;
    }

    private static Entity createCategory(Schema schema) {
        Entity category = schema.addEntity("CategoryLocalObject");
        category.addIdProperty();
        category.addStringProperty("lang");
        category.addStringProperty("category");
        category.addBooleanProperty("deleted");
        return category;
    }

    private static Entity createGameCategory(Schema schema) {
        Entity gameCategory = schema.addEntity("GameCategoryLocalObject");
        gameCategory.addIdProperty();
        gameCategory.addBooleanProperty("deleted");

        Property categoryId = gameCategory.addLongProperty("categoryId").getProperty();
        ToMany toMany = category.addToMany(gameCategory, categoryId);
        toMany.setName("games");

        Property gameId = gameCategory.addLongProperty("gameId").getProperty();
        gameCategory.addToOne(game, gameId);
        return gameCategory;

    }

}
