package com.cdut.playtask;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.Nullable;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.cdut.playtask.data.CountItem;
import com.cdut.playtask.data.DBMaster;
import com.cdut.playtask.databinding.ActivityMainBinding;
import com.cdut.playtask.util.CountViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    // 声明数据库操作实例
    @SuppressLint("StaticFieldLeak")
    public static DBMaster mDBMaster;
    // CountViewModel实例——用于监测Count数据的变化
    private CountViewModel countViewModel;


    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //启动数据库
        mDBMaster = new DBMaster(getApplicationContext());
        mDBMaster.openDataBase();


        // 布局文件绑定类
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        // 将ActivityMainBinding对象的根视图设置为Activity的视图
        setContentView(binding.getRoot());

        // 设置Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        // 底部导航栏类
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // 创建AppBarConfiguration对象，并将BottomNavigationView 的菜单ID传递给它
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_task, R.id.navigation_reward, R.id.navigation_count, R.id.navigation_me)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // 启动悬浮球
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 1001);
            } else {
                startService(new Intent(this, FloatingService.class));
            }
        } else {
            startService(new Intent(this, FloatingService.class));
        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        // 创建ViewModel实例，和子Fragment共享数据
        countViewModel = new ViewModelProvider(this).get(CountViewModel.class);
        int total = 0;
        ArrayList<CountItem> countItems = mDBMaster.mCountDAO.queryDataList();
        if (countItems != null) {
            for (CountItem countItem : countItems) {
                total += countItem.getScore();
            }
        }
        countViewModel.setData(total);
    }

    @Override
    protected void onActivityResult(int req, int res, @Nullable Intent data) {
        super.onActivityResult(req, res, data);
        if (req == 1001) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
                startService(new Intent(this, FloatingService.class));
            }
        }
    }


}
