package com.cn.wylee;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.cn.wylee.view.ShapeLoadingDialog;
import com.stonesun.newssdk.NewsAgent;
import com.stonesun.newssdk.fragment.NewsAFragment;

/**
 * Created by Administrator on 2018/1/30.
 */

public class NewsActivity extends AppCompatActivity {
    private FrameLayout frameLayout;
    private ShapeLoadingDialog shapeLoadingDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        shapeLoadingDialog = new ShapeLoadingDialog.Builder(this)
                .loadText("加载地图中...")
                .build();
        shapeLoadingDialog.setCanceledOnTouchOutside(false);
        shapeLoadingDialog.show();
        frameLayout=findViewById(R.id.fl_main);
        NewsAgent.setDebugMode(true);//打开调试日志
        NewsAgent.setPermission(this,true);//sdk请求权限
        NewsAgent.init(this);
        NewsAgent.createShareCommentViewActivity("详情页");
        NewsAgent.setLoading(false,true,true,true);
        NewsAgent.createDefaultRecomFragment("导航栏","SPOTS-414592","详情页");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        NewsAFragment recomFragment = NewsAgent.getDefaultRecomFragment("导航栏");
        transaction.add(R.id.fl_main,recomFragment).commit();
        shapeLoadingDialog.dismiss();

    }
}
