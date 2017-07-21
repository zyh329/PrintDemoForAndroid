package com.eyecool.utils;

import android.widget.Toast;

import com.eyecool.printer.App;

/**
 * Created by nj on 2016/11/30.
 */
public class ToastUtil {
    private static Toast toast;
    private static boolean isDebug = true;

    /**
     * 单例吐司方法
     *
     * @param text
     */
    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(App.appInstance, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);        //如果不为空，直接改变toast当前的文本
        }
        if (isDebug) {
            toast.show();
        }
    }
}
