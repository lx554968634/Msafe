package org.com.lix_.ui;

import java.util.List;

import org.com.lix_.enable.EnableOfAboveshow;
import org.com.lix_.enable.EnableOfRootStart;
import org.com.lix_.enable.engine.AppInfo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentOfAboveshow extends Fragment {
	private EnableOfAboveshow m_pEnable;

	private Context m_pContext;

	public FragmentOfAboveshow(List<AppInfo> szList, Context pContext) {
		super();
		m_pContext = pContext;
		m_pEnable = new EnableOfAboveshow(m_pContext,szList);
		init();
	}

	private void init() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.tabchild_root_notification, null);
		return v;
	}

}
