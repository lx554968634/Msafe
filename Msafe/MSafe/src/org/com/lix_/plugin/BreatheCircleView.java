package org.com.lix_.plugin;

import org.com.lix_.Define;
import org.com.lix_.enable.EnableOfMainActivity;
import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * 
 * @ClassName: BreatheCircleView
 * @Description: 模仿魅族呼吸灯效果 2015年12月5日 21:42:41 添加 雷达扫描
 * @author lix_
 * @created? true
 * @date 2015-12-3 上午10:39:31
 * 
 */
public class BreatheCircleView extends View {

	private final String TAG = "BreatheCircleView";

	private Paint m_pTextPaint;

	private Paint m_pDrawPaint;

	private Paint m_pWaterPaint;

	/*
	 * 调节这个值能 呼吸灯更缓和
	 */
	private final int FRAME_FPS = 5;

	private RadialGradient mRadialGradient;

	public BreatheCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public BreatheCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray pTypeAttri = context.obtainStyledAttributes(attrs,
				R.styleable.BreatheCircleView);
		int nTextColor = pTypeAttri.getColor(
				R.styleable.BreatheCircleView_textColor, 0xffffff);
		m_pTextPaint = new Paint();
		m_pTextPaint.setColor(nTextColor);
		int nDrawColor = pTypeAttri.getColor(
				R.styleable.BreatheCircleView_backgroundcolor,
				R.color.green_breathecircleview_background);
		m_pDrawPaint = new Paint();
		m_pDrawPaint.setColor(nDrawColor);
		m_pDrawPaint.setStyle(Style.FILL);
		m_pDrawPaint.setAntiAlias(true); // 消除锯齿
		m_pWaterPaint = new Paint();
		m_pWaterPaint.setColor(getResources().getColor(
				R.color.green_breathecircleview_background));
		m_pWaterPaint.setAntiAlias(true);
		m_pWaterPaint.setAlpha(400);
		pTypeAttri.recycle();
	}

	private int m_nCircleX;
	private int m_nCircleY;
	private int m_pRadioLen;
	private int m_nWaterFrameIndex;
	private long m_nInnerCurrentTime;
	private long m_nSectorCurrentTime;
	private int m_nAngle;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		m_nCircleX = Define.WIDTH / 2;
		m_nCircleY = Define.HEIGHT / 4;
		m_pRadioLen = m_nCircleX > m_nCircleY ? m_nCircleY : m_nCircleX;
		m_nCircleX = Define.WIDTH / 2;
		m_nCircleY = Define.HEIGHT / 4 ;
	}

	private void initRadiaGradient() {
		mRadialGradient = new RadialGradient(m_nCircleX, m_nCircleY,
				m_pRadioLen, new int[] {
						getResources().getColor(
								R.color.green_breathecircleview_background),
						getResources().getColor(
								R.color.green_breathecircleview_background) },
				null, Shader.TileMode.REPEAT);
	}

	private void drawSector(Canvas pCanvas, float radius, float nStart,
			float nEnd) {
		RectF pRect = new RectF(m_nCircleX - radius, m_nCircleY - radius,
				m_nCircleX + radius, m_nCircleY + radius);
		if (nStart >= 530) {
			m_nOffAngle = -2;
		}
		if (nStart <= 280) {
			m_nOffAngle = 2;
		}
		pCanvas.drawArc(pRect, nStart, nEnd, true, m_pWaterPaint);
	}

	private int m_nOffAngle = 1;

	public boolean m_nShowRadia = true;

	public boolean m_nShowSelect = false;

	public boolean m_nShowCheck = false;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (m_pDrawPaint != null) {
			if (mRadialGradient == null && m_pRadioLen > 0) {
				initRadiaGradient();
			}
			if (mRadialGradient != null && m_nShowRadia) {
				changeRadiaStatusTime();
				drawRadiaGradient(canvas);
			}
			changeAngleStatusTime();
			if (m_nShowSelect)
				drawSector(canvas, m_pRadioLen / 100 * 60, 270 + m_nAngle,
						90 + m_nAngle);
			if (m_nShowCheck) {
				drawCheckGradient(canvas);
			}
			// 核心的圆
			canvas.drawCircle(m_nCircleX, m_nCircleY, m_pRadioLen / 100 * 55,
					m_pDrawPaint);

		}
		postInvalidateDelayed(FRAME_FPS);
	}

	private void drawCheckGradient(Canvas pCanvas) {
		m_pWaterPaint.setShader(mRadialGradient);

		pCanvas.drawCircle(m_nCircleX, m_nCircleY, m_pRadioLen / 100 * 50
				+ (m_pRadioLen * 10000 / 100 * 15) / 30 * 22 / 10000,
				m_pWaterPaint);
	}

	private void drawRadiaGradient(Canvas pCanvas) {
		m_pWaterPaint.setShader(mRadialGradient);

		pCanvas.drawCircle(m_nCircleX, m_nCircleY, m_pRadioLen / 100 * 53
				+ (m_pRadioLen * 10000 / 100 * 15) / 40 * m_nWaterFrameIndex
				/ 10000, m_pWaterPaint);
	}

	private int m_nExt = 0;

	/*
	 * 1、扩散到最大边境 2、渐变apha
	 */
	private void changeRadiaStatusTime() {
		if (m_nExt == 0) {
			if (m_nWaterFrameIndex == 0)
				m_nExt = 1;
		}
		if (m_nWaterFrameIndex > 40) {
			m_nExt = -2;
		} else if (m_nWaterFrameIndex < 0) {
			m_nExt = 1;
		}
		if (m_nInnerCurrentTime == 0)
			m_nInnerCurrentTime = System.currentTimeMillis();
		if (System.currentTimeMillis() - m_nInnerCurrentTime >= 15) {
			m_nInnerCurrentTime = 0;
			m_nWaterFrameIndex += m_nExt;
		}
	}

	private void changeAngleStatusTime() {
		m_nAngle = (m_nAngle += m_nOffAngle) % 360;
	}

	public void overCheck() {
		m_nShowRadia = false;
		m_nShowSelect = false;
		m_nShowCheck = true;
	}


	public void startCheck() {
		m_nShowRadia = false;
		m_nShowSelect = true;
		m_nShowCheck = false;
	}
}
