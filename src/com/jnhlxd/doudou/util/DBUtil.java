package com.jnhlxd.doudou.util;

import android.content.Context;

import com.jnhlxd.doudou.manager.DBMgr;
import com.jnhlxd.doudou.model.ClassInfoModel;
import com.jnhlxd.doudou.model.SystemModel;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.orm.DataManager;
import com.qianjiang.framework.orm.DatabaseBuilder;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.PackageUtil;

/**
 * 数据库初始化类
 * 
 * @author zou.sq
 */
public class DBUtil {
	private static final String TAG = "pmr.DBUtil";

	private static DatabaseBuilder DATABASE_BUILDER;
	private static PMRDataManager INSTANCE;

	// 获取数据库实例
	static {
		if (DATABASE_BUILDER == null) {
			DATABASE_BUILDER = new DatabaseBuilder(PackageUtil.getConfigString("db_name"));
			DATABASE_BUILDER.addClass(SystemModel.class);
			DATABASE_BUILDER.addClass(ClassInfoModel.class);
		}
	}

	/**
	 * 
	 * @return 数据库管理器
	 */
	public static DataManager getDataManager() {
		EvtLog.d(TAG, "PMRDataManager getDataManager, pmr");
		if (INSTANCE == null) {
			INSTANCE = new PMRDataManager(QJApplicationBase.CONTEXT, DATABASE_BUILDER);
		}
		return INSTANCE;
	}

	static class PMRDataManager extends DataManager {
		protected PMRDataManager(Context context, DatabaseBuilder databaseBuilder) {
			super(context, PackageUtil.getConfigString("db_name"), PackageUtil.getConfigInt("db_version"), databaseBuilder);
			EvtLog.d(TAG, "PMRDataManager, pmr");
		}
	}

	/**
	 * 清除所有的数据表
	 */
	public static void clearAllTables() {
		if (null != DATABASE_BUILDER) {
			String[] tables = DATABASE_BUILDER.getTables();
			for (int i = 0; i < tables.length; i++) {
				DBMgr.deleteTableFromDb(tables[i]);
			}
		}
	}
}
