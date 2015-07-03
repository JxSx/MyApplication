package com.glodon.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.glodon.myapplication.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class RecyclerViewActivity extends ActionBarActivity {

    @InjectView(R.id.recyclerview)
    RecyclerView recyclerView;
    @InjectView(R.id.btn)
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_compat);
//        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.img6);
//        Palette
//        Palette palette = Palette.generate(bm);
//        if (palette.getLightVibrantColor() != null) {
//            name.setBackgroundColor(palette.getLightVibrantColor().getRgb());
//            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(palette.getLightVibrantColor().getRgb()));
//            // getSupportActionBar().
//
//        }
        ButterKnife.inject(this);
        // 创建一个线性布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
//        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        // 设置布局管理器
        /*RecyclerView 首先的一个特点就是，将 layout 抽象成了一个 LayoutManager，RecylerView 不负责子
        View 的布局，我们可以自定义 LayoutManager 来实现不同的布局效果，提供了LinearLayoutManager和GridLayoutManager。
        LinearLayoutManager 可以指定方向，默认是垂直， 可以指定水平， 这样就轻松实现了水平的 ListView。*/
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // 创建数据集
//        String[] dataset = new String[100];
        final List<String> dataset = new ArrayList<String>();
        for (int i = 0; i < 100; i++) {

            dataset.add("item" + i);
        }
//        // 创建Adapter，并指定数据集
        final MyAdapter adapter = new MyAdapter(dataset);
        // 设置Adapter
        recyclerView.setAdapter(adapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataset.remove(0);
                adapter.notifyItemRemoved(0);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_app_compat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
