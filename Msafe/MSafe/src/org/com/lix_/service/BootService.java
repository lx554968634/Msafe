package org.com.lix_.service;

import org.com.lix_.Define;
import org.com.lix_.enable.receiver.ShutdownReceiver;
import org.com.lix_.enable.receiver.WapReceiver;
import org.com.lix_.util.Debug;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.IBinder;

/*
 * ���뻹����Ҫserviceȥ���� wapReceiver����������
 */
public class BootService extends Service {

	private String TAG = "BootService";

	private WapReceiver m_pWapReceiver;

	private ShutdownReceiver m_pShutdownReceiver;

	@Override
	public void onCreate() {
		super.onCreate();
		Debug.i(TAG, "���� BootService");
		m_pShutdownReceiver = new ShutdownReceiver();
		m_pWapReceiver = new WapReceiver();
	}

	@Override
	public void onDestroy() {
		if (m_pWapReceiver != null) {
			Debug.i(TAG, "����WapReceiver");
			unregisterReceiver(m_pWapReceiver);
		}
		if (m_pShutdownReceiver != null) {
			Debug.i(TAG, "����ShutdownReceiver");
			unregisterReceiver(m_pShutdownReceiver);
		}
		Debug.i(TAG, "����BootService");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (m_pWapReceiver != null) {
			Debug.i(TAG, "ע�����WapReceiver");
			IntentFilter filter = new IntentFilter();
			filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
			registerReceiver(m_pWapReceiver, filter);
		}
		if (m_pShutdownReceiver != null) {
			Debug.i(TAG, "ע�����ShutdownReceiver");
			IntentFilter filter = new IntentFilter();
			filter = new IntentFilter();
			filter.addAction(Intent.ACTION_SHUTDOWN);
			filter.addAction(Define.TEST_ACTION);
			registerReceiver(m_pShutdownReceiver, filter);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
