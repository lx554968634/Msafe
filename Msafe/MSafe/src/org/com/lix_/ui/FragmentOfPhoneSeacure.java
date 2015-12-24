package org.com.lix_.ui;

import java.util.List;

import org.com.lix_.enable.EnableOfRootStart;
import org.com.lix_.enable.engine.AppInfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentOfPhoneSeacure extends Fragment{
	private List<AppInfo> m_szList;

	private EnableOfRootStart m_pEnable;

	public FragmentOfPhoneSeacure(List<AppInfo> szList) {
		super();
		m_szList = szList;
		init();
	}

	private void init() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tabchild_root_phoneseacure, null);
		return v;
	}
}
