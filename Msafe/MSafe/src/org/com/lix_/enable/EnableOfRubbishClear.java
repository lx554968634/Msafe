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
 * ���enable��Ҫʵ��5������ 1���ڴ���٣�ɱ�����̺ͷ����̨ 2��ϵͳӦ�û��棬ɾ��/data/data/packagename/..cache
 * ..databases .. files .. fhareprefs 3�������ļ� 4�����లװ�� 5��Ӧ��ж�ز��� adb shell mount -o
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
	 * ������
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
			Debug.e(TAG, "��ʼ׼��ɨ��sd��");
		}

		@Override
		protected Object doInBackground(Object... params) {
			String nStatus = Environment.getExternalStorageState();
			String sTag = "";
			int nTag = 0;
			if (Environment.MEDIA_MOUNTED.equals(nStatus)) {
				sTag = "sd��׼���ɹ�";
				nTag = 1;
			} else if (Environment.MEDIA_UNMOUNTED.equals(nStatus)) {
				sTag = "�ֻ�����Ϊsd��ж��";
			} else if (Environment.MEDIA_REMOVED.equals(nStatus)) {
				sTag = "����ȡ��sd��";
			} else if (Environment.MEDIA_BAD_REMOVAL.equals(nStatus)) {
				sTag = "�ֻ�ֱ�Ӱγ�sd��";
			} else if (Environment.MEDIA_SHARED.equals(nStatus)) {
				sTag = "�ֻ�sd���������ӵ��� ";
			} else if (Environment.MEDIA_CHECKING.equals(nStatus)) {
				sTag = "�ֻ�����ɨ��sd��������..";
			} else {
				sTag = "�ֻ�sd���쳣";
			}
			Debug.e(TAG, "sd����" + sTag);
			if (nTag == 1) {
				Debug.e(TAG, "��Ŀ¼��"
						+ Environment.getRootDirectory().getAbsolutePath());
				Debug.e(TAG, "extrĿ¼��"
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
