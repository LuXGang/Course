package com.luxg.course.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.Gravity;
import android.widget.Toast;

/**
 * dialog工具类
 *
 * @author user
 */
public class DialogHelper {
	private static final int TOAST_INTERVAL = 2000;
	private static long lastTime = 0;

	/**
	 * 显示toast
	 *
	 * @param c
	 * @param hintText
	 * @param makeItAQuickOne
	 */
	public static void showHint(final Context c, String hintText,
	                            boolean makeItAQuickOne) {
		int duration = makeItAQuickOne ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG;
		Toast t = Toast.makeText(c, hintText, duration);
		t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		t.show();
	}

	/**
	 * 获取列表dialog
	 *
	 * @param c
	 * @param listener
	 * @param title
	 * @param select
	 * @return
	 */
	public static AlertDialog getListOptionsDialog(final Context c,
	                                               OnClickListener listener, String title, String[] select) {
		Builder alertBuilder = new Builder(c)
				.setTitle(title);
		alertBuilder.setItems(select, listener);
		return alertBuilder.create();
	}

	/**
	 * 获取错误dialog
	 *
	 * @param c
	 * @param listener
	 * @param error
	 * @return
	 */
	public static AlertDialog getErrorDialog(final Context c,
	                                         OnClickListener listener, String error) {
		Builder alertBuilder = new Builder(c)
				.setMessage(error).setTitle("提示")
				.setPositiveButton("确定", listener);
		return alertBuilder.create();
	}

	/**
	 * 是否退出dialog
	 *
	 * @param c
	 * @param listener
	 * @param content
	 * @return
	 */
	public static AlertDialog getConfirmExitCreate(final Context c,
	                                               OnClickListener listener, String content) {
		Builder alertBuilder = new Builder(c)
				.setMessage("提示").setMessage(content)
				.setPositiveButton("退出", listener)
				.setNegativeButton("取消", listener);

		return alertBuilder.create();
	}

	/**
	 * 显示dialog
	 *
	 * @param ctx
	 * @param msg
	 * @param listerner
	 */
	public static void showAlert(Context ctx, String msg,
	                             OnClickListener... listerner) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setTitle("提示");
		builder.setPositiveButton("取消", listerner[0]);
		builder.setNegativeButton("确定", listerner[1]);
		if (!((Activity) ctx).isFinishing()) {
			builder.show();
		}
	}

	public static AlertDialog showAlertDialog(Context ctx, String msg, OnClickListener... listerner) {
		Builder builder = new Builder(ctx);
		builder.setTitle("提示");
		builder.setMessage(msg);
		builder.setPositiveButton("确定", listerner[0]);
		if (listerner.length > 1)
			builder.setNegativeButton("取消", listerner[1]);
		else
			builder.setNegativeButton("取消", null);
		return builder.create();
	}

	/**
	 * 显示dialog
	 *
	 * @param ctx
	 * @param title
	 * @param msg
	 * @param listerner
	 */
	public static void showAlert(Context ctx, String title, String msg,
	                             OnClickListener... listerner) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton("取消", listerner[0]);
		builder.setNegativeButton("确定", listerner[1]);
		if (!((Activity) ctx).isFinishing()) {
			builder.show();
		}
	}

	/**
	 * 显示dilog
	 *
	 * @param ctx
	 * @param titleId
	 * @param msgId
	 * @param listerner
	 */
	public static void showAlert(Context ctx, int titleId, int msgId,
	                             OnClickListener... listerner) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(false);
		builder.setMessage(msgId);
		builder.setTitle(titleId);
		builder.setPositiveButton("取消", listerner[0]);
		builder.setNegativeButton("确定", listerner[1]);
		if (!((Activity) ctx).isFinishing()) {
			builder.show();
		}
	}

	/**
	 * 显示dialog
	 *
	 * @param ctx
	 * @param isTwoButton
	 * @param isCancelable
	 * @param title
	 * @param msg
	 * @param firstBtnName
	 * @param SecoundBtnName
	 * @param listerner
	 */
	public static void showAlert(Context ctx, boolean isTwoButton,
	                             boolean isCancelable, String title, String msg,
	                             String firstBtnName, String SecoundBtnName,
	                             OnClickListener... listerner) {
		Builder builder = new Builder(ctx);
		builder.setCancelable(isCancelable);
		builder.setMessage(msg);
		builder.setTitle(title);
		builder.setPositiveButton(firstBtnName, listerner[0]);
		if (isTwoButton) {
			builder.setNegativeButton(SecoundBtnName, listerner[1]);
		}
		if (!((Activity) ctx).isFinishing()) {
			builder.show();
		}
	}

	/**
	 * 显示toast
	 *
	 * @param ctx
	 * @param msg
	 */
	public static void showToast(Context ctx, String msg) {
		if (lastTime == 0) {
			makeToast(ctx, msg);
			lastTime = System.currentTimeMillis();
		}
		long thisTime = System.currentTimeMillis();
		if (thisTime - lastTime > TOAST_INTERVAL) {
			makeToast(ctx, msg);
			lastTime = thisTime;
		}
	}

	private static void makeToast(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
	}

	// 显示进度条
	public static ProgressDialog showProgressDialog(Context ctx, String title,
	                                                String message, OnClickListener listen,
	                                                String button_name) {
		ProgressDialog dialog = new ProgressDialog(ctx);

		dialog.setTitle(title);

		dialog.setCancelable(false);

		if (message != null) {
			dialog.setMessage(message);
		}
		if (listen != null && button_name != null) {
			dialog.setButton(button_name, listen);
		}

		dialog.show();
		return dialog;
	}

	/**
	 * 取消进度条
	 *
	 * @param ctx
	 * @param dialog
	 */
	public static void dissmisDialog(Context ctx, ProgressDialog dialog) {
		if (dialog != null && dialog.isShowing()) {
			if (ctx != null && !((Activity) ctx).isFinishing()) {
				dialog.dismiss();
			}
		}
	}

	/**
	 * 下载进度条
	 *
	 * @param context
	 * @param message
	 */
	public static ProgressDialog getProgressDialog(Context context,
	                                               String message, OnClickListener listener) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setTitle("提示"); // 设置标题
		progressDialog.setMessage(message); // 设置body信息
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);
		progressDialog.setButton("取消", listener);

		return progressDialog;
	}
}
