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

import java.util.*;

import android.util.Log;
import org.celstec.arlearn2.android.asynctasks.db.CreateDownloadGeneralItems;
import org.celstec.arlearn2.android.asynctasks.db.CreateProximityEvents;
import org.celstec.arlearn2.android.asynctasks.db.GeneralItemDependencyHandler;
import org.celstec.arlearn2.android.asynctasks.db.InitDownloadStatusTask;
import org.celstec.arlearn2.android.asynctasks.db.InitGeneralItemVisibilityTask;
import org.celstec.arlearn2.android.asynctasks.db.LoadGeneralItemsFromDbTask;
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
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.beans.generalItem.*;
import org.celstec.arlearn2.beans.run.Response;
import org.celstec.arlearn2.client.GeneralItemClient;

import android.content.Context;
import android.net.Uri;
import android.os.DropBoxManager.Entry;

public class GeneralItemsDelegator {
	private static GeneralItemsDelegator instance;
	public static String AUDIO_LOCAL_ID = "audio";
	public static String VIDEO_LOCAL_ID = "video";
	public static String ICON_LOCAL_ID = "icon";
    private static long lastSyncDate = 0l;

	private GeneralItemsDelegator() {

	}

	public static GeneralItemsDelegator getInstance() {
		if (instance == null) {
			instance = new GeneralItemsDelegator();
		}
		return instance;
	}

    public void synchronizeGeneralItemsWithServer(Context ctx, Long runId, Long gameId) {
        synchronizeGeneralItemsWithServer(ctx,runId,gameId, false);

    }

	public void synchronizeGeneralItemsWithServer(Context ctx, Long runId, Long gameId, boolean overrideLastSyncDateCheck) {
        Log.i("TEST", "synchronizeGeneralItemsWithServer");
        long currentTime = System.currentTimeMillis();
        if (!overrideLastSyncDateCheck && (lastSyncDate + 20000 > currentTime)) {
            Log.i("TEST", "NOT UPDATING");
            return;
        }
        lastSyncDate = currentTime;
		LoadGeneralItemsFromDbTask loadFromDb_0 = new LoadGeneralItemsFromDbTask(gameId, runId);
		loadFromDb_0.run(ctx);
		
		SynchronizeGeneralItemsTask syncItemTask_1 = new SynchronizeGeneralItemsTask();
		syncItemTask_1.setGameId(gameId);
		SynchronizeGeneralItemVisiblityTask syncVisItemsTask_2 = new SynchronizeGeneralItemVisiblityTask();
		syncVisItemsTask_2.setRunId(runId);
		
		
		InitGeneralItemVisibilityTask visibilityTask_3 = new InitGeneralItemVisibilityTask(runId, gameId);
		InitDownloadStatusTask downloadstatus_4 = new InitDownloadStatusTask(gameId); 
		
		GeneralItemDependencyHandler dependencyTask_5 = new GeneralItemDependencyHandler();
		CreateProximityEvents events = new CreateProximityEvents(ctx,runId, gameId);
		
		
		downloadstatus_4.taskToRunAfterExecute(events);
		downloadstatus_4.taskToRunAfterExecute(dependencyTask_5);
		visibilityTask_3.taskToRunAfterExecute(downloadstatus_4);
		syncVisItemsTask_2.taskToRunAfterExecute(visibilityTask_3);
		syncItemTask_1.taskToRunAfterExecute(syncVisItemsTask_2);
		loadFromDb_0.taskToRunAfterExecute(syncItemTask_1);
		
		
		loadFromDb_0.run(ctx);
	}
	
	public void synchronizeGeneralItemsWithServer(Context ctx, Long gameId) {
		SynchronizeGeneralItemsTask syncItemTask_1 = new SynchronizeGeneralItemsTask();
		syncItemTask_1.setGameId(gameId);
		syncItemTask_1.run(ctx);
	}
	
	public GeneralItem getGeneralItemFromNetwork(Context ctx, Long gameId, Long generalItemId) {
		return GeneralItemClient.getGeneralItemClient().getGeneralItem(PropertiesAdapter.getInstance(ctx).getAuthToken(), gameId, generalItemId);
		
	}
	
//	public void synchronizeGeneralItemWithServer(Context ctx, Long gameId, Long generalItemId) {
//		SynchronizeGeneralItemTask syncTask = new SynchronizeGeneralItemTask();
//		syncTask.setGameId(gameId);
//		syncTask.setGeneralItemId(generalItemId);
//		syncTask.run(ctx);
//	}
	
	public TreeSet<Response> getResponses(Long runId, Long itemId) {
		return ResponseCache.getInstance().getResponses(runId, itemId);
	}

	public HashMap<String, Uri> getLocalMediaUriMap(GeneralItem gi) {
		return MediaGeneralItemCache.getInstance(gi.getGameId()).getLocalMediaUriMap(gi);
	}

	public double getDownloadPercentage(GeneralItem generalItem) {
		return MediaGeneralItemCache.getInstance(generalItem.getGameId()).getPercentageDownloaded(generalItem);
	}
	
