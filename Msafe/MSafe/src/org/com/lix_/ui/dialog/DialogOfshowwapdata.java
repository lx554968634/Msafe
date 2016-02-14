package org.com.lix_.ui.dialog;

import org.com.lix_.Define;
import org.com.lix_.plugin.shadow.ShadowViewHelper;
import org.com.lix_.ui.R;
import org.com.lix_.util.UiUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;

public class DialogOfshowwapdata extends Dialog {
	int layoutRes;// 布局文件
	Context context;

	public DialogOfshowwapdata(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 自定义布局的构造方法
	 * 
	 * @param context
	 * @param resLayout
	 */
	public DialogOfshowwapdata(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	/**
	 * 自定义主题及布局的构造方法
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public DialogOfshowwapdata(Context context, int theme, int resLayout) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);
		getWindow().setLayout(Define.WIDTH * 9 / 10, Define.HEIGHT / 2);
		getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
		setCanceledOnTouchOutside(true);
		ShadowViewHelper.bindShadowHelper(
				findViewById(R.id.pop_dialog_wapdatalog_types), context
						.getResources().getColor(R.color.shadowcolor), UiUtils
						.dipToPx(context, .5f), UiUtils.dipToPx(context, 3));
	}
}
