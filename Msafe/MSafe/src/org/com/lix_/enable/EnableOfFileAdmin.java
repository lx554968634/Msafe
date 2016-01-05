package org.com.lix_.enable;

import java.util.ArrayList;

import org.com.lix_.enable.engine.FileInfo;
import org.com.lix_.enable.engine.FileInfoEngine;
import org.com.lix_.util.Debug;
import org.com.lix_.util.MediaUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class EnableOfFileAdmin extends Enable {

	private String TAG = "EnableOfFileAdmin";

	public static final int NONE_SD = 2;

	public static final int SCAN_SIM_FILE = 3;

	public static final int FINISH_SIMFILE_SCAN = 4;


	private FileInfoEngine m_pFileEngine;

	private Context m_pContext;

	private EnableCallback m_pCallback;

	public EnableOfFileAdmin(Context pContext) {
		super(pContext);
		m_pContext = pContext;
	}

	@Override
	public void finish() {

	}

	@Override
	public void onViewClick(int nId) {

	}

	public void init(EnableCallback pCallback) {
		Debug.i(TAG, "init file admin enable");
		m_pCallback = pCallback;
		m_pFileEngine = new FileInfoEngine(m_pContext, m_pAsyHandler);
		doAsyWork();
	}

	/*
	 * 回调UI跟新
	 */

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		// TODO Auto-generated method stub
		super.doAsyWorkInTask(szObj);
		int nTag = m_pFileEngine.retSDCardStatus();
		Debug.i(TAG, "nTag == " + nTag);
		if (nTag == 1) {
			getSimpleBigFile();
			getTriBigFile();
			detailInfo() ;
		} else {
			Message pMsg = Message.obtain();
			pMsg.what = NONE_SD;
			sendMessage(pMsg);
		}
	}
	
	public int m_nTotalSize = 0 ;
	public long m_nTotalCache = 0 ;
	
	private void detailInfo() {
		if(m_szTmp == null || m_szTmp.size() == 0)
		{
			m_nTotalSize = 0 ;
		}else
		{
			m_nTotalSize = m_szTmp.size() ;
			for(int i = 0 ; i < m_nTotalSize ;i ++)
			{
				FileInfo pInfo = m_szTmp.get(i) ; 
				m_nTotalCache += pInfo.getSizeLong() ;
			}
		}
		Message msg = Message.obtain() ;
		msg.what = FINISH_SCAN;
		sendMessage(msg);
	}

	public static final int FINISH_SCAN = 10 ;
	public static final int START_SCAN_BIGFILE = 5;

	public static final int REFRESH_PROGRESS = 6;

	private int m_nTotalProgress = 0;

	private int m_nStepProgress = 0;

	@Override
	protected void doSynchrWork(Message msg) {
		super.doSynchrWork(msg);
		int nTag = msg.what;
		String sInfo = null;
		int nCount = 0;
		switch (nTag) {
		case FINISH_SCAN:
			m_pCallback.callback(nTag);
			break ;
		case NONE_SD:
			m_pCallback.callback(nTag);
			break;
		case REFRESH_PROGRESS:
			Debug.i(TAG, "显示进度:" + msg.arg1);
			m_pCallback.callback(nTag, msg.arg1);
			break;
		case FINISH_SIMFILE_SCAN:
			m_szTmp = ( ArrayList<FileInfo>) msg.obj ;
			m_nTotalSize = m_szTmp == null ? 0 : m_szTmp.size() ;
			m_pCallback.callback(FINISH_SIMFILE_SCAN);
			msg = Message.obtain();
			msg.what = REFRESH_PROGRESS;
			m_nTotalProgress = 50;
			msg.arg1 = m_nTotalProgress;
			sendMessage(msg);
			break;
		case START_SCAN_BIGFILE:
		case START_SCAN_TRI_FILE:
			nCount = msg.arg1;
			m_nStepProgress = 50 / nCount;
			msg = Message.obtain();
			msg.what = REFRESH_PROGRESS;
			msg.arg1 = m_nTotalProgress;
			sendMessage(msg);
			break ;
		case SCAN_TRI_FILE:
		case SCAN_SIM_FILE:
			msg = Message.obtain();
			msg.what = REFRESH_PROGRESS;
			m_nTotalProgress += m_nStepProgress;
			msg.arg1 = m_nTotalProgress;
			sendMessage(msg);
			break ;
		case FINISH_TRI_SCAN:
			ArrayList<FileInfo> pTmp = ( ArrayList<FileInfo>) msg.obj ;
			msg = Message.obtain();
			msg.what = REFRESH_PROGRESS;
			m_nTotalProgress = 100;
			msg.arg1 = m_nTotalProgress;
			sendMessage(msg);
			if(pTmp == null || pTmp.size() == 0)
			{
			}else
			{
				m_szTmp.addAll(pTmp);
				pTmp.clear() ;
			};
			pTmp = null ;
			System.gc(); 
			break ;
		}
	}
	
	private ArrayList<FileInfo> m_szTmp = new ArrayList<FileInfo>();
	public static final int START_SCAN_TRI_FILE = 7 ;
	public static final int SCAN_TRI_FILE = 8 ;
	public static final int FINISH_TRI_SCAN = 9 ;
	/*
	 * 获取基本文件信息 Moive DICM LOST.D...
	 */
	private void getSimpleBigFile() {
		m_pFileEngine.getSimpleBigFile();
	}

	/*
	 * 获取额外整理的文件信息 QQ文件，高德地图，iReader,微信
	 */
	private void getTriBigFile() {
		m_pFileEngine.getTriBigFile() ;
	}

	public ArrayList<FileInfo> getData() {
		return m_szTmp;
	}

	public void getMediaImage(View convertView, int position, Handler handler) {
		
	}

	public void getMediaImage(final int position,final String sPath ,final Handler handler) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Debug.i(TAG, "获取图片:"+sPath);
				Bitmap pBitmp = MediaUtils.getMediaImage(sPath) ;
				Message msg = Message.obtain() ;
				msg.arg1 = position ;
				msg.obj = pBitmp;
				handler.sendMessage(msg) ;
			}
		}).start(); 
	}

}
