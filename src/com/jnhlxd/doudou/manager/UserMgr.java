package com.jnhlxd.doudou.manager;

import com.jnhlxd.doudou.authentication.LoginProcessor;
import com.jnhlxd.doudou.model.SchoolInfoModel;
import com.jnhlxd.doudou.model.UserInfoModel;
import com.jnhlxd.doudou.util.ConstantSet;
import com.jnhlxd.doudou.util.DBUtil;
import com.jnhlxd.doudou.util.SharedPreferenceUtil;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.HttpClientUtil;
import com.qianjiang.framework.util.StringUtil;

/**
 * 用户相关逻辑类
 * 
 * @version 2014-8-18 下午5:01:12 当前登录的用户和本地的用户信息不一致就删除本地的用户记录</br>
 * @author zou.sq
 */
public class UserMgr {
	private UserMgr() {
	}

	/**
	 * 注销登录
	 */
	public static void logout() {
		// 清除所有的用户数据
		DBUtil.clearAllTables();
		// 重置本地状态
		LoginProcessor.getInstance().setLoginStatus(false);
		// 还原Cookies
		HttpClientUtil.setCookieStore(null);
	}

	/**
	 * 保存用户信息
	 * 
	 * @param userInfoModel
	 *            用户信息
	 */
	public static void saveUserInfo(UserInfoModel userInfoModel) {
		if (userInfoModel == null) {
			return;
		}
		UserInfoModel localModel = getUserInfoModel();
		if (null != localModel && !StringUtil.isNullOrEmpty(localModel.getName())) {
			if (!StringUtil.isNullOrEmpty(userInfoModel.getName())
					&& !userInfoModel.getName().equals(localModel.getName())) {
				// 当前登录的用户和本地的用户信息不一致就删除本地的用户记录
				// 清除本地的用户信息
				clearConfigInfo();
				// 清除所有的用户数据
				DBUtil.clearAllTables();
			}
		}
		SharedPreferenceUtil.saveObject(QJApplicationBase.CONTEXT, ConstantSet.KEY_FILE_DOUDOU_CONFIG_FILE,
				userInfoModel);
	}

	private static void clearConfigInfo() {
		SharedPreferenceUtil.clearObject(QJApplicationBase.CONTEXT, ConstantSet.KEY_FILE_DOUDOU_CONFIG_FILE);
	}

	/**
	 * 获取用户信息
	 * 
	 * @return UserInfoModel
	 */
	public static UserInfoModel getUserInfoModel() {
		UserInfoModel userInfoModel = (UserInfoModel) SharedPreferenceUtil.getObject(QJApplicationBase.CONTEXT,
				ConstantSet.KEY_FILE_DOUDOU_CONFIG_FILE, UserInfoModel.class);
		return userInfoModel;
	}

	/**
	 * 数据库是否已经存了用户信息
	 * 
	 * @return 数据库是否已经存了用户信息
	 */
	public static boolean hasUserInfo() {
		UserInfoModel userInfoModel = getUserInfoModel();
		return userInfoModel != null && !StringUtil.isNullOrEmpty(userInfoModel.getTeacherId());
	}

	/**
	 * 获取学校信息
	 * 
	 * @return schoolInfoModel 学校信息
	 */
	public static SchoolInfoModel getSchoolInfoModel() {
		SchoolInfoModel schoolInfoModel = (SchoolInfoModel) SharedPreferenceUtil.getObject(QJApplicationBase.CONTEXT,
				ConstantSet.KEY_FILE_DOUDOU_CONFIG_FILE, SchoolInfoModel.class);
		return schoolInfoModel;
	}

	/**
	 * 保存学校信息
	 * 
	 * @param schoolInfoModel
	 *            学校信息
	 */
	public static void saveSchoolInfoModel(final SchoolInfoModel schoolInfoModel) {
		if (schoolInfoModel == null) {
			return;
		}
		SharedPreferenceUtil.saveObject(QJApplicationBase.CONTEXT, ConstantSet.KEY_FILE_DOUDOU_CONFIG_FILE,
				schoolInfoModel);
	}

}
