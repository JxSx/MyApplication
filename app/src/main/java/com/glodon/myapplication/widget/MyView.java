package com.glodon.myapplication.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.glodon.myapplication.utils.LogUtils;

public class MyView extends View {

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LogUtils.i("MyView");

    }

    public MyView(Context context) {
        super(context);
        LogUtils.i("MyView");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogUtils.i("onMeasure");
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        LogUtils.i("Width:" + measuredWidth + "Height:" + measuredHeight);
        setMeasuredDimension(measuredWidth, measuredHeight);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        LogUtils.i("onLayout");
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        LogUtils.i("dispatchDraw");

        super.dispatchDraw(canvas);
    }

    @Override
    public void draw(Canvas canvas) {
        //首先调用super.draw(canvas)完成系统的绘制，再绘制自己
        super.draw(canvas);
        LogUtils.i("draw");
        // TODO Auto-generated method stub
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LogUtils.i("onDraw");
        // TODO Auto-generated method stub
        super.onDraw(canvas);
    }

}
