package org.com.lix_.ui;

import org.com.lix_.enable.EnableOfSettingRubbishClear;
import org.com.lix_.plugin.SwitchBtn;
import org.com.lix_.plugin.SwitchBtnListener;
import org.com.lix_.util.Debug;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;

/*
 * 麻痹，东西好多啊，这里面要实现功能太jb多了
 */
public class SceneOfSettingRubbishClear extends BaseActivity {

	private EnableOfSettingRubbishClear m_pEnable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_rubbishclear);
		m_pEnable = new EnableOfSettingRubbishClear(this);
		init();
	}

	@Override
	public void init() {
		boolean nFlag = checkShortCut();
		boolean nFlag1 = checkShortCut1();
		initSwitchButton(R.id.rubbish_switch_0);
		if (nFlag || nFlag1) {
			SwitchBtn.Enable(this, findViewById(R.id.rubbish_switch_0),
					R.id.rubbish_switch_tag0, R.id.rubbish_switch_tag1);
		}
	}

	private boolean checkShortCut1() {
		final ContentResolver cr = this.getContentResolver();
		final String AUTHORITY = "com.android.launcher2.settings";
		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI,
				new String[] { "title", "iconResource" }, "title=?",
				new String[] { getString(R.string.app_name) }, null);
		if (c != null && c.getCount() > 0) {
			System.out.println("已创建");
			return true;
		}
		return false;
	}

	private void initSwitchButton(int nId) {
		switch (nId) {
		case R.id.rubbish_switch_0:
			SwitchBtn.wrapBtnListener(this, findViewById(nId),
					R.id.rubbish_switch_tag0, R.id.rubbish_switch_tag1,
					getSwitchListener(nId));
			break;
		}
	}

	public boolean checkShortCut() {
		final ContentResolver cr = this.getContentResolver();
		final String AUTHORITY = "com.android.launcher.settings";
		final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = cr.query(CONTENT_URI,
				new String[] { "title", "iconResource" }, "title=?",
				new String[] { getString(R.string.app_name) }, null);
		if (c != null && c.getCount() > 0) {
			System.out.println("已创建");
			return true;
		}
		return false;
	}

	private SwitchBtnListener m_pListener = new SwitchBtnListener() {

		@Override
		public void onAnimEnd(int nTag) {
			if (nTag == 0) {
				m_pEnable.startShutcut();
			} else {
				m_pEnable.closeShutcut();
			}
		}
	};

	public SwitchBtnListener getSwitchListener(int nId) {
		switch (nId) {
		case R.id.rubbish_switch_0:
			return m_pListener;
		}
		return null;
	}

}
