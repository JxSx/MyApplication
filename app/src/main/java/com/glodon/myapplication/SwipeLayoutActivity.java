package com.glodon.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.daimajia.swipe.SwipeLayout;
import com.glodon.myapplication.adapter.ListViewAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class SwipeLayoutActivity extends Activity {


    private static final String TAG = SwipeLayoutActivity.class.getSimpleName();
    @InjectView(R.id.swipelayout)
    protected SwipeLayout swipeLayout;
    @InjectView(R.id.bottom_wrapper)
    protected LinearLayout bottom_wrapper;
    @InjectView(R.id.surface_view)
    protected LinearLayout surface_view;
    @InjectView(R.id.listview)
    protected ListView listview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_layout);

        ButterKnife.inject(this);

        listview.setAdapter(new ListViewAdapter(this));


        /**
         * 单个Item布局的SwipeLayout使用方式
         */
        //设置显示模式：默认随手势滑出，LayDown的效果是：隐藏在下面出现
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, bottom_wrapper);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Bottom, bottom_wrapper);
        swipeLayout.addDrag(SwipeLayout.DragEdge.Top, bottom_wrapper);

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout swipeLayout) {
                Log.i(TAG, "onStartOpen");
            }

            @Override
            public void onOpen(SwipeLayout swipeLayout) {
                Log.i(TAG, "onOpen");
            }

            @Override
            public void onStartClose(SwipeLayout swipeLayout) {
                Log.i(TAG, "onStartClose");
            }

            @Override
            public void onClose(SwipeLayout swipeLayout) {
                Log.i(TAG, "onClose");
            }

            @Override
            public void onUpdate(SwipeLayout swipeLayout, int i, int i2) {
            }

            @Override
            public void onHandRelease(SwipeLayout swipeLayout, float v, float v2) {
                Log.i(TAG, "onHandRelease");
            }
        });
    }

}
