package org.celstec.arlearn2.android.menu;

import org.celstec.arlearn2.android.R;
import org.celstec.arlearn2.android.activities.AnnotateActivity;
import org.celstec.arlearn2.android.activities.AnswerQuestionActivity;
import org.celstec.arlearn2.android.activities.ListExcursionsActivity;
import org.celstec.arlearn2.android.activities.ListMessagesActivity;
import org.celstec.arlearn2.android.activities.LoginActivity;
import org.celstec.arlearn2.android.activities.MapViewActivity;
import org.celstec.arlearn2.android.activities.SplashScreenActivity;
import org.celstec.arlearn2.android.activities.ViewAnswerActivity;
import org.celstec.arlearn2.android.asynctasks.RefreshTask;
import org.celstec.arlearn2.android.db.DBAdapter;
import org.celstec.arlearn2.android.db.GeneralItemAdapter;
import org.celstec.arlearn2.android.db.MyActions;
import org.celstec.arlearn2.android.db.MyResponses;
import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.genItemActivities.AudioObjectActivity;
import org.celstec.arlearn2.android.genItemActivities.NarratorItemActivity;
import org.celstec.arlearn2.android.service.BackgroundService;
import org.celstec.arlearn2.android.service.NotificationService;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

public class MenuHandler {

	public static final int LOGIN = 0;
	public static final int LOGOUT = 1;
	public static final int EXIT = 2;
	public static final int MESSAGES = 3;
	public static final int PROVIDE_ANSWER = 4;
	public static final int MY_LOCATION = 5;
	public static final int DELETE_ANSWER = 6;
	public static final int RESET = 7;
	public static final int REFRESH = 8;

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
			context.finish();
			context.startActivity(new Intent(context, SplashScreenActivity.class));
			break;
		case EXIT:
			intent = new Intent(context, BackgroundService.class);
			intent.putExtra("exit", true);
			context.startService(intent);
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
			((MapViewActivity) context).animateToMyLocation();
			break;
		case DELETE_ANSWER:
			((ViewAnswerActivity) context).deleteAnswer();
			break;
		case RESET:
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(context.getString(R.string.resetQuestion))
			       .setCancelable(false)
			       .setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			        	   DBAdapter db = new DBAdapter(context);
			        	   db.openForWrite();
			        	   Long runId = getPropertiesAdapter().getCurrentRunId();
			        	   getPropertiesAdapter().setCurrentRunId(-1l);
			        	   getPropertiesAdapter().setRunStart(runId, 0);
			        	   MyActions ma = ((MyActions) db.table(DBAdapter.MYACTIONS_ADAPTER));
			        	   ma.deleteRun(runId);
			        	   ((GeneralItemAdapter) db.table(DBAdapter.GENERALITEM_ADAPTER)).resetVisiblity(runId);
			        	   ((MyResponses) db.table(DBAdapter.MYRESPONSES_ADAPTER)).deleteRun(runId);
			        	   db.close();
			        	
			           }
			       })
			       .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
			           public void onClick(DialogInterface dialog, int id) {
			                dialog.cancel();
			           }
			       });
			AlertDialog alert = builder.create();
			alert.show();
			break;
			
		case REFRESH:
			(new RefreshTask((ListExcursionsActivity)context)).execute(new Object[] {});
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
