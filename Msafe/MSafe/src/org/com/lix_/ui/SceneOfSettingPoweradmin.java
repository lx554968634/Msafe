package org.com.lix_.ui;

import android.os.Bundle;
import android.view.Window;

public class SceneOfSettingPoweradmin extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poweradmin_setting);
	}

	@Override
	public void init() {
	}

}
