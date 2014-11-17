package com.jnhlxd.doudou.db;

import java.util.ArrayList;
import java.util.List;

import com.jnhlxd.doudou.model.DropPickModel;
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
	private static final String PRIMARY_KEY_WHERE = " = ?";

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

	/**
	 * 获取当前的打卡模式
	 * 
	 * @return SignModuleModel 打卡模式对象
	 */
	public static DropPickModel getCurrentModule() {
		DropPickModel results = null;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.get(DropPickModel.class, DropPickModel.IS_CURRENT_MODEL + PRIMARY_KEY_WHERE,
					new String[] { "1" });
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	/**
	 * 指定model值的打卡模式
	 * 
	 * @param model
	 *            指定的model值
	 * @return SignModuleModel 打卡模式对象
	 */
	public static DropPickModel getModule(int model) {
		DropPickModel results = null;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.get(DropPickModel.class, DropPickModel.SIGN_MODE + PRIMARY_KEY_WHERE,
					new String[] { "" + model });
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	/**
	 * 更新打卡模式
	 * 
	 * @param model
	 *            打卡模式
	 * @return int 受影响的行数
	 */
	public static int updateModel(DropPickModel model) {
		int results = -1;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.update(model);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}
}
