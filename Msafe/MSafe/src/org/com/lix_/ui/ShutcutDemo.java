package org.com.lix_.ui;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

public class ShutcutDemo extends Activity {

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
		Direction direction;
		// 02-01 08:44:14.989: I/System.out(1853): rect:253:0:120:103
		// 02-01 08:51:59.259: I/System.out(1899): rect:253:120:240:103

		mRelativeLayout.updateViewLayout(mShortcut, layoutParams);
		mRelativeLayout.setClickable(true);
		mRelativeLayout.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println("x:" + event.getX() + ":---y---:"
						+ event.getY());
				return false;
			}
		});
	}

}
