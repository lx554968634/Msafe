package org.com.lix_.enable;

import java.util.ArrayList;

import org.com.lix_.Define;
import org.com.lix_.db.engine.LocalSharedpreferencesDB;
import org.com.lix_.enable.receiver.WapReceiver;
import org.com.lix_.service.BootService;
import org.com.lix_.ui.R;
import org.com.lix_.ui.SceneOfApkAdmin;
import org.com.lix_.ui.SceneOfExtraFuction;
import org.com.lix_.ui.SceneOfFileAdmin;
import org.com.lix_.ui.SceneOfPowerAdmin;
import org.com.lix_.ui.SceneOfRootAdmin;
import org.com.lix_.ui.SceneOfRubbishClear;
import org.com.lix_.ui.SceneOfVirusAdmin;
import org.com.lix_.ui.SceneOfWapAdmin;
import org.com.lix_.util.Debug;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

public class EnableOfMainActivity extends Enable {

	private String TAG = "EnableOfMainActivity";

	public static final int SURFCHECK = 6;
	public static final int WAPCHECK = 5;
	public static final int BIGFILECHECKWORK = 1;
	public static final int STARTRUBBISHLIGHTWORK = 0;
	public static final int PHONESAFECHECK = 3;
	public static final int SELFSTARTAPPCHECK = 4;
	public static final int NOUSUALAPKCHECK = 2;
	public static final int OVERSCAN = 7;
	private static final int SCANSERVICE = 8;
	private final int TOTAL = 7;

	private int m_nScanCount = -1;

	private LocalSharedpreferencesDB m_pSharedPerferencesDB;

	public EnableOfMainActivity(Context pContext) {
		super(pContext);
		m_nScanCount = -1;
		m_pSharedPerferencesDB = new LocalSharedpreferencesDB(pContext);
		doAsyWork(SCANSERVICE);
	}

	private int m_nTotal = 100;

	@Override
	protected void doSynchrWork(Message pMsg) {
		super.doSynchrWork(pMsg);
		int nTag = pMsg.what;
		switch (nTag) {
		case STARTRUBBISHLIGHTWORK:
		case BIGFILECHECKWORK:
		case NOUSUALAPKCHECK:
		case PHONESAFECHECK:
		case SELFSTARTAPPCHECK:
		case WAPCHECK:
		case SURFCHECK:
			m_nScanCount++;
			break;
		case SCANSERVICE:
			startWapService();
			return;
		}
		m_pCallback.callback(nTag, m_nTotal -= (nTag + 1));
		if (m_nScanCount == TOTAL) {
			pMsg = Message.obtain();
			pMsg.what = OVERSCAN;
			m_nScanCount = -1;
			m_pAsyHandler.sendMessageDelayed(pMsg, 10000);
		}
	}

	private void startWapService() {
		Intent pIntent = new Intent();
		pIntent.setClass(m_pContext, BootService.class);
		m_pContext.startService(pIntent);
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		int nTag = Integer.parseInt(szObj[0].toString());
		switch (nTag) {
		case STARTRUBBISHLIGHTWORK:
			break;
		case BIGFILECHECKWORK:
			break;
		case NOUSUALAPKCHECK:
			break;
		case PHONESAFECHECK:
			break;
		case SELFSTARTAPPCHECK:
			break;
		case WAPCHECK:
			break;
		case SURFCHECK:
			break;
		case SCANSERVICE:
			CheckService();
			return;
		}
		sendOverMessage(nTag);
	}

	private void CheckService() {
		ActivityManager myManager = (ActivityManager) m_pContext
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
		Debug.i(TAG, "是否开启了WapService:" + bFlag);
		Message msg = new Message();
		msg.what = SCANSERVICE;
		msg.arg1 = bFlag ? 1 : 0;
		sendMessage(msg);
	}

	public void checkPhone(int position) {
		if (position == 0) {
			m_nScanCount = 0;
		}
		switch (position) {
		case SURFCHECK:
			surfCheck();
			break;
		case WAPCHECK:
			wapCheck();
			break;
		case BIGFILECHECKWORK:
			bigFileCheckWork();
			break;
		case PHONESAFECHECK:
			phoneSafeCheck();
			break;
		case SELFSTARTAPPCHECK:
			selfStartAppCheck();
			break;
		case NOUSUALAPKCHECK:
			noUsualApkCheck();
			break;
		case STARTRUBBISHLIGHTWORK:
			startRubbishLightWork();
			break;
		}
	}

	private void sendOverMessage(int nTag) {
		Message msg = Message.obtain();
		msg.what = nTag;
		m_pAsyHandler.sendMessage(msg);
	}

	private void surfCheck() {
		doRestartWork(SURFCHECK);
	}

	private void wapCheck() {
		doRestartWork(WAPCHECK);
	}

	private void startRubbishLightWork() {
		doRestartWork(STARTRUBBISHLIGHTWORK);
	}

	private void selfStartAppCheck() {
		doRestartWork(SELFSTARTAPPCHECK);
	}

	private void phoneSafeCheck() {
		doRestartWork(PHONESAFECHECK);
	}

	private void noUsualApkCheck() {
		doRestartWork(NOUSUALAPKCHECK);
	}

	private void bigFileCheckWork() {
		doRestartWork(BIGFILECHECKWORK);
	}

	@Override
	public void onViewClick(int nId) {
		Intent pIntent = new Intent();
		Class pJump2Class = null;
		switch (nId) {
		case R.id.title_btn_mainlayout:
			pIntent = null;
			break;
		case R.id.btn_apkadmin_mainacitivity:
			pJump2Class = SceneOfApkAdmin.class;
			break;
		case R.id.btn_extrafuction_mainacitivity:
			pJump2Class = SceneOfExtraFuction.class;
			break;
		case R.id.btn_fileadmin_mainacitivity:
			pJump2Class = SceneOfFileAdmin.class;
			break;
		case R.id.btn_poweradmin_mainacitivity:
			pJump2Class = SceneOfPowerAdmin.class;
			break;
		case R.id.btn_rootadmin_mainacitivity:
			pJump2Class = SceneOfRootAdmin.class;
			break;
		case R.id.btn_rubbishclear_mainacitivity:
			pJump2Class = SceneOfRubbishClear.class;
			break;
		case R.id.btn_virusadmin_mainacitivity:
			pJump2Class = SceneOfVirusAdmin.class;
			break;
		case R.id.btn_wapadmin_mainacitivity:
			pJump2Class = SceneOfWapAdmin.class;
			break;
		default:
			break;
		}
		if (pIntent != null && pJump2Class != null) {
			// 跳转到子页面
			pIntent.setClass(m_pContext, pJump2Class);
			m_pContext.startActivity(pIntent);
		} else {
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}

	public void startCheck() {
		m_nScanCount = 0;
	}

}
