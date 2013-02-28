//<<<<<<< HEAD
//package org.celstec.arlearn2.android.delegators;
//
//import org.celstec.arlearn2.android.broadcast.task.SynchronizeGeneralItemsTask;
//import org.celstec.arlearn2.android.delegators.game.CreateGameTask;
//import org.celstec.arlearn2.android.delegators.generalitem.CreateGeneralItemTask;
//import org.celstec.arlearn2.android.delegators.generalitem.DeleteGeneralItemTask;
//import org.celstec.arlearn2.beans.game.Game;
//import org.celstec.arlearn2.beans.generalItem.GeneralItem;
//
//import android.content.Context;
//
//public class GeneralItemsDelegator {
//
//	private static GeneralItemsDelegator instance;
//
//	private GeneralItemsDelegator() {
//
//	}
//
//	public static GeneralItemsDelegator getInstance() {
//		if (instance == null) {
//			instance = new GeneralItemsDelegator();
//		}
//		return instance;
//	}
//
//	public void fetchMyGeneralItemsFromServer(Long lGameId, Context ctx) {
//		(new SynchronizeGeneralItemsTask(lGameId, ctx)).addTaskToQueue(ctx);
//	}
//
//
//	public void createGeneralItem(Context ctx, GeneralItem gi) {
//		CreateGeneralItemTask cgTask = new CreateGeneralItemTask(ctx, gi);
//		cgTask.addTaskToQueue(ctx);
//=======
/*******************************************************************************
 * Copyright (C) 2013 Open Universiteit Nederland
 * 
 * This library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Contributors: Stefaan Ternier
 ******************************************************************************/
package org.celstec.arlearn2.android.delegators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.celstec.arlearn2.android.asynctasks.db.CreateDownloadGeneralItems;
import org.celstec.arlearn2.android.asynctasks.db.GeneralItemDependencyHandler;
import org.celstec.arlearn2.android.asynctasks.db.InitGeneralItemVisibilityTask;
import org.celstec.arlearn2.android.asynctasks.network.DownloadFileTask;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeGeneralItemsTask;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeRunsTask;
import org.celstec.arlearn2.android.asynctasks.network.SynchronizeGeneralItemVisiblityTask;
import org.celstec.arlearn2.android.cache.GeneralItemsCache;
import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;
import org.celstec.arlearn2.android.cache.ResponseCache;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems.DownloadItem;
import org.celstec.arlearn2.android.delegators.generalitem.CreateGeneralItemTask;
import org.celstec.arlearn2.android.delegators.generalitem.DeleteGeneralItemTask;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageTest;
import org.celstec.arlearn2.beans.generalItem.SingleChoiceImageTest;
import org.celstec.arlearn2.beans.generalItem.VideoObject;
import org.celstec.arlearn2.beans.run.Response;

import android.content.Context;
import android.net.Uri;

public class GeneralItemsDelegator {
	private static GeneralItemsDelegator instance;
	public static String AUDIO_LOCAL_ID = "audio";
	public static String VIDEO_LOCAL_ID = "video";

	private GeneralItemsDelegator() {

	}

	public static GeneralItemsDelegator getInstance() {
		if (instance == null) {
			instance = new GeneralItemsDelegator();
		}
		return instance;
	}

	public void synchronizeGeneralItemsWithServer(Context ctx, Long runId, Long gameId) {
		SynchronizeGeneralItemsTask syncItemTask_1 = new SynchronizeGeneralItemsTask();
		syncItemTask_1.setGameId(gameId);
		SynchronizeGeneralItemVisiblityTask syncVisItemsTask_2 = new SynchronizeGeneralItemVisiblityTask();
		syncVisItemsTask_2.setRunId(runId);
		
		
		InitGeneralItemVisibilityTask visibilityTask_3 = new InitGeneralItemVisibilityTask(runId, gameId);
		GeneralItemDependencyHandler dependencyTask_4 = new GeneralItemDependencyHandler();
		
		visibilityTask_3.taskToRunAfterExecute(dependencyTask_4);
		syncVisItemsTask_2.taskToRunAfterExecute(visibilityTask_3);
		syncItemTask_1.taskToRunAfterExecute(syncVisItemsTask_2);
		syncItemTask_1.run(ctx);
	}
	
	public TreeSet<Response> getResponses(Long runId, Long itemId) {
		return ResponseCache.getInstance().getResponses(runId, itemId);
	}

	public HashMap<String, Uri> getLocalMediaUriMap(GeneralItem gi) {
		return MediaGeneralItemCache.getInstance(gi.getGameId()).getLocalMediaUriMap(gi);
	}

	public double getDownloadPercentage(GeneralItem generalItem) {
		return MediaGeneralItemCache.getInstance(generalItem.getGameId()).getPercentageDownloaded(generalItem);
	}

	public DownloadItem[] getDownloadItems(GeneralItem gi) {
		if (gi instanceof AudioObject) {
			return getDownloadItemsAudioObject((AudioObject) gi);
		}
		if (gi instanceof VideoObject) {
			return getDownloadItemsVideoObject((VideoObject) gi);
		}
		if (gi instanceof SingleChoiceImageTest) {
			return getDownloadItemsSingleChoiceImageTest((SingleChoiceImageTest) gi);
		}
		if (gi instanceof MultipleChoiceImageTest) {
			return getDownloadItemsMultipleChoiceImageTest((MultipleChoiceImageTest) gi);
		}
		return null;
	}

