package com.jnhlxd.doudou.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jnhlxd.doudou.R;
import com.jnhlxd.doudou.model.ImageItemModel;
import com.jnhlxd.doudou.util.BitmapCache;
import com.jnhlxd.doudou.util.BitmapCache.ImageCallback;

public class ImageGridAdapter extends BaseAdapter {
	final String TAG = getClass().getSimpleName();

	private Context mContext;
	private ArrayList<ImageItemModel> mImageList;
	private BitmapCache mCache;

	private ImageCallback imgCallback = new ImageCallback() {
		@Override
		public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
			if (imageView != null && bitmap != null) {
				String url = (String) params[0];
				if (url != null && url.equals((String) imageView.getTag())) {
					((ImageView) imageView).setImageBitmap(bitmap);
				} else {
					Log.e(TAG, "callback, bmp not match");
				}
			} else {
				Log.e(TAG, "callback, bmp null");
			}
		}
	};

	public ImageGridAdapter(Context context, ArrayList<ImageItemModel> list) {
		this.mContext = context;
		mImageList = list;
		mCache = new BitmapCache();
	}

	@Override
	public int getCount() {
		int count = 0;
		if (mImageList != null) {
			count = mImageList.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		return mImageList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = View.inflate(mContext, R.layout.view_photo_item, null);
			holder.iv = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		final ImageItemModel item = mImageList.get(position);
		holder.iv.setTag(item.imagePath);
		mCache.displayBmp(holder.iv, item.thumbnailPath, item.imagePath, imgCallback);
		return convertView;
	}

	class Holder {
		private ImageView iv;
	}

}
