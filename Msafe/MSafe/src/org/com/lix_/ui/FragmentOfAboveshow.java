package org.com.lix_.ui;

import java.util.List;

import org.com.lix_.enable.engine.AppInfo;
import org.com.lix_.ui.enable.EnableOfRootStart;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentOfAboveshow extends Fragment{
	private List<AppInfo> m_szList;

	private EnableOfRootStart m_pEnable;

	public FragmentOfAboveshow(List<AppInfo> szList) {
		super();
		m_szList = szList;
		init();
	}

	private void init() {
		m_pEnable = new EnableOfRootStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tabchild_root_notification, null);
		return v;
	}

}
