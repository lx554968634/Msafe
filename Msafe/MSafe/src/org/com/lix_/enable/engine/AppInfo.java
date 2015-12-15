package org.com.lix_.enable.engine;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private String appName;
	private String packageName;
	private Drawable icon;
	private boolean isSystemApp;
	private int mServiceCount;
	private long mCache;

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

	private long m_nRam;

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public boolean isSystemApp() {
		return isSystemApp;
	}

	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", packageName=" + packageName
				+ ", icon=" + icon + ", isSystemApp=" + isSystemApp + ",Ram:"
				+ m_nRam + "," + mServiceCount + "]";
	}

}