package org.com.lix_.ui;

import android.os.Bundle;
import android.view.Window;

public class SceneOfPowerAdmin extends BaseActivity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.poweradmin);
	}

	@Override
	public void init() {
		
	}

}
