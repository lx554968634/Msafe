package org.com.lix_.ui;

import java.util.List;

import org.com.lix_.enable.EnableOfPhonePermission;
import org.com.lix_.enable.EnableOfPhoneSeacure;
import org.com.lix_.enable.EnableOfRootStart;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentOfPhonePermission extends BaseFragActivity {

	private String TAG = "FragmentOfPhonePermission";

	private EnableOfPhonePermission m_pEnable;
	private Context m_pContext;
	private boolean m_bInit = false;

	private boolean m_bShow = false;

	@Override
	public void onResume() {
		super.onResume();
		Debug.i(TAG, "onResume:" + m_bShow);
		Debug.i(TAG, "onResume:" + m_bInit);
		if (m_bShow && m_pView != null) {
			if (!m_bInit) {
				m_bInit = true;
				m_pEnable.init();
			}
		}
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		Debug.i(TAG, "setUserVisibleHint:" + isVisibleToUser);
		super.setUserVisibleHint(isVisibleToUser);
		m_bShow = isVisibleToUser;
		if (m_bShow && m_pView != null) {
			if (!m_bInit) {
				m_bInit = true;
				m_pEnable.init();
			}
		}
	}

	public FragmentOfPhonePermission(Context pContext, List<AppInfo> list) {
		super();
		m_pContext = pContext;
		m_pEnable = new EnableOfPhonePermission(m_pContext, list, m_pCallback);
	}

	private View m_pView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		m_bInit = false ;
		View v = inflater.inflate(R.layout.tabchild_phonepermission, null);
		m_pView = v;
		return v;
	}

	@Override
	public void doCallback(Object... szObj) {
		if (!m_bShow) {
			m_bInit = false;
			return;
		}
		int nTag = Integer.parseInt(szObj[0].toString());
		switch (nTag) {
		case EnableOfPhonePermission.SCAN_OVER:
			m_pView.findViewById(R.id.des).setVisibility(View.GONE);
			m_pView.findViewById(R.id.total).setVisibility(View.VISIBLE);
			break;
		case EnableOfPhonePermission.LIST_NONE:
			m_pView.findViewById(R.id.des).setVisibility(View.VISIBLE);
			UiUtils.setText(m_pView, R.id.des, m_pContext.getResources()
					.getString(R.string.tab_child0_root_));
			m_pView.findViewById(R.id.total).setVisibility(View.INVISIBLE);
			break;
		}
	}
}
