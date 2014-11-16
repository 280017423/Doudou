package com.jnhlxd.doudou.listener;

/**
 * 添加图片Item回调接口
 * 
 */
public interface ImageGridItemListener {
	/**
	 * 删除Item点击回调
	 * 
	 * @param position
	 *            点击位置
	 */
	void onItemDelClick(int position);

	/**
	 * 添加Item点击回调
	 * 
	 */
	void onAddPhotoClick();
}
