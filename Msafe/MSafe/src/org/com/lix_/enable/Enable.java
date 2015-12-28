package org.com.lix_.enable;

import java.io.Serializable;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

/**
 * 逻辑处理 不处理UI变化，顾及使用线程回引起UI变化
 * 
 * @author punsher
 *
 */
public abstract class Enable implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Context m_pContext;
	protected EnableCallback m_pCallback;

	public Enable() {
	}

	public Enable(Context pContext) {
		m_pContext = pContext;
	}

	public void setCallback(EnableCallback pCallback) {
		m_pCallback = pCallback;
	}

	public abstract void finish();

	public abstract void onViewClick(int nId);

	public void doAsyWork() {
		m_pAsyTask.execute(0);
	}

	Handler m_pAsyHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			doSynchrWork(msg);
		}

	};

	AsyncTask m_pAsyTask = new AsyncTask() {
		@Override
		protected Object doInBackground(Object... params) {
			doAsyWorkInTask();
			return null;
		}

	};

	protected void doSynchrWork(Message pMsg) {

	}

	protected void doAsyWorkInTask(Object... szObj) {

	}
}
