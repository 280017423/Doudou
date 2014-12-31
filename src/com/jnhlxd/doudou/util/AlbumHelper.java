package com.jnhlxd.doudou.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.MediaColumns;
import android.text.TextUtils;
import android.util.Log;

import com.jnhlxd.doudou.model.ImageAlbumModel;
import com.jnhlxd.doudou.model.ImageItemModel;
import com.qianjiang.framework.util.EvtLog;

/**
 * 
 * 相册工具类
 * 
 * @author Administrator
 * 
 */
public class AlbumHelper {
	private static final String TAG = "AlbumHelper";
	private static final int QUALITY = 80;
	private static AlbumHelper INSTANCE;
	private boolean mHasBuildImagesBucketList;

	private Context mContext;
	private ContentResolver mResolver;
	private HashMap<String, String> mThumbnailMap = new HashMap<String, String>();
	private List<HashMap<String, String>> mAlbumList = new ArrayList<HashMap<String, String>>();
	private HashMap<String, ImageAlbumModel> mBucketMap = new HashMap<String, ImageAlbumModel>();

	private AlbumHelper() {
	}

	/**
	 * @Description 单例模式
	 * @return 当前实例
	 */
	public static AlbumHelper getHelper() {
		if (INSTANCE == null) {
			INSTANCE = new AlbumHelper();
		}
		return INSTANCE;
	}

	/**
	 * 
	 * @Description 初始化
	 * @param context
	 *            上下文对象
	 * 
	 */
	public void init(Context context) {
		if (this.mContext == null) {
			this.mContext = context;
			mResolver = context.getContentResolver();
		}
	}

