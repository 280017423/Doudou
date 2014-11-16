package com.jnhlxd.doudou.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.listener.IDialogProtocol;
import com.qianjiang.framework.util.StringUtil;

/**
 * 
 * Description the class 自定义对话框封装类
 * 
 * @author zou.sq
 * @version 2013-10-24 下午3:05:15 zou.sq 新建<br>
 */
public class CustomDialog extends Dialog {

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文对象
	 * @param theme
	 *            主题
	 */
	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            上下文对象
	 */
	public CustomDialog(Context context) {
		super(context);
	}

	/**
	 * Helper class for creating a custom dialog
	 */
	public static class Builder {

		private Context mContext;
		private int mTheme;
		private String mTitle;
		private String mMessage;
		private String mSubMessage;
		private String mPositiveButtonText;
		private String mNegativeButtonText;
		private View mContentView;
		private View mDialogView;
		private int mNegativeButtonBgColor;
		private int mPositiveButtonBgColor;
		private int mNegativeButtonTextColor;
		private int mPositiveButtonTextColor;
		private int mMessageTextColor;
		private int mMessageTextSize;
		private int mSubMessageTextColor;
		private int mSubMessageTextSize;
		private int mTitleTextColor;
		private int mTitleTextSize;

		private IDialogProtocol mPositiveButtonClickListener;
		private IDialogProtocol mNegativeButtonClickListener;

		/**
		 * 构造函数
		 * 
		 * @param context
		 *            上下文对象
		 */
		public Builder(Context context) {
			this.mContext = context;
		}

		/**
		 * Set the Dialog message from String
		 * 
		 * @param theme
		 *            对话框主题
		 * @return Builder对象
		 */
		public Builder setTheme(int theme) {
			this.mTheme = theme;
			return this;
		}

		/**
		 * Set the Dialog message from String
		 * 
		 * @param message
		 *            对话框内容
		 * @return Builder对象
		 */
		public Builder setMessage(String message) {
			this.mMessage = message;
			return this;
		}

