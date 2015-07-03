package com.glodon.myapplication.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理工具;
 * <p>当程序发生了未捕获的异常时,由此类来接管程序，并记录设备信息和错误日志
 *
 * @author jiax-a
 */
public class CrashHandler implements UncaughtExceptionHandler {


    /**
     * ↓↓↓↓↓单例设计，只有一个崩溃处理类↓↓↓↓↓↓↓↓↓↓
     */
    private static CrashHandler crashHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        if (crashHandler == null) {
            return new CrashHandler();
        }
        return crashHandler;
    }

    /**
     * ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
     */


    private Context mContext;
    /**
     * 获取系统默认的异常处理器
     */
    private UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;
    /**
     * 标记是否需要错误提示
     */
    private static final boolean isPrompt = false;
    private static final String TAG = "CrashHandler";
    /**
     * 用来存储设备信息和异常信息
     */
    private Map<String, String> infos = new HashMap<String, String>();


    /**
     * 初始化崩溃处理类
     */
    public void init(Context context) {
        mContext = context;
        mDefaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置当前的处理类为默认异常处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        boolean isHandle = myHandleException(ex);
        if (!isHandle) {
            if (mDefaultUncaughtExceptionHandler != null) {
                //如果程序员没有处理异常，则交给系统的异常处理器
                mDefaultUncaughtExceptionHandler.uncaughtException(thread, ex);
            } else {
                //如果默认异常处理器为空，则需要退出程序。
                exitSystem();
            }
        }
    }

    /**
     * 系统退出
     */
    private void exitSystem() {
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    /**
     * 自定义异常处理方式
     *
     * @param ex
     * @return 如果处理了该异常信息返回true;否则返回false.
     */
    private boolean myHandleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        // 收集设备参数信息
        collectDeviceInfo();
        // 保存日志文件
        saveCrashInfo2File(ex);
        if (isPrompt) {
            // 使用Toast来显示异常信息
            new Thread() {
                @Override
                public void run() {
//					Looper.prepare();
//					Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出.", Toast.LENGTH_LONG).show();
//					Looper.loop();
//					SystemClock.sleep(2000);
//					exitSystem();
                }
            }.start();
        } else {
            exitSystem();
        }
        return true;
    }

    /**
     * 保存日志文件
     *
     * @param ex
     * @return 返回文件名，便于传到服务器端
     */
    private String saveCrashInfo2File(Throwable ex) {

        Log.e(TAG, "错误信息", ex);

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : infos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key + "=" + value + "\n");
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String time = format.format(new Date());
            String fileName = "crash-" + time + "-" + timestamp + ".log";
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory().getPath() + "/" + AppUtils.getAppName(mContext) + "/Logs";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(path + "/" + fileName);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occured while writing file...", e);
        }
        return null;
    }

    /**
     * 收集设备参数信息
     */
    private void collectDeviceInfo() {
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                String versionCode = packageInfo.versionCode + "";
                String versionName = packageInfo.versionName == null ? "null" : packageInfo.versionName;
                infos.put("PackageName", mContext.getPackageName());
                infos.put("VersionCode", versionCode);
                infos.put("VersionName", versionName);
            }
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    infos.put(field.getName(), field.get(null).toString());
                } catch (IllegalAccessException e) {
                    Log.e(TAG, "an error occured when collect package info", e);
                } catch (IllegalArgumentException e) {
                    Log.e(TAG, "an error occured when collect package info", e);
                }
            }
        } catch (NameNotFoundException e) {
            Log.e(TAG, "an error occured when collect package info", e);
        }
    }

}
