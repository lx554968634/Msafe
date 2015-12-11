package org.com.lix_.plugin;

import org.com.lix_.Define;
import org.com.lix_.ui.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 接受一个nProgress 参数进行画圆
 * 
 * @author punsher
 *
 */
public class AutodrawCircleView extends View {

	private int m_nCircleX;
	private int m_nCircleY;
	private int m_pRadioLen;
	private Paint m_pWaterPaint;
	public int m_nClearCount;

	public AutodrawCircleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AutodrawCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
		TypedArray pTypeAttri = context.obtainStyledAttributes(attrs,
				R.styleable.AutodrawCircleView);
		float nStokeCircle = (float) pTypeAttri.getDimension(
				R.styleable.AutodrawCircleView_stokeSize, 5);
		m_pWaterPaint.setStrokeWidth(nStokeCircle);
		pTypeAttri.recycle();
	}

	private void init() {
		m_nCircleX = Define.WIDTH / 2;
		m_nCircleY = Define.HEIGHT / 4;
		m_pRadioLen = m_nCircleX > m_nCircleY ? m_nCircleY : m_nCircleX;
		m_pWaterPaint = new Paint();
		m_pWaterPaint.setAntiAlias(true);
		;
		m_pWaterPaint.setStyle(Paint.Style.STROKE);
		m_pWaterPaint.setColor(getResources().getColor(R.color.white));
	}

	private void drawSector(Canvas pCanvas, float radius, float nStart,
			float nEnd, boolean bStroke) {
		RectF pRect = new RectF(m_nCircleX - radius, m_nCircleY - radius,
				m_nCircleX + radius, m_nCircleY + radius);
		pCanvas.drawArc(pRect, nStart, nEnd, bStroke, m_pWaterPaint);
	}

	private long m_nTestTime;

	private void test() {
		if(m_nClearCount == 11) 
			return ;
		if (m_nTestTime == 0)
			m_nTestTime = System.currentTimeMillis();
		if (System.currentTimeMillis() - m_nTestTime >= 10) {
			m_nClearCount++;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		test() ;
		drawSector(canvas, m_pRadioLen / 100 * 60, 135, (m_nClearCount >= 11 ? 10 : m_nClearCount) * 27, false);
		postInvalidate();
	}
}
