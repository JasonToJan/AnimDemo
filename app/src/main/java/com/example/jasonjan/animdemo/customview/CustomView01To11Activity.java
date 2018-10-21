package com.example.jasonjan.animdemo.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.jasonjan.animdemo.R;
import com.example.jasonjan.animdemo.customview.view.CustomView01;
import com.example.jasonjan.animdemo.customview.view.CustomView04;
import com.example.jasonjan.animdemo.customview.view.CustomView05;
import com.example.jasonjan.animdemo.customview.view.CustomView06;
import com.example.jasonjan.animdemo.customview.view.CustomView07;
import com.example.jasonjan.animdemo.customview.view.CustomView08;
import com.example.jasonjan.animdemo.customview.view.CustomView09;
import com.example.jasonjan.animdemo.customview.view.CustomView10;
import com.example.jasonjan.animdemo.customview.view.CustomView11;
import com.example.jasonjan.animdemo.util.LogHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * tips :  基础的自定义视图相关
 * *
 * time : 18-10-19上午11:51
 * owner: jasonjan
 */
public class CustomView01To11Activity extends AppCompatActivity {

    @BindView(R.id.custom01_root) LinearLayout root;
    @BindView(R.id.custom01_cv) CustomView01 customView01;
    @BindView(R.id.custom01_btn) Button startBtn;
    @BindView(R.id.custom04_btn) Button custom04Btn;
    @BindView(R.id.custom05_btn) Button custom05Btn;
    @BindView(R.id.custom06_btn) Button custom06Btn;
    @BindView(R.id.custom07_btn) Button custom07Btn;
    @BindView(R.id.custom08_btn) Button custom08Btn;
    @BindView(R.id.custom09_btn) Button custom09Btn;
    @BindView(R.id.custom10_btn) Button custom10Btn;
    @BindView(R.id.custom11_btn) Button custom11Btn;
    @BindView(R.id.custom04_cv) CustomView04 customView04;
    @BindView(R.id.custom05_cv) CustomView05 customView05;
    @BindView(R.id.custom06_cv) CustomView06 customView06;
    @BindView(R.id.custom07_cv) CustomView07 customView07;
    @BindView(R.id.custom08_cv) CustomView08 customView08;
    @BindView(R.id.custom09_cv) CustomView09 customView09;
    @BindView(R.id.custom10_cv) CustomView10 customView10;
    @BindView(R.id.custom11_cv) CustomView11 customView11;

    Thread startCircleThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view01);

        ButterKnife.bind(this);

    }

    @OnClick({R.id.custom01_btn,R.id.custom04_btn,R.id.custom05_btn,R.id.custom06_btn,
            R.id.custom07_btn, R.id.custom08_btn,R.id.custom09_btn,R.id.custom10_btn,R.id.custom11_btn})
    public void onClick(Button btn){
        switch (btn.getId()){
            case R.id.custom01_btn:{
                if(startCircleThread==null) {
                    customView01.setIsStop(false);
                    startCircleThread = new Thread(customView01);
                    startCircleThread.start();
                }else{
                    customView01.setIsStop(true);
                    startCircleThread=null;
                    LogHelper.d("线程停止了！");
                }
                break;
            }

            case R.id.custom04_btn:{
                customView04.setVisibility(View.VISIBLE);
                customView05.setVisibility(View.GONE);
                customView06.setVisibility(View.GONE);
                customView07.setVisibility(View.GONE);
                customView08.setVisibility(View.GONE);
                customView09.setVisibility(View.GONE);
                customView10.setVisibility(View.GONE);
                customView11.setVisibility(View.GONE);
                break;
            }

            case R.id.custom05_btn:{
                customView04.setVisibility(View.GONE);
                customView05.setVisibility(View.VISIBLE);
                customView06.setVisibility(View.GONE);
                customView07.setVisibility(View.GONE);
                customView08.setVisibility(View.GONE);
                customView09.setVisibility(View.GONE);
                customView10.setVisibility(View.GONE);
                customView11.setVisibility(View.GONE);
                break;
            }

            case R.id.custom06_btn:{
                customView04.setVisibility(View.GONE);
                customView05.setVisibility(View.GONE);
                customView06.setVisibility(View.VISIBLE);
                customView07.setVisibility(View.GONE);
                customView08.setVisibility(View.GONE);
                customView09.setVisibility(View.GONE);
                customView10.setVisibility(View.GONE);
                customView11.setVisibility(View.GONE);
                break;
            }

            case R.id.custom07_btn:{
                customView04.setVisibility(View.GONE);
                customView05.setVisibility(View.GONE);
                customView06.setVisibility(View.GONE);
                customView07.setVisibility(View.VISIBLE);
                customView08.setVisibility(View.GONE);
                customView09.setVisibility(View.GONE);
                customView10.setVisibility(View.GONE);
                customView11.setVisibility(View.GONE);
                break;
            }

            case R.id.custom08_btn:{
                customView04.setVisibility(View.GONE);
                customView05.setVisibility(View.GONE);
                customView06.setVisibility(View.GONE);
                customView07.setVisibility(View.GONE);
                customView08.setVisibility(View.VISIBLE);
                customView09.setVisibility(View.GONE);
                customView10.setVisibility(View.GONE);
                customView11.setVisibility(View.GONE);
                break;
            }

            case R.id.custom09_btn:{
                customView04.setVisibility(View.GONE);
                customView05.setVisibility(View.GONE);
                customView06.setVisibility(View.GONE);
                customView07.setVisibility(View.GONE);
                customView08.setVisibility(View.GONE);
                customView09.setVisibility(View.VISIBLE);
                customView10.setVisibility(View.GONE);
                customView11.setVisibility(View.GONE);
                break;
            }

            case R.id.custom10_btn:{
                customView04.setVisibility(View.GONE);
                customView05.setVisibility(View.GONE);
                customView06.setVisibility(View.GONE);
                customView07.setVisibility(View.GONE);
                customView08.setVisibility(View.GONE);
                customView09.setVisibility(View.GONE);
                customView10.setVisibility(View.VISIBLE);
                customView11.setVisibility(View.GONE);
                break;
            }

            case R.id.custom11_btn:{
                customView04.setVisibility(View.GONE);
                customView05.setVisibility(View.GONE);
                customView06.setVisibility(View.GONE);
                customView07.setVisibility(View.GONE);
                customView08.setVisibility(View.GONE);
                customView09.setVisibility(View.GONE);
                customView10.setVisibility(View.GONE);
                customView11.setVisibility(View.VISIBLE);
                break;
            }
        }
        
    }
}
