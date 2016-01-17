package org.com.lix_.ui;

import org.com.lix_.util.Debug;

import android.content.Context;
import android.content.res.Resources;
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
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class SceneOfWapAdmin_listview {
	private String TAG = "SceneOfWapAdmin_listview";
	Context m_pContext;

	public SceneOfWapAdmin_listview(Context pContext) {
		m_pContext = pContext;
	}

	private TabHost m_pTabHost;
	private ViewPager m_pViewPager;
	private PagerAdapter m_pPagerAdapter;

	private View findViewById(int nId) {
		return ((FragmentActivity) m_pContext).findViewById(nId);
	}

	public Resources getResources() {
		return m_pContext.getResources();
	}

	public void initViewPager() {
		m_pViewPager = (ViewPager) findViewById(R.id.wap_viewPager);
		View pView = null;
		m_pTabHost = (TabHost) findViewById(android.R.id.tabhost);
		m_pTabHost.setup();
		pView = LayoutInflater.from(m_pContext).inflate(R.layout.tab_indicator,
				null);
		((TextView) pView.findViewById(R.id.title)).setText(getResources()
				.getString(R.string.wapadmin_title0));
		m_pTabHost.addTab(m_pTabHost.newTabSpec("one").setIndicator(pView)
				.setContent(R.id.wap_viewPager));
		pView = LayoutInflater.from(m_pContext).inflate(R.layout.tab_indicator,
				null);
		((TextView) pView.findViewById(R.id.title)).setText(getResources()
				.getString(R.string.wapadmin_title1));
		m_pTabHost.addTab(m_pTabHost.newTabSpec("two").setIndicator(pView)
				.setContent(R.id.wap_viewPager));
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
		m_pPagerAdapter = new MyPagerAdapter(
				((FragmentActivity) m_pContext).getSupportFragmentManager());
		m_pViewPager.setAdapter(m_pPagerAdapter);
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(1);
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(0);
	}

	private FragmentOfWapadmin_net m_pNetFragment;
	private FragmentOfWapadmin_net m_pWlanFragment;

	public Fragment getFragment(int position) {
		switch (position) {
		case 0:
			if (m_pNetFragment == null)
				m_pNetFragment = new FragmentOfWapadmin_net(1, m_pContext);
			return m_pNetFragment;
		default:
			if (m_pWlanFragment == null)
				m_pWlanFragment = new FragmentOfWapadmin_net(0, m_pContext);
			return m_pWlanFragment;
		}
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
			return WAP_TAB_SIZE;
		}
	}

	private final int WAP_TAB_SIZE = 2;

	public void onResume() {
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(1);
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(0);
	}
}
