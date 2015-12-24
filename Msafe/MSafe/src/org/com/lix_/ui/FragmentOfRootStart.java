package org.com.lix_.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.com.lix_.util.Debug;

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

	private View m_pView;

	private LayoutInflater m_pInflater;

	public FragmentOfRootStart(List<RunningServiceInfo> szList, Context pContext) {
		super();
		m_pContext = pContext;
		m_pInflater = LayoutInflater.from(pContext);
		m_pCallback = new RootStartCallback();
		m_pEnable = new EnableOfRootStart(m_pContext, m_pCallback, szList);
	}

	private void init() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		m_pView = inflater.inflate(R.layout.tabchild_rootstart, null);
		m_pAutoStartListView = (ListView) m_pView
				.findViewById(R.id.autostart_list0);
		m_pAutoStartListView.setAdapter(new AutoStartApdater());
		return m_pView;
	}

	private void initList(View pView) {
		TextView pText;
//		if (m_pEnable.getAutoStartList().size() == 0) {
//			Debug.i(TAG, "能够自动开启的服务:" + m_pEnable.getAutoStartList());
//			pText = (TextView) pView.findViewById(R.id.tab_child0_tip0);
//			pText.setText(m_pContext.getResources().getString(
//					R.string.tab_child0_root_));
//		} else {
//			((TextView) pView.findViewById(R.id.tip_child0_tab))
//					.setText(m_pEnable.getAutoStartList().size()
//							+ m_pContext.getResources().getString(
//									R.string.tab_child0_tip0));
//			pView.findViewById(R.id.tab_child0_tip0).setVisibility(
//					View.INVISIBLE);
//			pView.findViewById(R.id.autostart_list0)
//					.setVisibility(View.VISIBLE);
//			m_pAutoStartListView = (ListView) pView
//					.findViewById(R.id.autostart_list0);
//			m_pAutoStartListView.setAdapter(new AutoStartApdater());
//		}
//		if (m_pEnable.getAutoCloseStartList().size() == 0) {
//			Debug.i(TAG, "关闭自动开启的服务:" + m_pEnable.getAutoCloseStartList());
//			pText = (TextView) pView.findViewById(R.id.tab_child0_tip1);
//			pText.setText(m_pContext.getResources().getString(
//					R.string.tab_child0_root_));
//		} else {
//			((TextView) pView.findViewById(R.id.tip_child1_tab))
//					.setText(m_pEnable.getAutoCloseStartList().size()
//							+ m_pContext.getResources().getString(
//									R.string.tab_child0_tip1));
//			pView.findViewById(R.id.tab_child0_tip1).setVisibility(
//					View.INVISIBLE);
//			pView.findViewById(R.id.nowaystart_list1).setVisibility(
//					View.VISIBLE);
//			m_pAutoStartListView = (ListView) pView
//					.findViewById(R.id.nowaystart_list1);
//		}
	}

	class AutoCloseAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return m_pEnable.getAutoCloseStartList().size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Debug.i(TAG, "converview:" + (convertView == null));
			try {
				if (convertView == null) {
					convertView = m_pInflater.inflate(
							R.layout.root_child0_item, null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return convertView;
		}
	}

	class AutoStartApdater extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return m_pEnable.getAutoStartList().size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

	}

	class RootStartCallback implements EnableCallback {

		@Override
		public void callback(Object... obj) {
			int nWhat = Integer.parseInt(obj[0].toString());
			switch (nWhat) {
			case EnableOfRootStart.FINISH_DIVIDE_LIST:
				initList(m_pView);
				Debug.i(TAG, "结束分割list!~~~");
				break;
			}
		}

	}

}
