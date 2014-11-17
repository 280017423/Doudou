package com.jnhlxd.doudou.db;

import java.util.ArrayList;
import java.util.List;

import com.jnhlxd.doudou.util.DBUtil;
import com.qianjiang.framework.orm.BaseModel;
import com.qianjiang.framework.orm.DataAccessException;
import com.qianjiang.framework.orm.DataManager;
import com.qianjiang.framework.util.StringUtil;

/**
 * 
 * Description the class 打卡数据处理类
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public final class DbDao {

	private DbDao() {
	}

	/**
	 * 
	 * @Description 程保存签到数据到数据库
	 * @param model
	 *            签到数据
	 * @param <T>
	 *            签到数据类型
	 * 
	 */
	public static <T extends BaseModel> void saveModel(final T model) {
		if (model == null) {
			return;
		}
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.beginTransaction();
		try {
			dataManager.save(model);
		} catch (DataAccessException e) {
			e.printStackTrace();
			dataManager.rollBack();
		}
		dataManager.endTransaction();
	}

	/**
	 * 
	 * @Description 根据model类型获取所有的本地数据
	 * @param type
	 *            需要获取的数据类型
	 * @param <T>
	 *            Basemodel的子类
	 * @return List<T> 指定数据类型集合
	 * 
	 */
	public static <T extends BaseModel> List<T> getModels(Class<T> type) {
		List<T> results = new ArrayList<T>();
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.getList(type, null, null);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	/**
	 * 清除表中所有的数据
	 * 
	 * @param type
	 *            表的类型
	 * @param <T>
	 *            表的泛型类型
	 * @return boolean 清除是否成功
	 */
	public static <T extends BaseModel> boolean deleteTableFromDb(Class<T> type) {
		boolean isSucces = false;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		isSucces = dataManager.delete(type, null, null);
		dataManager.close();
		return isSucces;
	}

	/**
	 * @Description 根据表明清除表的数据
	 * @param tableName
	 *            表的名字
	 * @param <T>
	 *            表的泛型类型
	 * @return boolean 是否清除成功
	 */
	public static <T extends BaseModel> boolean deleteTableFromDb(String tableName) {
		boolean isSucces = false;
		if (StringUtil.isNullOrEmpty(tableName)) {
			return isSucces;
		}
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		isSucces = dataManager.delete(tableName, null, null);
		dataManager.close();
		return isSucces;
	}

}
