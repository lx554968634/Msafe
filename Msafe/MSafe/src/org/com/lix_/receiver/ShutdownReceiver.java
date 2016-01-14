package org.com.lix_.receiver;

import java.util.ArrayList;
import java.util.List;

import org.com.lix_.Define;
import org.com.lix_.db.dao.IDao;
import org.com.lix_.db.dao.TmpRecordWapDaoImpl;
import org.com.lix_.db.dao.TmpStatusDaoImpl;
import org.com.lix_.db.dao.WapRecordDaoImpl;
import org.com.lix_.db.dao.WapuselogDaoImpl;
import org.com.lix_.db.entity.TmpRecordWapEntity;
import org.com.lix_.db.entity.TmpStatusEntity;
import org.com.lix_.enable.EnableOfWapmonitor;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.AppInfoEngine;
import org.com.lix_.service.BootService;
import org.com.lix_.util.Debug;

import android.Manifest;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class ShutdownReceiver extends BroadcastReceiver {

	private String TAG = "ShutdownReceiver";
	EnableOfWapmonitor m_pEnable;

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
				Debug.i(TAG, "找到指定service:" + Define.WAPSERVICE_PATH);
				bFlag = true;
			} else {
				Debug.i(TAG,
						"判断service:"
								+ runningService.get(i).service.getClassName());
			}
		}
		if (bFlag) {
			m_pEnable = new EnableOfWapmonitor(context);
			stoneWapInfo(context);
		}
	}

	private void stoneWapInfo(final Context pContext) {
		final InnerHandler pHandler = new InnerHandler();
		final AppInfoEngine pEngine1 = new AppInfoEngine(pContext);
		new Thread(new Runnable() {

			@Override
			public void run() {
				List<AppInfo> pList = pEngine1.getInstalledAppWithPermiss();
				stoneInfo(pList);
				Message msg = Message.obtain();
				msg.what = SCAN_OVER;
				msg.obj = pContext;
				pHandler.sendMessage(msg);
			}
		}).start();
	}

	private void stoneInfo(List<AppInfo> pList) {
		if (pList == null || pList.size() == 0)
			return;
		for (AppInfo pInfo : pList) {
			m_pEnable.saveAndCheckRecord(pInfo);
		}
	}

	private final int SCAN_OVER = 2;

	class InnerHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int nTag = msg.what;
			switch (nTag) {
			case SCAN_OVER:
				Debug.i(TAG, "扫描结束");
				Intent pIntent = new Intent();
				pIntent.setClass(((Context) msg.obj), BootService.class);
				((Context) msg.obj).stopService(pIntent);
				return;
			}
		}
	}
}
