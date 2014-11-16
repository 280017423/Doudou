package com.jnhlxd.doudou.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;

import com.jnhlxd.doudou.manager.SystemItemMgr;
import com.jnhlxd.doudou.model.SystemModel;
import com.qianjiang.framework.app.QJApplicationBase;
import com.qianjiang.framework.util.EvtLog;
import com.qianjiang.framework.util.PackageUtil;

/**
 * Description the class 用户头像上传工具类
 * 
 * @author zou.sq
 * @version 2012-11-9 上午10:12:20 zou.sq 用户头像上传工具类
 */
public class ImageUtil {

	public static final int BUFFER_SIZE = 1024;
	public static final String TAG = "ImageUtil";
	private static final int INSAMPLE_SIZE = 4;
	private static final int QUALITY = 50;

	public static String getImgServerUrl() {
		String url = "";
		SystemModel systemModel = SystemItemMgr.getSystemItem(ServerAPIConstant.KEY_IMG_SERVER);
		if (null != systemModel) {
			url = systemModel.getItemValue();
		}
		return url;
	}

	/**
	 * 判断是否有SD卡
	 * 
	 * @return true为有SDcard，false则表示没有
	 */
	public static boolean hasSdcard() {
		boolean hasCard = false;
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			hasCard = true;
		}
		return hasCard;
	}

	/**
	 * 
	 * 使用系统当前日期加以调整作为照片的名称
	 * 
	 * @return String 文件拍照名字
	 * @throws
	 */
	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss", Locale.CHINA);
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * 检查是否有相机
	 * 
	 * @return boolean true为有相机，false则表示没有
	 */
	public static boolean hasCamera() {
		boolean hasCamera = false;
		PackageManager pm = QJApplicationBase.CONTEXT.getPackageManager();
		if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			hasCamera = true;
		}
		return hasCamera;
	}

	/**
	 * 
	 * 压缩图片成gz文件
	 * 
	 * @param srcFile
	 *            需要被压缩的文件
	 * @param destFile
	 *            压缩后的文件
	 */
	public static void doCompressFile(File srcFile, File destFile) {
		try {
			if (null != destFile && destFile.exists()) {
				destFile.delete();
			}
			GZIPOutputStream out = null;
			try {
				out = new GZIPOutputStream(new FileOutputStream(destFile));
			} catch (FileNotFoundException e) {
				EvtLog.d(TAG, e.toString());
			}
			FileInputStream in = null;
			try {
				in = new FileInputStream(srcFile);
				byte[] buf = new byte[BUFFER_SIZE];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			} catch (FileNotFoundException e) {
				EvtLog.d(TAG, e.toString());
			} finally {
				if (null != in) {
					in.close();
				}
				if (null != out) {
					out.finish();
					out.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理图片
	 * 
	 * @param bm
	 *            所要转换的bitmap
	 * @param newWidth
	 *            新的宽
	 * @param newHeight
	 *            新的高
	 * @return 指定宽高的bitmap
	 */
	public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
		return newbm;
	}

	/**
	 * 存储裁剪后的图片到本地
	 * 
	 * @param bfile
	 *            照片的字节数组
	 * @param filePath
	 *            照片的存储路径
	 * @param fileName
	 *            照片的名字
	 * @return File 文件对象
	 */
	public static File getFile(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + "/" + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		return file;
	}

	/**
	 * 
	 * Uri转bitmap
	 * 
	 * @param uri
	 *            图像的uri对象
	 * @return Bitmap 对象
	 * @throws
	 */
	public static Bitmap getBitmapFromUri(Uri uri) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = INSAMPLE_SIZE;
			InputStream inputStream = QJApplicationBase.CONTEXT.getContentResolver().openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(inputStream, null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 加载本地图片 http://bbs.3gstdy.com
	 * 
	 * @param url
	 *            本地图片路径
	 * @return Bitmap Bitmap对象
	 */
	public static Bitmap getLoacalBitmap(String url) {
		try {
			FileInputStream fis = new FileInputStream(url);
			BitmapFactory.Options options = new Options();
			// 降低图片质量，降低OOM几率
			options.inSampleSize = INSAMPLE_SIZE;
			options.inPreferredConfig = Config.ARGB_4444;
			return BitmapFactory.decodeStream(fis, null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 
	 * @Method: getLoadPicFileFromUrl
	 * @Description: 通过url生产图片文件路径
	 * @param context
	 *            上下文
	 * @param url
	 *            网络图片文件路径
	 * @param @return
	 * @return File
	 * @throws
	 */
	public static File getLoadPicFileFromUrl(Context context, String url) {
		if (url != null) {
			int lastSpit = url.lastIndexOf("?");
			int firstSpit = url.lastIndexOf("/");
			String fileName = null;
			if (lastSpit >= 0 && firstSpit >= 0) {
				fileName = url.substring(firstSpit, lastSpit);
			}
			if (fileName == null) {
				return null;
			}
			File cacheDir = null;
			if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				cacheDir = new File(
						android.os.Environment.getExternalStorageDirectory(), PackageUtil.getConfigString("image_dir"));
			} else {
				cacheDir = context.getCacheDir();
			}
			if (!cacheDir.exists()) {
				cacheDir.mkdirs();
			}
			return new File(cacheDir, fileName);
		}
		return null;
	}

	/**
	 * 根据指定的图像路径和大小来获取缩略图 此方法有两点好处： 1.
	 * 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
	 * 第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。 2.
	 * 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使 用这个工具生成的图像不会被拉伸。
	 * 
	 * @param imagePath
	 *            图像的路径
	 * @param width
	 *            指定输出图像的宽度
	 * @param height
	 *            指定输出图像的高度
	 * @return 生成的缩略图
	 */
	public static Bitmap getImageThumbnail(String imagePath, int width, int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
	}

	/**
	 * 
	 * @Name scanMedia
	 * @Description 及时扫描拍照后的照片，在相册就能看到
	 * @param context
	 *            上下文对象
	 * @param path
	 *            照片的路径
	 * 
	 */
	public static void scanMedia(Context context, String path) {
		File file = new File(path);
		Uri uri = Uri.fromFile(file);
		Intent scanFileIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
		context.sendBroadcast(scanFileIntent);
	}

	/**
	 * 
	 * @Description 压缩图片工具类
	 * @param srcPath
	 *            原图片路径
	 * @param desPath
	 *            目标图片路径
	 * @Author zou.sq
	 * @Date 2014-8-26 下午3:39:49
	 * 
	 */
	public static void compressImage(String srcPath, String desPath) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts); // 此时返回bm为空
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f; // 这里设置高度为480f
		float ww = 480f; // 这里设置宽度为270f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1; // be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0) {
			be = 1;
		}
		newOpts.inSampleSize = be; // 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

		CompressFormat format = Bitmap.CompressFormat.JPEG;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(desPath);
		} catch (Exception e) {
			EvtLog.d(TAG, e.toString());
		}
		if (null != bitmap && null != stream) {
			bitmap.compress(format, QUALITY, stream);
		}
	}

}
