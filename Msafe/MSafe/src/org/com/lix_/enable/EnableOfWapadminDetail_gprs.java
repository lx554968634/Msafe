package org.com.lix_.enable;

import java.util.List;

import org.com.lix_.db.entity.WapRecordEntity;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.os.Message;

/**
 * 管理wapadmin ，详细流量信息逻辑
 * 
 * @author punsher
 *
 */
public class EnableOfWapadminDetail_gprs extends Enable {

	public static final int GETLIST = 1;
	private int m_nStatus = -1;
	private String TAG = "EnableOfWapadminDetail_gprs : " + m_nStatus;

	private List<WapRecordEntity> m_pWapDetailList;

	private EnableOfWapmonitor m_pWapmonitor;

	public EnableOfWapadminDetail_gprs(int nType, Context pContext) {
		super(pContext);
		m_nStatus = nType;
		m_pWapmonitor = new EnableOfWapmonitor(pContext);
		TAG = "EnableOfWapadminDetail_gprs : " + m_nStatus;
	}

	@Override
	public void finish() {
	}

	@Override
	public void onViewClick(int nId) {
	}

	public void startLoading(int m_nNetType) {
		Debug.i(TAG, "加载信息");
		doRestartWork(m_nNetType);
	}

	public List<WapRecordEntity> getRecordList() {
		return m_pWapDetailList;
	}

	@Override
	protected void doAsyWorkInTask(Object... szObj) {
		super.doAsyWorkInTask(szObj);
		Integer nTag = Integer.parseInt(szObj[0].toString());
		switch (nTag) {
		case 0:
			break;
		case 1:
			break;
		}
		m_pWapDetailList = getWapdetailList(nTag);
		Message msg = Message.obtain();
		msg.what = GETLIST;
		m_nDataSize = m_pWapDetailList == null ? 0 : m_pWapDetailList.size();
		sendMessage(msg);
	}

	private List<WapRecordEntity> getWapdetailList(Integer nTag) {
		return m_pWapmonitor.getWapDetail(nTag);
	}

	@Override
	protected void doSynchrWork(Message pMsg) {
		super.doSynchrWork(pMsg);
		int nTag = Integer.parseInt(pMsg.what + "");
		switch (nTag) {
		case GETLIST:
			m_pCallback.callback(GETLIST);
			Debug.i(TAG, "GETLIST:" + m_nDataSize);
			break;
		}
	}

	private int m_nDataSize = 0;

	public int getDataSize() {
		return m_nDataSize;
	}

	public void initData() {
		m_nDataSize = 0;
	}

}
