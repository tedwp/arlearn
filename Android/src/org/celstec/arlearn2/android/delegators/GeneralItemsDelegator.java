package org.celstec.arlearn2.android.delegators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import org.celstec.arlearn2.android.asynctasks.db.CreateDownloadGeneralItems;
import org.celstec.arlearn2.android.asynctasks.network.DownloadFileTask;
import org.celstec.arlearn2.android.cache.MediaGeneralItemCache;
import org.celstec.arlearn2.android.cache.ResponseCache;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems;
import org.celstec.arlearn2.android.db.MediaCacheGeneralItems.DownloadItem;
import org.celstec.arlearn2.beans.generalItem.AudioObject;
import org.celstec.arlearn2.beans.generalItem.GeneralItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceAnswerItem;
import org.celstec.arlearn2.beans.generalItem.MultipleChoiceImageAnswerItem;
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

}
