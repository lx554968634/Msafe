package org.com.lix_.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageManager;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfRootStart;
import org.com.lix_.enable.engine.AppInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/*
 * 这个其实是启动服务判断
 * 1、获取启动服务
 * 2、判断哪些是用户服务
 * 3、在这里进行设置
 *  setComponentEnabledSetting
 */
public class FragmentOfRootStart extends Fragment {

	private String TAG = "FragmentOfRootStart";

	private ListView m_pAutoStartListView;
	private ListView m_pNoStartListView;

	private EnableOfRootStart m_pEnable;

	private RootStartCallback m_pCallback;

	private Context m_pContext;

	private View m_pViewTitle;

	private View m_pViewContent;

	private LayoutInflater m_pInflater;

	public FragmentOfRootStart(List<RunningServiceInfo> szList, Context pContext) {
		super();
		m_pContext = pContext;
		m_pInflater = LayoutInflater.from(pContext);
		m_pCallback = new RootStartCallback();
		m_pEnable = new EnableOfRootStart(m_pContext, m_pCallback, szList);
	}

	private void init() {
		m_pViewTitle = m_pInflater.inflate(R.layout.item0_child_tab0, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Debug.i(TAG, "oncreate View");
		View pView = inflater.inflate(R.layout.tabchild_rootstart, null);
		m_pTotalView = pView.findViewById(R.id.tab_child_tab);
		init();
		return pView;
	}

	private View m_pTotalView;

	private void initList() {
		int nCount = 0;
		int nIndex = 0;
		//测试添加 
		if (m_pEnable.getAutoStartList() == null)
			return;
		((ViewGroup) m_pTotalView).addView(m_pViewTitle);
		if (m_pEnable.getAutoStartList().size() == 0) {
			UiUtils.setText(m_pViewTitle, R.id.tip_child0_tab, m_pContext
					.getResources().getString(R.string.tab_child0_tip00));
			UiUtils.setText(m_pViewTitle, R.id.tab_child0_tip0, m_pContext
					.getResources().getString(R.string.tab_child0_root_));
		} else {
			m_pViewTitle.findViewById(R.id.tab_child0_tip0).setVisibility(
					View.INVISIBLE);
			nCount = m_pEnable.getAutoStartList().size();
			UiUtils.setText(
					m_pViewTitle,
					R.id.tip_child0_tab,
					nCount
							+ m_pContext.getResources().getString(
									R.string.tab_child0_tip0));
			for (nIndex = 0; nIndex < nCount; nIndex++) {
				((ViewGroup) m_pTotalView).addView(getAutoStartView(nIndex));
			}
		}
		m_pViewTitle = m_pInflater.inflate(R.layout.item0_child_tab0, null);
		((ViewGroup) m_pTotalView).addView(m_pViewTitle);
		if (m_pEnable.getAutoCloseStartList().size() == 0) {
			UiUtils.setText(m_pViewTitle, R.id.tip_child0_tab, m_pContext
					.getResources().getString(R.string.tab_child0_tip11));
			UiUtils.setText(m_pViewTitle, R.id.tab_child0_tip0, m_pContext
					.getResources().getString(R.string.tab_child0_root_));
		} else {
			m_pViewTitle.findViewById(R.id.tab_child0_tip0).setVisibility(
					View.INVISIBLE);
			nCount = m_pEnable.getAutoCloseStartList().size();
			UiUtils.setText(
					m_pViewTitle,
					R.id.tip_child0_tab,
					nCount
							+ m_pContext.getResources().getString(
									R.string.tab_child0_tip1));
			for (nIndex = 0; nIndex < nCount; nIndex++) {
				((ViewGroup) m_pTotalView)
						.addView(getAutoCloseStartView(nIndex));
			}
		}
	}

	private View getAutoStartView(int nIndex) {
		m_pViewContent = m_pInflater.inflate(R.layout.root_child0_item, null);
		return m_pViewContent;
	}

	private View getAutoCloseStartView(int nIndex) {
		m_pViewContent = m_pInflater.inflate(R.layout.root_child0_item, null);
		return m_pViewContent;
	}

	class RootStartCallback implements EnableCallback {

		@Override
		public void callback(Object... obj) {
			int nWhat = Integer.parseInt(obj[0].toString());
			switch (nWhat) {
			case EnableOfRootStart.FINISH_DIVIDE_LIST:
				Debug.i(TAG, "结束分割list!~~~");
				initList();
				break;
			}
		}

	}

}
