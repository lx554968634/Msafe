package org.com.lix_.ui;

import org.com.lix_.enable.EnableOfSettingRubbishClear;
import org.com.lix_.plugin.SwitchBtn;
import org.com.lix_.plugin.SwitchBtnListener;

import android.os.Bundle;
import android.view.Window;
/*
 * 麻痹，东西好多啊，这里面要实现功能太jb多了
 */
public class SceneOfSettingRubbishClear extends BaseActivity{
	
	private EnableOfSettingRubbishClear m_pEnable ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_rubbishclear);
		m_pEnable = new EnableOfSettingRubbishClear(this) ;
		init();
	}
	
	@Override
	public void init() {
		initSwitchButton(R.id.rubbish_switch_0) ;
	}

	private void initSwitchButton(int nId) {
		switch(nId)
		{
		case R.id.rubbish_switch_0:
			SwitchBtn.wrapBtnListener(this,findViewById(nId),R.id.rubbish_switch_tag0,R.id.rubbish_switch_tag1,getSwitchListener(nId)); 
			break ;
		}
	}
	
	private SwitchBtnListener m_pListener = new SwitchBtnListener() {
		
		@Override
		public void onAnimEnd(int nTag) {
			if(nTag == 0)
			{
				m_pEnable.startShutcut() ;
			}else
			{
				m_pEnable.closeShutcut() ;
			}
		}
	};
	
	public SwitchBtnListener getSwitchListener(int nId)
	{
		switch(nId)
		{
		case R.id.rubbish_switch_0:
			return m_pListener;
		}
		return null ;
	}

}
