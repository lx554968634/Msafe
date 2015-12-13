package org.com.lix_.enable;

import java.io.File;
import java.util.ArrayList;

import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

/**
 * 这个enable需要实现5个功能 1、内存加速，杀死进程和服务后台 2、系统应用缓存，删除/data/data/packagename/..cache
 * ..databases .. files .. fhareprefs 3、垃圾文件 4、多余安装包 5、应用卸载残余 adb shell mount -o
 * remount rw /
 * 
 * @author punsher
 *
 */
public class EnableOfRubbishClear extends Enable {

	private String TAG = "EnableOfRubbishClear";

	private Context m_pContext;

	private Thread m_pInnerThread;

	public EnableOfRubbishClear(Context pContext) {
		super(pContext);
		m_pContext = pContext;
		init();
	}

	@Override
	public void finish() {

	}

	private void init() {
		m_pRubbishTask.execute(0);
	}

	@Override
	public void onViewClick(int nId) {

	}

	/**
	 * 处理器
	 */
	Handler m_pRubbishHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}

	};

	AsyncTask m_pRubbishTask = new AsyncTask() {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Debug.e(TAG, "开始准备扫描sd卡");
		}

		@Override
		protected Object doInBackground(Object... params) {
			String nStatus = Environment.getExternalStorageState();
			String sTag = "";
			int nTag = 0;
			if (Environment.MEDIA_MOUNTED.equals(nStatus)) {
				sTag = "sd卡准备成功";
				nTag = 1;
			} else if (Environment.MEDIA_UNMOUNTED.equals(nStatus)) {
				sTag = "手机设置为sd卡卸载";
			} else if (Environment.MEDIA_REMOVED.equals(nStatus)) {
				sTag = "正常取出sd卡";
			} else if (Environment.MEDIA_BAD_REMOVAL.equals(nStatus)) {
				sTag = "手机直接拔出sd卡";
			} else if (Environment.MEDIA_SHARED.equals(nStatus)) {
				sTag = "手机sd卡正在链接电脑 ";
			} else if (Environment.MEDIA_CHECKING.equals(nStatus)) {
				sTag = "手机正在扫描sd卡过程中..";
			} else {
				sTag = "手机sd卡异常";
			}
			Debug.e(TAG, "sd卡：" + sTag);
			if (nTag == 1) {
				Debug.e(TAG, "根目录："
						+ Environment.getRootDirectory().getAbsolutePath());
				Debug.e(TAG, "extr目录："
						+ Environment.getExternalStorageDirectory()
								.getAbsolutePath());
				ArrayList<String> szContents = new ArrayList<String>() ;
				UiUtils.readFile(Environment.getExternalStorageDirectory() ,szContents) ;
				Debug.DEBUG_STR += Environment.getExternalStorageDirectory().getAbsolutePath()  ;
				for(String sInfo:szContents)
				{
					Debug.DEBUG_STR +=szContents ;
				}
			}
			return null;
		}
	};

}
