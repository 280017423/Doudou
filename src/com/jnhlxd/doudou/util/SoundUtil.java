package com.jnhlxd.doudou.util;

import java.util.HashMap;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.jnhlxd.doudou.R;

/**
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class SoundUtil {

	public static void playSounds(Context context) {
		SoundPool mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		mSoundPool.load(context, R.raw.bleep, 1);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		mSoundPool.play((Integer) 1, 1, 1, 0, 0, 1);
	}
}
