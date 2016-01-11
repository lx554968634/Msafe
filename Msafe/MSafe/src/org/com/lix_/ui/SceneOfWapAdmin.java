package org.com.lix_.ui;

import org.com.lix_.enable.EnableCallback;
import org.com.lix_.enable.EnableOfWapAdmin;
import org.com.lix_.plugin.BtnListener;
import org.com.lix_.plugin.ButtonPlugin;
import org.com.lix_.plugin.SinkingView;
import org.com.lix_.util.Debug;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class SceneOfWapAdmin extends FragmentActivity implements
		SensorEventListener, View.OnClickListener {
	private TabHost m_pTabHost;
	private ViewPager m_pViewPager;
	private PagerAdapter m_pPagerAdapter;

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
	private FragmentOfWapadmin_wlan m_pWlanFragment;

	public Fragment getFragment(int position) {
		switch (position) {
		case 0:
			if (m_pNetFragment == null)
				m_pNetFragment = new FragmentOfWapadmin_net();
			return m_pNetFragment;
		default:
			if (m_pWlanFragment == null)
				m_pWlanFragment = new FragmentOfWapadmin_wlan();
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

	@Override
	protected void onResume() {
		super.onResume();
		m_nSettingClock = 0;
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(1);
		if (m_pViewPager != null)
			m_pViewPager.setCurrentItem(0);
	}

	protected String TAG = "SceneOfWapAdmin";
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private TextView m_pTitleTextView;
	private EnableOfWapAdmin m_pEnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wapadmin);
		init();
	}

	private SinkingView mSinkingView;

	private float percent = 0;

	/**
	 * 开启硬件加速
	 */
	protected void moveQuick() {
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
	}

	protected void callback(Object... obj) {

	}

	EnableCallback m_pCallback = new EnableCallback() {
		@Override
		public void callback(Object... obj) {
			callback(obj);
		}
	};

	private void onBtnClick(int nTag) {
		switch (nTag) {
		case 0:
			if (findViewById(R.id.wap_tag0).getVisibility() == View.VISIBLE)
				return;
			findViewById(R.id.wap_tag0).setVisibility(View.VISIBLE);
			findViewById(R.id.wap_title_des).setVisibility(View.VISIBLE);
			findViewById(android.R.id.tabhost).setVisibility(View.GONE);
			break;
		case 1:
			if (findViewById(android.R.id.tabhost).getVisibility() == View.VISIBLE)
				return;
			findViewById(R.id.wap_tag0).setVisibility(View.GONE);
			findViewById(R.id.wap_title_des).setVisibility(View.GONE);
			findViewById(android.R.id.tabhost).setVisibility(View.VISIBLE);
			break;
		}
	}

	public void init() {
		moveQuick();
		initViewPager();
		m_pEnable = new EnableOfWapAdmin(this, m_pCallback);
		mSinkingView = (SinkingView) findViewById(R.id.wap_sinking);
		percent = 0.01f;
		((ButtonPlugin) findViewById(R.id.fileadmin_btn_clear0))
				.setListener(new BtnListener() {

					@Override
					public void listener() {
						onBtnClick(0);
					}
				});
		((ButtonPlugin) findViewById(R.id.fileadmin_btn_clear1))
				.setListener(new BtnListener() {

					@Override
					public void listener() {
						onBtnClick(1);
					}
				});
		m_pTitleTextView = (TextView) findViewById(R.id.wap_show_title);
		m_pTitleTextView.setOnClickListener(this);
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);// TYPE_GRAVITY
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_UI);
		mSinkingView.setPercent(percent);
	}

	private int m_nSettingClock = 0;

	@Override
	public void onClick(View v) {
		Debug.i(TAG, "点击----");
		int nId = v.getId();
		switch (nId) {
		case R.id.wap_show_title:
			Debug.i(TAG, "执行");
			if (m_nSettingClock == 1)
				return;
			m_nSettingClock = 1;
			m_pEnable.onViewClick(nId);
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			test();
			break;
		}
		return super.onTouchEvent(event);
	}

	private void test() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				percent = 0.25f;
				mSinkingView.setPercent(percent);
			}
		});
		thread.start();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/**
	 * 0 正常 1 》0 -1 《 0
	 */
	private int m_nRotateStatus = 0;

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor == null) {
			return;
		}
		if (mSinkingView != null)
			mSinkingView.onSensorChanged(event);

	}
}
