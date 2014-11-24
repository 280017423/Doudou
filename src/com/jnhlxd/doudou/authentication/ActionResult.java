package com.jnhlxd.doudou.authentication;

import com.qianjiang.framework.authentication.BaseActionResult;

/**
 * 动作执行函数
 * 
 * @author zeng.ww
 * @version 1.1.0<br>
 *          2013-03-21，tan.xx，修改继承BaseActionResult
 */
public class ActionResult extends BaseActionResult {

	/**
	 * 登录成功
	 */
	public static final String RESULT_CODE_SUCCESS = "1000";
	/**
	 * 网络异常
	 */
	public static final String RESULT_CODE_NET_ERROR = "100";
	/**
	 * 家园通未登录状态
	 */
	public static final String RESULT_CODE_NOLOGIN = "998";
}
