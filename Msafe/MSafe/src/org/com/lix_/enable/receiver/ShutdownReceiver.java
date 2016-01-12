package org.com.lix_.enable.receiver;

import java.util.ArrayList;

import org.com.lix_.Define;
import org.com.lix_.service.BootService;
import org.com.lix_.util.Debug;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShutdownReceiver extends BroadcastReceiver {

	private String TAG = "ShutdownReceiver";

	@Override
	public void onReceive(Context context, Intent intent) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		boolean bFlag = false;
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals(Define.WAPSERVICE_PATH)) {
				Debug.i(TAG, "ÕÒµ½Ö¸¶¨service:" + Define.WAPSERVICE_PATH);
				bFlag = true;
			} else {
				Debug.i(TAG,
						"ÅÐ¶Ïservice:"
								+ runningService.get(i).service.getClassName());
			}
		}
		if (bFlag) {
			Intent pIntent = new Intent();
			pIntent.setClass(context, BootService.class);
			context.stopService(pIntent);
		}
	}

}
