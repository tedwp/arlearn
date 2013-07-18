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
package org.celstec.arlearn2.android.menu;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnswerQuestionActivity;
import org.celstec.arlearn2.android.activities.DownloadOSMMapTilesActivity;
import org.celstec.arlearn2.android.activities.GenericMapViewActivity;
import org.celstec.arlearn2.android.activities.IntentIntegrator;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.ListRunsParticipateActivity;
import org.celstec.arlearn2.android.activities.LoginActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.activities.OauthProvidersList;
import org.celstec.arlearn2.android.activities.SplashScreenActivity;
import org.celstec.arlearn2.android.activities.ViewAnswerActivity;
import org.celstec.arlearn2.android.broadcast.NetworkSwitcher;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.service.ChannelAPINotificationService;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;
//import org.jivesoftware.smack.Chat;
//import org.jivesoftware.smack.ChatManager;
//import org.jivesoftware.smack.ConnectionConfiguration;
//import org.jivesoftware.smack.MessageListener;
//import org.jivesoftware.smack.XMPPConnection;
//import org.jivesoftware.smack.XMPPException;
//import org.jivesoftware.smack.packet.Message;

public class MenuHandler {

	public static final int LOGIN = 0;
	public static final int LOGOUT = 1;
	public static final int EXIT = 2;
	public static final int MESSAGES = 3;
	public static final int PROVIDE_ANSWER = 4;
	public static final int MY_LOCATION = 5;
	public static final int DELETE_ANSWER = 6;
//	public static final int RESET = 7;
	public static final int REFRESH = 8;
	public static final int SCAN = 9;
	public static final int DOWNLOAD_MAP_TILES = 10;
	public static final int UNREGISTER = 11;
	public static final int SCAN_RUN = 15;
	public static final int OAUTH_LOGIN = 20;
	
	private Activity context;
	private PropertiesAdapter pa;

	public MenuHandler(Activity context) {
		this.context = context;
		pa = new PropertiesAdapter(context);
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case LOGIN:
			context.startActivity(new Intent(context, LoginActivity.class));
			break;
		case LOGOUT:
			pa.disAuthenticate();
//			context.startService(new Intent(context, ChannelAPINotificationService.class).putExtra("stop", true));
			context.finish();
			context.startActivity(new Intent(context, SplashScreenActivity.class));
			break;
		case EXIT:
//			intent = new Intent(context, BackgroundService.class);
//			intent.putExtra("exit", true);
//			context.startService(intent);
			context.finish();
			break;
		case MESSAGES:
			context.startActivity(new Intent(context, ListMessagesActivity.class));
			break;
		case PROVIDE_ANSWER:
			intent = new Intent(context, AnswerQuestionActivity.class);
			intent.putExtra("runId", getPropertiesAdapter().getCurrentRunId());
			intent.putExtra("bean", ((NarratorItemActivity) context).getNarratorBean());
			intent.putExtra("generalItemId", ((NarratorItemActivity) context).getItemId());
			context.startActivity(intent);
			break;
		case MY_LOCATION:
			if  (context instanceof MapViewActivity) {
				((MapViewActivity) context).animateToMyLocation();
			} else {
				((GenericMapViewActivity) context).animateToMyLocation();	
			}
			
			break;
		case DELETE_ANSWER:
			((ViewAnswerActivity) context).deleteAnswer();
			break;
		case UNREGISTER:
			((ListRunsParticipateActivity) context).showUnregister();

			break;
		case OAUTH_LOGIN:
			context.startActivity(new Intent(context, OauthProvidersList.class));
//			Intent intent2 = new Intent(context, OauthActivity.class);
//	        intent2.setData(Uri.parse("https://accounts.google.com/o/oauth2/auth?redirect_uri=http://ar-learn.appspot.com/oauth/google&response_type=code&client_id=594104153413-7ec2cn8jkuh7b4j27dn6ohjqst929s8h.apps.googleusercontent.com&approval_prompt=force&scope=profile+email"));
//	        context.startActivityForResult(intent2, 0);
			break;
//		case RESET:
//			AlertDialog.Builder builder = new AlertDialog.Builder(context);
//			builder.setMessage(context.getString(R.string.resetQuestion))
//			       .setCancelable(false)
//			       .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			        	   DBAdapter db = new DBAdapter(context);
//			        	   db.openForWrite();
//			        	   Long runId = getPropertiesAdapter().getCurrentRunId();
//			        	   getPropertiesAdapter().setCurrentRunId(-1l);
//			        	   getPropertiesAdapter().setRunStart(runId, 0);
//			        	   MyActions ma = ((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER));
//			        	   ma.deleteRun(runId);
//			        	   ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).resetVisiblity(runId);
//			        	   ((MyResponses) db.table(DBAdapter.MYRESPONSES_ADAPTER)).deleteRun(runId);
//			        	   db.close();
//			        	
//			           }
//			       })
//			       .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			                dialog.cancel();
//			           }
//			       });
//			AlertDialog alert = builder.create();
//			alert.show();
//			break;
		case SCAN_RUN:
			if (!NetworkSwitcher.isOnline(context)) {
				Toast.makeText(context, context.getString(R.string.networkUnavailable), Toast.LENGTH_LONG).show();
			} else {
				IntentIntegrator integrator = new IntentIntegrator(context);
				integrator.initiateScan();
			}
			break;
		case SCAN:
			IntentIntegrator integrator = new IntentIntegrator(context);
			integrator.initiateScan();
			break;
		case DOWNLOAD_MAP_TILES:
			intent = new Intent(context, DownloadOSMMapTilesActivity.class);
			intent.putExtra("runId", getPropertiesAdapter().getCurrentRunId());
			context.startActivity(intent);
			break;
		default:
			break;
		}
		return false;
	}
	
	public PropertiesAdapter getPropertiesAdapter() {
		return pa;
	}
	
	public Context getContext() {
		return context;
	}
}
