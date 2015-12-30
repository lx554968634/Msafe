package org.com.lix_.ui;

import java.util.List;

import org.com.lix_.enable.EnableOfAboveshow;
import org.com.lix_.enable.EnableOfRootStart;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * ���� ��������֪ͨ����
 */
public class FragmentOfAboveshow extends BaseFragActivity {

	private EnableOfAboveshow m_pEnable;

	private View m_pTotalView;

	private LayoutInflater m_pInFlater;

	public FragmentOfAboveshow(List<AppInfo> szList, Context pContext) {
		super();
		m_pEnable = new EnableOfAboveshow(pContext, szList, m_pCallback);
		m_pInFlater = LayoutInflater.from(pContext);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tabchild_root_notification, null);
		m_pTotalView = v.findViewById(R.id.root_notification_content);
		return v;
	}

	@Override
	public void doCallback(Object... szObj) {
		int nTag = Integer.parseInt(szObj[0].toString());
		switch (nTag) {
		case EnableOfAboveshow.FINISH_NOLIST:
			showNoList();
			break;
		case EnableOfAboveshow.FINISH_SCAN:
			showList();
			break;
		}
	}

	private void showList() {
		if (m_pEnable.getListCount() == 0) {
			showNoList();
			return;
		}
		if (m_pTotalView == null) {
			Debug.e(TAG, "m_pTotalView == null in showList ~!");
		} else {
			for (int i = 0; i < m_pEnable.getListCount(); i++) {
				View pTmpView = m_pInFlater.inflate(R.layout.item1_child_tab1,
						null);
				pTmpView.findViewById(R.id.no_list).setVisibility(View.INVISIBLE );
				((ViewGroup) m_pTotalView).addView(pTmpView);
			}
		}
	}

	private void showNoList() {
		if (m_pTotalView == null) {
			Debug.e(TAG, "m_pTotalView == null in showList ~!");
		} else {
			View pTmpView = m_pInFlater
					.inflate(R.layout.item1_child_tab1, null);
			pTmpView.findViewById(R.id.init_list).setVisibility(View.INVISIBLE );
			((ViewGroup) m_pTotalView).addView(pTmpView);
		}
	}

}