	private void getThumbnail() {
		Cursor cursor = null;
		try {
			String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID, Thumbnails.DATA };
			cursor = mResolver.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null, null, null);
			getThumbnailColumnData(cursor);
		} catch (Exception e) {
			EvtLog.e(TAG, e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
	}

	void getAlbum() {
		Cursor cursor = null;
		try {
			String[] projection = {
					Albums._ID,
					Albums.ALBUM,
					Albums.ALBUM_ART,
					Albums.ALBUM_KEY,
					Albums.ARTIST,
					Albums.NUMBER_OF_SONGS };
			cursor = mResolver.query(Albums.EXTERNAL_CONTENT_URI, projection, null, null, null);
			getAlbumColumnData(cursor);
		} catch (Exception e) {
			EvtLog.e(TAG, e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
	}

	void buildImagesBucketList() {
		Cursor cur = null;
		try {
			long startTime = System.currentTimeMillis();

			// 构造缩略图索引
			getThumbnail();
			// 构造相册索引
			String columns[] = new String[] {
					Media._ID,
					Media.BUCKET_ID,
					Media.PICASA_ID,
					Media.DATA,
					Media.DISPLAY_NAME,
					Media.TITLE,
					Media.SIZE,
					Media.BUCKET_DISPLAY_NAME };
			// 得到一个游标
			cur = mResolver.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, null);
			if (cur.moveToFirst()) {
				// 获取指定列的索引
				int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
				int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
				int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
				int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
				int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
				int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
				int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
				int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
				do {
					String _id = cur.getString(photoIDIndex);
					String name = cur.getString(photoNameIndex);
					String path = cur.getString(photoPathIndex);
					String title = cur.getString(photoTitleIndex);
					String size = cur.getString(photoSizeIndex);
					String bucketName = cur.getString(bucketDisplayNameIndex);
					String bucketId = cur.getString(bucketIdIndex);
					String picasaId = cur.getString(picasaIdIndex);

					Log.i(TAG, _id + ", bucketId: " + bucketId + ", picasaId: " + picasaId + " name:" + name + " path:"
							+ path + " title: " + title + " size: " + size + " bucket: " + bucketName + "---");

					ImageAlbumModel bucket = mBucketMap.get(bucketId);
					if (bucket == null) {
						bucket = new ImageAlbumModel();
						mBucketMap.put(bucketId, bucket);
						bucket.imageList = new ArrayList<ImageItemModel>();
						bucket.bucketName = bucketName;
					}
					bucket.count++;
					ImageItemModel imageItem = new ImageItemModel();
					imageItem.imageId = _id;
					imageItem.imagePath = path;
					imageItem.thumbnailPath = mThumbnailMap.get(_id);
					bucket.imageList.add(imageItem);

				} while (cur.moveToNext());
			}

			Iterator<Entry<String, ImageAlbumModel>> itr = mBucketMap.entrySet().iterator();
			while (itr.hasNext()) {
				Map.Entry<String, ImageAlbumModel> entry = (Map.Entry<String, ImageAlbumModel>) itr.next();
				ImageAlbumModel bucket = entry.getValue();
				Log.d(TAG, entry.getKey() + ", " + bucket.bucketName + ", " + bucket.count + " ---------- ");
				for (int i = 0; i < bucket.imageList.size(); ++i) {
					ImageItemModel image = bucket.imageList.get(i);
					Log.d(TAG, "----- " + image.imageId + ", " + image.imagePath + ", " + image.thumbnailPath);
				}
			}
			mHasBuildImagesBucketList = true;
			long endTime = System.currentTimeMillis();
			Log.d(TAG, "use time: " + (endTime - startTime) + " ms");
		} catch (Exception e) {
			EvtLog.e(TAG, e.getMessage());
		} finally {
			if (cur != null && !cur.isClosed()) {
				cur.close();
			}
		}
	}

	public List<ImageAlbumModel> getImagesBucketList(boolean refresh) {
		if (refresh || (!refresh && !mHasBuildImagesBucketList)) {
			mThumbnailMap.clear();
			mAlbumList.clear();
			mBucketMap.clear();
			buildImagesBucketList();
		}

		List<ImageAlbumModel> tmpList = new ArrayList<ImageAlbumModel>();
		Iterator<Entry<String, ImageAlbumModel>> itr = mBucketMap.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ImageAlbumModel> entry = (Map.Entry<String, ImageAlbumModel>) itr.next();
			tmpList.add(entry.getValue());
		}
		return tmpList;
	}

	String getOriginalImagePath(String image_id) {
		String path = null;
		Cursor cursor = null;
		try {
			String[] projection = { Media._ID, Media.DATA };
			cursor = mResolver.query(Media.EXTERNAL_CONTENT_URI, projection, Media._ID + "=" + image_id, null, null);
			if (cursor != null) {
				cursor.moveToFirst();
				path = cursor.getString(cursor.getColumnIndex(Media.DATA));

			}
		} catch (Exception e) {
			EvtLog.e(TAG, e.getMessage());
		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return path;
	}

	private void getThumbnailColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int _id;
			int image_id;
			String image_path;
			int _idColumn = cur.getColumnIndex(Thumbnails._ID);
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

			do {
				_id = cur.getInt(_idColumn);
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);

				// Do something with the values.
				Log.i(TAG, _id + " image_id:" + image_id + " path:" + image_path + "---");
				mThumbnailMap.put("" + image_id, image_path);
			} while (cur.moveToNext());
		}
	}

	private void getAlbumColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int _id;
			String album;
			String albumArt;
			String albumKey;
			String artist;
			int numOfSongs;

			int _idColumn = cur.getColumnIndex(Albums._ID);
			int albumColumn = cur.getColumnIndex(Albums.ALBUM);
			int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
			int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
			int artistColumn = cur.getColumnIndex(Albums.ARTIST);
			int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				album = cur.getString(albumColumn);
				albumArt = cur.getString(albumArtColumn);
				albumKey = cur.getString(albumKeyColumn);
				artist = cur.getString(artistColumn);
				numOfSongs = cur.getInt(numOfSongsColumn);

				// Do something with the values.
				Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt + "albumKey: " + albumKey + " artist: "
						+ artist + " numOfSongs: " + numOfSongs + "---");
				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("_id", _id + "");
				hash.put("album", album);
				hash.put("albumArt", albumArt);
				hash.put("albumKey", albumKey);
				hash.put("artist", artist);
				hash.put("numOfSongs", numOfSongs + "");
				mAlbumList.add(hash);

			} while (cur.moveToNext());

		}
	}

	/**
	 * 通知系统媒体库扫描指定路径
	 * 
	 * @param imgPath
	 */
	public void refreshSystemAlbum(String picPath) {
		utils.refreshSystemAlbum(picPath);
	}

	Utils utils = new Utils();

	class Utils {
		public boolean saveImg(Bitmap bmp, String savePath) {
			if (TextUtils.isEmpty(savePath)) {
				return false;
			}

			boolean saveOk = false;
			try {
				saveOk = bmp.compress(CompressFormat.JPEG, QUALITY, new FileOutputStream(new File(savePath)));
				if (saveOk) {
					refreshSystemAlbum(savePath);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return saveOk;
		}

		public void refreshSystemAlbum(String picPath) {
			Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			Uri uri = Uri.fromFile(new File(picPath));
			intent.setData(uri);
			mContext.sendBroadcast(intent);
		}

		/**
		 * 调用 MediaStore.Images.Media.insertImage方法将图片插入系统相册后，根据返回的uri得到图片路径
		 * 
		 * @param context
		 * @param uri
		 * @return
		 */
		public String getFilePathByContentResolver(Context context, Uri uri) {
			Cursor c = null;
			String filePath = null;
			try {
				if (null == uri) {
					return null;
				}
				c = mResolver.query(uri, null, null, null, null);
				filePath = null;
				if (null == c) {
					throw new IllegalArgumentException("Query on " + uri + " returns null result.");
				}
				try {
					if ((c.getCount() != 1) || !c.moveToFirst()) {
					} else {
						filePath = c.getString(c.getColumnIndexOrThrow(MediaColumns.DATA));
					}
				} finally {
					c.close();
				}
				return filePath;
			} catch (Exception e) {
				EvtLog.e(TAG, e.getMessage());
			} finally {
				if (c != null && !c.isClosed()) {
					c.close();
				}
			}

			return filePath;
		}
	}
}
