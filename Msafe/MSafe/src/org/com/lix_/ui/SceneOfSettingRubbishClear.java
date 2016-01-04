package org.com.lix_.ui;

import android.os.Bundle;
import android.view.Window;
/*
 * 麻痹，东西好多啊，这里面要实现功能太jb多了
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
