package com.liudonghan.gallery;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import androidx.annotation.RequiresApi;

/**
 * Description：
 *
 * @author Created by: Li_Min
 * Time:1/10/23
 */
public class App extends Application {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder =new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

    }
}
