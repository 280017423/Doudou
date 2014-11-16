package com.jnhlxd.doudou.authentication;

import android.app.Activity;
import android.content.Intent;

import com.jnhlxd.doudou.listener.IActionListener;
import com.qianjiang.framework.authentication.BaseActionProcessor;
import com.qianjiang.framework.authentication.BaseLoginProcessor.LOGIN_TYPE;
import com.qianjiang.framework.model.ActionModel;

/**
 * 登录认证处理器
 * 
 * @author zeng.ww
 * @version 1.1.0
 */
public class ActionProcessor extends BaseActionProcessor<ActionResult, IActionListener> {
	// private static final String TAG = "ActionProcessor";

	// 登录界面返回时，是否关闭之前Activity
	private boolean mLoginCancelFinish;

	/**
	 * 构造函数
	 */
	public ActionProcessor() {
	}

	/**
	 * 构造函数
	 * 
	 * @param loginCancelFinish
	 *            是否关闭之前Activity
	 */
	public ActionProcessor(boolean loginCancelFinish) {
		this.mLoginCancelFinish = loginCancelFinish;
	}

	/**
	 * @Method: startAction
	 * @param activity
	 *            上下文
	 * @param isOnUiThreadCallBack
	 *            是否主线程执行回调
	 * @param returnToMain
	 *            取消登录是否返回首页
	 * @param listener
	 *            回调对象
	 * @see com.pdw.framework.authentication.BaseActionProcessor#startAction(android.app.Activity,
	 *      boolean, boolean,
	 *      com.pdw.framework.authentication.IBaseActionListener)
	 */
	@Override
	public void startAction(Activity activity, boolean isOnUiThreadCallBack, boolean returnToMain,
			IActionListener listener) {
		ActionModel<IActionListener> action = new ActionModel<IActionListener>(
				activity, listener, null, isOnUiThreadCallBack);
		// 登录类型
		LOGIN_TYPE loginType = LOGIN_TYPE.From_GetData_Type;
		// 暂时这两种情况不会并行，并行需扩展
		if (mLoginCancelFinish) {
			loginType = LOGIN_TYPE.From_GetData_And_Cancel_Finish_Type;
		} else if (returnToMain) {
			loginType = LOGIN_TYPE.From_GetData_And_Cancel_ReturnMain_Type;
		}
		action.setLoginType(loginType);
		new ActionTask().executeTask(action, false);

	}

	/**
	 * jumpActivity 界面跳转
	 * 
	 * @param action
	 * @see com.pdw.framework.authentication.BaseActionProcessor#jumpActivity(com.pdw.framework.model.ActionModel)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void jumpActivity(ActionModel action) {
		LoginProcessor.getInstance().startActivity(action);
	}

	/**
	 * 登录界面返回时，是否关闭之前Activity
	 * 
	 * @param loginCancelFinish
	 *            是否关闭之前Activity
	 */
	public void setLoginCancelFinish(boolean loginCancelFinish) {
		this.mLoginCancelFinish = loginCancelFinish;
	}

	/**
	 * 跳转至登录界面
	 * 
	 * @param activity
	 *            上下文
	 * @param intent
	 *            跳转对象
	 * @param loginType
	 *            登录类型
	 * @see com.pdw.framework.authentication.BaseActionProcessor#jumpToLoginActivity(android.app.Activity,
	 *      com.pdw.framework.authentication.BaseLoginProcessor.LOGIN_TYPE)
	 */
	@Override
	public void jumpToLoginActivity(Activity activity, Intent intent, LOGIN_TYPE loginType) {
		LoginProcessor.getInstance().jumpToLoginActivity(activity, intent, loginType);
	}

	/**
	 * 获取进入登录界面Intent （ActivityGroup等跳转中使用,需要传递其它参数时使用）
	 * 
	 * @param activity
	 *            上下文
	 * @param loginType
	 *            登录类型
	 * @return Intent
	 * @see com.pdw.framework.authentication.BaseActionProcessor#getLoginIntent(android.app.Activity,
	 *      com.pdw.framework.authentication.BaseLoginProcessor.LOGIN_TYPE)
	 */
	@Override
	public Intent getLoginIntent(Activity activity, LOGIN_TYPE loginType) {
		return LoginProcessor.getInstance().getLoginIntent(activity, loginType);
	}

	/**
	 * 获取从个人中心跳转到登录界面 Intent
	 * 
	 * @param activity
	 *            上下文
	 * @return Intent
	 */
	public Intent getLoginIntentFromMyPaui(Activity activity) {
		return getLoginIntent(activity, LOGIN_TYPE.From_UserInfo_Type);
	}

}
