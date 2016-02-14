package org.com.lix_.ui;

import org.com.lix_.util.UiUtils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ShutcutDemo extends Activity implements View.OnClickListener {

	private static enum Direction {
		RIGHT, LEFT
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(Animation.INFINITE, Animation.INFINITE);
		setContentView(R.layout.shutcutdemo);
		if (VERSION.SDK_INT >= 19) {
			// 这样会使系统标题栏与应用黏在一起，需要重新定义位置
			// 透明状态栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// 透明导航栏
			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		RelativeLayout mRelativeLayout = (RelativeLayout) findViewById(R.id.total);
		Rect rect = getIntent().getSourceBounds();
		int width = getWindowManager().getDefaultDisplay().getWidth();
		int hight = getWindowManager().getDefaultDisplay().getHeight();
		RelativeLayout mShortcut = (RelativeLayout) findViewById(R.id.shutcutdemo_total);
		RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mShortcut
				.getLayoutParams();
		System.out.println("rect:" + rect.bottom + ":" + rect.left + ":"
				+ rect.right + ":" + rect.top);
		layoutParams.topMargin = (rect.top) + 70;
		layoutParams.leftMargin = rect.left + 50;
		layoutParams.width = 152;
		layoutParams.height = 154;
		Direction direction;
		// 02-01 08:44:14.989: I/System.out(1853): rect:253:0:120:103
		// 02-01 08:51:59.259: I/System.out(1899): rect:253:120:240:103

		mRelativeLayout.updateViewLayout(mShortcut, layoutParams);
		mRelativeLayout.setClickable(true);
		mRelativeLayout.setOnClickListener(this);
		
		m_pAnimFlag = true;
		UiUtils.setImg(findViewById(R.id.rubbishclear_shutcut_bgcircle),
				getResources().getDrawable(R.drawable.rubbishclear_bgcircle1));
		findViewById(R.id.rubbishclear_shutcut_bgcircle).startAnimation(
				AnimationUtils.loadAnimation(this, R.anim.anim_autorotate));
		startThread();
	}

	boolean m_pAnimFlag = false;

	@Override
	public void onClick(View v) {
		if (m_pAnimFlag)
			return;
		
	}

	private int m_nTime = 0;

	Handler m_pHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			m_nTime++;
			if (m_nTime == 5) {
				m_bLimit = false;
				Animation pAnim = AnimationUtils.loadAnimation(
						ShutcutDemo.this, R.anim.slide_rightup);
				pAnim.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						findViewById(R.id.rubbishclear_shutcut_obj)
								.setVisibility(View.GONE);
						findViewById(R.id.rubbish_shutcut_penqi).setVisibility(
								View.GONE);
						findViewById(R.id.rubbishclear_shutcut_bgcircle)
								.clearAnimation();
						findViewById(R.id.rubbishclear_shutcut_bgcircle)
								.setVisibility(View.GONE);
						startSedThread();
					}

				});
				findViewById(R.id.rubbishclear_shutcut_obj).startAnimation(
						pAnim);
			}
			if (m_nTime >= 5)
				return;
			int nResId = -1;
			if (m_nItem)
				nResId = R.drawable.rubbishclear_penqi0;
			else
				nResId = R.drawable.rubbishclear_penqi1;
			UiUtils.setImg(findViewById(R.id.rubbish_shutcut_penqi),
					getResources().getDrawable(nResId));
			m_nItem = !m_nItem;
		}

	};

	private int m_nTotal = 100;

	Handler m_pHandler2 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (m_nTotal < 30) {
				m_bLimit2 = false;
				startThirdThread() ;
				return;
			}
			UiUtils.setText(findViewById(R.id.rubbishclear_shutcut_txt),
					(m_nTotal -= 1) + "%");
		}
	};
	Handler m_pHandler3 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			finish(); 
		}
	};

	protected void startThirdThread() {
		Toast.makeText(this, "已经清理316M垃圾内存!", 800) .show();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				m_pHandler3.sendEmptyMessage(0);
			}
		}).start(); 
	}

	private void startSedThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (m_bLimit2) {
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					m_pHandler2.sendEmptyMessage(0);
				}
			}

		}).start();
	}

	private boolean m_bLimit2 = true;

	private boolean m_bLimit = true;

	private boolean m_nItem = false;

	private void startThread() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (m_bLimit) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					m_pHandler.sendEmptyMessage(0);
				}
			}

		}).start();
	}

}
