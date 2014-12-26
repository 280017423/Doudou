package com.jnhlxd.doudou.db;

import java.util.ArrayList;
import java.util.List;

import com.jnhlxd.doudou.model.StudentModel;
import com.jnhlxd.doudou.util.DBUtil;
import com.qianjiang.framework.orm.DataManager;
import com.qianjiang.framework.util.StringUtil;

/**
 * 学生数据处理类
 * 
 * @version 1.0
 * @author zou.sq
 */
public class StudentDao {

	private StudentDao() {
	}

	/**
	 * 保存班级信息列表
	 * 
	 * @param studentModels
	 *            学生信息列表
	 */
	public static void saveStudentModels(List<StudentModel> studentModels) {
		if (studentModels != null) {
			DataManager dataManager = DBUtil.getDataManager();
			dataManager.beginTransaction();
			try {
				dataManager.delete(StudentModel.class, null, null);
				for (StudentModel model : studentModels) {
					if (model != null) {
						dataManager.save(model);
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
	 * @return List<StudentModel> 学生信息列表
	 */
	public static List<StudentModel> getStudentModels(String classId) {
		if (StringUtil.isNullOrEmpty(classId)) {
			return null;
		}
		List<StudentModel> models = new ArrayList<StudentModel>();
		DataManager dataManager = DBUtil.getDataManager();
		try {
			// 刻意使用一个局部变量作为承载，若返回空，catch，并直接返回空列表，杜绝外部空的可能
			List<StudentModel> studentModels = dataManager.getList(StudentModel.class, StudentModel.KEY_CLASS_ID
					+ " = ?", new String[] { classId });
			models.addAll(studentModels);
		} catch (Exception e) {
		}
		return models;
	}

	/**
	 * 获取全校学生信息
	 * 
	 * @return List<StudentModel> 学生信息列表
	 */
	public static List<StudentModel> getAllStudentModels() {
		List<StudentModel> models = new ArrayList<StudentModel>();
		DataManager dataManager = DBUtil.getDataManager();
		try {
			// 刻意使用一个局部变量作为承载，若返回空，catch，并直接返回空列表，杜绝外部空的可能
			List<StudentModel> studentModels = dataManager.getList(StudentModel.class, null, null);
			models.addAll(studentModels);
		} catch (Exception e) {
		}
		return models;
	}
}
