package com.jnhlxd.doudou.req;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.authentication.ActionResult;
import com.jnhlxd.doudou.db.ClassDao;
import com.jnhlxd.doudou.db.DbDao;
import com.jnhlxd.doudou.db.StudentDao;
import com.jnhlxd.doudou.manager.UserMgr;
import com.jnhlxd.doudou.model.AdverModel;
import com.jnhlxd.doudou.model.ClassInfoModel;
import com.jnhlxd.doudou.model.SchoolInfoModel;
import com.jnhlxd.doudou.model.StudentModel;
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
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_LOGIN);
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
					List<StudentModel> childfoModels = jsonResult.getData(ServerAPIConstant.KEY_CHILDS,
							new TypeToken<List<StudentModel>>() {
							}.getType());
					// 保存用户信息
					userInfoModel.setPwd(pwd);
					UserMgr.saveUserInfo(userInfoModel);
					// 保存管理对应的班级信息
					ClassDao.saveClassInfoModels(classInfoModels);
					// 保存学校信息
					UserMgr.saveSchoolInfoModel(schoolInfoModel);
					StudentDao.saveStudentModels(childfoModels);
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

	/**
	 * 上传头像
	 * 
	 * @param userAccount
	 *            用户账号
	 * @param file
	 *            文件
	 * @return 服务器返回结果
	 * 
	 */
	public static ActionResult uploadHeadImage(File file, String childId) {

		ActionResult result = new ActionResult();
		ArrayList<File> list = new ArrayList<File>();
		list.add(file);
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_UPLOAD_PHOTO);
		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
		postParams.add(new BasicNameValuePair(ServerAPIConstant.KEY_CHILD_ID, childId));
		JsonResult jsonResult;
		try {
			jsonResult = HttpClientUtil.post(url, null, postParams, list);
			if (jsonResult != null) {
				if (jsonResult.isOK()) {
					String headUrl = jsonResult.getDataString(ServerAPIConstant.KEY_USER_IMAGE);
					result.ResultCode = jsonResult.Code;
					result.ResultObject = headUrl;
				} else {
					result.ResultCode = jsonResult.Code;
					result.ResultObject = jsonResult.Msg;
				}
			} else {
				result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
			}
		} catch (Exception e) {
			result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
		}
		return result;
	}

	public static ActionResult getAdver() {
		ActionResult result = new ActionResult();
		String url = ServerAPIConstant.getUrl(ServerAPIConstant.API_GET_ADV);
		try {
			JsonResult jsonResult = HttpClientUtil.get(url, null);
			if (jsonResult != null) {
				result.ResultCode = jsonResult.Code;
				if (jsonResult.isOK()) {
					AdverModel model = jsonResult.getData(new TypeToken<AdverModel>() {
					}.getType());
					// AdverModel model = new AdverModel();
					// model.setImg("http://img2.imgtn.bdimg.com/it/u=3497083248,2140699794&fm=21&gp=0.jpg");
					// model.setMc("这个是美女的测试图片这个是美女的测试图片这个是美女的测试图片这个是美女的测试图片这个是美女的测试图片这个是美女的测试图片这个是美女的测试图片这个是美女的测试图片这个是美女的测试图片这个是美女的测试图片");
					// model.setUrl("http://img2.imgtn.bdimg.com/it/u=3497083248,2140699794&fm=21&gp=0.jpg");
					if (null != model) {
						DbDao.saveModel(model);
					}
					result.ResultObject = model;
				} else {
					result.ResultObject = jsonResult.Msg;
				}
			} else {
				result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
			}
		} catch (Exception e) {
			result.ResultCode = ActionResult.RESULT_CODE_NET_ERROR;
		}
		return result;
	}
}
