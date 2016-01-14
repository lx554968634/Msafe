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
		Debug.i(TAG, "onWIfiConnected 记录Wifi tag");
		TmpStatusEntity pEntity = TmpStatusEntity.getWifiEntity() ;
		m_pEnable.updateStatusWap(pEntity) ;
		//记录uid的wap数据
		m_pEnable.updateTmpUidwapdata(0) ;
	}

	protected void onWifiDisconnected() {
		Debug.i(TAG, "onWifiDisconnected 对比Wifi消耗，并记录数据");
		m_pEnable.stoneInfoWhenWifiDisconnected() ;
	}

	protected void onGprsConnected() {
		Debug.i(TAG, "onGprsConnected 记录流量tag");
		TmpStatusEntity pEntity = TmpStatusEntity.getGprsEntity() ;
		m_pEnable.updateStatusWap(pEntity) ;
		m_pEnable.updateTmpUidwapdata(1) ;
		//记录uid的wap数据
	}

	protected void onGprsDisConnected() {
		Debug.i(TAG, "onGprsDisConnected 对比流量消耗，并记录数据");
		m_pEnable.stoneInfoWhenGprsDisconnected() ;
	}

	EnableOfWapmonitor m_pEnable;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Debug.i(TAG, "接受了Receiver:"+intent.getAction());
		m_pEnable = new EnableOfWapmonitor(context) ;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo gprsInfo = cm
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// 判断是否是Connected事件
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

		// 判断是否是Disconnected事件，注意：处于中间状态的事件不上报给应用！上报会影响体验
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
	 * 检查当前WIFI是否连接，两层意思——是否连接，连接是不是WIFI
	 * 
	 * @param context
	 * @return true表示当前网络处于连接状态，且是WIFI，否则返回false
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
	 * 检查当前GPRS是否连接，两层意思——是否连接，连接是不是GPRS
	 * 
	 * @param context
	 * @return true表示当前网络处于连接状态，且是GPRS，否则返回false
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
	 * 检查当前是否连接
	 * 
	 * @param context
	 * @return true表示当前网络处于连接状态，否则返回false
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
