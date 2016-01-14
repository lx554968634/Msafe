package org.com.lix_.receiver;

import org.com.lix_.db.entity.TmpStatusEntity;
import org.com.lix_.enable.EnableOfWapmonitor;
import org.com.lix_.util.Debug;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.util.Log;

// <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
public class WapReceiver extends BroadcastReceiver {

	private String TAG = "WapReceiver";

	protected void onWIfiConnected() {
		Debug.i(TAG, "onWIfiConnected ��¼Wifi tag");
		TmpStatusEntity pEntity = TmpStatusEntity.getWifiEntity() ;
		m_pEnable.updateStatusWap(pEntity) ;
		//��¼uid��wap����
		m_pEnable.updateTmpUidwapdata(0) ;
	}

	protected void onWifiDisconnected() {
		Debug.i(TAG, "onWifiDisconnected �Ա�Wifi���ģ�����¼����");
		m_pEnable.stoneInfoWhenWifiDisconnected() ;
	}

	protected void onGprsConnected() {
		Debug.i(TAG, "onGprsConnected ��¼����tag");
		TmpStatusEntity pEntity = TmpStatusEntity.getGprsEntity() ;
		m_pEnable.updateStatusWap(pEntity) ;
		m_pEnable.updateTmpUidwapdata(1) ;
		//��¼uid��wap����
	}

	protected void onGprsDisConnected() {
		Debug.i(TAG, "onGprsDisConnected �Ա��������ģ�����¼����");
		m_pEnable.stoneInfoWhenGprsDisconnected() ;
	}

	EnableOfWapmonitor m_pEnable;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Debug.i(TAG, "������Receiver:"+intent.getAction());
		m_pEnable = new EnableOfWapmonitor(context) ;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo gprsInfo = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// �ж��Ƿ���Connected�¼�
		boolean wifiConnected = false;
		boolean gprsConnected = false;
		if (wifiInfo != null && wifiInfo.isConnected()) {
			wifiConnected = true;
		}
		if (gprsInfo != null && gprsInfo.isConnected()) {
			gprsConnected = true;
		}
		if (wifiConnected) {
			onWIfiConnected();
			return;
		}
		if (gprsConnected) {
			onGprsConnected();
			return;
		}

		// �ж��Ƿ���Disconnected�¼���ע�⣺�����м�״̬���¼����ϱ���Ӧ�ã��ϱ���Ӱ������
		boolean wifiDisconnected = false;
		boolean gprsDisconnected = false;
		if (wifiInfo == null || wifiInfo != null
				&& wifiInfo.getState() == State.DISCONNECTED) {
			wifiDisconnected = true;
		}
		if (gprsInfo == null || gprsInfo != null
				&& gprsInfo.getState() == State.DISCONNECTED) {
			gprsDisconnected = true;
		}
		if (wifiDisconnected) {
			onWifiDisconnected();
			return;
		}
		if (gprsDisconnected) {
			onGprsDisConnected();
			return;
		}
	}

	/**
	 * ��鵱ǰWIFI�Ƿ����ӣ�������˼�����Ƿ����ӣ������ǲ���WIFI
	 * 
	 * @param context
	 * @return true��ʾ��ǰ���紦������״̬������WIFI�����򷵻�false
	 */
	public static boolean isWifiConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()
				&& ConnectivityManager.TYPE_WIFI == info.getType()) {
			return true;
		}
		return false;
	}

	/**
	 * ��鵱ǰGPRS�Ƿ����ӣ�������˼�����Ƿ����ӣ������ǲ���GPRS
	 * 
	 * @param context
	 * @return true��ʾ��ǰ���紦������״̬������GPRS�����򷵻�false
	 */
	public static boolean isGprsConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()
				&& ConnectivityManager.TYPE_MOBILE == info.getType()) {
			return true;
		}
		return false;
	}

	/**
	 * ��鵱ǰ�Ƿ�����
	 * 
	 * @param context
	 * @return true��ʾ��ǰ���紦������״̬�����򷵻�false
	 */
	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		}
		return false;
	}
}
