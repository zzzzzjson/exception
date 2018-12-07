package soexample.umeng.com.umeng.myappi;

import android.app.Application;

import soexample.umeng.com.umeng.UnCatchHandler;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        UnCatchHandler.getInstance().init(getApplicationContext());
    }
}
