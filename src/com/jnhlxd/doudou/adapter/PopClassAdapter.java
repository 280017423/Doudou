package com.jnhlxd.doudou.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.model.ClassInfoModel;

/**
 * 选班级列表适配器
 * 
 * @author zou.sq
 * @since 2013-03-12 下午04:37:29
 * @version 1.0
 */
public class PopClassAdapter extends BaseAdapter {
	private List<ClassInfoModel> mClassInfoModels;
	private Context mContext;

	/**
	 * @param context
	 *            上下文对象
	 * @param models
	 *            班级数据
	 */
	public PopClassAdapter(Context context, List<ClassInfoModel> models) {
		mClassInfoModels = models;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mClassInfoModels == null ? 0 : mClassInfoModels.size();
	}

	@Override
	public Object getItem(int position) {
		return mClassInfoModels == null ? null : mClassInfoModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ClassInfoModel classInfoModel = (ClassInfoModel) getItem(position);
		ViewHoder hoder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_pop_item, null);
			hoder = new ViewHoder();
			hoder.mTvClassName = (TextView) convertView.findViewById(R.id.tv_teacher_name);
			convertView.setTag(hoder);
		} else {
			hoder = (ViewHoder) convertView.getTag();
		}
		if (classInfoModel.isCurrentModel()) {
			hoder.mTvClassName.setBackgroundResource(R.drawable.student_signing_selector);
		} else {
			hoder.mTvClassName.setBackgroundResource(R.drawable.student_not_sign_selector);
		}
		hoder.mTvClassName.setText(classInfoModel.getClassName());
		return convertView;
	}

	class ViewHoder {
		TextView mTvClassName;
	}
}
