package org.com.lix_.enable;

import org.com.lix_.ui.R;
import org.com.lix_.ui.ShutcutDemo;

import android.content.Context;
import android.content.Intent;

public class EnableOfSettingRubbishClear extends Enable {

	public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	public static final String ACTION_UNINSTALL_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";

	public EnableOfSettingRubbishClear() {
		super();
	}

	public EnableOfSettingRubbishClear(Context pContext) {
		super(pContext);
	}

	@Override
	public void finish() {

	}

	@Override
	public void onViewClick(int nId) {

	}
	public void startShutcut() {
		Intent pIntent = getShortcutIntent();
		pIntent.setAction(ACTION_INSTALL_SHORTCUT);
		m_pContext.sendBroadcast(pIntent);
	}
	public void closeShutcut() {
		Intent pIntent = getShortcutIntent();
		pIntent.setAction(ACTION_UNINSTALL_SHORTCUT);
		m_pContext.sendBroadcast(pIntent);
	}
	private Intent getShortcutIntent() {
		Intent pTargetIntent = new Intent();
		Intent pIntent = null;
		pIntent = new Intent(m_pContext, ShutcutDemo.class);
		pIntent.setAction(m_pContext.getPackageName() + "."
				+ ShutcutDemo.class.getSimpleName());
		// pIntent.setComponent(new ComponentName(getPackageName(),
		// getPackageName() + "." + ShutcutDemo.class.getSimpleName()));
		// 设置启动的模式
		pIntent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_NEW_TASK);

		pTargetIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, pIntent);
		pTargetIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(m_pContext,
						R.drawable.icon_11));
		pTargetIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				m_pContext.getString(R.string.app_name));
		return pTargetIntent;
	}

	public void onSetTimeClearRubbish() {
		// TODO Auto-generated method stub
		
	}

	public void onStopTimeClearRubbish() {
		// TODO Auto-generated method stub
		
	}
}
