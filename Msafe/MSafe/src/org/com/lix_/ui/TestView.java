package org.com.lix_.ui;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class TestView extends FragmentActivity {

	private static final String TAG = "TestView";
	private TabHost m_pTabHost;
	private ViewPager m_pViewPager;
	private PagerAdapter m_pPagerAdapter;
	@Override
	protected void onResume() {
		super.onResume();
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(1);
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(0);
	}
	private void initViewPager() {
		m_pViewPager = (ViewPager) findViewById(R.id.wap_viewPager);
		View pView = null;
		m_pTabHost = (TabHost) findViewById(android.R.id.tabhost);
		m_pTabHost.setup();
		pView = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		((TextView) pView.findViewById(R.id.title)).setText(getResources()
				.getString(R.string.wapadmin_title0));
		m_pTabHost.addTab(m_pTabHost.newTabSpec("one").setIndicator(pView)
				.setContent(R.id.wap_viewPager));
		pView = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
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
		m_pPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		m_pViewPager.setAdapter(m_pPagerAdapter);
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(1);
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(0);
	}

	private FragmentOfWapadmin_net m_pNetFragment;
	private FragmentOfWapadmin_wlan m_pWlanFragment ;

	public Fragment getFragment(int position) {
		switch (position) {
		case 0:
			if (m_pNetFragment == null)
				m_pNetFragment = new FragmentOfWapadmin_net();
			return m_pNetFragment;
		default:
			if(m_pWlanFragment == null)
				m_pWlanFragment=  new FragmentOfWapadmin_wlan() ;
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
	
	private final int WAP_TAB_SIZE = 2 ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.testview);
		initViewPager();
	}

	public void testOnTouchEvent() {
		findViewById(R.id.grid_rubbish_checkbox).setOnTouchListener(
				new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						Debug.i("cacaca", "v:" + event.getAction());
						return false;
					}
				});
	}

	public void testSwitch() {
		findViewById(R.id.rubbish_switch_0).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (TestView.this
								.findViewById(R.id.rubbish_switch_tag0)
								.getVisibility() == View.VISIBLE) {
							Animation pAnimation = AnimationUtils
									.loadAnimation(TestView.this,
											R.anim.slide_right);
							pAnimation
									.setAnimationListener(new AnimationListener() {
										@Override
										public void onAnimationStart(
												Animation animation) {
										}

										@Override
										public void onAnimationRepeat(
												Animation animation) {
										}

										@Override
										public void onAnimationEnd(
												Animation animation) {
											TestView.this
													.findViewById(
															R.id.rubbish_switch_0)
													.setBackgroundDrawable(
															getResources()
																	.getDrawable(
																			R.drawable.switch_blue));
										}
									});
							TestView.this
									.findViewById(R.id.rubbish_switch_tag0)
									.setVisibility(View.INVISIBLE);
							TestView.this
									.findViewById(R.id.rubbish_switch_tag1)
									.setAnimation(pAnimation);
							TestView.this
									.findViewById(R.id.rubbish_switch_tag1)
									.setVisibility(View.VISIBLE);
						} else {
							TestView.this
									.findViewById(R.id.rubbish_switch_tag1)
									.setVisibility(View.INVISIBLE);
							Animation pAnimation = AnimationUtils
									.loadAnimation(TestView.this,
											R.anim.slide_left);
							pAnimation
									.setAnimationListener(new AnimationListener() {

										@Override
										public void onAnimationStart(
												Animation animation) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onAnimationRepeat(
												Animation animation) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onAnimationEnd(
												Animation animation) {
											TestView.this
													.findViewById(
															R.id.rubbish_switch_0)
													.setBackgroundDrawable(
															getResources()
																	.getDrawable(
																			R.drawable.switch_gray));
										}
									});
							TestView.this
									.findViewById(R.id.rubbish_switch_tag0)
									.setAnimation(pAnimation);
							TestView.this
									.findViewById(R.id.rubbish_switch_tag0)
									.setVisibility(View.VISIBLE);
						}
					}
				});
	}

}
