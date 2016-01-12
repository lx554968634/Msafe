package org.com.lix_.ui;

import java.util.Currency;

import org.com.lix_.db.dao.TmpRecordWapDapImpl;
import org.com.lix_.db.dao.WapDaoImpl;
import org.com.lix_.db.dao.WapRecordDaoImpl;
import org.com.lix_.db.entity.TmpRecordWapEntity;
import org.com.lix_.db.entity.TmpWapEntity;
import org.com.lix_.db.entity.WapRecordEntity;
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
		p.setNid(1);
		p.setStatus(1);
		p.setTimesmt(System.currentTimeMillis());
		pDap.insert(p);
		pDap.finish();

		WapRecordDaoImpl pWapRecordDao = new WapRecordDaoImpl(this,
				WapRecordEntity.class);
		pWapRecordDao.createTable();
		WapRecordEntity p2 = new WapRecordEntity();
		p2.setNwapdata(1000203);
		p2.setsPckname("org.com.lix_.ui");
		p2.setStatus(1);
		p2.setUid(1003);
		pWapRecordDao.insert(p2);
		pWapRecordDao.finish();

		TmpRecordWapDapImpl tmp = new TmpRecordWapDapImpl(this,
				TmpRecordWapEntity.class);
		tmp.createTable();
		TmpRecordWapEntity p3 = new TmpRecordWapEntity();
		p3.setNwapdata(1000203);
		p3.setTimesmt(System.currentTimeMillis());
		p3.setsPckname("org.com.lix_.ui");
		p3.setStatus(1);
		p3.setUid(1003);
		tmp.insert(p3);

		tmp.finish();
	}
}
