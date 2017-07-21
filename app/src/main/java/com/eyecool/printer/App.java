package com.eyecool.printer;

import android.app.Application;
import android.content.Context;

/**
 * Created date: 2017/7/1
 * Author:  Leslie
 */

public class App extends Application {
    Context mContext;
   public static App appInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        appInstance = (App) this.getApplicationContext();
    }

    public static App getAppInstance() {
        return appInstance;
    }

    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }
}
