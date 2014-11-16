package com.jnhlxd.doudou.authentication;

import com.jnhlxd.doudou.listener.IActionListener;
import com.jnhlxd.doudou.req.UserReq;
import com.qianjiang.framework.authentication.BaseActionResult;
import com.qianjiang.framework.authentication.BaseActionTask;
import com.qianjiang.framework.model.ActionModel;
import com.qianjiang.framework.util.EvtLog;

/**
 * 
 * @author xu.xb
 * @version 2013-12-12 下午3:20:33 xu.xb
 */
public class ActionTask extends BaseActionTask<ActionResult> {

	/**
	 * 构造方法
	 * 
	 */
	protected ActionTask() {
		super();

	}

	/**
	 * 请求数据完成回调
	 * 
	 * @param result
	 * @param action
	 * @see com.qianjiang.framework.authentication.BaseActionTask#doResultCallBack(com.qianjiang.framework.authentication.BaseActionResult,
	 *      com.qianjiang.framework.authentication.BaseLoginProcessor.ActionModel)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void doResultCallBack(ActionResult result, ActionModel action) {
		IActionListener listener = null;
		if (action == null) {
			return;
		}
		listener = (IActionListener) action.getListener();
		if (listener == null) {
			return;
		}
		if (result == null) {
			listener.onError(new ActionResult());
		} else if (ActionResult.RESULT_CODE_NOLOGIN.equals(result.ResultCode)) {
			EvtLog.d(TAG, "接口返回未登录");
			if (action.isAfterAutoLogin()) {
				// 自动登录后接口还是返回未登录(接口正常应该不会出现此现象)
				// 进入到登录界面
				LoginProcessor.getInstance().processorToLogin(action);
			} else {
				// 进入自动登录
				LoginProcessor.getInstance().executeAutoLoginTask(action);
			}
		} else {
			EvtLog.d(TAG, "AsyncRun onBack...");
			if (ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
				// 返回成功
				listener.onSuccess(result);
			} else if (!BaseActionResult.RESULT_CODE_IS_RELEASE.equals(result.ResultCode)) {
				// 返回错误时
				listener.onError(result);
			}

		}

	}

	/**
	 * 自动登录回调
	 * 
	 * @param result
	 * @param action
	 * @see com.qianjiang.framework.authentication.BaseActionTask#doAutoLoginCallBack(com.qianjiang.framework.authentication.BaseActionResult,
	 *      com.qianjiang.framework.model.ActionModel)
	 */
	@Override
	protected void doAutoLoginCallBack(ActionResult result, ActionModel<?> action) {
		IActionListener listener = null;
		if (action == null) {
			return;
		}
		// 登录前回调
		listener = (IActionListener) action.getListener();
		if (listener == null) {
			return;
		}
		if (result == null) {
			// 进入到登录界面
			LoginProcessor.getInstance().processorToLogin(action);
		} else if (ActionResult.RESULT_CODE_SUCCESS.equals(result.ResultCode)) {
			// 登录成功，继续之前网络请求
			action.setIsAfterAutoLogin(true);
			LoginProcessor.getInstance().doAction(action, false);
		} else if (!BaseActionResult.RESULT_CODE_IS_RELEASE.equals(result.ResultCode)) {
			// 登录错误时，进入到登录界面
			LoginProcessor.getInstance().processorToLogin(action);

		}
	}

	/**
	 * 取消登录回调 doLoginCancelAction
	 * 
	 * @param action
	 * @see com.qianjiang.framework.authentication.BaseActionTask#doLoginCancelAction(com.qianjiang.framework.model.ActionModel)
	 */
	@Override
	protected void doLoginCancelAction(ActionModel<?> action) {
		if (action == null) {
			return;
		}
		IActionListener listener = (IActionListener) action.getListener();
		if (listener == null) {
			return;
		}
		ActionResult result = new ActionResult();
		result.ResultCode = ActionResult.RESULT_CODE_NOLOGIN;
		listener.onError(result);

	}

	/**
	 * 自动登录请求
	 * 
	 * @return
	 * @see com.qianjiang.framework.authentication.BaseActionTask#doAutoLoginReq()
	 */
	@Override
	protected ActionResult doAutoLoginReq() {
		return UserReq.autoLogin();
	}
}
