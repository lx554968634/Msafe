package org.com.lix_.enable;

import java.util.List;

import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.os.Message;

public class EnableOfAboveshow extends Enable {
	private List<AppInfo> m_szList;

	private String TAG = "EnableOfAboveshow";

	private EnableCallback m_pCallback;

	public static final int FINISH_SCAN = 1;

	public static final int FINISH_NOLIST = 2;

	public EnableOfAboveshow(Context pContext, 
			List<AppInfo> list, EnableCallback pCalback) {
		super(pContext);
		m_szList = list ;
		m_pCallback = pCalback;
	}
	
	public void init()
	{
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
		int nTag = pMsg.what;
		switch (nTag) {
		case FINISH_SCAN:
			m_pCallback.callback(FINISH_SCAN);
			break;
		case FINISH_NOLIST:
			m_pCallback.callback(FINISH_NOLIST);
			break;
		}
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		super.doAsyWorkInTask(szObj);
		Message pMsg = Message.obtain();
		if (m_szList != null) {
			pMsg.what = FINISH_SCAN;
		} else {
			pMsg.what = FINISH_NOLIST;
		}
		m_pAsyHandler.sendMessage(pMsg);
	}

	public int getListCount() {
		if (m_szList.size() == 0)
			return 0;
		return m_szList.size() ;
	}

}