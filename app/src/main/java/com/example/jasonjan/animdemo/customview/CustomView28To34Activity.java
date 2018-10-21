package com.example.jasonjan.animdemo.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.example.jasonjan.animdemo.R;
import com.example.jasonjan.animdemo.customview.view.CustomView28;
import com.example.jasonjan.animdemo.customview.view.CustomView30;
import com.example.jasonjan.animdemo.customview.view.CustomView32;
import com.example.jasonjan.animdemo.customview.view.CustomView33;
import com.example.jasonjan.animdemo.customview.view.CustomView34;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomView28To34Activity extends AppCompatActivity {

    @BindView(R.id.custom28_cv) CustomView28 customView28;
    @BindView(R.id.custom30_cv) CustomView30 customView30;
    @BindView(R.id.custom32_cv) CustomView32 customView32;
    @BindView(R.id.custom33_cv) CustomView33 customView33;
    @BindView(R.id.custom34_cv) CustomView34 customView34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view28_to34);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.custom28_btn1,R.id.custom28_btn2,R.id.custom28_btn3,R.id.custom28_btn4,
            R.id.custom28_btn5,R.id.custom30_btn1,R.id.custom30_btn2,R.id.custom30_btn3,
            R.id.custom32_btn1,R.id.custom32_btn2,R.id.custom33_btn1,R.id.custom33_btn2,
            R.id.custom33_btn3,R.id.custom34_btn1,R.id.custom34_btn2,R.id.custom34_btn3,
            R.id.custom34_btn4})
    public void onClick(Button button){
        switch (button.getId()){
            case R.id.custom28_btn1:
                customView28.do2Clamp();
                break;

            case R.id.custom28_btn2:
                customView28.do2Mirror();
                break;

            case R.id.custom28_btn3:
                customView28.do2Repeat();
                break;

            case R.id.custom28_btn4:
                customView28.doClampAndMirror();
                break;

            case R.id.custom28_btn5:
                customView28.doMirrorAndClamp();
                break;

            case R.id.custom30_btn1:
                customView30.changeCoordinate();
                break;

            case R.id.custom30_btn2:
                customView30.changeStyle();
                break;

            case R.id.custom30_btn3:
                customView30.changeStyle2();
                break;

            case R.id.custom32_btn1:
                customView32.changeSweepGradient();
                break;

            case R.id.custom32_btn2:
                customView32.changeRadialGradient();
                break;

            case R.id.custom33_btn1:
                customView33.setColorFilter();
                break;

            case R.id.custom33_btn2:
                customView33.setFocusePoint();
                break;

            case R.id.custom33_btn3:
                customView33.setDarkCorner();
                break;

            case R.id.custom34_btn1:
                customView34.setZero();
                break;

            case R.id.custom34_btn2:
                customView34.set1And4();
                break;

            case R.id.custom34_btn3:
                customView34.set1And2();
                break;

            case R.id.custom34_btn4:
                customView34.setRotate20();
                break;
        }
    }
}