	private DownloadItem[] getDownloadItemsAudioObject(AudioObject oa) {
		DownloadItem[] returnObject = new DownloadItem[1];
		returnObject[0] = getBaseItem(oa);
		returnObject[0].setLocalId(AUDIO_LOCAL_ID);
		returnObject[0].setRemoteUrl(oa.getAudioFeed());
		return returnObject;
	}

	private DownloadItem[] getDownloadItemsVideoObject(VideoObject gi) {
		DownloadItem[] returnObject = new DownloadItem[1];
		returnObject[0] = getBaseItem(gi);
		returnObject[0].setLocalId(VIDEO_LOCAL_ID);
		returnObject[0].setRemoteUrl(gi.getVideoFeed());
		return returnObject;
	}

	private DownloadItem[] getDownloadItemsSingleChoiceImageTest(SingleChoiceImageTest gi) {
		ArrayList<DownloadItem> list = new ArrayList<MediaCacheGeneralItems.DownloadItem>();
		if (gi.getAudioQuestion() != null) {
			DownloadItem audioQuestion = getBaseItem(gi);
			audioQuestion.setLocalId(AUDIO_LOCAL_ID);
			audioQuestion.setRemoteUrl(gi.getAudioQuestion());
			list.add(audioQuestion);
		}
		gi.getAnswers();
		for (MultipleChoiceAnswerItem imageAnswer : gi.getAnswers()) {
			MultipleChoiceImageAnswerItem mciai = (MultipleChoiceImageAnswerItem) imageAnswer;
			if (mciai.getImageUrl() != null) {
				DownloadItem imageDi = getBaseItem(gi);
				imageDi.setLocalId(mciai.getId() + ":i");
				imageDi.setRemoteUrl(mciai.getImageUrl());
				list.add(imageDi);
			}
			if (mciai.getAudioUrl() != null) {
				DownloadItem audioDi = getBaseItem(gi);
				audioDi.setLocalId(mciai.getId() + ":a");
				audioDi.setRemoteUrl(mciai.getAudioUrl());
				list.add(audioDi);
			}
		}
		return list.toArray(new DownloadItem[] {});
	}
	
	private DownloadItem[] getDownloadItemsMultipleChoiceImageTest(MultipleChoiceImageTest gi) {
		ArrayList<DownloadItem> list = new ArrayList<MediaCacheGeneralItems.DownloadItem>();
		if (gi.getAudioQuestion() != null) {
			DownloadItem audioQuestion = getBaseItem(gi);
			audioQuestion.setLocalId(AUDIO_LOCAL_ID);
			audioQuestion.setRemoteUrl(gi.getAudioQuestion());
			list.add(audioQuestion);
		}
		gi.getAnswers();
		for (MultipleChoiceAnswerItem imageAnswer : gi.getAnswers()) {
			MultipleChoiceImageAnswerItem mciai = (MultipleChoiceImageAnswerItem) imageAnswer;
			if (mciai.getImageUrl() != null) {
				DownloadItem imageDi = getBaseItem(gi);
				imageDi.setLocalId(mciai.getId() + ":i");
				imageDi.setRemoteUrl(mciai.getImageUrl());
				list.add(imageDi);
			}
			if (mciai.getAudioUrl() != null) {
				DownloadItem audioDi = getBaseItem(gi);
				audioDi.setLocalId(mciai.getId() + ":a");
				audioDi.setRemoteUrl(mciai.getAudioUrl());
				list.add(audioDi);
			}
		}
		return list.toArray(new DownloadItem[] {});
	}

	private DownloadItem getBaseItem(GeneralItem gi) {
		DownloadItem di = new DownloadItem();
		di.setGameId(gi.getGameId());
		di.setItemId(gi.getId());
		di.setReplicated(MediaCacheGeneralItems.REP_STATUS_TODO);
		return di;
	}

	public void createDownloadTasks(Context ctx, GeneralItem gi) {
		// TODO Auto-generated method stub
		DownloadFileTask syncTask = new DownloadFileTask();
		syncTask.gameId = gi.getGameId();

		DownloadItem[] item = getDownloadItems(gi);
		if (item != null) {
			CreateDownloadGeneralItems task = new CreateDownloadGeneralItems();
			task.items = item;
			task.taskToRunAfterExecute(syncTask);
			task.run(ctx);
		}

		// db.getMediaCacheGeneralItems().addToCache(item.getGameId());
		// (new GeneralItemMediaSyncTask(ctx, gameId)).addTaskToQueue(ctx);

	}

	public void initializeGeneralItemsVisibility(DBAdapter db, Long runId, Long gameId) {
		// TODO Auto-generated method stub
		
	}

	public GeneralItem getGeneralItem(long itemId) {
		return GeneralItemsCache.getInstance().getGeneralItems(itemId);
//>>>>>>> refs/heads/master
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
