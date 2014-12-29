package com.jnhlxd.doudou.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

/**
 * 两种方式安装服务组件
 * 
 */
public class ApkInstaller {

	private static final int BUFFERSIZE = 1024;

	/**
	 * 
	 * @Description 把assets中服务组件apk传到SDcard中，再在SDcord中安装服务组件apk
	 * @param context
	 *            上下文对象
	 * @param assetsApk
	 *            资源路径
	 * @return boolean
	 * @Author Administrator
	 * @Date 2014年7月14日 上午10:02:02
	 * 
	 */
	public static boolean installFromAssets(Context context, String assetsApk) {
		try {
			AssetManager assets = context.getAssets();
			InputStream stream;
			stream = assets.open(assetsApk);
			if (stream == null) {
				Toast.makeText(context, "assets no apk", Toast.LENGTH_SHORT).show();
				return false;
			}

			File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
			if (!dataDir.exists()) {
				dataDir.mkdirs();
			}

			File file = new File(dataDir, "SpeechService.apk");
			if (!writeStreamToFile(stream, file)) {
				return false;
			}
			installApk(context, file);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 从输入流中写数据到一个文件中。
	 * 
	 * @param stream
	 * @param file
	 */
	private static boolean writeStreamToFile(InputStream stream, File file) {
		OutputStream output = null;
		try {
			output = new FileOutputStream(file);
			final byte[] buffer = new byte[BUFFERSIZE];
			int read;
			while ((read = stream.read(buffer)) != -1) {
				output.write(buffer, 0, read);
			}
			output.flush();
		} catch (Exception e1) {
			e1.printStackTrace();
			return false;
		} finally {
			try {
				if (null != output) {
					output.close();
				}
				if (null != stream) {
					stream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @Description 根据apk文件路径安装apk包
	 * @param context
	 *            上下文对象
	 * @param apkFile
	 *            apk安装文件
	 * @Author Administrator
	 * @Date 2014年7月15日 下午3:39:02
	 * 
	 */
	private static void installApk(Context context, File apkFile) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
		context.startActivity(intent);
	}
}
