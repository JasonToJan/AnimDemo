package com.example.jasonjan.animdemo.customview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.jasonjan.animdemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomViewActivity extends AppCompatActivity{

    @BindView(R.id.custom_view_btn1) Button btn1;
    @BindView(R.id.custom_view_btn2) Button btn2;
    @BindView(R.id.custom_view_btn3) Button btn3;
    @BindView(R.id.custom_view_btn4) Button btn4;
    @BindView(R.id.custom_view_btn5) Button btn5;
    @BindView(R.id.custom_view_btn6) Button btn6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.custom_view_btn1,R.id.custom_view_btn2,R.id.custom_view_btn3,
            R.id.custom_view_btn4,R.id.custom_view_btn5,R.id.custom_view_btn6})
    public void onClick(Button btn){
        switch (btn.getId()){
            case R.id.custom_view_btn1:
                Intent intent1=new Intent(CustomViewActivity.this,CustomView01To11Activity.class);
                startActivity(intent1);
                break;
            case R.id.custom_view_btn2:
                Intent intent2=new Intent(CustomViewActivity.this,CustomView12To18Activity.class);
                startActivity(intent2);
                break;
            case R.id.custom_view_btn3:
                Intent intent3=new Intent(CustomViewActivity.this,CustomView19To27Activity.class);
                startActivity(intent3);
                break;
            case R.id.custom_view_btn4:
                Intent intent4=new Intent(CustomViewActivity.this,CustomView28To34Activity.class);
                startActivity(intent4);
                break;
            case R.id.custom_view_btn5:
                Intent intent5=new Intent(CustomViewActivity.this,CustomView35To42Activity.class);
                startActivity(intent5);
                break;
            case R.id.custom_view_btn6:

                break;
            default:
                break;
        }
    }

}
