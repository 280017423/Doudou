package com.jnhlxd.doudou.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.GZIPOutputStream;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;

import com.jnhlxd.doudou.app.DoudouApplication;
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
		PackageManager pm = DoudouApplication.CONTEXT.getPackageManager();
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
				in.close();
				out.finish();
				out.close();
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
			InputStream inputStream = DoudouApplication.CONTEXT.getContentResolver().openInputStream(uri);
			bitmap = BitmapFactory.decodeStream(inputStream, null, options);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 
	 * @Method: getBitmapFromWeb
	 * @Description: 通过uri 从网络拿取图片
	 * @param url
	 *            图片网络路径
	 * @param file
	 *            写入图片的文件
	 * @param @return
	 * @return Bitmap
	 * @throws
	 */
	public static Bitmap getBitmapFromWeb(String url, File file) {
		try {
			file.delete();
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(file);
			final int bufferSize = 1024;
			try {
				byte[] bytes = new byte[bufferSize];
				for (;;) {
					int count = is.read(bytes, 0, bufferSize);
					if (count == -1) {
						break;
					}
					os.write(bytes, 0, count);
				}
			} catch (Exception ex) {
				EvtLog.w(TAG, ex);
			}
			os.close();
			is.close();
			return BitmapFactory.decodeFile(file.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
}
