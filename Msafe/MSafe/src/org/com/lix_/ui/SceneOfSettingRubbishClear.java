package org.com.lix_.ui;

import android.os.Bundle;
import android.view.Window;
/*
 * ��ԣ������öడ��������Ҫʵ�ֹ���̫jb����
 */
public class SceneOfSettingRubbishClear extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_rubbishclear);
		init();
	}
	
	@Override
	public void init() {
	}

}
