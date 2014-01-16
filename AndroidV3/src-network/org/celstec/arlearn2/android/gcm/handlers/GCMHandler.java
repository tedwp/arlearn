package org.celstec.arlearn2.android.gcm.handlers;

import android.content.Context;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.run.Message;
import org.celstec.arlearn2.beans.run.Run;
import org.celstec.arlearn2.beans.run.User;
import org.celstec.arlearn2.beans.run.VariableInstance;

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
public abstract class GCMHandler {
    protected Context ctx;
    protected GCMHandler(Context ctx){
        this.ctx = ctx;
    }

    public static GCMHandler createHandler(Context ctx, HashMap<String, String> map) {
        if (map.containsKey("type")){
//            if (map.get("type").equals(Run.class.getName())) {
//                return new GCMRunHandler(ctx);
//            } else
            if (map.get("type").equals(Game.class.getName())) {
                return new GCMGameHandler(ctx);
            } else if (map.get("type").equals(Run.class.getName())) {
                return new GCMRunHandler(ctx);
            }  else if (map.get("type").equals(GeneralItemModification.class.getName())){
                return new GCMGeneralItemHandler(ctx, Long.parseLong(map.get("gameId")));
            }
//            else if (map.get("type").equals(Message.class.getName())) {
//                return new GCMMessageHandler(ctx, map);
//            } else if (map.get("type").equals(Run.class.getName())) {
//                return new GCMRunHandler(ctx);
//            } else if (map.get("type").equals(User.class.getName())) {
//                return new GCMRunHandler(ctx);
//            } else if (map.get("type").equals(VariableInstance.class.getName())) {
//                VariableDelegator.getInstance().saveInstance(Long.parseLong(map.get("runId")), map.get("name"), Integer.parseInt(map.get("value")));
//                ActivityUpdater.updateActivities(ctx, MapViewActivity.class.getCanonicalName());
//
//
//            }
        }
        return null;
    }

    public abstract void handle() ;
}
