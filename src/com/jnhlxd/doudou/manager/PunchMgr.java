package com.jnhlxd.doudou.manager;

import java.util.ArrayList;
import java.util.List;

import com.jnhlxd.doudou.authentication.ActionResult;
import com.jnhlxd.doudou.db.DbDao;
import com.jnhlxd.doudou.db.PunchDao;
import com.jnhlxd.doudou.model.DropPickModel;
import com.jnhlxd.doudou.model.SignModel;
import com.jnhlxd.doudou.req.PunchReq;
import com.qianjiang.framework.util.DateUtil;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.StringUtil;

/**
 * 
 * Description the class 签到数据处理类
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class PunchMgr {

	private static final String TAG = "PunchMgr";

	private PunchMgr() {
	}

	/**
	 * @Description 获取本地考勤数据
	 * @return List<SignModel> 本地考勤数据
	 */
	public static int getNoSendDataSize() {
		return PunchDao.getNoSendDataSize();
	}

	/**
	 * @Description 获取发送到服务器的本地考勤数据
	 * @return List<SignModel> 本地考勤数据
	 */
	public static List<SignModel> getSendPunchInfo() {
		return PunchDao.getSendData();
	}

	/**
	 * 
	 * @Name savePunchModel2Db
	 * @Description 根据字符串得到 PunchModel并保存到数据库，用来发送到服务器
	 * @param punchId
	 *            学生卡号
	 * @param context
	 *            上下文对象
	 * 
	 */
	public static void savePunchModel2Db(String punchId, int signMode) {
		if (StringUtil.isNullOrEmpty(punchId)) {
			return;
		}
		EvtLog.d(TAG, "插入：" + punchId);
		SignModel model = new SignModel();
		model.setSignId(punchId);
		model.setSignMode(signMode);
		model.setSignTime(DateUtil.getSysDate(DateUtil.DEFAULT_DATETIME_FORMAT));
		DbDao.saveModel(model);
		// 临时解决立即发送的功能
		final List<SignModel> models = new ArrayList<SignModel>();
		models.add(model);
		new Thread(new Runnable() {

			@Override
			public void run() {
				PunchReq.submitSignInfo(models);
			}
		}).start();
	}

	/**
	 * 获取考勤模块
	 * 
	 * @return List<SignModuleModel> 考勤模块
	 */
	public static List<DropPickModel> getSignModules() {
		List<DropPickModel> models = DbDao.getModels(DropPickModel.class);
		return models;
	}
}
