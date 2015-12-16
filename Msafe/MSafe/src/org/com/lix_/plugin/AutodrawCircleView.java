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
	private int m_nClearCount;
	private Paint m_pInnerPaint ;
	private float m_nStokeCircle ;

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
		m_nStokeCircle = nStokeCircle ;
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
		m_pInnerPaint = new Paint () ;
		m_pInnerPaint.setColor(getResources().getColor(R.color.white));
		m_pInnerPaint.setStyle(Paint.Style.STROKE);
		m_pInnerPaint.setAntiAlias(true);
		m_nTargetNum = 0 ;
		m_nClearCount = 0 ;
		m_pWaterPaint.setColor(getResources().getColor(R.color.white));
	}

	private void drawSector(Canvas pCanvas, float radius, float nStart,
			float nEnd, boolean bStroke,Paint pPaint) {
		RectF pRect = new RectF(m_nCircleX - radius, m_nCircleY - radius,
				m_nCircleX + radius, m_nCircleY + radius);
		pCanvas.drawArc(pRect, nStart, nEnd, bStroke, pPaint);
	}

	public int m_nTargetNum ;

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if(m_nTargetNum > m_nClearCount)
		{
			m_nClearCount ++ ;
		}
		drawSector(canvas, m_pRadioLen / 100 * 60, 135,
				(m_nClearCount >= 28? 27 : m_nClearCount) * 10, false,m_pWaterPaint);
		drawSector(canvas, m_pRadioLen / 100 * 60 - m_nStokeCircle / 2, 135,
				10 * 27, false,m_pInnerPaint);
		postInvalidateDelayed(100);;
	}
}
