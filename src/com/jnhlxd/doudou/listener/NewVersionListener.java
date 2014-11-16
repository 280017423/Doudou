package com.jnhlxd.doudou.listener;

import com.qianjiang.framework.model.VersionInfo;

/**
 * 新版本检测回调接口
 * 
 */
public interface NewVersionListener {
	/**
	 * 
	 * @Description 检查新版本回调函数
	 * @param versionInfo 版本信心对象
	 * @author Administrator
	 * @Date 2014年4月15日 下午3:22:47
	 * 
	 */
	void onUpdateReturned(VersionInfo versionInfo);

}
