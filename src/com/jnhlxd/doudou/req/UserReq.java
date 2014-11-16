package com.jnhlxd.doudou.req;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.authentication.ActionResult;
import com.jnhlxd.doudou.manager.UserMgr;
import com.jnhlxd.doudou.model.ClassInfoModel;
import com.jnhlxd.doudou.model.SchoolInfoModel;
import com.jnhlxd.doudou.model.UserInfoModel;
import com.jnhlxd.doudou.util.ServerAPIConstant;
import com.pdw.gson.reflect.TypeToken;
import com.qianjiang.framework.app.JsonResult;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.HttpClientUtil;
import com.qianjiang.framework.util.StringUtil;

/**
 * 用户信息请求类
 * 
 * @author zou.sq
 */
public class UserReq {
	private UserReq() {
	}

	/**
	 * 
	 * 登录接口处理类
	 * 
	 * @param name
	 *            用户名字
	 * @param pwd
	 *            用户密码
	 * @return ActionResult 登录处理结果
	 * 
	 */
	public static ActionResult login(String name, String pwd) {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.LOGIN_API);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_USER_NAME, name));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_PWD, pwd));
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_APP, ServerAPIConstant.getAppSign()));
		try {
			JsonResult jsonResult = HttpClientUtil.post(url, HttpClientUtil.NORMAL_REQUEST, null, postParams);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					UserInfoModel userInfoModel = jsonResult.getData(ServerAPIConstant.KEY_USER_INFO,
							UserInfoModel.class);
					SchoolInfoModel schoolInfoModel = jsonResult.getData(ServerAPIConstant.KEY_SCHOOL_INFO,
							SchoolInfoModel.class);
					List<ClassInfoModel> classInfoModels = jsonResult.getData(ServerAPIConstant.KEY_CLASS_INFO,
							new TypeToken<List<ClassInfoModel>>() {
							}.getType());
					// 保存用户信息
					userInfoModel.setPwd(pwd);
					UserMgr.saveUserInfo(userInfoModel);
					// 保存管理对应的班级信息
					UserMgr.updateClassInfoModels(classInfoModels);
					// 保存学校信息
					UserMgr.saveSchoolInfoModel(schoolInfoModel);
				}
				// 登录成功有积分提示语
				result.ResultObject = jsonResult.Msg;
				result.ResultCode = jsonResult.Code;
			} else {
				result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
				result.ResultObject = QJApplicationBase.CONTEXT.getString(R.string.network_is_not_available);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
		}
		return result;
	}

	/**
	 * 自动登录
	 * 
	 * @return true: 成功 false:失败
	 */
	public static ActionResult autoLogin() {
		ActionResult result = new ActionResult();
		UserInfoModel user = UserMgr.getUserInfoModel();
		if (null != user) {
			String username = user.getName();
			String userpwd = user.getPwd();
			if (!StringUtil.isNullOrEmpty(username) && !StringUtil.isNullOrEmpty(userpwd)) {
				result = login(username, userpwd);
			}
		}
		return result;
	}
}
