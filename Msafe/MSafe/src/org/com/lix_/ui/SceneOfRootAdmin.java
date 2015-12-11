package org.com.lix_.ui;

import java.util.Random;
import java.util.zip.Inflater;

import android.graphics.Color;
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
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

public class SceneOfRootAdmin extends FragmentActivity {

	private static final String TAG = "SceneOfRootAdmin";
	private TabHost m_pTabHost;
	private ViewPager m_pViewPager;
	private PagerAdapter m_pPagerAdapter;
	private String[] addresses = { "first", "second", "third", "four" };

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
		m_pViewPager = (ViewPager) findViewById(R.id.viewPager1);
		m_pPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		m_pViewPager.setAdapter(m_pPagerAdapter);
		View pView = LayoutInflater.from(this).inflate(R.layout.tab_indicator,
				null);
		pView.findViewById(R.id.stoke_line_indito).setVisibility(View.VISIBLE);
		m_pTabHost = (TabHost) findViewById(android.R.id.tabhost);
		m_pTabHost.setup();
		m_pTabHost.addTab(m_pTabHost.newTabSpec("one").setIndicator(pView)
				.setContent(R.id.viewPager1));
		pView = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		m_pTabHost.addTab(m_pTabHost.newTabSpec("two").setIndicator(pView)
				.setContent(R.id.viewPager1));
		pView = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		m_pTabHost.addTab(m_pTabHost.newTabSpec("three").setIndicator(pView)
				.setContent(R.id.viewPager1));
		pView = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
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
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	private class MyPagerAdapter extends FragmentStatePagerAdapter {
		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return MyFragment.create(addresses[position]);
		}

		@Override
		public int getCount() {
			return addresses.length;
		}
	}

	public static class MyFragment extends Fragment {
		public static MyFragment create(String address) {
			MyFragment f = new MyFragment();
			Bundle b = new Bundle();
			b.putString("address", address);
			f.setArguments(b);
			return f;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Random r = new Random(System.currentTimeMillis());
			Bundle b = getArguments();
			View v = inflater.inflate(R.layout.wapadmin, null);
			v.setBackgroundColor(r.nextInt() >> 8 | 0xFF << 24);
			TextView txvAddress = (TextView) v.findViewById(R.id.wap_title_des);
			txvAddress.setTextColor(r.nextInt() >> 8 | 0xFF << 24);
			txvAddress.setBackgroundColor(r.nextInt() >> 8 | 0xFF << 24);
			txvAddress.setText("sadfasdfasd");
			return v;
		}
	}

}
