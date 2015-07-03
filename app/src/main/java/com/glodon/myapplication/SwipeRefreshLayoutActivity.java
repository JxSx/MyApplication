package com.glodon.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by jiax-a on 2015/5/19.
 */
public class SwipeRefreshLayoutActivity extends Activity {

    @InjectView(R.id.listview)
    protected ListView listview;
    @InjectView(R.id.swiperefreshlayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_swipe_refresh_layout);

        ButterKnife.inject(this);

        String[] strings = new String[15];
        for (int i = 0; i < 15; i++) {
            strings[i] = "小编开发中..." + i;
        }
        listview.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, strings));

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mSwipeRefreshLayout.isRefreshing()) {
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, 5000);
            }
        });
        mSwipeRefreshLayout.setColorSchemeResources(
                R.color.swiperefresh_color1, R.color.swiperefresh_color2,
                R.color.swiperefresh_color3, R.color.swiperefresh_color4);
    }

}
