package com.jnhlxd.doudou.listener;

import android.content.Context;
import android.content.DialogInterface;

import com.jnhlxd.doudou.widget.CustomDialog;

/**
 * 自定义对话框接口
 * 
 * @author zou.sq
 * @version 1.1.0
 */
public interface IDialogProtocol {

	/**
	 * 
	 * @Method: createMessageDialog
	 * @Description: 创建标题、信息、按钮对话框
	 * @param context
	 *            上下文对象
	 * @param title
	 *            标题
	 * @param message
	 *            提示信息
	 * @param positiveBtnName
	 *            确定按钮
	 * @param negativeBtnName
	 *            取消按钮
	 * @return Dialog 对话框对象
	 */
	CustomDialog.Builder createDialogBuilder(Context context, String title, String message, String positiveBtnName,
			String negativeBtnName);

	/**
	 * 
	 * @Method: onPositiveBtnClick
	 * @Description: 确定按钮回调
	 * @param id
	 *            当前对话框对象的ID
	 * @param dialog
	 *            DialogInterface 对象
	 * @param which
	 *            dialog ID
	 */
	void onPositiveBtnClick(int id, DialogInterface dialog, int which);

	/**
	 * 
	 * @Method: onPositiveBtnClick
	 * @Description: 取消按钮回调
	 * @param id
	 *            当前对话框对象的ID
	 * @param dialog
	 *            DialogInterface 对象
	 * @param which
	 *            dialog ID
	 */
	void onNegativeBtnClick(int id, DialogInterface dialog, int which);

}
