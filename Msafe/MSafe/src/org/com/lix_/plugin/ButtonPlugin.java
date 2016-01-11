package org.com.lix_.plugin;

import java.util.Random;

import org.com.lix_.ui.R;
import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class ButtonPlugin extends View {
	private final int INVALIDATE_DURATION = 1; // ÿ��ˢ�µ�ʱ����
	private int DIFFUSE_GAP = 10; // ��ɢ�뾶����
	private int TAP_TIMEOUT; // �жϵ���ͳ�����ʱ��
	private String TAG = "ButtonPlugin";
	public int A = 1;

	private int viewWidth; // �ؼ���Ⱥ͸߶�
	private int viewHeight;
	private int pointX; // �ؼ�ԭ�����꣨���Ͻǣ�
	private int pointY;
	private int maxRadio; // ��ɢ�����뾶
	private int shaderRadio;
	private Paint bottomPaint; // ����
	private Paint colorPaint;
	private Paint m_pTextPaint;
	private int isPushButton = 0; // ��¼�Ƿ�ť������
	private int m_nBtnType = 0; // 0 δ��ʼ����1����ʾstr��btn��2,��ʾimage��btn
	final int BTN_TXT = 1;
	final int BTN_IMG = 2;
	Bitmap m_pEnableImage;
	Bitmap m_pUnEnableImage;
	boolean m_pEnable = false;
	private Paint m_pImagePaint;
	int m_nImageSize;
	private String m_sTxt;

	public ButtonPlugin(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPaint();
		A = new Random().nextInt(10000);
		TAP_TIMEOUT = ViewConfiguration.getLongPressTimeout();
		TypedArray pTypeAttri = context.obtainStyledAttributes(attrs,
				R.styleable.ButtonPlugin);
		m_sTxt = pTypeAttri.getString(R.styleable.ButtonPlugin_title_str);
		Drawable pBgImgEnable = pTypeAttri
				.getDrawable(R.styleable.ButtonPlugin_image_ref_enabled);
		Drawable pBgImgUnEnable = pTypeAttri
				.getDrawable(R.styleable.ButtonPlugin_image_ref_unenabled);
		if (m_sTxt == null) {
			m_nBtnType = BTN_IMG;
			m_pImagePaint = new Paint();
			m_nImageSize = (int) getResources().getDimension(
					R.dimen.padding_list2);
			m_pImagePaint.setAntiAlias(true);
			Bitmap pTmp = null;
			pTmp = UiUtils.drawable2Bitmap(pBgImgEnable);
			m_pEnableImage = Bitmap.createScaledBitmap(pTmp, m_nImageSize,
					m_nImageSize, true);
			if (!pTmp.isRecycled())
				pTmp.isRecycled();
			pTmp = UiUtils.drawable2Bitmap(pBgImgUnEnable);
			m_pUnEnableImage = Bitmap.createScaledBitmap(pTmp, m_nImageSize,
					m_nImageSize, true);
			if (!pTmp.isRecycled())
				pTmp.isRecycled();
		} else {
			m_nBtnType = BTN_TXT;
		}
		pTypeAttri.recycle();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		this.viewWidth = w;
		this.viewHeight = h;
	}

	float m_nTextSize = -1;
	float m_nBaseline = -1;
	RadialGradient mRadialGradient;

	/**
	 * ��ʼ��������Դ
	 */
	private void initPaint() {
		colorPaint = new Paint();
		colorPaint.setColor(getResources().getColor(
				R.color.rubbish_clear_bg_start));
		bottomPaint = new Paint();
		colorPaint.setAntiAlias(true);
		bottomPaint.setColor(getResources().getColor(
				android.R.color.transparent));
	}

	private long downTime = 0;

	private BtnListener m_pListener;

	public void setListener(BtnListener pListener) {
		m_pListener = pListener;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// ֻ��Ҫȡһ��ʱ��
			if (isPushButton == 1)
				return true;
			if (isPushButton != 0)
				return true;
			if (downTime == 0) {
				downTime = SystemClock.elapsedRealtime();
			}
			// �������뾶
			countMaxRadio();
			if (!checkCope(event.getX()))
				return true;
			if (m_pListener != null)
				m_pListener.listener();
			isPushButton = 1;
			shaderRadio = maxRadio;
			postInvalidateDelayed(INVALIDATE_DURATION);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (isPushButton == 2)
				return true;
			if (isPushButton != 1)
				return true;
			isPushButton = 2;
			postInvalidateDelayed(INVALIDATE_DURATION);
			break;
		}
		return true;
	}

	private boolean checkCope(float nX) {
		int x = Math.abs((int) (nX - getX()));
		int nMax = viewWidth / 2 + maxRadio;
		int nMin = viewWidth / 2 - maxRadio;
		if (nMax > x && nMin < x) {
			return true;
		}
		return false;
	}

	/**
	 * �����ʱ�����뾶
	 */
	private void countMaxRadio() {
		maxRadio = viewHeight * 3 / 2 / 2;
	}

	/**
	 * ����ı�����ݣ���ʼ�����ݣ�
	 */
	private void clearData() {
		downTime = 0;
		DIFFUSE_GAP = 10;
		isPushButton = 0;
		shaderRadio = 0;
		postInvalidate();
	}

	Rect bounds = null;

	private void draw1(Canvas canvas) {
		if (m_pTextPaint == null && m_nBtnType == BTN_TXT) {
			m_nTextSize = getResources().getDimension(
					R.dimen.gird_item_up_textsize);
			m_pTextPaint = new Paint();
			m_pTextPaint.setAntiAlias(true);
			m_pTextPaint.setColor(getResources().getColor(
					R.color.green_mainlayout_background));
			m_pTextPaint.setTextSize(m_nTextSize);
			m_pTextPaint.setTextAlign(Align.LEFT);
			bounds = new Rect();
			m_pTextPaint.getTextBounds(m_sTxt, 0, m_sTxt.length(), bounds);
			FontMetricsInt fontMetrics = m_pTextPaint.getFontMetricsInt();
			m_nBaseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top)
					/ 2 - fontMetrics.top;
		}
		if (m_nBtnType == BTN_TXT) {
			canvas.drawText(m_sTxt,
					getMeasuredWidth() / 2 - bounds.width() / 2, m_nBaseline,
					m_pTextPaint);
		} else {
			if (m_pEnable) {
				canvas.drawBitmap(m_pUnEnableImage,
						(viewWidth - m_nImageSize) / 2,
						(viewHeight - m_nImageSize) / 2, m_pImagePaint);
			} else {
				canvas.drawBitmap(m_pUnEnableImage,
						(viewWidth - m_nImageSize) / 2,
						(viewHeight - m_nImageSize) / 2, m_pImagePaint);
			}
		}
		if (isPushButton == 0)
			return; // �����ťû�б������򷵻�

		// ���ư��º����������
		if (isPushButton == 1) {
			// ������ɢԲ�α���
			canvas.clipRect(pointX, pointY, pointX + viewWidth, pointY
					+ viewHeight);
			canvas.drawCircle(viewWidth / 2, viewHeight / 2, shaderRadio,
					colorPaint);
			if (m_nBtnType == BTN_TXT) {
				canvas.drawText(m_sTxt, getMeasuredWidth() / 2 - bounds.width()
						/ 2, m_nBaseline, m_pTextPaint);
			} else {
				if (m_pEnable) {
					canvas.drawBitmap(m_pEnableImage,
							(viewWidth - m_nImageSize) / 2,
							(viewHeight - m_nImageSize) / 2, m_pImagePaint);
				} else {
					canvas.drawBitmap(m_pUnEnableImage,
							(viewWidth - m_nImageSize) / 2,
							(viewHeight - m_nImageSize) / 2, m_pImagePaint);
				}
			}
		}
		if (isPushButton == 2) {
			canvas.save();
			// ������ɢԲ�α���
			canvas.clipRect(pointX, pointY, pointX + viewWidth, pointY
					+ viewHeight);
			canvas.drawCircle(viewWidth / 2, viewHeight / 2, shaderRadio,
					colorPaint);
			if (m_nBtnType == BTN_TXT) {
				canvas.drawText(m_sTxt, getMeasuredWidth() / 2 - bounds.width()
						/ 2, m_nBaseline, m_pTextPaint);
			} else {
				if (m_pEnable) {
					canvas.drawBitmap(m_pEnableImage,
							(viewWidth - m_nImageSize) / 2,
							(viewHeight - m_nImageSize) / 2, m_pImagePaint);
				} else {
					canvas.drawBitmap(m_pUnEnableImage,
							(viewWidth - m_nImageSize) / 2,
							(viewHeight - m_nImageSize) / 2, m_pImagePaint);
				}
			}
			canvas.restore();
			// ֱ���뾶�������뾶
			if (shaderRadio > 0) {
				postInvalidateDelayed(INVALIDATE_DURATION);
				shaderRadio -= DIFFUSE_GAP;
			} else {
				clearData();
			}
		}
	}

	@Override
	public void onDraw(Canvas pCanvas) {
		draw1(pCanvas);
	}
}
