package org.celstec.arlearn2.android;

import java.util.HashMap;

import org.celstec.arlearn2.android.db.PropertiesAdapter;
import org.celstec.arlearn2.android.service.GCMHandler;
import org.celstec.arlearn2.beans.notification.GCMDeviceDescription;
import org.celstec.arlearn2.client.NotificationClient;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
    
public class GCMIntentService extends GCMBaseIntentService {

//	public GCMIntentService() {
//        super("594104153413" );
//        // TODO Auto-generated constructor stub
//        Log.i( "TEST", "GCMIntentService constructor called" );
//    }
	
	@Override
	protected void onError(Context arg0, String arg1) {
        Log.i( "TEST", "GCMIntentService constructor called" );
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
        Log.i( "TEST", "onMessage constructor called" );
        HashMap map = new HashMap<String, String>();
        for (String key: arg1.getExtras().keySet()) {
        	map.put(key, arg1.getExtras().getString(key));
        		
        }
        GCMHandler handler = GCMHandler.createHandler(this, map);
        if (handler != null) handler.handle();
	}

	@Override
	protected void onRegistered(Context context, String regid) {
        Log.i( "TEST", "GCMIntentService onRegistered called" );
        String deviceId = "35" + 
	        	Build.BOARD.length()%10+ Build.BRAND.length()%10 + 
	        	Build.CPU_ABI.length()%10 + Build.DEVICE.length()%10 + 
	        	Build.DISPLAY.length()%10 + Build.HOST.length()%10 + 
	        	Build.ID.length()%10 + Build.MANUFACTURER.length()%10 + 
	        	Build.MODEL.length()%10 + Build.PRODUCT.length()%10 + 
	        	Build.TAGS.length()%10 + Build.TYPE.length()%10 + 
	        	Build.USER.length()%10 ; //13 digits
	 GCMDeviceDescription desc = new GCMDeviceDescription();
	 desc.setAccount(PropertiesAdapter.getInstance(context).getFullId());
	 desc.setDeviceUniqueIdentifier(deviceId);
	 desc.setRegistrationId(regid);
	 NotificationClient.getOauthClient().gcm(PropertiesAdapter.getInstance(context).getAuthToken(), desc);

	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
        Log.i( "TEST", "GCMIntentService onUnregistered called" );
	}
	
	
}
//APA91bHZNqIZs5B3APnxfM-GqK9geZJ-vKLPoAhsEDwfQZtWsqUylYRPyhYEmdBYiT0xGY8TBdpH2vOxB3yGjbm1coqtgbkBdvbmdpez9lzly-2WiKbMcPFMmbdiaVQGswb_x87g-bEqHWiUQg9_FrE63TOqzh198bxd4Bo1agxlqLgdxFVvJB0