package org.com.lix_.ui;

import java.util.List;

import org.com.lix_.enable.EnableOfPhonePermission;
import org.com.lix_.enable.EnableOfPhoneSeacure;
import org.com.lix_.enable.EnableOfRootStart;
import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentOfPhonePermission extends BaseFragActivity{

	private EnableOfPhonePermission m_pEnable;
	private Context m_pContext ;

	public FragmentOfPhonePermission(Context pContext) {
		super();
		m_pContext = pContext ;
	}

	private View m_pView ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tabchild_phonepermission, null);
		m_pView = v ;
		return v;
	}

	@Override
	public void doCallback(Object... szObj) {
		int nTag = Integer.parseInt(szObj[0].toString()) ;
		switch(nTag)
		{
		case EnableOfPhonePermission.SCAN_OVER:
			m_pView.findViewById(R.id.des).setVisibility(View.INVISIBLE);
			m_pView.findViewById(R.id.total).setVisibility(View.VISIBLE);
			break ;
		case EnableOfPhonePermission.LIST_NONE:
			m_pView.findViewById(R.id.des).setVisibility(View.VISIBLE);
			UiUtils.setText(m_pView, R.id.des, m_pContext.getResources().getString(R.string.tab_child0_root_));
			m_pView.findViewById(R.id.total).setVisibility(View.INVISIBLE);
			break ;
		}
	}

	public void setData(List<AppInfo> installedAppInfo) {
		m_pEnable = new EnableOfPhonePermission(m_pContext, m_pCallback) ;		
	}
}
