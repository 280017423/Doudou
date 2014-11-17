package com.jnhlxd.doudou.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.StringUtil;

/**
 * 
 * SharedPreference操作类； 在操作实体类的时候做为SharePreference的实体Model，必须带无参构造方法(默认即可)；<br>
 * Model注意地方：<br>
 * 1、数据类型只能为String,Integer等基本对象类型，不能为int,boolean基本类型；<br>
 * 2、数据如果为fianl,private修饰是不会保存到SharePreference；<br>
 * 3、尽量对应的Model文件名保存为:对应Model中final变量SHAREPREFERENCES_NAME中；
 * 使用SharePreference即可以整个Model操作，也可以根据单个key操作；
 * 
 * @author huang.b
 * @version 2013-7-9 下午3:28:55
 */
public class SharedPreferenceUtil {

	// 无效标记
	public static final int INVALID_CODE = -1;
	private static final String TAG = "SharedPreferenceUtil";
	private static String SEPARATOR = "_";

	/**
	 * 根据KEY 查询字符串值
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param fileName
	 *            文件名字
	 * @param key
	 *            关键字
	 * @return String 查询字符串值
	 */
	public static String getStringValueByKey(Context mContext, String fileName, String key) {
		String value = "";
		if (!StringUtil.isNullOrEmpty(key)) {
			SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
			value = sharedPreferences.getString(key, "");
		}
		return value;
	}

	/**
	 * 根据KEY 查询Boolean值
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param fileName
	 *            文件名字
	 * @param key
	 *            关键字
	 * @return boolean Boolean值
	 */
	public static boolean getBooleanValueByKey(Context mContext, String fileName, String key) {
		return getBooleanValueByKey(mContext, fileName, key, false);
	}

	/**
	 * 根据KEY 查询Boolean值
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param fileName
	 *            文件名字
	 * @param key
	 *            关键字
	 * @param defaultValue
	 *            默认值
	 * @return boolean Boolean值
	 * 
	 */
	public static boolean getBooleanValueByKey(Context mContext, String fileName, String key, boolean defaultValue) {
		boolean value = defaultValue;
		if (!StringUtil.isNullOrEmpty(key)) {
			SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
			value = sharedPreferences.getBoolean(key, defaultValue);
		}
		return value;
	}

	/**
	 * 根据KEY 查询Integer值
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param fileName
	 *            文件名字
	 * @param key
	 *            关键字
	 * @return int Integer值
	 */
	public static int getIntegerValueByKey(Context mContext, String fileName, String key) {
		int value = INVALID_CODE;
		if (!StringUtil.isNullOrEmpty(key)) {
			SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
			value = sharedPreferences.getInt(key, INVALID_CODE);
		}
		return value;
	}

	/**
	 * 根据KEY 查询float值
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param fileName
	 *            文件名字
	 * @param key
	 *            关键字
	 * @return float float值
	 */
	public static float getFloatValueByKey(Context mContext, String fileName, String key) {
		float value = INVALID_CODE;
		if (!StringUtil.isNullOrEmpty(key)) {
			SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
			value = sharedPreferences.getFloat(key, INVALID_CODE);
		}
		return value;
	}

	/**
	 * 避免多个地方同时修改所以加synchronized; 保存本地数据，在读取的时候就不需要加了；
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param fileName
	 *            文件名字
	 * @param key
	 *            关键字
	 * @param value
	 *            保存的对象
	 */
	public static synchronized void saveValue(Context mContext, String fileName, String key, Object value) {
		if (!StringUtil.isNullOrEmpty(key)) {
			SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			if (value != null) {
				if (value instanceof String) {
					editor.putString(key, String.valueOf(value));
				} else if (value instanceof Integer) {
					editor.putInt(key, (Integer) value);
				} else if (value instanceof Boolean) {
					editor.putBoolean(key, (Boolean) value);
				}
				editor.commit();
			}
		}
	}

	/**
	 * 避免多个地方同时修改所以加synchronized;
	 * 
	 * @Description: 移除保存的数据
	 * @param mContext
	 *            上下文对象
	 * @param fileName
	 *            文件名字
	 * @param key
	 *            待移除数据的key
	 */
	public static synchronized void removeValue(Context mContext, String fileName, String key) {
		if (!StringUtil.isNullOrEmpty(key)) {
			SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
			Editor editor = sharedPreferences.edit();
			editor.remove(key);
			editor.commit();
		}
	}

	/**
	 * 直接将对象保存，字段类型只能为String,Integer,Long,Boolean,Float,Double类型，如果有其它类型，
	 * 那么需要另做对象保存；
	 * 
	 * @param mContext
	 *            上下文
	 * @param fileName
	 *            文件名字
	 * @param obj
	 *            需要保存的对象
	 */
	public static synchronized void saveObject(Context mContext, String fileName, Object obj) {
		saveObject(mContext, fileName, obj, obj.getClass().getName());
	}

