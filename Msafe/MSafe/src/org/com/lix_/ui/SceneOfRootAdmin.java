package org.com.lix_.ui;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfRootAdmin;
import org.com.lix_.util.Debug;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.FrameLayout;
import android.widget.TabWidget;
import android.widget.TextView;

public class SceneOfRootAdmin extends FragmentActivity {

	private static final String TAG = "SceneOfRootAdmin";
	private TabHost m_pTabHost;
	private ViewPager m_pViewPager;
	private PagerAdapter m_pPagerAdapter;
	private String[] addresses = { "first", "second", "third", "four" };

	private CallbackOfRootadmin m_pCallback;

	private EnableOfRootAdmin m_pTotalEnable;

	@Override
	protected void onResume() {
		super.onResume();
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(1);
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(0);
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rootadmin);
		init();
	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return getFragment(position);
		}

		@Override
		public int getCount() {
			return addresses.length;
		}
	}

	public void init() {
		m_pViewPager = (ViewPager) findViewById(R.id.viewPager1);
		View pView = LayoutInflater.from(this).inflate(R.layout.tab_indicator,
				null);
		pView.findViewById(R.id.stoke_line_indito).setVisibility(View.VISIBLE);
		m_pTabHost = (TabHost) findViewById(android.R.id.tabhost);
		m_pTabHost.setup();
		m_pTabHost.addTab(m_pTabHost.newTabSpec("one").setIndicator(pView)
				.setContent(R.id.viewPager1));
		pView = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		((TextView) pView.findViewById(R.id.title)).setText(getResources()
				.getString(R.string.rootamin_title1));
		m_pTabHost.addTab(m_pTabHost.newTabSpec("two").setIndicator(pView)
				.setContent(R.id.viewPager1));
		pView = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		((TextView) pView.findViewById(R.id.title)).setText(getResources()
				.getString(R.string.rootamin_title2));
		m_pTabHost.addTab(m_pTabHost.newTabSpec("three").setIndicator(pView)
				.setContent(R.id.viewPager1));
		pView = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		((TextView) pView.findViewById(R.id.title)).setText(getResources()
				.getString(R.string.rootamin_title3));
		m_pTabHost.addTab(m_pTabHost.newTabSpec("four").setIndicator(pView)
				.setContent(R.id.viewPager1));
		final TabWidget tabWidget = m_pTabHost.getTabWidget();
		final int count = tabWidget.getChildCount();
		for (int i = 0; i != count; i++) {
			final int index = i;
			tabWidget.getChildAt(i).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					for (int j = 0; j < count; j++) {
						tabWidget.getChildAt(j)
								.findViewById(R.id.stoke_line_indito)
								.setVisibility(View.INVISIBLE);
					}
					m_pTabHost.setCurrentTab(index);
					m_pTabHost.getCurrentTabView()
							.findViewById(R.id.stoke_line_indito)
							.setVisibility(View.VISIBLE);
					m_pViewPager.setCurrentItem(index);
				}
			});
		}
		m_pTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				for (int j = 0; j < count; j++) {
					tabWidget.getChildAt(j)
							.findViewById(R.id.stoke_line_indito)
							.setVisibility(View.INVISIBLE);
				}
				int index = 0;
				if (tabId.equals("one")) {
					index = 0;
					m_pViewPager.setCurrentItem(0);
				} else if (tabId.equals("two")) {
					index = 1;
					m_pViewPager.setCurrentItem(1);
				} else if (tabId.equals("three")) {
					index = 2;
					m_pViewPager.setCurrentItem(2);
				} else {
					index = 3;
					m_pViewPager.setCurrentItem(3);
				}
				m_pTabHost.setCurrentTab(index);
				m_pViewPager.setCurrentItem(index);
				m_pTabHost.getCurrentTabView()
						.findViewById(R.id.stoke_line_indito)
						.setVisibility(View.VISIBLE);
			}
		});
		m_pViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				m_pTabHost.setCurrentTab(arg0);
				Debug.e(TAG, "当前点击选项--:" + arg0);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

		m_pCallback = new CallbackOfRootadmin();
		m_pTotalEnable = new EnableOfRootAdmin(this, m_pCallback);
	}

	private FragmentOfRootStart m_pFragmentOfRootStart;

	private FragmentOfAboveshow m_pFragmentOfAboveshow;

	private FragmentOfPhoneSeacure m_pFragmentOfPhoneSeacure;

	private FragmentOfPhonePermission m_pFragmentOfPhonePermission;

	public Fragment getFragment(int position) {
		switch (position) {
		case 0:
			if (m_pFragmentOfRootStart == null)
				m_pFragmentOfRootStart = new FragmentOfRootStart(
						m_pTotalEnable.getRunningServers(),
						SceneOfRootAdmin.this);
			return m_pFragmentOfRootStart;
		case 1:
			if (m_pFragmentOfAboveshow == null)
				m_pFragmentOfAboveshow = new FragmentOfAboveshow(
						m_pTotalEnable.getInstalledAppInfo(),
						SceneOfRootAdmin.this);
			return m_pFragmentOfAboveshow;
		case 2:
			if (m_pFragmentOfPhoneSeacure == null)
				m_pFragmentOfPhoneSeacure = new FragmentOfPhoneSeacure(m_pTotalEnable.getInstalledAppInfo(),SceneOfRootAdmin.this);
			return m_pFragmentOfPhoneSeacure;
		case 3:
			if (m_pFragmentOfPhonePermission == null)
				m_pFragmentOfPhonePermission = new FragmentOfPhonePermission(
						m_pTotalEnable.getInstalledAppInfo(),SceneOfRootAdmin.this);
			return m_pFragmentOfPhonePermission;
		}
		return new FragmentOfRootStart(null, SceneOfRootAdmin.this);
	}

	class CallbackOfRootadmin implements EnableCallback {

		@Override
		public void callback(Object... obj) {
			switch (Integer.parseInt(obj[0].toString())) {
			case EnableOfRootAdmin.SCAN_FINISH:
				Debug.e(TAG, "scan finish-------");
				m_pPagerAdapter = new MyPagerAdapter(
						getSupportFragmentManager());
				findViewById(R.id.wait_for_scan).setVisibility(View.INVISIBLE);
				m_pViewPager.setAdapter(m_pPagerAdapter);
				findViewById(R.id.viewPager1).setVisibility(View.VISIBLE);
				if (m_pViewPager != null)
					m_pViewPager.setCurrentItem(1);
				if (m_pViewPager != null)
					m_pViewPager.setCurrentItem(0);
				break;
			}
		}

	}
}
