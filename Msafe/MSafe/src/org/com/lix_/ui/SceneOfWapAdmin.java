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

/*
 * 组合一个工具类SceneOfWapAdmin_listview 这个类负责处理listview的逻辑
 */
public class SceneOfWapAdmin extends FragmentActivity implements
		SensorEventListener, View.OnClickListener {
	protected String TAG = "SceneOfWapAdmin";
	private SensorManager mSensorManager;
	private Sensor mSensor;
	private TextView m_pTitleTextView;
	private EnableOfWapAdmin m_pEnable;
	private SceneOfWapAdmin_listview m_pInnerView;

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
		m_pInnerView = new SceneOfWapAdmin_listview(this);
		m_pInnerView.initViewPager();
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
	protected void onResume() {
		super.onResume();
		m_nSettingClock = 0;
	}

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
