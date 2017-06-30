package com.amber.livedemo.app;

import android.app.Application;

import com.ksyun.media.streamer.util.device.DeviceInfoTools;

/**
 * Created by luosiyi on 2017/6/30.
 */

public class LApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        //初始化本地存储，若本地无信息或者信息已经过期，会向服务器发起请求
        DeviceInfoTools.getInstance().init(this);
    }
}
