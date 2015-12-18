package org.com.lix_.enable;

import java.util.ArrayList;

import org.apache.http.entity.FileEntity;
import org.com.lix_.enable.engine.FileInfo;
import org.com.lix_.enable.engine.FileInfoEngine;
import org.com.lix_.ui.R;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class EnableOfFileAdmin extends Enable{

	public static final int GET_LIST = 1;

	protected static final int NONE_SD = 2;
	
	private String[] m_szSimpleArr ;
	
	private FileInfoEngine m_pFileEngine ;
	
	private String[] m_szTriArr ;
	
	private Context m_pContext ;
	
	private EnableCallback m_pCallback ;

	public EnableOfFileAdmin(Context pContext) {
		super(pContext);
		m_pContext = pContext ;
	}

	@Override
	public void finish() {
		
	}

	@Override
	public void onViewClick(int nId) {
		
	}
	
	public void init(EnableCallback pCallback) {
		m_pCallback = pCallback ;
		m_pFileEngine = new FileInfoEngine(m_pContext, m_pHandler) ;
		m_szSimpleArr = m_pContext.getResources().getStringArray(R.array.file_type_simple) ;
		m_szTriArr = m_pContext.getResources().getStringArray(R.array.file_type_tri) ;
		m_pFileAdminTask.execute(0) ;
	}
	
	/*
	 * 回调UI跟新
	 */
	Handler m_pHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int nTag = msg.what ;
			switch(nTag)
			{
			case NONE_SD:
				break ;
			}
		}
		
	} ;
	
	/*
	 * 异步处理后台逻辑
	 */
	AsyncTask m_pFileAdminTask = new AsyncTask() {

		@Override
		protected Object doInBackground(Object... params) {
			int nTag = m_pFileEngine.retSDCardStatus();
			if(nTag != 1)
			{
				getSimpleBigFile() ;
				getTriBigFile() ;
			}else
			{
				Message pMsg = Message.obtain() ;
				pMsg.what = NONE_SD ;
			}
			return null;
		}

	} ;
	
	/*
	 * 获取基本文件信息
	 * Moive  DICM LOST.D...
	 */
	private void getSimpleBigFile() {
		
	}
	/*
	 * 获取额外整理的文件信息
	 * QQ文件，高德地图，iReader,微信
	 */
	protected void getTriBigFile() {
		
	}

}
