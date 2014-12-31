package com.jnhlxd.doudou.util;

import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.qianjiang.framework.util.EvtLog;

public class BitmapCache {
	private static final String TAG = "BitmapCache";
	private static final int MAX_WIDTH = 128;
	private static final int MAX_HEIGHT = 128;

	private ExecutorService mFixedThreadPool = Executors.newFixedThreadPool(2);
	private LruCache<String, Bitmap> mImageCache;
	{
		initMemoryCache();
	}

	void initMemoryCache() {
		final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
		final int cacheSize = maxMemory / 8;
		mImageCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				int size = bitmap.getRowBytes() * bitmap.getHeight() / 1024;
				return size;
			}
		};
	}

	void put(String path, Bitmap bmp) {
		if (!TextUtils.isEmpty(path) && bmp != null) {
			mImageCache.put(path, bmp);
		}
	}

	Bitmap get(String key) {
		try {
			return mImageCache.get(key);
		} catch (Throwable e) {
			Log.e(TAG, "get: " + key, e);
		}
		return null;
	}

	public void displayBmp(final ImageView iv, final String thumbPath, final String sourcePath,
			final ImageCallback callback) {
		if (TextUtils.isEmpty(thumbPath) && TextUtils.isEmpty(sourcePath)) {
			return;
		}
		final String path;
		final boolean isThumbPath;
		if (!TextUtils.isEmpty(thumbPath) && isBitmapExist(thumbPath)) {
			path = thumbPath;
			isThumbPath = true;
			EvtLog.d(TAG, "use thumbnails");
		} else if (!TextUtils.isEmpty(sourcePath) && isBitmapExist(sourcePath)) {
			path = sourcePath;
			isThumbPath = false;
			EvtLog.d(TAG, "use source");
		} else {
			EvtLog.e(TAG, "thumbnails and source are all not exist");
			iv.setImageBitmap(null);
			return;
		}

		Bitmap bmp = get(path);
		if (bmp != null) {
			if (callback != null) {
				callback.imageLoad(iv, bmp, sourcePath);
			}
			Log.d(TAG, "hit cache");
			return;
		}
		iv.setImageBitmap(null);

		mFixedThreadPool.execute(new Runnable() {

			@Override
			public void run() {
				final Bitmap thumb;
				if (isThumbPath) {
					// 如果缩略图太大，就再缩小一点
					BitmapFactory.Options o = null;
					try {
						o = new BitmapFactory.Options();
						o.inJustDecodeBounds = true;
						BitmapFactory.decodeStream(new FileInputStream(path), null, o);
					} catch (Exception e) {
						EvtLog.e(TAG, e);
						o = null;
					}
					if (o != null && (o.outWidth > MAX_WIDTH || o.outHeight > MAX_HEIGHT)) {
						BitmapFactory.Options opts = getResizeBmpOption(new File(path), MAX_WIDTH, MAX_HEIGHT);
						thumb = BitmapFactory.decodeFile(path, opts);
					} else {
						thumb = BitmapFactory.decodeFile(path);
					}
				} else {
					BitmapFactory.Options opts = getResizeBmpOption(new File(path), MAX_WIDTH, MAX_HEIGHT);
					thumb = BitmapFactory.decodeFile(path, opts);
				}
				Log.d(TAG, isThumbPath + ", decodeFile " + path + ", " + thumb);
				put(path, thumb);

				if (callback != null) {
					h.postDelayed(new Runnable() {
						@Override
						public void run() {
							callback.imageLoad(iv, thumb, sourcePath);
						}
					}, 20);
				}
			}
		});
	}

	Handler h = new Handler();

	public BitmapFactory.Options getResizeBmpOption(File f, int requiredSizeW, int requiredSizeH) {
		try {
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);
			int scale = 1;
			if (requiredSizeW > 0 || requiredSizeH > 0) {
				while (true) {
					int scaleSizeW = o.outWidth / scale;
					int scaleSizeH = o.outHeight / scale;
					if ((!(requiredSizeW > 0) || (requiredSizeW > 0 && scaleSizeW <= requiredSizeW))
							&& (!(requiredSizeH > 0) || (requiredSizeH > 0 && scaleSizeH <= requiredSizeH))) {
						break;
					}
					scale += scale;
				}
			}
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			o2.inPurgeable = true;
			o2.inPreferredConfig = Config.RGB_565;
			return o2;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isBitmapExist(String imgLocalPath) {
		File temp = new File(imgLocalPath);
		if (temp.exists() && temp.isFile() && temp.length() > 100) {
			return true;
		}
		return false;
	}

	public interface ImageCallback {
		public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params);
	}
}
