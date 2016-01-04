package org.com.lix_.ui;

import org.com.lix_.util.Debug;
import org.com.lix_.util.UiUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

public class TestView extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_item_rubbishactivity);
		findViewById(R.id.grid_rubbish_checkbox)
		.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Debug.i("cacaca", "v:"+event.getAction());
				return false;
			}
		});
		
	}
	
	public void testSwitch()
	{
findViewById(R.id.rubbish_switch_0).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(TestView.this.findViewById(R.id.rubbish_switch_tag0).getVisibility() == View.VISIBLE)
				{
					Animation pAnimation = AnimationUtils.loadAnimation(TestView.this, R.anim.slide_right) ;
					pAnimation.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
						}
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
						@Override
						public void onAnimationEnd(Animation animation) {
							TestView.this.findViewById(R.id.rubbish_switch_0).setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_blue));
						}
					});
					TestView.this.findViewById(R.id.rubbish_switch_tag0).setVisibility(View.INVISIBLE);
					TestView.this.findViewById(R.id.rubbish_switch_tag1).setAnimation(pAnimation);
					TestView.this.findViewById(R.id.rubbish_switch_tag1).setVisibility(View.VISIBLE);
				}else
				{
					TestView.this.findViewById(R.id.rubbish_switch_tag1).setVisibility(View.INVISIBLE);
					Animation pAnimation = AnimationUtils.loadAnimation(TestView.this, R.anim.slide_left);
					pAnimation.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							TestView.this.findViewById(R.id.rubbish_switch_0).setBackgroundDrawable(getResources().getDrawable(R.drawable.switch_gray));
						}
					});
					TestView.this.findViewById(R.id.rubbish_switch_tag0).setAnimation(pAnimation);
					TestView.this.findViewById(R.id.rubbish_switch_tag0).setVisibility(View.VISIBLE);
				}
			}
		});
	}

}
