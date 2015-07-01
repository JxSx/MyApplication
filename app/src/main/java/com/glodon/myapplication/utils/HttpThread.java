package com.glodon.myapplication.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;

public class HttpThread extends Thread {

    private static final String TAG = HttpThread.class.getSimpleName();
    /**
     * Handler中Message消息的标记
     */
    private static final int WHAT = 0;

    private byte[] sLock = new byte[0]; // 锁
    private boolean bStop = false;
    private int mID = 0;//唯一表示该线程的标志（不能重复）
    private IHttpRunnable mRunnable;//唯一表示该线程的标志（不能重复）
    private IHttpResult mResult;//唯一表示该线程的标志（不能重复）

    public static HttpThread quickRequest(int id, IHttpRunnable runnable, IHttpResult result) {
        HttpThread httpThread = new HttpThread(id, runnable, result);
        httpThread.start();
        return httpThread;
    }

    public final int getID() {
        return mID;
    }

    /**
     * 中断指定的id线程的循环执行
     *
     * @param id
     */
    public void stopRuning(int id) {
        synchronized (sLock) {
            if (mID != id) {
                return;
            }
            stopRuning();
        }
    }

    /**
     * 中断线程执行
     */
    public void stopRuning() {
        try {
            synchronized (sLock) {
                interrupt();
                bStop = true;
                mHandler.removeMessages(0);
            }
        } catch (Exception e) {
        }
    }

    /**
     * 判断指定的id线程是否执行
     *
     * @param id
     * @return
     */
    public boolean isRuning(int id) {
        synchronized (sLock) {
            if (mID != id) {
                return false;
            }
            return isRuning();
        }
    }

    /**
     * 判断线程是否运行
     *
     * @return
     */
    public boolean isRuning() {
        synchronized (sLock) {
            State st = this.getState();
            return (st != State.TERMINATED && st != State.NEW);
        }
    }

    /**
     * 判断执行的id线程是否停止运行
     *
     * @param id
     * @return
     */
    public boolean isStopRuning(int id) {
        synchronized (sLock) {
            if (mID != id) {
                return false;
            }
            return isStopRuning();
        }
    }

    /**
     * 判断执行的id线程是否停止运行
     *
     * @return
     */
    public boolean isStopRuning() {
        synchronized (sLock) {
            return bStop;
        }
    }

    @Override
    public void run() {
        String obj = null;
        int n = 0;
        try {
            do {
                LogUtils.i(TAG, "线程执行了" + mID);
                if (isStopRuning()) {
                    return;
                }
                if (mRunnable != null) {
                    obj = mRunnable.execute(this);
                }
                if (obj != null) {
                    //如果请求到数据，则跳出循环
                    return;
                }
                SystemClock.sleep(300);
            } while (n++ < 1 && !isStopRuning());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mHandler.removeMessages(WHAT);
            //因为中断线程或者请求数据为空时，不需要发送消息处理结果
            if (!isStopRuning()) {
                mHandler.sendMessage(mHandler.obtainMessage(WHAT, mID, 0, obj));
            }
        }
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (mResult != null) {
                mResult.onResult(msg.arg1, (String) msg.obj);
            }
        }
    };

    /**
     * 私有的构造函数
     *
     * @param id
     */
    private HttpThread(int id, IHttpRunnable runnable, IHttpResult result) {
        this.mID = id;
        this.mRunnable = runnable;
        this.mResult = result;
    }

    /**
     * 接口回调，复写execute方法，具体执行请求网络的代码
     *
     * @author jiax-a
     */
    public interface IHttpRunnable {
        /**
         * @param t
         * @return
         */
        public String execute(HttpThread t);
    }

    /**
     * 对处理结果的监听
     *
     * @author jiax-a
     */
    public interface IHttpResult {
        public void onResult(int id, String obj);
    }
}
