package org.com.lix_.plugin;

import org.com.lix_.Define;
import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

public class ButtonPlugin extends TextView {
	private static final int INVALIDATE_DURATION = 5; // 每次刷新的时间间隔
	private static int DIFFUSE_GAP = 10; // 扩散半径增量
	private static int TAP_TIMEOUT; // 判断点击和长按的时间

	private int viewWidth; // 控件宽度和高度
	private int viewHeight;
	private int pointX; // 控件原点坐标（左上角）
	private int pointY;
	private int maxRadio; // 扩散的最大半径
	private int shaderRadio;

	private Paint bottomPaint; // 画笔
	private Paint colorPaint;
	private Paint m_pTextPaint;

	private int isPushButton = 0; // 记录是否按钮被按下

	public ButtonPlugin(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
		TAP_TIMEOUT = ViewConfiguration.getLongPressTimeout();
	}

	float m_nTextSize = -1;
	float m_nBaseline = -1;
	RadialGradient mRadialGradient;

	/**
	 * 初始化画笔资源
	 */
	private void initPaint() {
		colorPaint = new Paint();
		colorPaint.setColor(getResources().getColor(R.color.rubbish_clear_bg_start));
		bottomPaint = new Paint();
		colorPaint.setAntiAlias(true);
		bottomPaint.setColor(getResources().getColor(
				android.R.color.transparent));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.viewWidth = w;
		this.viewHeight = h;
	}

	private long downTime = 0;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 只需要取一次时间
			if(isPushButton == 1)
				return true;
			if(isPushButton != 0)
				return true;
			if (downTime == 0) {
				downTime = SystemClock.elapsedRealtime();
			}
			// 计算最大半径
			countMaxRadio();
			if(!checkCope(event.getX()))
				return true ;
			isPushButton = 1;
			shaderRadio = maxRadio;
			postInvalidateDelayed(INVALIDATE_DURATION);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if(isPushButton == 2)
				return true ;
			if(isPushButton != 1)
				return true ;
			isPushButton = 2;
			postInvalidateDelayed(INVALIDATE_DURATION);
			break;
		}
		Debug.i("testview", "isPushButton:" + isPushButton);
		return true;
	}

	private boolean checkCope(float x) {
		int nMax = Define.WIDTH / 2 +maxRadio ;
		int nMin = Define.WIDTH / 2 - maxRadio ;
		if(nMax  > x && nMin < x)
		{
			return true ;
		}
		return false;
	}

	/**
	 * 计算此时的最大半径
	 */
	private void countMaxRadio() {
		maxRadio = viewHeight * 3 / 2 / 2;
	}

	/**
	 * 清理改变的数据（初始化数据）
	 */
	private void clearData() {
		downTime = 0;
		DIFFUSE_GAP = 10;
		isPushButton = 0;
		shaderRadio = 0;
		postInvalidate();
	}

	Rect bounds = null;

	@Override
	protected void dispatchDraw(Canvas canvas) {
		Debug.i("testview", "vvv:"+isPushButton+":"+getX() +":"+getY()+":"+shaderRadio);
		if (m_pTextPaint == null) {
			m_nTextSize = getResources().getDimension(
					R.dimen.gird_item_up_textsize);
			m_pTextPaint = new Paint();
			m_pTextPaint.setAntiAlias(true);
			m_pTextPaint.setColor(getResources().getColor(R.color.green_mainlayout_background));
			m_pTextPaint.setTextSize(m_nTextSize);
			m_pTextPaint.setTextAlign(Align.LEFT);
			bounds = new Rect();
			m_pTextPaint.getTextBounds("清理", 0, "清理".length(), bounds);
			FontMetricsInt fontMetrics = m_pTextPaint.getFontMetricsInt();
			m_nBaseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top)
					/ 2 - fontMetrics.top;
		}
		canvas.drawText("清理", getMeasuredWidth() / 2 - bounds.width() / 2,
				m_nBaseline, m_pTextPaint);
		if (isPushButton == 0)
			return; // 如果按钮没有被按下则返回

		// 绘制按下后的整个背景
		if (isPushButton == 1) {
			canvas.drawRect(pointX, pointY, pointX + viewWidth, pointY
					+ viewHeight, bottomPaint);
			canvas.save();
			// 绘制扩散圆形背景
			canvas.clipRect(pointX, pointY, pointX + viewWidth, pointY
					+ viewHeight);
			canvas.drawCircle(getX() + viewWidth / 2, getY() + viewHeight / 2,
					shaderRadio, colorPaint);
			canvas.drawText("清理", getMeasuredWidth() / 2 - bounds.width() / 2,
					m_nBaseline, m_pTextPaint);
			canvas.restore();
		}
		if(isPushButton == 2)
		{
			canvas.drawRect(pointX, pointY, pointX + viewWidth, pointY
					+ viewHeight, bottomPaint);
			canvas.save();
			// 绘制扩散圆形背景
			canvas.clipRect(pointX, pointY, pointX + viewWidth, pointY
					+ viewHeight);
			canvas.drawCircle(getX() + viewWidth / 2, getY() + viewHeight / 2,
					shaderRadio, colorPaint);
			canvas.drawText("清理", getMeasuredWidth() / 2 - bounds.width() / 2,
					m_nBaseline, m_pTextPaint);
			canvas.restore();
			// 直到半径等于最大半径
			if (shaderRadio > 0) {
				postInvalidateDelayed(INVALIDATE_DURATION, pointX, pointY, pointX
						+ viewWidth, pointY + viewHeight);
				shaderRadio -= DIFFUSE_GAP;
			} else {
				 clearData();
			}
		}
		
		super.dispatchDraw(canvas);
	}
}
