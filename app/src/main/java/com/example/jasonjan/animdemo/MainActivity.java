package com.example.jasonjan.animdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jasonjan.animdemo.customview.CustomViewActivity;
import com.example.jasonjan.animdemo.meituan.MeiTuanRefreshActivity;
import com.example.jasonjan.animdemo.rocket.RocketActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 启动火箭动画按钮
     */
    private Button rocketBtn;

    /**
     * 美团刷新按钮
     */
    private Button meituanRefreshBtn;

    /**
     * 自定义视图系列
     */
    private Button customViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rocketBtn = (Button) findViewById(R.id.rocketBtn);
        rocketBtn.setOnClickListener(this);

        meituanRefreshBtn=(Button) findViewById(R.id.meituanRefreshBtn);
        meituanRefreshBtn.setOnClickListener(this);

        customViewBtn=(Button) findViewById(R.id.customViewBtn);
        customViewBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.rocketBtn:
                Intent intent=new Intent(this,RocketActivity.class);
                startActivity(intent);
                break;

            case R.id.meituanRefreshBtn:
                Intent intent2=new Intent(this,MeiTuanRefreshActivity.class);
                startActivity(intent2);
                break;

            case R.id.customViewBtn:
                Intent intent3=new Intent(this,CustomViewActivity.class);
                startActivity(intent3);
                break;

            default:
                break;
        }
    }

}
