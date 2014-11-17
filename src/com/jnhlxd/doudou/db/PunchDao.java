package com.jnhlxd.doudou.db;

import java.util.ArrayList;
import java.util.List;

import com.jnhlxd.doudou.model.SignModel;
import com.jnhlxd.doudou.util.ConstantSet;
import com.jnhlxd.doudou.util.DBUtil;
import com.qianjiang.framework.orm.DataAccessException;
import com.qianjiang.framework.orm.DataManager;

/**
 * 
 * Description the class 打卡数据处理类
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class PunchDao {

	private static final String EQUAl = " = ";
	private static final String QUOTES = "\"";

	private PunchDao() {
	}

	/**
	 * 删除打卡历史数据
	 * 
	 * @param models
	 *            打卡历史数据
	 */
	public static void deleteHistoryData(List<SignModel> models) {
		if (null == models || models.isEmpty()) {
			return;
		}
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		int size = models.size();
		for (int i = 0; i < size; i++) {
			SignModel model = models.get(i);
			String where = SignModel.SIGN_ID + EQUAl + QUOTES + model.getSignId() + QUOTES + " and "
					+ SignModel.SIGN_TIME + EQUAl + QUOTES + model.getSignTime() + QUOTES;
			dataManager.delete(SignModel.class, where, null);
		}

		dataManager.close();
	}

	/**
	 * 获取20条本地数据发送到服务器
	 * 
	 * @return List<PunchModel> 考勤记录数据20条
	 */
	public static List<SignModel> getHistoryData() {
		List<SignModel> results = new ArrayList<SignModel>();
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.getList(SignModel.class, true, null, null, null, null, null,
					ConstantSet.INFO_NUM_IN_ONE_PAGE + "");
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}
}
