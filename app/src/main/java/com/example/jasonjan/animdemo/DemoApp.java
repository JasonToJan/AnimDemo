package com.example.jasonjan.animdemo;

import android.app.Application;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * tips :
 * *
 * time : 18-10-19上午11:25
 * owner: jasonjan
 */
public class DemoApp extends Application {

    private static DemoApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;

        initLogger();
    }

    private void initLogger() {
        //DEBUG版本才打控制台log
        if (BuildConfig.DEBUG) {
            Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder().
                    tag(getString(R.string.app_name)).build()));
        }
    }

    public static DemoApp getInstance() {
        return instance;
    }
}
