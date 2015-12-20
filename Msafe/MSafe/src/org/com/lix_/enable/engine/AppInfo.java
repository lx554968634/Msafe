package org.com.lix_.enable.engine;

import java.io.Serializable;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppInfo implements Serializable {
	private static final long serialVersionUID = 8802173452293625312L;
	private String appName = "";
	private String packageName = "";
	private boolean isSystemApp = false;
	private int mServiceCount = 0;
	private long mCache = 0;

	public long getmCache() {
		return mCache;
	}

	public void setmCache(long mCache) {
		this.mCache = mCache;
	}

	public int getmServiceCount() {
		return mServiceCount;
	}

	public void setmServiceCount(int mServiceCount) {
		this.mServiceCount = mServiceCount;
	}

	public String getAppName() {
		return appName;
	}

	public long getM_nRam() {
		return m_nRam;
	}

	public void setM_nRam(long m_nRam) {
		this.m_nRam = m_nRam;
	}

	private long m_nRam = 0;

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public boolean isSystemApp() {
		return isSystemApp;
	}

	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

	public AppInfo() {
	}

	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", packageName=" + packageName
				+ ", isSystemApp=" + isSystemApp + ",Ram:" + m_nRam + ","
				+ mServiceCount + "]";
	}
}