package org.com.lix_.plugin.shadow;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

public class ShadowViewDrawable extends Drawable {
	private Paint paint;

	private RectF bounds = new RectF();

	private int width;
	private int height;

	private ShadowProperty shadowProperty;
	private int shadowOffset;

	private RectF drawRect;

	private float rx;
	private float ry;

	public ShadowViewDrawable(ShadowProperty shadowProperty, int color,
			float rx, float ry) {
		this.shadowProperty = shadowProperty;
		shadowOffset = this.shadowProperty.getShadowOffset();

		this.rx = rx;
		this.ry = ry;

		paint = new Paint();
		paint.setAntiAlias(true);
		/**
		 * �����תʱ�ľ������
		 */
		paint.setFilterBitmap(true);
		paint.setDither(true);
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(color);
		/**
		 * ������Ӱ
		 */
		paint.setShadowLayer(shadowProperty.getShadowRadius(),
				shadowProperty.getShadowDx(), shadowProperty.getShadowDy(),
				shadowProperty.getShadowColor());

		drawRect = new RectF();
	}

	@Override
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		if (bounds.right - bounds.left > 0 && bounds.bottom - bounds.top > 0) {
			this.bounds.left = bounds.left;
			this.bounds.right = bounds.right;
			this.bounds.top = bounds.top;
			this.bounds.bottom = bounds.bottom;
			width = (int) (this.bounds.right - this.bounds.left);
			height = (int) (this.bounds.bottom - this.bounds.top);

			drawRect = new RectF(shadowOffset, shadowOffset, width
					- shadowOffset, height - shadowOffset);
			invalidateSelf();
		}
	}
	@Override
	public void draw(Canvas canvas) {
		canvas.drawRoundRect(drawRect, rx, ry, paint);
	}
	public ShadowViewDrawable setColor(int color) {
		paint.setColor(color);
		return this;
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
	}
}