package org.com.lix_.ui;

import java.util.Calendar;

import org.com.lix_.Define;
import org.com.lix_.enable.EnableOfSettingRubbishClear;
import org.com.lix_.plugin.SwitchBtn;
import org.com.lix_.plugin.SwitchBtnListener;
import org.com.lix_.util.Debug;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

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
		initSwitchButton(R.id.rubbish_switch_1);
		decorateTimetextview();
		addClickListener();
		if (nFlag || nFlag1) {
			SwitchBtn.Enable(this, findViewById(R.id.rubbish_switch_0),
					R.id.rubbish_switch_tag0, R.id.rubbish_switch_tag1);
		}
	}

	private void addClickListener() {
		findViewById(R.id.setting_rubbishclear_detail_time).setOnClickListener(
				this);
		findViewById(R.id.setting_rubbish_scan_frame).setClickable(true);
		findViewById(R.id.setting_rubbish_scan_frame).setOnClickListener(this);
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
		case R.id.rubbish_switch_1:
			SwitchBtn.wrapBtnListener(this, findViewById(nId),
					R.id.rubbish_switch_tag00, R.id.rubbish_switch_tag01,
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

	/*
	 * 生成垃圾清理快捷方式
	 */
	private SwitchBtnListener m_pSwitchListener0 = new SwitchBtnListener() {

		@Override
		public void onAnimEnd(int nTag) {
			if (nTag == 0) {
				m_pEnable.startShutcut();
			} else {
				m_pEnable.closeShutcut();
			}
		}
	};
	/*
	 * 定时扫描计划
	 */
	private SwitchBtnListener m_pSwitchListener1 = new SwitchBtnListener() {

		@Override
		public void onAnimEnd(int nTag) {
			if (nTag == 0) {
				m_pEnable.onSetTimeClearRubbish();
			} else {
				m_pEnable.onStopTimeClearRubbish();
			}
		}
	};

	public SwitchBtnListener getSwitchListener(int nId) {
		switch (nId) {
		case R.id.rubbish_switch_0:
			return m_pSwitchListener0;
		case R.id.rubbish_switch_1:
			return m_pSwitchListener1;
		}
		return null;
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		int nId = v.getId();
		switch (nId) {
		case R.id.setting_rubbishclear_detail_time:
			initTimepickerDialog();
			break;
		case R.id.setting_rubbish_scan_frame:
			initScanTimeDialog();
			break;
		}
	}

	private AlertDialog m_pScantimeDialog;

	private View.OnClickListener pScantimeDialog = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Debug.i(TAG, "scan click listener : " + (m_pScantimeDialog == null));
			if (m_pScantimeDialog != null)
				m_pScantimeDialog.dismiss();
		}
	};

	private void initScanTimeDialog() {
		if (m_pScantimeDialog == null) {
			setTheme(R.style.class_dialog);
			LayoutInflater pLayoutInflater = LayoutInflater.from(this);
			View view = pLayoutInflater.inflate(
					R.layout.settingrubbish_scantime, null);
			m_pScantimeDialog = new AlertDialog.Builder(this).setView(view)
					.create();
			Window pWin = m_pScantimeDialog.getWindow();
			LayoutParams pParam = pWin.getAttributes();
			pParam.x = Define.WIDTH / 2;
			pParam.y = 0;
			pParam.width = Define.WIDTH / 3;
			pParam.height = Define.HEIGHT / 3;
			m_pScantimeDialog.getWindow().setAttributes(pParam);
		}
		m_pScantimeDialog.show();
		m_pScantimeDialog.findViewById(R.id.settingrubbish_scantime_part0)
				.setOnClickListener(pScantimeDialog);
		m_pScantimeDialog.findViewById(R.id.settingrubbish_scantime_part1)
				.setOnClickListener(pScantimeDialog);
		m_pScantimeDialog.findViewById(R.id.settingrubbish_scantime_part2)
				.setOnClickListener(pScantimeDialog);
		m_pScantimeDialog.findViewById(R.id.settingrubbish_scantime_part3)
				.setOnClickListener(pScantimeDialog);
	}

	private String decorateTime(int nTime) {
		if (nTime < 0)
			return "0" + nTime;
		return "" + nTime;
	}

	private void decorateTimetextview() {
		TextView pView = (TextView) findViewById(R.id.setting_rubbish_set_time);
		pView.setText(decorateTime(m_nHourOfDay) + ":"
				+ decorateTime(m_nMinuteOfHour));
	}

	private int m_nHourOfDay;
	private int m_nMinuteOfHour;
	private TimePickerDialog m_pTimepickerDialog;

	private void initTimepickerDialog() {
		if (m_pTimepickerDialog == null)
			m_pTimepickerDialog = new TimePickerDialog(
					SceneOfSettingRubbishClear.this, R.style.class_dialog,
					new TimePickerDialog.OnTimeSetListener() {
						@Override
						public void onTimeSet(TimePicker view, int hourOfDay,
								int minute) {
							m_nHourOfDay = hourOfDay;
							m_nMinuteOfHour = minute;
							decorateTimetextview();
						}

					}, m_nHourOfDay, m_nMinuteOfHour, true);
		m_pTimepickerDialog.setTitle("扫描时间");
		m_pTimepickerDialog.show();
	}
}