		/**
		 * Set the Dialog message from String
		 * 
		 * @param message
		 *            对话框内容
		 * @return Builder对象
		 */
		public Builder setSubMessage(String message) {
			this.mSubMessage = message;
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param message
		 *            对话框内容资源id
		 * @return Builder对象
		 */
		public Builder setMessage(int message) {
			this.mMessage = (String) mContext.getText(message);
			return this;
		}

		/**
		 * Set the Dialog message from resource
		 * 
		 * @param message
		 *            对话框内容资源id
		 * @return Builder对象
		 */
		public Builder setSubMessage(int message) {
			this.mSubMessage = (String) mContext.getText(message);
			return this;
		}

		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 *            标题资源id
		 * @return Builder对象
		 */
		public Builder setTitle(int title) {
			this.mTitle = (String) mContext.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 *            标题
		 * @return Builder对象
		 */
		public Builder setTitle(String title) {
			this.mTitle = title;
			return this;
		}

		/**
		 * Set a custom content view for the Dialog. If a message is set, the
		 * contentView is not added to the Dialog...
		 * 
		 * @param v
		 *            视图对象
		 * @return Builder对象
		 */
		public Builder setContentView(View v) {
			this.mContentView = v;
			return this;
		}

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 *            按钮字符串资源id
		 * @param listener
		 *            按钮监听
		 * @return Builder对象
		 */
		public Builder setPositiveButton(int positiveButtonText, IDialogProtocol listener) {
			this.mPositiveButtonText = (String) mContext.getText(positiveButtonText);
			this.mPositiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the positive button text and it's listener
		 * 
		 * @param positiveButtonText
		 *            按钮字符串资源
		 * @param listener
		 *            按钮监听
		 * @return Builder对象
		 */
		public Builder setPositiveButton(String positiveButtonText, IDialogProtocol listener) {
			this.mPositiveButtonText = positiveButtonText;
			this.mPositiveButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button Text color
		 * 
		 * @param res
		 *            按钮字符串颜色id
		 * @return Builder对象
		 */
		public Builder setPositiveButtonTextColor(int res) {
			this.mPositiveButtonTextColor = res;
			return this;
		}

		/**
		 * Set the Title Text color
		 * 
		 * @param res
		 *            Title字符串颜色id
		 * @return Builder对象
		 */
		public Builder setTitleTextColor(int res) {
			this.mTitleTextColor = res;
			return this;
		}

		/**
		 * Set the Title Text size
		 * 
		 * @param size
		 *            Title字符大小
		 * @return Builder对象
		 */
		public Builder setTitleTextSize(int size) {
			this.mTitleTextSize = size;
			return this;
		}

		/**
		 * Set the Message Text color
		 * 
		 * @param res
		 *            Message字符串颜色id
		 * @return Builder对象
		 */
		public Builder setMessageTextColor(int res) {
			this.mMessageTextColor = res;
			return this;
		}

		/**
		 * Set the Message Text size
		 * 
		 * @param size
		 *            Message字符大小
		 * @return Builder对象
		 */
		public Builder setMessageTextSize(int size) {
			this.mMessageTextSize = size;
			return this;
		}

		/**
		 * Set the negative button Text color
		 * 
		 * @param res
		 *            按钮字符串颜色id
		 * @return Builder对象
		 */
		public Builder setNegativeButtonTextColor(int res) {
			this.mNegativeButtonTextColor = res;
			return this;
		}

		/**
		 * Set the negative button bg color
		 * 
		 * @param res
		 *            按钮字符串背景id
		 * @return Builder对象
		 */
		public Builder setPositiveButtonBgColor(int res) {
			this.mPositiveButtonBgColor = res;
			return this;
		}

		/**
		 * Set the negative button bg color
		 * 
		 * @param res
		 *            按钮字符串背景id
		 * @return Builder对象
		 */
		public Builder setNegativeButtonBgColor(int res) {
			this.mNegativeButtonBgColor = res;
			return this;
		}

		/**
		 * Set the negative button resource and it's listener
		 * 
		 * @param negativeButtonText
		 *            按钮字符串资源id
		 * @param listener
		 *            按钮监听
		 * @return Builder对象
		 */
		public Builder setNegativeButton(int negativeButtonText, IDialogProtocol listener) {
			this.mNegativeButtonText = (String) mContext.getText(negativeButtonText);
			this.mNegativeButtonClickListener = listener;
			return this;
		}

		/**
		 * Set the negative button text and it's listener
		 * 
		 * @param negativeButtonText
		 *            按钮字符串资源
		 * @param listener
		 *            按钮监听
		 * @return Builder对象
		 */
		public Builder setNegativeButton(String negativeButtonText, IDialogProtocol listener) {
			this.mNegativeButtonText = negativeButtonText;
			this.mNegativeButtonClickListener = listener;
			return this;
		}

		/**
		 * 
		 * @Method: getmDialogView
		 * @Description: 获取增加布局
		 * @param @return
		 * @return View
		 * @throws
		 */
		public View getmDialogView() {
			return mDialogView;
		}

		/**
		 * 增加自定义view
		 * 
		 * @param dialogView
		 *            自定义view
		 */
		public void setmDialogView(View dialogView) {
			this.mDialogView = dialogView;
		}

		/**
		 * Create the custom dialog
		 * 
		 * @param id
		 *            当前对话框对象的ID
		 * @return CustomDialog 自定义对话框的对象
		 */
		public CustomDialog create(int id) {
			final CustomDialog dialog = new CustomDialog(mContext, 0 == mTheme ? R.style.Dialog : mTheme);
			View layout = View.inflate(mContext, R.layout.dialog_commom_layout, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			TextView tvTitle = (TextView) layout.findViewById(R.id.tv_dialog_layout_title);
			TextView tvMessage = (TextView) layout.findViewById(R.id.tv_dialog_layout_msg);
			TextView tvSubMessage = (TextView) layout.findViewById(R.id.tv_dialog_layout_msg_1);
			Button positiveButton = (Button) layout.findViewById(R.id.btn_dialog_layout_sure);
			Button negativeButton = (Button) layout.findViewById(R.id.btn_dialog_layout_cancel);
			LinearLayout contentLayout = (LinearLayout) layout.findViewById(R.id.ll_dialog_layout);
			LinearLayout dialogLayout = (LinearLayout) layout.findViewById(R.id.ll_dialog_view);

			setDialogTitle(tvTitle);
			setDialogMessage(tvMessage);
			setSubDialogMessage(tvSubMessage);
			setDialogPositiveBtn(id, dialog, positiveButton);
			setDialogNegativeBtn(id, dialog, negativeButton);
			if (null != mDialogView) {
				dialogLayout.setVisibility(View.VISIBLE);
				dialogLayout.addView(mDialogView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			} else {
				dialogLayout.setVisibility(View.GONE);
			}
			if (mContentView != null) {
				contentLayout.removeAllViews(); // 如果没有消息内容则添加contentView
				contentLayout.addView(mContentView, new LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			dialog.setContentView(layout);
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}

		/**
		 * @param id
		 *            当前对话框对象的ID
		 * @param dialog
		 *            当前对话框对象
		 * @param negativeButton
		 *            取消按钮
		 */
		private void setDialogNegativeBtn(final int id, final CustomDialog dialog, Button negativeButton) {
			if (0 != mNegativeButtonBgColor) {
				negativeButton.setBackgroundResource(mNegativeButtonBgColor);
			}
			if (0 != mNegativeButtonTextColor) {
				negativeButton.setTextColor(mNegativeButtonTextColor);
			}
			if (!StringUtil.isNullOrEmpty(mNegativeButtonText)) {
				negativeButton.setText(mNegativeButtonText); // 设置取消按钮
				if (mNegativeButtonClickListener != null) {
					negativeButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mNegativeButtonClickListener
									.onNegativeBtnClick(id, dialog, DialogInterface.BUTTON_NEGATIVE);
							if (null != dialog && dialog.isShowing()) {
								dialog.dismiss();
							}
						}
					});
				}
			} else {
				negativeButton.setVisibility(View.GONE); // 如果没有取消按钮则不显示
			}
		}

		/**
		 * @param id
		 *            当前对话框对象的ID
		 * 
		 * @param dialog
		 *            当前对话框对象
		 * @param positiveButton
		 *            确定按钮
		 */
		private void setDialogPositiveBtn(final int id, final CustomDialog dialog, Button positiveButton) {
			if (0 != mPositiveButtonBgColor) {
				positiveButton.setBackgroundResource(mPositiveButtonBgColor);
			}
			if (0 != mPositiveButtonTextColor) {
				positiveButton.setTextColor(mPositiveButtonTextColor);
			}

			if (!StringUtil.isNullOrEmpty(mPositiveButtonText)) {
				positiveButton.setText(mPositiveButtonText); // 设置确认按钮
				if (mPositiveButtonClickListener != null) {
					positiveButton.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mPositiveButtonClickListener
									.onPositiveBtnClick(id, dialog, DialogInterface.BUTTON_POSITIVE);
							if (null != dialog && dialog.isShowing()) {
								dialog.dismiss();
							}
						}
					});
				}
			} else {
				positiveButton.setVisibility(View.GONE); // 如果没有确认按钮则不显示
			}
		}

		/**
		 * @param tvTitle
		 *            对话框标题对象
		 */
		private void setDialogTitle(TextView tvTitle) {
			if (!StringUtil.isNullOrEmpty(mTitle)) {
				tvTitle.setText(mTitle); // 设置对话框的标题
			} else {
				tvTitle.setVisibility(View.GONE);
			}
			if (0 != mTitleTextSize) {
				tvTitle.setTextSize(mTitleTextSize);
			}
			if (0 != mTitleTextColor) {
				tvTitle.setTextColor(mTitleTextColor);
			}
		}

		/**
		 * @param tvMessage
		 *            对话框Message对象
		 */
		public void setDialogMessage(TextView tvMessage) {
			if (0 != mMessageTextSize) {
				tvMessage.setTextSize(mMessageTextSize);
			}
			if (0 != mMessageTextColor) {
				tvMessage.setTextColor(mMessageTextColor);
			}
			if (!StringUtil.isNullOrEmpty(mMessage)) {
				tvMessage.setText(mMessage); // 设置消息内容
			} else {
				tvMessage.setVisibility(View.GONE);
			}
		}

		/**
		 * @param tvMessage
		 *            对话框Message对象
		 */
		private void setSubDialogMessage(TextView tvMessage) {
			if (0 != mSubMessageTextSize) {
				tvMessage.setTextSize(mSubMessageTextSize);
			}
			if (0 != mSubMessageTextColor) {
				tvMessage.setTextColor(mSubMessageTextColor);
			}
			if (!StringUtil.isNullOrEmpty(mSubMessage)) {
				tvMessage.setText(mSubMessage); // 设置消息内容
			} else {
				tvMessage.setVisibility(View.GONE);
			}
		}
	}
}
