package com.luxg.course.widget.basewise;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * toast 复写类，多次点击不会重复长时间显示
 *
 * @author chenming.ma
 */
public class NewToast {

	protected static Toast toast = null;

	public static void makeText(Context context, int resId) {
		makeText(context, context.getString(resId), Toast.LENGTH_SHORT);
	}

	public static void makeText(Context context, String str) {
		makeText(context, str, Toast.LENGTH_SHORT);
	}
	/**
	 * 文字的弹框 *
	 */
	public static void makeText(Context context, String s, int duration) {
		try {
			if (null == context || null == s) {
				return;
			}

			if (toast == null) {
				toast = Toast.makeText(context, s, duration);
			}
			toast.setText(s);
//			toast.setGravity(Gravity.CENTER, 0, 0);
//			toast.setView(v);
			toast.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 * 带菊花的对话框 *
	 */
	public static Dialog makeDialog(final Context context, String msg) {
		return makeDialog(context, msg, false, null);
	}

	/**
	 * 带菊花的对话框 *
	 */
	public static Dialog makeDialog(final Context context, String msg, boolean cancelable, DialogInterface.OnCancelListener listener) {
		final Dialog loadingDialog = ProgressDialog.show(context, "", TextUtils.isEmpty(msg) ? "正在加载，请稍候..." : msg);
		loadingDialog.setCancelable(cancelable);
		loadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialogInterface) {
				try {
					if (loadingDialog != null && loadingDialog.isShowing() && !((Activity) context).isFinishing()) {
						loadingDialog.dismiss();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialogInterface) {
				try {
					if (loadingDialog != null && loadingDialog.isShowing() && !((Activity) context).isFinishing()) {
						loadingDialog.dismiss();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		if (listener != null){
			loadingDialog.setOnCancelListener(listener);
		}
		return loadingDialog;
	}

	private static OnRefreshClickListener OnRefreshClickListener;
	public interface OnRefreshClickListener {
		void refreshClick();
	}
}
