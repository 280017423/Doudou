package com.jnhlxd.doudou.manager;

import java.util.ArrayList;
import java.util.List;

import com.jnhlxd.doudou.util.ConstantSet;
import com.jnhlxd.doudou.util.DBUtil;
import com.qianjiang.framework.orm.BaseModel;
import com.qianjiang.framework.orm.DataAccessException;
import com.qianjiang.framework.orm.DataManager;
import com.qianjiang.framework.util.StringUtil;

/**
 * 数据库处理类
 * 
 * @author zou.sq
 * 
 */
public class DBMgr {
	private DBMgr() {
	}

	/**
	 * 
	 * @Name saveModelAsync
	 * @Description 保存model
	 * @param model
	 *            需保存的对象
	 * @param <T>
	 *            泛型
	 * 
	 */
	public static <T extends BaseModel> void saveModel(final T model) {
		if (null == model) {
			return;
		}
		DataManager dataManager = DBUtil.getDataManager();
		try {
			dataManager.save(model);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Name deletModel
	 * @param model
	 *            待删除model
	 * @param <T>
	 *            泛型
	 * 
	 */
	public static <T extends BaseModel> void deletModel(final T model) {
		if (null == model) {
			return;
		}
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.delete(model.getClass(), model.getID());
	}

	/**
	 * @param type
	 *            删除model的类型
	 * @param primarykey
	 *            关键字
	 * @param value
	 *            关键字的值
	 * @param <T>
	 *            泛型
	 */
	public static <T extends BaseModel> void deletNewsModel(final Class<T> type, String primarykey, String value) {
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.delete(type, primarykey + "=?", new String[] { value });
	}

	/**
	 * 获取缓存数据
	 * 
	 * @param type
	 *            获取model的类型
	 * @param key
	 *            关键字
	 * @return List<T> 泛型数据集合
	 * @param <T>
	 *            泛型
	 */
	public static <T extends BaseModel> List<T> getHistoryData(Class<T> type, String key) {
		List<T> results = new ArrayList<T>();
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.getList(type, true, null, null, null, null, key + " desc",
					ConstantSet.INFO_NUM_IN_ONE_PAGE + "");
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	/**
	 * 获取BaseModel本地记录，倒序
	 * 
	 * @param type
	 *            获取的model类型
	 * @param <T>
	 *            泛型
	 * @return List<T> 缓存数据集合
	 */
	public static <T extends BaseModel> List<T> getBaseModel(Class<T> type) {
		List<T> results = null;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.getList(type, null, null, "_id desc", null);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	/**
	 * 获取BaseModel下一页本地记录，倒序
	 * 
	 * @param type
	 *            获取的model类型
	 * @param min_id
	 *            最小id
	 * @param key
	 *            关键字
	 * @param <T>
	 *            泛型
	 * @return List<T> 缓存数据集合
	 */
	public static <T extends BaseModel> List<T> getHistoryData(Class<T> type, int min_id, String key) {
		List<T> results = new ArrayList<T>();
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		try {
			results = dataManager.getList(type, true, key + " < " + min_id, null, null, null, key + " desc",
					ConstantSet.INFO_NUM_IN_ONE_PAGE + "");
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		dataManager.close();
		return results;
	}

	/**
	 * 从数据库删除表
	 * 
	 * @param tableName
	 *            表名字
	 * @param <T>
	 *            泛型
	 * @return boolean 是否删除成功
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

	/**
	 * 从数据库删除表
	 * 
	 * @param type
	 *            表类型
	 * @param <T>
	 *            泛型
	 * @return boolean 是否删除成功
	 */
	public static <T extends BaseModel> boolean deleteTableFromDb(Class<T> type) {
		boolean isSucces = false;
		DataManager dataManager = DBUtil.getDataManager();
		dataManager.open();
		isSucces = dataManager.delete(type, null, null);
		dataManager.close();
		return isSucces;
	}
}
