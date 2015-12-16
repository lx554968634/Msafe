package org.com.lix_.enable.engine;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

public class AppInfo implements Parcelable {
	private String appName;
	private String packageName;
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

	public boolean isSystemApp() {
		return isSystemApp;
	}

	public void setSystemApp(boolean isSystemApp) {
		this.isSystemApp = isSystemApp;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeString(appName);
		out.writeString(packageName);
		out.writeBooleanArray(new boolean[] { isSystemApp });
		out.writeLong(m_nRam);
		out.writeInt(mServiceCount);
	}

	public static final Parcelable.Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
		@Override
		public AppInfo[] newArray(int size) {
			return new AppInfo[size];
		}

		@Override
		public AppInfo createFromParcel(Parcel in) {
			return new AppInfo(in);
		}
	};

	public AppInfo() {
	}

	public AppInfo(Parcel in) {
		appName = in.readString();
		packageName = in.readString();
		boolean[] pBools = new boolean[] { true };
		in.readBooleanArray(pBools);
		isSystemApp = pBools[0];
		m_nRam = in.readLong();
		mServiceCount = in.readInt();
	}

	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", packageName=" + packageName
				+ ", isSystemApp=" + isSystemApp + ",Ram:"
				+ m_nRam + "," + mServiceCount + "]";
	}
}