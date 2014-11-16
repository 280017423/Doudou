package com.jnhlxd.doudou.authentication;

import android.app.Activity;

import com.jnhlxd.doudou.activity.LoginActivity;
import com.qianjiang.framework.authentication.BaseLoginProcessor;
import com.qianjiang.framework.model.ActionModel;
import com.qianjiang.framework.util.EvtLog;

/**
 * 登录验证器
 * 
 * @author tan.xx
 * @version 2013-12-12 上午10:39:56 tan.xx TODO
 */
public class LoginProcessor extends BaseLoginProcessor<ActionResult> {
	private static final String TAG = "LoginProcessor";

	/**
	 * 构造方法
	 * 
	 * @param loginActivityClass
	 *            登录界面
	 */
	private LoginProcessor(Class<? extends Activity> loginActivityClass) {
		super(loginActivityClass);
	}

	private static class ProcessorHolder {
		static LoginProcessor INSTANCE = new LoginProcessor(LoginActivity.class);
	}

	/**
	 * 获取实例
	 * 
	 * @return LoginProcessor
	 */
	public static LoginProcessor getInstance() {
		return ProcessorHolder.INSTANCE;
	}

	/**
	 * 执行操作(登录成功或者登录取消后回调)
	 * 
	 * @param action
	 * @param isCancelLogin
	 * @see com.qianjiang.framework.authentication.BaseLoginProcessor#doAction(com.qianjiang.framework.model.ActionModel,
	 *      boolean)
	 */
	@Override
	protected void doAction(final ActionModel<?> action, boolean isCancelLogin) {
		new ActionTask().executeTask(action, isCancelLogin);
	}

	/**
	 * hasUserLogin
	 * 
	 * @see com.qianjiang.framework.authentication.BaseLoginProcessor#hasUserLogin()
	 */
	@Override
	protected boolean hasUserLogin() {
		// return UserDb.hasUserInfo();
		return false;
	}

	/**
	 * 执行自动登录
	 * 
	 * @param action
	 *            动作
	 * @see com.qianjiang.framework.authentication.BaseLoginProcessor#executeAutoLoginTask(com.qianjiang.framework.model.ActionModel)
	 */
	@Override
	public void executeAutoLoginTask(ActionModel<?> action) {
		EvtLog.d(TAG, "执行自动登录==============");
		new ActionTask().executeAutoLoginTask(action);
	}

	@Override
	protected void jumpToUserInfo() {
		// TODO Auto-generated method stub

	}

}
