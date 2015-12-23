package org.com.lix_.ui;

import java.util.ArrayList;
import java.util.List;

import org.com.lix_.ui.enable.EnableOfRootStart;
import org.com.lix_.util.Debug;

import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import org.com.lix_.enable.engine.AppInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

	private List<RunningServiceInfo> m_szRunningAutoStartService;

	private List<RunningServiceInfo> m_szRunningCloseAutoStartService;
	
	private ListView m_pAutoStartListView  ;
	private ListView m_pNoStartListView ;

	private EnableOfRootStart m_pEnable;

	private Context m_pContext;

	public FragmentOfRootStart(List<RunningServiceInfo> szList, Context pContext) {
		super();
		m_pContext = pContext;
		divideList(szList);
		init();
	}

	private void init() {
		m_pEnable = new EnableOfRootStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tabchild_rootstart, null);
		initList(v);
		return v;
	}

	private void initList(View pView) {
		TextView pText ;
		if (m_szRunningAutoStartService.size() == 0) {
			pText = (TextView) pView.findViewById(R.id.tab_child0_tip0);
			pText.setText(m_pContext.getResources().getString(R.string.tab_child0_root_));
		}else
		{
			pView.findViewById(R.id.tab_child0_tip0).setVisibility(View.INVISIBLE);
			pView.findViewById(R.id.autostart_list0).setVisibility(View.VISIBLE) ;
			m_pAutoStartListView = (ListView) pView.findViewById(R.id.autostart_list0);
		}
		if (m_szRunningCloseAutoStartService.size() == 0) {
			pText = (TextView) pView.findViewById(R.id.tab_child0_tip1);
			pText.setText(m_pContext.getResources().getString(R.string.tab_child0_root_));
		}else
		{
			pView.findViewById(R.id.tab_child0_tip1).setVisibility(View.INVISIBLE);
			pView.findViewById(R.id.nowaystart_list1).setVisibility(View.VISIBLE) ;
			m_pAutoStartListView = (ListView) pView.findViewById(R.id.nowaystart_list1);
		}
	}
	
	

	private void divideList(List<RunningServiceInfo> szList) {
		int nStatue = 0;
		if (szList == null || szList.size() == 0) {
			Debug.e(TAG, "麻痹,没有正在运行的服务");
		} else {
			m_szRunningAutoStartService = new ArrayList<RunningServiceInfo>();
			m_szRunningCloseAutoStartService = new ArrayList<RunningServiceInfo>();
			for (RunningServiceInfo pInfo : szList) {
				nStatue = m_pContext.getPackageManager()
						.getComponentEnabledSetting(pInfo.service);
				switch (nStatue) {
				case PackageManager.COMPONENT_ENABLED_STATE_DEFAULT:
					m_szRunningAutoStartService.add(pInfo);
					break;
				case PackageManager.COMPONENT_ENABLED_STATE_DISABLED:
					m_szRunningCloseAutoStartService.add(pInfo);
					break;
				case PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER:
					m_szRunningCloseAutoStartService.add(pInfo);
					break;
				case PackageManager.COMPONENT_ENABLED_STATE_ENABLED:
					m_szRunningAutoStartService.add(pInfo);
					break;
				default:
					break;
				}
				Debug.i(TAG, "pInfo:" + pInfo.service.getPackageName() + ":"
						+ nStatue);
			}
		}
	}

}
