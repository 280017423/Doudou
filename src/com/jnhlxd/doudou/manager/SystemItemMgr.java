package com.jnhlxd.doudou.manager;

import java.util.List;

import android.content.ContentValues;

import com.jnhlxd.doudou.model.SystemModel;
import com.jnhlxd.doudou.util.DBUtil;
import com.qianjiang.framework.orm.DataAccessException;
import com.qianjiang.framework.orm.DataManager;
import com.qianjiang.framework.util.EvtLog;

/**
 * 系统设置相关管理类
 * 
 * @author zou.sq
 */
public class SystemItemMgr {
	private static final String TAG = "SystemItemMgr";
	private static final int NINE = 9;

	private SystemItemMgr() {
	}

	/**
	 * 查找一个SystemItem
	 * 
	 * @param key
	 *            要查找的对应的key
	 * @return 找到的SystemItem实例
	 */
	public static SystemModel getSystemItem(String key) {
		SystemModel item = null;

		DataManager dataManager = DBUtil.getDataManager();
		try {
			dataManager.open();
			item = dataManager.get(SystemModel.class, "item_key = ?", new String[] { key });
		} catch (Exception e) {
			EvtLog.e(TAG, e);
		} finally {
			dataManager.close();
		}
		return item;
	}

	/**
	 * 保存SystemItem，存在则更新，不存在，则插入
	 * 
	 * @param item
	 *            需要保存的实例
	 */
	public static void saveSystemItem(SystemModel item) {
		DataManager dataManager = DBUtil.getDataManager();
		try {
			dataManager.open();
			SystemModel tmpItem = dataManager
					.get(SystemModel.class, "item_key = ?", new String[] { item.getItemKey() });
			if (tmpItem != null && tmpItem.getItemKey().equals(item.getItemKey())) {
				ContentValues values = new ContentValues();
				values.put(SystemModel.COLUMN_ITEM_KEY, tmpItem.getItemKey());
				values.put(SystemModel.COLUMN_ITEM_VALUE, item.getItemValue());
				dataManager.updateByClause(SystemModel.class, values, "item_key = ?",
						new String[] { item.getItemKey() });
			} else {
				dataManager.insert(item);
			}
		} catch (Exception e) {
			EvtLog.e(TAG, e);
		} finally {
			dataManager.close();
		}
	}

	/**
	 * 保存一个搜索关键字记录,统一key只保存十条记录
	 * 
	 * @param item
	 *            对象
	 * @param key
	 *            itemkey
	 * @param orderby
	 *            排序规则
	 */
	public static void savaSystemItemToList(SystemModel item, String key, String orderby) {
		DataManager db = DBUtil.getDataManager();
		List<SystemModel> mList = null;
		try {
			mList = db.getList(SystemModel.class, SystemModel.COLUMN_ITEM_KEY + " = ?  ", new String[] { key },
					orderby, null);
		} catch (DataAccessException e) {
			EvtLog.e(TAG, e);
		}
		if (mList != null) {
			for (int i = mList.size() - NINE, j = mList.size() - 1; i > 0; i--) {
				db.delete(SystemModel.class, mList.get(j)._id);
				j--;
			}
		}
		try {
			db.save(item);
		} catch (DataAccessException e) {
			EvtLog.e(TAG, e);
		}
	}

	/**
	 * 获取列表
	 * 
	 * @param itemkey
	 *            item键
	 * @param itemvalue
	 *            item值
	 * @param orderby
	 *            排序
	 * @return 列表
	 */
	public static List<SystemModel> getSystemitemList(String itemkey, String itemvalue, String orderby) {
		DataManager db = DBUtil.getDataManager();
		List<SystemModel> mList = null;
		if (itemvalue == null || "".equals(itemvalue)) {
			return getSystemitemList(itemkey, orderby);
		}
		try {
			mList = db.getList(SystemModel.class, SystemModel.COLUMN_ITEM_KEY + " = ? AND "
					+ SystemModel.COLUMN_ITEM_VALUE + " = ?", new String[] { itemkey, "%" + itemvalue + "%" }, orderby,
					null);

		} catch (DataAccessException e) {
			EvtLog.e(TAG, e);
		}
		return mList;
	}

	/**
	 * 重载方法 获取列表
	 * 
	 * @param itemkey
	 *            item键
	 * @param orderby
	 *            排序
	 * @return 列表
	 */
	private static List<SystemModel> getSystemitemList(String itemkey, String orderby) {
		DataManager db = DBUtil.getDataManager();
		List<SystemModel> mList = null;
		try {
			mList = db.getList(SystemModel.class, SystemModel.COLUMN_ITEM_KEY + " = ? ", new String[] { itemkey },
					orderby, null);
		} catch (DataAccessException e) {
			EvtLog.e(TAG, e);
		}
		return mList;
	}

	/**
	 * 删除搜索记录
	 * 
	 * @param itemkey
	 *            条件值
	 */
	public static void deleteSystemItem(String itemkey) {
		deleteSystemItem(itemkey, false);
	}

	/**
	 * 
	 * @Method: deleteSystemItem
	 * @Description: 删除收藏记录
	 * @param itemkey
	 *            条件值
	 * @param isLike
	 *            是不是模糊删除
	 * @throws
	 */
	public static void deleteSystemItem(String itemkey, boolean isLike) {
		DataManager db = DBUtil.getDataManager();
		if (isLike) {
			EvtLog.d(TAG, "-->" + "ITEM_KEY" + " like \"%" + itemkey + "%\"");
			db.delete(SystemModel.class, "ITEM_KEY" + " like \"%" + itemkey + "%\"", null);
		} else {
			db.delete(SystemModel.class, SystemModel.COLUMN_ITEM_KEY + " = ? ", new String[] { itemkey });
		}
		db.close();
	}
}
