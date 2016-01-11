package org.com.lix_.plugin;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/*
 * 暂时没办法解决 一个Activity引入多个baseplugin，造成卡顿的解决办法，闲暇时间再搞
 * 还是采用VIew的方法来绘制特殊图像
 * 保留2016年1月11日13:15:17
 */
public abstract class BasePlugin extends SurfaceView implements
		SurfaceHolder.Callback {

	private SurfaceHolder holder;
	protected int m_nColor = Color.BLUE;
	private InnerThread myThread;

	public BasePlugin(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		holder = this.getHolder();
		holder.addCallback(this);
	}

	public BasePlugin(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public BasePlugin(Context context) {
		super(context);
		init();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (myThread != null)
			return;
		if (myThread == null)
			myThread = new InnerThread(holder);// 创建一个绘图线程
		myThread.isRun = true;
		myThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
	}

	public void release() {
		myThread.isRun = false;
	}

	private Paint m_pCanvasPaint;

	public abstract void drawView(Canvas pCanvas);

	protected class InnerThread extends Thread {
		private SurfaceHolder holder;
		public boolean isRun;

		public InnerThread(SurfaceHolder holder) {
			this.holder = holder;
			isRun = true;
			if (m_pCanvasPaint == null)
				m_pCanvasPaint = new Paint();
			m_pCanvasPaint.setAntiAlias(true);
		}

		@Override
		public void run() {
			while (isRun) {
				Canvas c = null;
				try {
					c = holder.lockCanvas();
					if (c == null)
						continue;
					c.drawColor(m_nColor);
					drawView(c);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (c != null) {
						holder.unlockCanvasAndPost(c);
					}
				}
			}
		}
	}

}
