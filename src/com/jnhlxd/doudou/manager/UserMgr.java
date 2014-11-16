package com.jnhlxd.doudou.manager;

import java.util.ArrayList;
import java.util.List;

import com.jnhlxd.doudou.authentication.LoginProcessor;
import com.jnhlxd.doudou.model.ClassInfoModel;
import com.jnhlxd.doudou.model.SchoolInfoModel;
import com.jnhlxd.doudou.model.UserInfoModel;
import com.jnhlxd.doudou.util.ConstantSet;
import com.jnhlxd.doudou.util.DBUtil;
import com.jnhlxd.doudou.util.SharedPreferenceUtil;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.orm.DataManager;
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
	 * 登出方法
	 */
	public static void logout() {
		// 清除本地的用户信息
		clearUserInfoModel();
		// 清除所有的用户数据
		DBUtil.clearAllTables();
		// 重置本地状态
		LoginProcessor.getInstance().setLoginStatus(false);
		// 还原Cookies
		HttpClientUtil.setCookieStore(null);
	}

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
				clearUserInfoModel();
				// 清除所有的用户数据
				DBUtil.clearAllTables();
			}
		}
		SharedPreferenceUtil.saveObject(QJApplicationBase.CONTEXT, ConstantSet.USER_INFO_SHAREDPREFERENCE_NAME,
				userInfoModel);
	}

	public static void clearConfigInfo() {
		SharedPreferenceUtil.clearObject(QJApplicationBase.CONTEXT, ConstantSet.FILE_QIANJIANG_CONFIG);
	}

	public static void clearUserInfoModel() {
		SharedPreferenceUtil.clearObject(QJApplicationBase.CONTEXT, ConstantSet.USER_INFO_SHAREDPREFERENCE_NAME);
		SharedPreferenceUtil.clearObject(QJApplicationBase.CONTEXT, ConstantSet.SYSTEM_INFO_SHAREDPREFERENCE_NAME);
		SharedPreferenceUtil.clearObject(QJApplicationBase.CONTEXT, ConstantSet.SCHOOL_INFO_SHAREDPREFERENCE_NAME);
	}

	public static UserInfoModel getUserInfoModel() {
		UserInfoModel userInfoModel = (UserInfoModel) SharedPreferenceUtil.getObject(QJApplicationBase.CONTEXT,
				ConstantSet.USER_INFO_SHAREDPREFERENCE_NAME, UserInfoModel.class);
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

	public static SchoolInfoModel getSchoolInfoModel() {
		SchoolInfoModel schoolInfoModel = (SchoolInfoModel) SharedPreferenceUtil.getObject(QJApplicationBase.CONTEXT,
				ConstantSet.SCHOOL_INFO_SHAREDPREFERENCE_NAME, SchoolInfoModel.class);
		return schoolInfoModel;
	}

	public static void saveSchoolInfoModel(final SchoolInfoModel schoolInfoModel) {
		if (schoolInfoModel == null) {
			return;
		}
		SharedPreferenceUtil.saveObject(QJApplicationBase.CONTEXT, ConstantSet.SCHOOL_INFO_SHAREDPREFERENCE_NAME,
				schoolInfoModel);
	}

	public static void updateClassInfoModels(List<ClassInfoModel> classInfoModels) {
		if (classInfoModels != null) {
			DataManager dataManager = DBUtil.getDataManager();
			dataManager.beginTransaction();
			try {
				// 清空，重新保存
				dataManager.delete(ClassInfoModel.class, null, null);
				for (ClassInfoModel classInfoModel : classInfoModels) {
					if (classInfoModel != null) {
						dataManager.save(classInfoModel);
					}
				}
				dataManager.endTransaction();
			} catch (Exception e) {
				dataManager.rollBack();
			}
		}
	}

	public static List<ClassInfoModel> getClassInfoModels() {
		List<ClassInfoModel> classInfoModels = new ArrayList<ClassInfoModel>();
		DataManager dataManager = DBUtil.getDataManager();
		try {
			// 刻意使用一个局部变量作为承载，若返回空，catch，并直接返回空列表，杜绝外部空的可能
			List<ClassInfoModel> localClassInfoModels = dataManager.getList(ClassInfoModel.class, null, null);
			classInfoModels.addAll(localClassInfoModels);
		} catch (Exception e) {
		}
		return classInfoModels;
	}

	public static String getClassIdString(String split) {
		String splitStr = split;
		StringBuffer resultBuffer = new StringBuffer();
		if (StringUtil.isNullOrEmpty(splitStr)) {
			splitStr = ",";
		}
		List<ClassInfoModel> classInfoModels = getClassInfoModels();
		for (ClassInfoModel classInfoModel : classInfoModels) {
			resultBuffer.append(classInfoModel.getKeyId() + split);
		}
		if (resultBuffer.length() > split.length()) {
			// 清除掉最后一个分隔符
			resultBuffer.delete(resultBuffer.length() - split.length(), resultBuffer.length());
		}
		return resultBuffer.toString();
	}
}
