package org.com.lix_.ui.dialog;

import org.com.lix_.Define;
import org.com.lix_.enable.engine.FileInfo;
import org.com.lix_.plugin.shadow.ShadowViewHelper;
import org.com.lix_.ui.R;
import org.com.lix_.util.UiUtils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.WindowManager;

public class DialogOfFileItemClick extends Dialog {

	int layoutRes;// 布局文件
	Context context;
	FileInfo m_pFileInfo;

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

	public DialogOfFileItemClick(Context context, int theme,
			int dialogFileadminItem, FileInfo pFileInfo) {
		super(context, theme);
		this.context = context;
		this.layoutRes = dialogFileadminItem;
		m_pFileInfo = pFileInfo;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.dimAmount = .1f;
		getWindow().setAttributes(lp);
		getWindow().setGravity(Gravity.CENTER);
		getWindow().setLayout(Define.WIDTH * 9 / 10, Define.HEIGHT / 2);
		setCanceledOnTouchOutside(true);
		ShadowViewHelper.bindShadowHelper(
				findViewById(R.id.pop_dialog_fileadmin_item), context
						.getResources().getColor(R.color.shadowcolor), UiUtils
						.dipToPx(context, .5f), UiUtils.dipToPx(context, 3));
		if (m_pFileInfo != null) {
			UiUtils.setText(findViewById(R.id.fileadminitemclick_name),
					m_pFileInfo.m_sFileName);
			UiUtils.setText(findViewById(R.id.fileadminitem_cache),
					m_pFileInfo.getSize());
			UiUtils.setText(findViewById(R.id.dialog_fileadmin_item_filetype),
					context.getResources().getString(R.string.source_str)+"   "
							+ m_pFileInfo.m_sType);
			UiUtils.setText(
					findViewById(R.id.dialog_fileadmin_item_modifytime),
					context.getResources().getString(R.string.modify_time_str)+"   "
							+ m_pFileInfo.m_sModifyTime);
			UiUtils.setText(findViewById(R.id.dialog_fileadmin_item_filepath),
					Html.fromHtml(UiUtils.getHtmlString(context.getResources()
							.getString(R.string.filepath_str), "#7b7b7b")
							+"   "+ UiUtils.getHtmlString(m_pFileInfo.m_sAbFilePath,
									"#41ba6b")));
		}
	}
}
