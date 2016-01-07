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

public class DialogOfFileItemClick extends Dialog{

		int layoutRes;// 布局文件
		Context context;

		public DialogOfFileItemClick(Context context) {
			super(context);
			this.context = context;
		}

		/**
		 * 自定义布局的构造方法
		 * 
		 * @param context
		 * @param resLayout
		 */
		public DialogOfFileItemClick(Context context, int resLayout) {
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
		public DialogOfFileItemClick(Context context, int theme, int resLayout) {
			super(context, theme);
			this.context = context;
			this.layoutRes = resLayout;
		}

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			this.setContentView(layoutRes);
			WindowManager.LayoutParams lp= getWindow().getAttributes();
			lp.dimAmount=.1f;
			getWindow().setAttributes(lp);
			getWindow().setLayout(Define.WIDTH * 2 / 3, Define.HEIGHT / 3);
			setCanceledOnTouchOutside(true);
		}

	}
