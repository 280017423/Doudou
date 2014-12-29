package com.jnhlxd.doudou.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.authentication.ActionResult;
import com.jnhlxd.doudou.listener.IDialogProtocol;
import com.jnhlxd.doudou.util.DialogManager;
import com.jnhlxd.doudou.widget.CustomDialog.Builder;
import com.qianjiang.framework.app.QJActivityBase;
import com.qianjiang.framework.imageloader.core.DisplayImageOptions;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.imageloader.core.display.SimpleBitmapDisplayer;
import com.qianjiang.framework.util.QJActivityManager;
import com.qianjiang.framework.widget.LoadingUpView;
import com.umeng.analytics.MobclickAgent;

/**
 * Activity基类
 * 
 * @author zou.sq
 */
public class ActivityBase extends QJActivityBase implements IDialogProtocol {
	protected ImageLoader mImageLoader = ImageLoader.getInstance();
	protected DisplayImageOptions mOptions = new DisplayImageOptions.Builder().cacheInMemory().cacheOnDisc()
			.displayer(new SimpleBitmapDisplayer()).build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QJActivityManager.getInstance().pushActivity(this);
		MobclickAgent.onPageStart("");
		MobclickAgent.setDebugMode(false);
		// SDK在统计Fragment时，需要关闭Activity自带的页面统计，
		// 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		// MobclickAgent.setAutoLocation(true);
		// MobclickAgent.setSessionContinueMillis(1000);
		MobclickAgent.updateOnlineConfig(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("");
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("");
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		QJActivityManager.getInstance().popActivity(this);
		MobclickAgent.onPageStart("");
	}

	/**
	 * toast显示错误消息
	 * 
	 * @param result
	 */
	protected void showErrorMsg(ActionResult result) {
		showErrorToast(result, getResources().getString(R.string.network_is_not_available));
	}

	/**
	 * toast显示错误消息
	 * 
	 * @param result
	 * @param netErrorMsg
	 *            网络异常时显示消息
	 */
	protected void showErrorMsg(ActionResult result, String netErrorMsg) {
		showErrorToast(result, netErrorMsg);
	}

	/**
	 * 显示错误消息
	 * 
	 * @param result
	 * @param netErrorMsg
	 *            网络异常时显示消息
	 */
	private void showErrorToast(ActionResult result, String netErrorMsg) {
		if (result == null) {
			return;
		}
		if (ActionResult.RESULT_CODE_NET_ERROR.equals(result.ResultCode)) {
			toast(netErrorMsg);
		} else if (result.ResultObject != null) {
			// 增加RESULT_CODE_ERROR值时也弹出网络异常
			if (ActionResult.RESULT_CODE_NET_ERROR.equals(result.ResultObject.toString())) {
				toast(netErrorMsg);
			} else {
				toast(result.ResultObject.toString());
			}
		}

	}

	protected static boolean showLoadingUpView(LoadingUpView loadingUpView) {
		if (loadingUpView != null && !loadingUpView.isShowing()) {
			loadingUpView.showPopup();
			return true;
		}
		return false;
	}

	protected static boolean dismissLoadingUpView(LoadingUpView loadingUpView) {
		if (loadingUpView != null && loadingUpView.isShowing()) {
			loadingUpView.dismiss();
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (getParent() != null) {
			return getParent().onKeyDown(keyCode, event);
		}
		return super.onKeyDown(keyCode, event);
	}

	void handleActivityResult(int requestCode, int resultCode, Intent data) {
	}

	@Override
	public Builder createDialogBuilder(Context context, String title, String message, String positiveBtnName,
			String negativeBtnName) {
		return DialogManager
				.createMessageDialogBuilder(context, title, message, positiveBtnName, negativeBtnName, this);
	}

	@Override
	public void onPositiveBtnClick(int id, DialogInterface dialog, int which) {
	}

	@Override
	public void onNegativeBtnClick(int id, DialogInterface dialog, int which) {
	}
}
