package com.cn.sschwsc.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.cn.sschwsc.R;

/**
 * Created by Administrator on 2018/2/7.
 */

public class ToastActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.toastactivity);
        Toast.makeText(this,"评论成功！",Toast.LENGTH_SHORT).show();
        finish();
    }
}
