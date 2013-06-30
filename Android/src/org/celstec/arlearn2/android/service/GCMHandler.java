package org.celstec.arlearn2.android.service;

import java.util.HashMap;

import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.notification.GeneralItemModification;
import org.celstec.arlearn2.beans.run.Message;
import org.celstec.arlearn2.beans.run.Run;

import android.content.Context;
import org.celstec.arlearn2.beans.run.User;

public abstract class GCMHandler {
	protected Context ctx;
	protected GCMHandler(Context ctx){
		this.ctx = ctx;
	}
	
	public static GCMHandler createHandler(Context ctx, HashMap<String, String> map) {
		if (map.containsKey("type")){
			if (map.get("type").equals(Run.class.getName())) {
				return new GCMRunHandler(ctx);
			} else if (map.get("type").equals(Game.class.getName())) {
				return new GCMGameHandler(ctx);
			} else if (map.get("type").equals(GeneralItemModification.class.getName())){
				return new GCMGeneralItemHandler(ctx, Long.parseLong(map.get("runId")),Long.parseLong(map.get("gameId")));
			}else if (map.get("type").equals(Message.class.getName())) {
				return new GCMMessageHandler(ctx, map);
			} else if (map.get("type").equals(Run.class.getName())) {
                return new GCMRunHandler(ctx);
            } else if (map.get("type").equals(User.class.getName())) {
                return new GCMRunHandler(ctx);
            }
        }
		return null;
	}

	public abstract void handle() ;
}
