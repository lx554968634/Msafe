package org.com.lix_.enable;

import java.util.List;

import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.AppInfoEngine;
import org.com.lix_.enable.engine.PropInfoEngine;

import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class EnableOfRootAdmin extends Enable{

	public static final int SCAN_START = 1;
	
	public static final int SCAN_ITEM = 2 ;

	public static final int SCAN_FINISH = 3;
	
	private AppInfoEngine m_pEngine ;
	
	private PropInfoEngine m_pEngine2 ;

	public EnableOfRootAdmin(Context pContext,
			EnableCallback pCallback) {
		super(pContext) ;
		setCallback(pCallback);
		init() ;
	}
	
	private void init()
	{
		m_pEngine = new AppInfoEngine(m_pContext,m_pHandler) ;
		m_pEngine2 = new PropInfoEngine(m_pContext, m_pHandler) ;
		m_pTask.execute(0) ;
	}
	
	private List<AppInfo> m_szAppInfo = null ;
	
	private List<RunningServiceInfo> m_szRunningService = null ;
	
	AsyncTask m_pTask = new AsyncTask()
	{

		@Override
		protected Object doInBackground(Object... params) {
			m_szAppInfo = m_pEngine.getInstalledAppWithPermiss() ;
			m_szRunningService = m_pEngine2.getRunningService() ;
			Message msg = Message.obtain() ;
			msg.what = SCAN_FINISH ;
			m_pHandler.sendMessage(msg) ;
			return null;
		}
		
	} ;

	Handler m_pHandler = new Handler() 
	{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int nTag = msg.what ;
			switch(nTag)
			{
			case SCAN_ITEM:
				break ;
			case SCAN_START :
				break ;
			case SCAN_FINISH:
				m_pCallback.callback(nTag);
				break ;
			}
		}
		
	} ;
	
	@Override
	public void finish() {
	}

	@Override
	public void onViewClick(int nId) {
	}
	
	public List<RunningServiceInfo> getRunningServers()
	{
		return m_szRunningService ;
	}

	public List<AppInfo> getInstalledAppInfo() {
		return m_szAppInfo;
	}

}
