package org.com.lix_.enable;

import java.util.ArrayList;
import java.util.List;

import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.enable.engine.AppInfoEngine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class EnableOfApkAdmin extends Enable{

	public static final int STARTSCAN = 1;

	public static final int SCAN_ITEM =2 ;

	public static final int SCAN_OVER = 3;

	private Context m_pContext ;
	
	private EnableCallback m_pCallback ;
	
	private AppInfoEngine m_pEngine ;
	
	public EnableOfApkAdmin(Context pContext) {
		super(pContext);
		m_pContext = pContext ;
	}
	
	public void init(EnableCallback pCallback)
	{
		m_pCallback = pCallback ;
		m_pEngine = new AppInfoEngine(m_pContext) ;
		m_pEngine.init(m_pHandler);
		m_pApkAdminTask.execute(0) ; 
	}
	
	Handler m_pHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int nTag = msg.what ;
			switch(nTag)
			{
			case STARTSCAN:
				m_pCallback.callback(""+msg.what,""+msg.arg1);
				break ;
			case SCAN_ITEM:
				m_pCallback.callback(""+msg.what,msg.obj);
				break ;
			case 3:
				m_pCallback.callback(""+msg.what);
				break ;
			}
		}
		
	} ;
	
	AsyncTask m_pApkAdminTask = new AsyncTask() {

		@Override
		protected Object doInBackground(Object... params) {
			scanUserApk() ;
			return null;
		}
		
	} ;
	
	private List<AppInfo> m_pData ;
	
	private void scanUserApk() 
	{
		m_pData  = m_pEngine.getInstalledApp() ;
		Message msg = Message.obtain() ;
		msg.what = SCAN_OVER ;
		m_pHandler.sendMessage(msg);
	}

	@Override
	public void finish() {
	}

	@Override
	public void onViewClick(int nId) {
		// TODO Auto-generated method stub
		
	}

	public int getDataCount() {
		return m_pData == null ? 0 : m_pData.size();
	}

	public AppInfo getDataInfo(int position) {
		return m_pData == null ? null :m_pData.get(position);
	}

}
