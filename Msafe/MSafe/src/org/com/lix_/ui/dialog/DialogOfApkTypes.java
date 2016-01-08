package org.com.lix_.ui.dialog;

import org.com.lix_.Define;
import org.com.lix_.plugin.shadow.ShadowViewHelper;
import org.com.lix_.ui.R;
import org.com.lix_.util.UiUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;

public class DialogOfApkTypes extends Dialog {

	int layoutRes;// �����ļ�
	Context context;

	public DialogOfApkTypes(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * �Զ��岼�ֵĹ��췽��
	 * 
	 * @param context
	 * @param resLayout
	 */
	public DialogOfApkTypes(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	/**
	 * �Զ������⼰���ֵĹ��췽��
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public DialogOfApkTypes(Context context, int theme, int resLayout) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = 0f;
		getWindow().setAttributes(lp);
		getWindow().setLayout(Define.WIDTH * 2 / 3, Define.HEIGHT / 5);
		getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
		setCanceledOnTouchOutside(true);
		ShadowViewHelper.bindShadowHelper(
				findViewById(R.id.pop_dialog_fileadmin_types), context
						.getResources().getColor(R.color.shadowcolor), UiUtils
						.dipToPx(context, .5f), UiUtils.dipToPx(context, 3));
	}

}