	public int getAverageDownloadStatus(GeneralItem generalItem) {
		 HashMap<String, Integer> statusMap = MediaGeneralItemCache.getInstance(generalItem.getGameId()).getReplicationStatus(generalItem.getId());
		 boolean todo = false;
		 if (statusMap == null) return MediaCacheGeneralItems.REP_STATUS_DONE;
		 for (Map.Entry<String, Integer> e : statusMap.entrySet()) {
			 if (e.getValue() == MediaCacheGeneralItems.REP_STATUS_FILE_NOT_FOUND) return MediaCacheGeneralItems.REP_STATUS_FILE_NOT_FOUND; 
			 if (e.getValue() == MediaCacheGeneralItems.REP_STATUS_TODO) todo = true; 
			 if (e.getValue() == MediaCacheGeneralItems.REP_STATUS_SYNCING) todo = true; 

		 }
		 if (todo) return MediaCacheGeneralItems.REP_STATUS_TODO;
		return MediaCacheGeneralItems.REP_STATUS_DONE;
	}

	public DownloadItem[] getDownloadItems(GeneralItem gi) {
		ArrayList<DownloadItem> downloadArray = new ArrayList<MediaCacheGeneralItems.DownloadItem>();
		if (gi.getIconUrl() != null) {
			downloadArray.add(getIconDownloadObject(gi));
		}
        if (gi.getFileReferences() != null && !gi.getFileReferences().isEmpty()) {
            downloadArray.addAll(getFileReferences(gi));
        }

		if (gi instanceof AudioObject) {
			downloadArray.add(getDownloadItemsAudioObject((AudioObject) gi));
		}
		if (gi instanceof VideoObject) {
			downloadArray.add(getDownloadItemsVideoObject((VideoObject) gi));
		}
		if (gi instanceof SingleChoiceImageTest) {
            downloadArray.addAll(getDownloadItemsSingleChoiceImageTest((SingleChoiceImageTest) gi));
		}
		if (gi instanceof MultipleChoiceImageTest) {
            downloadArray.addAll(getDownloadItemsMultipleChoiceImageTest((MultipleChoiceImageTest) gi));
		}
		return downloadArray.toArray(new DownloadItem[]{});
	}



    private DownloadItem getIconDownloadObject(GeneralItem gi) {
		DownloadItem returnObject = new DownloadItem();
		returnObject = getBaseItem(gi);
		returnObject.setLocalId(ICON_LOCAL_ID);
		returnObject.setRemoteUrl(gi.getIconUrl());
        if (gi.getIconUrlMd5Hash()!=null) returnObject.setMd5Hash(gi.getIconUrlMd5Hash());
		return returnObject;
	}
	
	private DownloadItem getDownloadItemsAudioObject(AudioObject oa) {
		DownloadItem returnObject = new DownloadItem();
		returnObject = getBaseItem(oa);
		returnObject.setLocalId(AUDIO_LOCAL_ID);
		returnObject.setRemoteUrl(oa.getAudioFeed());
        if (oa.getMd5Hash()!= null) returnObject.setMd5Hash(oa.getMd5Hash());

        return returnObject;
	}

	private DownloadItem getDownloadItemsVideoObject(VideoObject gi) {
		DownloadItem returnObject = new DownloadItem();
		returnObject = getBaseItem(gi);
		returnObject.setLocalId(VIDEO_LOCAL_ID);
		returnObject.setRemoteUrl(gi.getVideoFeed());
        if (gi.getMd5Hash()!= null) returnObject.setMd5Hash(gi.getMd5Hash());
		return returnObject;
	}

    private Collection<? extends DownloadItem> getFileReferences(GeneralItem gi) {
        ArrayList<DownloadItem> list = new ArrayList<MediaCacheGeneralItems.DownloadItem>();
        for (FileReference fileReference: gi.getFileReferences()) {
            DownloadItem downloadItem = getBaseItem(gi);
            downloadItem.setLocalId(fileReference.getKey());
            downloadItem.setRemoteUrl(fileReference.getFileReference());
            downloadItem.setPreferredFileName(fileReference.getKey());
            if (fileReference.getMd5Hash()!= null) downloadItem.setMd5Hash(fileReference.getMd5Hash());
            list.add(downloadItem);
        }
        return list;
    }

	private ArrayList<DownloadItem> getDownloadItemsSingleChoiceImageTest(SingleChoiceImageTest gi) {
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
		return list; //.toArray(new DownloadItem[] {});
	}
	
	private ArrayList<DownloadItem> getDownloadItemsMultipleChoiceImageTest(MultipleChoiceImageTest gi) {
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
				imageDi.setRemoteUrl(mciai.getImageUrl().trim());
				list.add(imageDi);
			}
			if (mciai.getAudioUrl() != null) {
				DownloadItem audioDi = getBaseItem(gi);
				audioDi.setLocalId(mciai.getId() + ":a");
				audioDi.setRemoteUrl(mciai.getAudioUrl().trim());
				list.add(audioDi);
			}
		}
		return list; //.toArray(new DownloadItem[] {});
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
	}
	
	public GeneralItem getGeneralItem(DBAdapter db, long itemId) {
		return db.getGeneralItemAdapter().queryById(itemId);
	}

	

}