	/**
	 * 如果同一个对象可以在SharePreference中保存多个实例，如WeiboInfoModel; 可以有Sina，QQ，RenRen
	 * 直接将对象保存，字段类型只能为String,Integer,Long,Boolean,Float,Double类型，如果有其它类型，
	 * 那么需要另做对象保存；
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param fileName
	 *            文件名字
	 * @param obj
	 *            保存的对象
	 * @param keyPrefixType
	 *            前缀类型
	 */
	public static synchronized void saveObject(Context mContext, String fileName, Object obj, String keyPrefixType) {
		String prefix = "";
		if (keyPrefixType != null && keyPrefixType.length() > 0) {
			prefix = keyPrefixType + SEPARATOR;
		}
		Class<?> clazz = obj.getClass();
		EvtLog.d(TAG, "saveObject:" + clazz.getName() + "----" + obj.toString());
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		// 获取实体类的所有属性，返回Field数组
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if (field.getName() == null) {
					continue;
				}
				if (field.get(obj) == null) {
					editor.remove(prefix + field.getName());
					continue;
				}
				// 如果类型是String// 如果类型是Integer// 如果类型是Boolean 是封装类
				String type = field.getType().toString();
				if ("class java.lang.String".equals(type)) {
					editor.putString(prefix + field.getName(), field.get(obj).toString());
				} else if ("class java.lang.Integer".equals(type) || "int".equals(type)) {
					editor.putInt(prefix + field.getName(), Integer.parseInt(field.get(obj).toString()));
				} else if ("class java.lang.Boolean".equals(type) || "boolean".equals(type)) {
					editor.putBoolean(prefix + field.getName(), Boolean.parseBoolean(field.get(obj).toString()));
				} else if ("class java.lang.Long".equals(type) || "long".equals(type)) {
					editor.putLong(prefix + field.getName(), Long.parseLong(field.get(obj).toString()));
				} else if ("class java.lang.Double".equals(type) || "class java.lang.Float".equals(type)
						|| "double".equals(type) || "float".equals(type)) {
					editor.putFloat(prefix + field.getName(), Float.parseFloat(field.get(obj).toString()));
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		editor.commit();
	}

	/**
	 * 直接从SharePreference中获得需要的实体类；
	 * 
	 * @param mContext
	 *            上下问对象
	 * @param fileName
	 *            文件名字
	 * @param clazz
	 *            需要返回的实体类变量；
	 * @return Object 需要的实体类
	 * @throws
	 */
	public static Object getObject(Context mContext, String fileName, Class<?> clazz) {
		return getObject(mContext, fileName, clazz, clazz.getName());
	}

	/**
	 * 
	 * 直接从SharePreference中获得需要的实体类；
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param fileName
	 *            文件名字
	 * @param clazz
	 *            类类型
	 * @param keyPrefixType
	 *            前缀标记
	 * @return Object 实体类
	 */
	public static Object getObject(Context mContext, String fileName, Class<?> clazz, String keyPrefixType) {
		Object object = null;
		try {
			// 得到前缀字符串；
			String prefix = "";
			if (keyPrefixType != null && keyPrefixType.length() > 0) {
				prefix = keyPrefixType + SEPARATOR;
			}

			object = clazz.newInstance();
			SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
			// 获取实体类的所有属性，返回Field数组
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Class<?> typeClass = field.getType();
				Constructor<?> con;
				Object valueObj = null;
				// 如果类型是String// 如果类型是Integer// 如果类型是Boolean 是封装类
				String type = typeClass.toString();
				if ("class java.lang.String".equals(type)) {
					con = String.class.getConstructor(String.class);
					String value = sharedPreferences.getString(prefix + field.getName(), "");
					valueObj = con.newInstance(value);
				} else if ("class java.lang.Integer".equals(type) || "int".equals(type)) {
					con = Integer.class.getConstructor(String.class);
					Integer value = sharedPreferences.getInt(prefix + field.getName(), -1);
					valueObj = con.newInstance(value + "");
				} else if ("class java.lang.Boolean".equals(type) || "boolean".equals(type)) {
					con = Boolean.class.getConstructor(String.class);
					Boolean value = sharedPreferences.getBoolean(prefix + field.getName(), false);
					valueObj = con.newInstance(value + "");
				} else if ("class java.lang.Long".equals(type) || "long".equals(type)) {
					con = Long.class.getConstructor(String.class);
					Long value = sharedPreferences.getLong(prefix + field.getName(), -1);
					valueObj = con.newInstance(value + "");
				} else if ("class java.lang.Double".equals(type) || "double".equals(type)) {
					con = Double.class.getConstructor(String.class);
					Float value = sharedPreferences.getFloat(prefix + field.getName(), -1f);
					valueObj = con.newInstance(value + "");
				} else if ("class java.lang.Float".equals(type) || "float".equals(type)) {
					con = Float.class.getConstructor(String.class);
					Float value = sharedPreferences.getFloat(prefix + field.getName(), -1f);
					valueObj = con.newInstance(value + "");
				}
				if (valueObj != null) {
					if (Modifier.toString(field.getModifiers()) != null
							&& !"final".endsWith(Modifier.toString(field.getModifiers()))) {
						// 如果为final类型，那么不能赋值，不然会报错；
						field.set(object, valueObj);
					}
				}
			}
			return object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * 清除当前Model的本地存储；
	 * 
	 * @param mContext
	 *            上下文对象
	 * @param fileName
	 *            文件名字
	 * @throws
	 */
	public static void clearObject(Context mContext, String fileName) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.clear();
		editor.commit();
	}
}
