package com.glodon.myapplication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.glodon.myapplication.widget.ActionBarDrawerToggle;
import com.glodon.myapplication.widget.DraggableFlagView;
import com.glodon.myapplication.widget.DrawerArrowDrawable;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements DraggableFlagView.OnDraggableFlagViewListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @InjectView(R.id.content_frame)
    protected RelativeLayout content_frame;//主界面内容区域
    @InjectView(R.id.left_drawer)
    protected ListView left_drawer;//侧滑抽屉区
    @InjectView(R.id.drawer_layout)
    protected DrawerLayout drawer_layout;//父布局
    DrawerArrowDrawable drawerArrow;
    //    ActionBarDrawerToggle toggle;
    ActionBarDrawerToggle toggle;
    String[] items = {"SwipeRefreshLayout", "SwipeLayout", "SlidingActivity", "RecyclerView", "CircleImageView", "ObservalbelListView"};
    private SystemBarTintManager tintManager;

    private View mDecorView;

    private boolean hasHideSystemUi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initWindow();
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

//        new MyView(this);
        DraggableFlagView draggableFlagView = (DraggableFlagView) findViewById(R.id.main_dfv);
        draggableFlagView.setOnDraggableFlagViewListener(this);
        draggableFlagView.setText("7");


        initListView();

        //设置抽屉滑动时边界的渐变阴影
        drawer_layout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        restoreActionBar();
        drawerArrow = new DrawerArrowDrawable(this) {
            @Override
            public boolean isLayoutRtl() {
                return true;
            }
        };
        toggle = new ActionBarDrawerToggle(this, drawer_layout, drawerArrow, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                //对抽屉的监听，关闭后调用此方法
                invalidateOptionsMenu();
                Log.i(TAG, "close");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                Log.i(TAG, "open");
            }
        };
       /* toggle = new ActionBarDrawerToggle(this, drawer_layout,R.drawable.ic_drawer, R.string.drawer_close,R.string.drawer_open){
            @Override
            public void onDrawerClosed(View drawerView) {
                //对抽屉的监听，关闭后调用此方法
                invalidateOptionsMenu();
                Log.i(TAG, "close");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                Log.i(TAG,"open");
            }
        };*/
        drawer_layout.setDrawerListener(toggle);

        getOverflowMenu();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//            if (hasFocus) {
//                getWindow().getDecorView().setSystemUiVisibility(
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                | View.SYSTEM_UI_FLAG_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//            }
    }

    //force to show overflow menu in actionbar for android 4.4 below
    private void getOverflowMenu() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeButtonEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    /**
     * 初始化侧滑菜单
     */
    private void initListView() {

        left_drawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
        left_drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(MainActivity.this, SwipeRefreshLayoutActivity.class);
                        startActivity(intent);
                        drawer_layout.closeDrawer(left_drawer);
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, SwipeLayoutActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, SlidingActivity.class));
                        break;
                    case 3:

                        startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
                        break;
                    case 4:
                        startActivity(new Intent(MainActivity.this, CircleImageView.class));
                        break;
                    case 5:
                        startActivity(new Intent(MainActivity.this, ObservableListViewActivity.class));
                        break;
                }
            }
        });
    }


    @Override
    public void onFlagDismiss(DraggableFlagView view) {
        Toast.makeText(this, "onFlagDismiss", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        toggle.syncState();


    }


    protected int getColor(int res) {
        if (res <= 0)
            throw new IllegalArgumentException("resource id can not be less 0");
        return getResources().getColor(res);
    }

    @TargetApi(19)
    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(getColor(R.color.colorPrimary));
            tintManager.setStatusBarTintEnabled(true);
        }
        mDecorView = getWindow().getDecorView();
    }

    @TargetApi(19)
    public void hideSystemUI() {
        hasHideSystemUi = true;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (mDecorView != null) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }

        if (tintManager != null) {
            tintManager.setStatusBarTintEnabled(false);
        }
    }

    @TargetApi(16)
    public void showSystemUI() {
        hasHideSystemUi = false;
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (mDecorView != null) {
            mDecorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        if (tintManager != null) {
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    public boolean hasNavBar() {
        if (tintManager == null)
            return false;
        SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
        return config.hasNavigtionBar();
    }

}
