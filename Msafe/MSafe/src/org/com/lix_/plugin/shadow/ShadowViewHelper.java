package org.com.lix_.plugin.shadow;

import org.com.lix_.util.Debug;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

public class ShadowViewHelper {
	private ShadowProperty shadowProperty;
	private View view;
	private ShadowViewDrawable shadowViewDrawable;
	private ShadowViewDrawable shadowViewDrawableHighlighted;
	private int color;
	private int highlightedColor;
	private float rx;
	private float ry;
	private StateListDrawable stateListDrawable;

	public static ShadowViewHelper bindShadowHelper(
			ShadowProperty shadowProperty, View view) {
		return new ShadowViewHelper(shadowProperty, view, Color.WHITE,
				Color.WHITE, 0, 0);
	}

	public static ShadowViewHelper bindShadowHelper(
			ShadowProperty shadowProperty, View view, int color) {
		return new ShadowViewHelper(shadowProperty, view, color, color, 0, 0);
	}

	public static ShadowViewHelper bindShadowHelper(
			ShadowProperty shadowProperty, View view, int color,
			int highlightedColor) {
		return new ShadowViewHelper(shadowProperty, view, color,
				highlightedColor, 0, 0);
	}

	public static ShadowViewHelper bindShadowHelper(
			ShadowProperty shadowProperty, View view, float rx, float ry) {
		return new ShadowViewHelper(shadowProperty, view, Color.WHITE,
				Color.WHITE, rx, ry);
	}
	
	private static String TAG = "shadowviewhelper" ;
	public static ShadowViewHelper bindShadowHelper(
			 View view,	int nColor, int colDy,int nRadius) {
		Debug.i(TAG, "view == null:"+(view == null));
		return ShadowViewHelper.bindShadowHelper(
			    new ShadowProperty()
			        .setShadowColor(nColor)
			        .setShadowDy(colDy)
			        .setShadowRadius(nRadius)
			    , view);
	}

	public static ShadowViewHelper bindShadowHelper(
			ShadowProperty shadowProperty, View view, int color, float rx,
			float ry) {
		return new ShadowViewHelper(shadowProperty, view, color, color, rx, ry);
	}

	public static ShadowViewHelper bindShadowHelper(
			ShadowProperty shadowProperty, View view, int color,
			int highlightedColor, float rx, float ry) {
		return new ShadowViewHelper(shadowProperty, view, color,
				highlightedColor, rx, ry);
	}

	private ShadowViewHelper(ShadowProperty shadowProperty, View view,
			int color, int highlightedColor, float rx, float ry) {
		this.shadowProperty = shadowProperty;
		this.view = view;
		this.color = color;
		this.highlightedColor = highlightedColor;
		this.rx = rx;
		this.ry = ry;
		// If default color is not the same as	 highlighted color,
		// then set background to be a StateListDrawable
		init(this.color != this.highlightedColor ? true : false);
	}
@SuppressLint("NewApi")
	private void init(boolean useStateListDrawable) {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		int shadowOffset = shadowProperty.getShadowOffset();
		view.setPadding(view.getPaddingLeft() + shadowOffset,
				view.getPaddingTop() + shadowOffset, view.getPaddingRight()
						+ shadowOffset, view.getPaddingBottom() + shadowOffset);

		shadowViewDrawable = new ShadowViewDrawable(shadowProperty, color, rx,
				ry);
		view.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						shadowViewDrawable.setBounds(0, 0,
								view.getMeasuredWidth(),
								view.getMeasuredHeight());
						if (Build.VERSION.SDK_INT >= 16) {
							view.getViewTreeObserver()
									.removeOnGlobalLayoutListener(this);
						} else {
							view.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
					}
				});

		// Create StateListDrawable with different states if highlighted color
		// is different from default colour.
		if (useStateListDrawable) {
			shadowViewDrawableHighlighted = new ShadowViewDrawable(
					shadowProperty, highlightedColor, rx, ry);
			view.getViewTreeObserver().addOnGlobalLayoutListener(
					new ViewTreeObserver.OnGlobalLayoutListener() {
						@Override
						public void onGlobalLayout() {
							shadowViewDrawableHighlighted.setBounds(0, 0,
									view.getMeasuredWidth(),
									view.getMeasuredHeight());
							if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
								view.getViewTreeObserver()
										.removeOnGlobalLayoutListener(this);
							} else {
								view.getViewTreeObserver()
										.removeGlobalOnLayoutListener(this);
							}
						}
					});

			stateListDrawable = new StateListDrawable();

			int[] stateHighlighted = new int[] { android.R.attr.state_pressed };
			Drawable highlightedDrawable = shadowViewDrawableHighlighted;
			stateListDrawable.addState(stateHighlighted, highlightedDrawable);

			int[] stateNormal = new int[] {};
			Drawable normalDrawable = shadowViewDrawable;
			stateListDrawable.addState(stateNormal, normalDrawable);
		}

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			if (stateListDrawable != null) {
				view.setBackgroundDrawable(stateListDrawable);
			} else {
				view.setBackgroundDrawable(shadowViewDrawable);
			}
		} else {
			if (stateListDrawable != null) {
				view.setBackground(stateListDrawable);
			} else {
				view.setBackground(shadowViewDrawable);
			}
		}
	}

	public ShadowViewDrawable getShadowViewDrawable() {
		return shadowViewDrawable;
	}

	public View getView() {
		return view;
	}

	public ShadowProperty getShadowProperty() {
		return shadowProperty;
	}
}