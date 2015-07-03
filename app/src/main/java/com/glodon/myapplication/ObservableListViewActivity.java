package com.glodon.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import butterknife.InjectView;

/**
 * Created by jiax-a on 2015/6/5.
 */
public class ObservableListViewActivity extends ActionBarActivity implements ObservableScrollViewCallbacks {

    @InjectView(R.id.scrollable)
    ObservableListView observableListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ob_listview);


    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b2) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}
