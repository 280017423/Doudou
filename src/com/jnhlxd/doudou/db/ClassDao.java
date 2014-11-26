package com.jnhlxd.doudou.db;

import java.util.ArrayList;
import java.util.List;

import com.jnhlxd.doudou.model.ClassInfoModel;
import com.jnhlxd.doudou.util.DBUtil;
import com.qianjiang.framework.orm.DataManager;

/**
 * Description the class班级数据处理类
 * 
 * @version 1.0
 * @author zou.sq
 */
public class ClassDao {

	private ClassDao() {
	}

	/**
	 * 保存班级信息列表
	 * 
	 * @param classInfoModels
	 *            班级信息列表
	 */
	public static void saveClassInfoModels(List<ClassInfoModel> classInfoModels) {
		if (classInfoModels != null) {
			DataManager dataManager = DBUtil.getDataManager();
			dataManager.beginTransaction();
			try {
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

	/**
	 * 获取班级信息
	 * 
	 * @return List<ClassInfoModel> 班级信息列表
	 */
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
}
