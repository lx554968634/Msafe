package org.com.lix_.enable;

import java.util.List;

import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.os.Message;

public class EnableOfAboveshow extends Enable {	private List<AppInfo> m_szList;

private String TAG = "EnableOfAboveshow" ;

	public EnableOfAboveshow(Context pContext,List<AppInfo> szList) {
		super(pContext);
		m_szList = szList ;
		init();
	}

	private void init() {
		doAsyWork();
	}

	@Override
	public void finish() {

	}

	@Override
	public void onViewClick(int nId) {

	}

	@Override
	protected void doSynchrWork(Message pMsg) {
		super.doSynchrWork(pMsg);
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		super.doAsyWorkInTask(szObj);
		if(m_szList != null)
		{
			
		}else
		{
			Debug.e(TAG, "不可能扫描不到一个app应用的");
		}
	}

}