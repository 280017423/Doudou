package com.jnhlxd.doudou.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.model.DropPickModel;

/**
 * 选班级列表适配器
 * 
 * @author zou.sq
 * @since 2013-03-12 下午04:37:29
 * @version 1.0
 */
public class PopDropPickAdapter extends BaseAdapter {
	private List<DropPickModel> mDropPickModels;
	private Context mContext;

	/**
	 * @param context
	 *            上下文对象
	 * @param models
	 *            接送数据
	 */
	public PopDropPickAdapter(Context context, List<DropPickModel> models) {
		mDropPickModels = models;
		mContext = context;
	}

	@Override
	public int getCount() {
		return mDropPickModels == null ? 0 : mDropPickModels.size();
	}

	@Override
	public Object getItem(int position) {
		return mDropPickModels == null ? null : mDropPickModels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DropPickModel model = (DropPickModel) getItem(position);
		ViewHoder hoder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_pop_item, null);
			hoder = new ViewHoder();
			hoder.mTvClassName = (TextView) convertView.findViewById(R.id.tv_teacher_name);
			convertView.setTag(hoder);
		} else {
			hoder = (ViewHoder) convertView.getTag();
		}
		hoder.mTvClassName.setText(model.getSignModeName());
		if (model.isCurrentModel()) {
			hoder.mTvClassName.setBackgroundResource(R.drawable.student_signing_selector);
		} else {
			hoder.mTvClassName.setBackgroundResource(R.drawable.student_not_sign_selector);
		}
		return convertView;
	}

	class ViewHoder {
		TextView mTvClassName;
	}
}
