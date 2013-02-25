package org.celstec.arlearn2.android.delegators;

import org.celstec.arlearn2.android.broadcast.task.SynchronizeGeneralItemsTask;
import org.celstec.arlearn2.android.delegators.game.CreateGameTask;
import org.celstec.arlearn2.android.delegators.generalitem.CreateGeneralItemTask;
import org.celstec.arlearn2.android.delegators.generalitem.DeleteGeneralItemTask;
import org.celstec.arlearn2.beans.game.Game;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;

import android.content.Context;

public class GeneralItemsDelegator {

	private static GeneralItemsDelegator instance;

	private GeneralItemsDelegator() {

	}

	public static GeneralItemsDelegator getInstance() {
		if (instance == null) {
			instance = new GeneralItemsDelegator();
		}
		return instance;
	}

	public void fetchMyGeneralItemsFromServer(Long lGameId, Context ctx) {
		(new SynchronizeGeneralItemsTask(lGameId, ctx)).addTaskToQueue(ctx);
	}

	public void deleteGeneralItem(Context ctx, long giId, long gameId) {
		DeleteGeneralItemTask dgTask = new DeleteGeneralItemTask(ctx);
		dgTask.setGeneralItemId(giId);
		dgTask.setGameId(gameId);
		dgTask.addTaskToQueue(ctx);
	}

	public void createGeneralItem(Context ctx, GeneralItem gi) {
		CreateGeneralItemTask cgTask = new CreateGeneralItemTask(ctx, gi);
		cgTask.addTaskToQueue(ctx);
	}

}
