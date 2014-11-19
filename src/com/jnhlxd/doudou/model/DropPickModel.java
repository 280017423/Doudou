package com.jnhlxd.doudou.model;

import java.util.List;

import com.jnhlxd.doudou.db.PunchDao;
import com.jnhlxd.doudou.manager.PunchMgr;
import com.qianjiang.framework.orm.BaseModel;

/**
 * 考勤模块model
 * 
 * @version 1.0
 * @author zou.sq
 */
public class DropPickModel extends BaseModel {

	public static final String IS_CURRENT_MODEL = "IS_CURRENT_MODEL";
	public static final String SIGN_MODE = "SIGN_MODE";

	// 上学上车签到模块
	public static final int SIGN_TYPE_UP_BUS_TO_SCHOOL = 1;
	// 上学下车签到模块
	public static final int SIGN_TYPE_DOWN_BUS_TO_SCHOOL = 2;
	// 上学入园签到模块
	public static final int SIGN_TYPE_IN_SCHOOL = 3;
	// 放学出园签到模块
	public static final int SIGN_TYPE_OUT_SCHOOL = 4;
	// 放学上车签到模块
	public static final int SIGN_TYPE_UP_BUS_TO_HOME = 5;
	// 放学下车签到模块
	public static final int SIGN_TYPE_DOWN_BUS_TO_HOME = 6;
	// 病假
	public static final int SIGN_TYPE_SICK_LEAVE = 7;
	// 事假
	public static final int SIGN_TYPE_CASUAL_LEAVE = 8;

	private static final long serialVersionUID = 3353521583726991281L;

	private int signMode; // 模式类型
	private String signModeName; // 模式类型
	private String signToast; // 语音播报提示音
	private int isCurrentModel; // 是否是当前选择模式，1表示当前模式，0不是当前模式

	public int getSignMode() {
		return signMode;
	}

	public void setSignMode(int signMode) {
		this.signMode = signMode;
	}

	public String getSignToast() {
		return null == signToast ? "" : signToast;
	}

	public void setSignToast(String signToast) {
		this.signToast = signToast;
	}

	public boolean isCurrentModel() {
		return 1 == isCurrentModel;
	}

	public void setCurrentModel(int isCurrentModel) {
		this.isCurrentModel = isCurrentModel;
	}

	public String getSignModeName() {
		return signModeName;
	}

	public void setSignModeName(String signModeName) {
		this.signModeName = signModeName;
	}

	/**
	 * 获取当前的签到模式
	 * 
	 * @return int 当前的签到模式
	 */
	public static int getCurrentMode() {
		DropPickModel model = PunchDao.getCurrentModule();
		if (null != model && 1 == model.isCurrentModel) {
			return model.getSignMode();
		}
		return SIGN_TYPE_IN_SCHOOL;
	}

	/**
	 * 获取语音提示信息
	 * 
	 * @param mode
	 *            考勤模式
	 * @return String 语音提示信息
	 */
	public static String getSpeechMsg(int mode) {
		DropPickModel model = PunchDao.getModule(mode);
		if (null != model) {
			return model.getSignToast();
		}
		return "";
	}

	/**
	 * 更新提示信息
	 * 
	 * @param mode
	 *            指定模块
	 * @param speechMsg
	 *            提示信息
	 */
	public static void updateSpeechMsg(int mode, String speechMsg) {
		DropPickModel model = PunchDao.getModule(mode);
		if (null != model) {
			model.setSignToast(speechMsg);
			PunchDao.updateModel(model);
		}
	}

	/**
	 * @Description 设置当前的签到模式
	 * @param mode
	 *            当前的签到模式
	 */
	public static void updateCurrentMode(int mode) {
		List<DropPickModel> models = PunchMgr.getSignModules();
		if (null != models && !models.isEmpty()) {
			for (int i = 0; i < models.size(); i++) {
				DropPickModel model = models.get(i);
				model.setCurrentModel(model.getSignMode() == mode ? 1 : 0);
				PunchDao.updateModel(model);
			}
		}
	}

}
