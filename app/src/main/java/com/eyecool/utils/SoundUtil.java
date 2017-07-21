package com.eyecool.utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

/**
 * Created date: 2017/7/10
 * Author:  Leslie
 * 声音工具类
 */

public class SoundUtil {
    private static final String TAG = "SoundUtil";
    private static int mSoundId;
    private static SoundPool soundPool;

    public static int init(Context context,int resId){
        soundPool = new SoundPool(3, AudioManager.STREAM_ALARM, 0);
        int soundId = soundPool.load(context, resId, 1);
        mSoundId = soundId;
        return soundId;
    }

    public static void  play(int soundId){
        soundPool.play(soundId,1.0f,1.0f,0,0,1.0f);
    }

    public static void close(int soundId){
        soundPool.unload(soundId);
        soundPool.release();
    }
}
