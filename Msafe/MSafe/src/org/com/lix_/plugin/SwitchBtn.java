package org.com.lix_.plugin;

import org.com.lix_.ui.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

/**
 * btn view
 * 
 * @author Administrator
 *
 */
public class SwitchBtn {

	

	public static void Enable(final Context pContext, final View view,
			int rubbishSwitchTag0, int rubbishSwitchTag1,
			final SwitchBtnListener pListener) {
		if (view.findViewById(rubbishSwitchTag0).getVisibility() == View.VISIBLE) {
			// ¹Ø±Õ
			view.findViewById(rubbishSwitchTag0).setVisibility(View.INVISIBLE);
			view.findViewById(rubbishSwitchTag1).setVisibility(View.VISIBLE);
			Animation pAnim = AnimationUtils.loadAnimation(pContext,
					R.anim.slide_right);
			pAnim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					view.setBackgroundDrawable(pContext.getResources()
							.getDrawable(R.drawable.switch_blue));
					pListener.onAnimEnd(0);
				}
			});
			view.findViewById(rubbishSwitchTag1).startAnimation(pAnim);
		} else if (view.findViewById(rubbishSwitchTag1).getVisibility() == View.VISIBLE) {
			// ´ò¿ª
			view.findViewById(rubbishSwitchTag1).setVisibility(View.INVISIBLE);
			view.findViewById(rubbishSwitchTag0).setVisibility(View.VISIBLE);
			Animation pAnim = AnimationUtils.loadAnimation(pContext,
					R.anim.slide_left);
			pAnim.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					view.setBackgroundDrawable(pContext.getResources()
							.getDrawable(R.drawable.switch_gray));
					pListener.onAnimEnd(1);
				}
			});
			view.findViewById(rubbishSwitchTag0).startAnimation(pAnim);
		}
	}

	public static void wrapBtnListener(final Context pContext, final View view,
			final int rubbishSwitchTag0, final int rubbishSwitchTag1,
			final SwitchBtnListener pListener) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Enable(pContext, v, rubbishSwitchTag0, rubbishSwitchTag1,
						pListener);
			}
		});
	}

}
