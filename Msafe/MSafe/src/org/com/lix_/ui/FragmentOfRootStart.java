package org.com.lix_.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfRootStart;
import org.com.lix_.enable.engine.AppInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Debug.i(TAG, "onResume:" + m_bShow);
		Debug.i(TAG, "onResume:" + m_bInit);
		if (m_bShow) {
			if (m_pTotalView != null
					&& View.VISIBLE == m_pLoadTxt.getVisibility() && !m_bInit) {
				m_bInit = true;
				m_pEnable.init();
			}
		}
	}

	private EnableOfRootStart m_pEnable;

	private RootStartCallback m_pCallback;
	private Context m_pContext;
	private View m_pViewTitle;
	private View m_pViewContent;
	private LayoutInflater m_pInflater;

	public FragmentOfRootStart(Context pContext) {
		super();
		m_pContext = pContext;
		m_pInflater = LayoutInflater.from(pContext);
	}

	public FragmentOfRootStart(SceneOfRootAdmin sceneOfRootAdmin,
			List<AppInfo> installedAppInfo,
			List<RunningServiceInfo> runningServers) {
		m_pContext = sceneOfRootAdmin;
		m_pCallback = new RootStartCallback();
		m_pEnable = new EnableOfRootStart(sceneOfRootAdmin, m_pCallback,
				installedAppInfo, runningServers);
	}

	private View m_pLoadTxt;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		m_bInit = false ;
		View pView = inflater.inflate(R.layout.tabchild_rootstart, null);
		m_pTotalView = pView.findViewById(R.id.tab_child_tab);
		m_pInflater = inflater;
		m_pLoadTxt = pView.findViewById(R.id.root_start_loading);
		m_pViewTitle = inflater.inflate(R.layout.item0_child_tab0, null);
		Debug.i(TAG, "oncreate View FragmentOfRootStart : "
				+ (m_pTotalView == null) + ":" + (m_pViewTitle == null));
		return pView;
	}

	private boolean m_bInit = false;

	private boolean m_bShow = false;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		Debug.i(TAG, "setUserVisibleHint:" + isVisibleToUser);
		super.setUserVisibleHint(isVisibleToUser);
		m_bShow = isVisibleToUser;
		if (m_bShow) {
			if (m_pTotalView != null
					&& View.VISIBLE == m_pLoadTxt.getVisibility() && !m_bInit) {
				m_bInit = true;
				m_pEnable.init();
			}
		}
	}

	private View m_pTotalView;

	private void initList() {
		int nCount = 0;
		int nIndex = 0;
		m_pLoadTxt.setVisibility(View.GONE);
		// 测试添加
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
		decorateAutoStartView(m_pViewContent, nIndex);
		return m_pViewContent;
	}

	private void decorateAutoStartView(View pView, int position) {
		AppInfo pInfo = m_pEnable.getAutoStartList().get(position);
		UiUtils.setText(pView.findViewById(R.id.root_startitem_name),
				pInfo.getAppName());
		try {
			((ImageView) pView.findViewById(R.id.root_start_image_title))
					.setImageDrawable(m_pContext
							.getPackageManager()
							.getApplicationIcon(
									m_pContext
											.getPackageManager()
											.getApplicationInfo(
													pInfo.getPackageName(),
													PackageManager.GET_META_DATA)));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Debug.i(TAG, ":图片找不到" + pInfo.getPackageName());
		}
	}

	private View getAutoCloseStartView(int nIndex) {
		m_pViewContent = m_pInflater.inflate(R.layout.root_child0_item, null);
		decorateAutoCloseStartView(m_pViewContent, nIndex);
		return m_pViewContent;
	}

	private void decorateAutoCloseStartView(View pView, int nIndex) {
		AppInfo pInfo = m_pEnable.getAutoCloseStartList().get(nIndex);
		UiUtils.setText(pView.findViewById(R.id.root_startitem_name),
				pInfo.getAppName());
		try {
			((ImageView) pView.findViewById(R.id.root_start_image_title))
					.setImageDrawable(m_pContext
							.getPackageManager()
							.getApplicationIcon(
									m_pContext
											.getPackageManager()
											.getApplicationInfo(
													pInfo.getPackageName(),
													PackageManager.GET_META_DATA)));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			Debug.i(TAG, ":图片找不到" + pInfo.getPackageName());
		}
	}

	class RootStartCallback implements EnableCallback {

		@Override
		public void callback(Object... obj) {
			if (!m_bShow) {
				m_bInit = false;
				return;
			}
			int nWhat = Integer.parseInt(obj[0].toString());
			switch (nWhat) {
			case EnableOfRootStart.FINISH_DIVIDE_LIST:
				initList();
				break;
			}
		}

	}

}
