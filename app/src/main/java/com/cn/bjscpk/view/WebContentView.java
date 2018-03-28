package com.cn.bjscpk.view;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.cn.bjscpk.R;
import com.stonesun.newssdk.activity.ContentViewActivity;

import org.json.JSONObject;

/**
 * Created by Administrator on 2018/2/7.
 */

public class WebContentView extends ContentViewActivity{
    private TextView title;
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.contentview);
        title=findViewById(R.id.title_tv);
    }

    @Override
    public void onLoaded(JSONObject jsonObject) {
        Log.d("lee","onLoaded");
        if(jsonObject!=null){
            String mess="";
        try{
            mess=jsonObject.getString("fmedia");
            title.setText(mess);
        }catch (Exception e){

        }
        }
    }
}
