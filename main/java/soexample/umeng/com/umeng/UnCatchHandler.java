package soexample.umeng.com.umeng;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UnCatchHandler implements Thread.UncaughtExceptionHandler{
    private static UnCatchHandler mUnCatchHandler = new UnCatchHandler();
    private Context mContext;

    public static UnCatchHandler getInstance() {
        return mUnCatchHandler;
    }

    public void init(Context context) {
        //获取默认的系统异常捕获器
        //把当前的crash捕获器设置成默认的crash捕获器
        Thread.setDefaultUncaughtExceptionHandler(this);
        mContext = context.getApplicationContext();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            saveSD(e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //存储sd卡
    private void saveSD(Throwable throwable) throws Exception {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.i("dj", "return");
            return;
        }

        //获取手机的一些信息
        PackageManager pm = mContext.getPackageManager();
        PackageInfo inFo = pm.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);

        //获取版本信息
        String versionName = inFo.versionName;
        int versionCode = inFo.versionCode;

        int version_code = Build.VERSION.SDK_INT;

        //Android版本号
        String release = Build.VERSION.RELEASE;
        //手机型号
        String mobile = Build.MODEL;

        //手机制造商
        String mobileName = Build.MANUFACTURER;

        //存储
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.i("dj",path);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_DD_HH_mm_ss");
        String time = simpleDateFormat.format(new Date());

        File f = new File(path, "exception");
        f.mkdirs();

        File file = new File(f.getAbsolutePath(), "exception" + time + ".txt");

        if (!file.exists()) {
            file.createNewFile();
        }

        String data = "\nMobile型号：" + mobile + "\nMobileName：" + mobileName + "\nSDK版本：" + version_code +
                "\n版本名称：" + versionName + "\n版本号：" + versionCode + "\n异常信息：" + throwable.getMessage();

        Log.i("dj", data);

        byte[] buffer = data.trim().getBytes();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        // 开始写入数据到这个文件。
        fileOutputStream.write(buffer, 0, buffer.length);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
