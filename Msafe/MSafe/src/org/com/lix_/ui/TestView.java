package org.com.lix_.ui;

import java.util.Currency;

import org.com.lix_.db.dao.WapDaoImpl;
import org.com.lix_.db.entity.TmpWapEntity;
import org.com.lix_.util.Debug;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class TestView extends FragmentActivity {
	private static final String TAG = "TestView";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
				WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
		setContentView(R.layout.testview);
		WapDaoImpl pDap = new WapDaoImpl(this, TmpWapEntity.class);
		pDap.createTable();
		TmpWapEntity p = new TmpWapEntity();
		p.setStatus(1);
		p.setTimesmt(System.currentTimeMillis());
		pDap.insert(p);
	}
}
