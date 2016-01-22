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
 * 屏蔽 悬浮窗和通知窗口
 */
public class FragmentOfAboveshow extends BaseFragActivity {
	
	private String TAG = "FragmentOfAboveshow" ;

	@Override
	public void onResume() {
		super.onResume();
	}

	private boolean m_bInit = false;

	private boolean m_bShow = false;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		Debug.i(TAG, "setUserVisibleHint:" + isVisibleToUser);
		super.setUserVisibleHint(isVisibleToUser);
		m_bShow = isVisibleToUser;
		if (m_bShow && getView() != null) {
			if (!m_bInit) {
				Debug.i(TAG, "获取数据!");
				m_bInit = true;
				m_pEnable.init();
			}
		}
	}

	private EnableOfAboveshow m_pEnable;

	private View m_pTotalView;

	private LayoutInflater m_pInFlater;

	public FragmentOfAboveshow(List<AppInfo> list, Context pContext) {
		super();
		m_pEnable = new EnableOfAboveshow(pContext, list, m_pCallback);
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
			Debug.i(TAG, "回调数据:没有数据");
			showNoList();
			break;
		case EnableOfAboveshow.FINISH_SCAN:
			Debug.i(TAG, "回调数据:存在数据");
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
		} else {
			for (int i = 0; i < m_pEnable.getListCount(); i++) {
				View pTmpView = m_pInFlater.inflate(R.layout.item1_child_tab1,
						null);
				pTmpView.findViewById(R.id.no_list).setVisibility(
						View.INVISIBLE);
				decorateView(pTmpView,i) ;
				((ViewGroup) m_pTotalView).addView(pTmpView);
			}
		}
	}

	private void decorateView(View pTmpView, int i) {
		
	}

	private void showNoList() {
		if (m_pTotalView == null) {
		} else {
			View pTmpView = m_pInFlater
					.inflate(R.layout.item1_child_tab1, null);
			pTmpView.findViewById(R.id.init_list).setVisibility(View.INVISIBLE);
			((ViewGroup) m_pTotalView).addView(pTmpView);
		}
	}

}
