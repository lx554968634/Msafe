package org.com.lix_.ui;

import org.com.lix_.Define;
import org.com.lix_.plugin.Rect;
import org.com.lix_.ui.dialog.DialogOfshowwapdata;
import org.com.lix_.util.Debug;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager.LayoutParams;
import android.view.Window;
import android.widget.ProgressBar;

public class SceneOfSettingWapAdmin extends BaseActivity {

	@Override
	public void onClick(View pView) {
		super.onClick(pView);
		int nId = pView.getId();
		switch (nId) {
		case R.id.settingwapadmin_totalwapdata:
			getPopFileTypeRect(Define.WIDTH / 10, Define.HEIGHT / 4 ,
					pView.getMeasuredWidth(), pView.getMeasuredHeight());
			showwaptotaldata();
			break;
		}
	}

	private Rect m_pFileTypePopRect;

	private void getPopFileTypeRect(float nX, float nY, int nWidth, int nHeight) {
		m_pFileTypePopRect = new Rect(nX, nY, nWidth, nHeight);
	}

	private DialogOfshowwapdata m_pShowwapdatadialog;

	private void showwaptotaldata() {
		if (m_pShowwapdatadialog != null)
			m_pShowwapdatadialog.show();
		else {
			m_pShowwapdatadialog = new DialogOfshowwapdata(this,
					R.style.noshade_dialog, R.layout.dialog_settinglogwapdata);
			Window pWin = m_pShowwapdatadialog.getWindow();
			LayoutParams pParam = pWin.getAttributes();
			pParam.x = (int) m_pFileTypePopRect.m_nX;
			pParam.width = m_pFileTypePopRect.m_nWidth / 2;
			pParam.height = m_pFileTypePopRect.m_nHeight;
			pParam.y = (int) (m_pFileTypePopRect.m_nY
					- getResources().getDimension(R.dimen.padding_list) + m_pFileTypePopRect.m_nHeight);
			m_pShowwapdatadialog.getWindow().setAttributes(pParam);
			m_pShowwapdatadialog.show();
		}
	}

	private String TAG = "SceneOfSettingWapAdmin";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_wapadmin);
		init();
	}

	private View m_pToggleView;

	private int m_nTotalWidth;

	private int m_nOriginX;

	@Override
	public void init() {
		m_pToggleView = findViewById(R.id.setting_wap_limit_btn);
		findViewById(R.id.settingwapadmin_totalwapdata).setClickable(true);
		findViewById(R.id.settingwapadmin_totalwapdata)
				.setOnClickListener(this);
		final View pView = findViewById(R.id.setting_wap_limit);
		pView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						m_nTotalWidth = pView.getWidth();
						m_nOriginX = (int) pView.getX();
						m_pToggleView.setClickable(true);
						initDragProgress(pView, R.id.setting_wap_progress,
								R.id.setting_wap_limit_btn);
					}
				});
	}

	private void initDragProgress(View pView, int nProgressId, int nToggleId) {
		Debug.i(TAG, "¼ÓÈë¼àÌý");
		findViewById(nProgressId).setOnTouchListener(m_pToggleClickListener);
		findViewById(nToggleId).setOnTouchListener(m_pToggleListener);
	}

	OnTouchListener m_pToggleClickListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int nAction = event.getAction();
			switch (nAction) {
			case MotionEvent.ACTION_DOWN:
				int nValue = 0;
				if (event.getRawX() > m_nTotalWidth) {
					nValue = m_nTotalWidth - m_nOriginX;
				} else if (event.getRawX() < m_nOriginX) {
					nValue = 0;
				} else {
					nValue = (int) (event.getRawX() - m_nOriginX);
				}
				m_pToggleView.setX(nValue);
				fixProgress(nValue);
				break;
			}
			return true;
		}
	};
	OnTouchListener m_pToggleListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int nAction = event.getAction();
			switch (nAction) {
			case MotionEvent.ACTION_UP:
				break;
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_MOVE:
				int nValue = 0;
				if (event.getRawX() > m_nTotalWidth) {
					nValue = m_nTotalWidth - m_nOriginX;
				} else if (event.getRawX() < m_nOriginX) {
					nValue = 0;
				} else {
					nValue = (int) (event.getRawX() - m_nOriginX);
				}
				m_pToggleView.setX(nValue);
				fixProgress(nValue);
				break;
			}
			return true;
		}
	};

	protected void fixProgress(int nValue) {
		int nTmp = (nValue + 5) * 100 / m_nTotalWidth;
		((ProgressBar) findViewById(R.id.setting_wap_progress))
				.setProgress(nTmp);
	}
}
