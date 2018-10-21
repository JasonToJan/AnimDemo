package com.example.jasonjan.animdemo.customview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jasonjan.animdemo.R;
import com.example.jasonjan.animdemo.customview.view.CustomView13;
import com.example.jasonjan.animdemo.customview.view.CustomView14;
import com.example.jasonjan.animdemo.customview.view.CustomView15;
import com.example.jasonjan.animdemo.customview.view.CustomView16;
import com.example.jasonjan.animdemo.customview.view.CustomView17;
import com.example.jasonjan.animdemo.customview.view.CustomView18;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomView12To18Activity extends AppCompatActivity {

    @BindView(R.id.custom13_btn) Button btn13;
    @BindView(R.id.custom13_cv) CustomView13 customView13;
    @BindView(R.id.custom14_cv) CustomView14 customView14;
    @BindView(R.id.custom15_cv) CustomView15 customView15;
    @BindView(R.id.custom16_cv) CustomView16 customView16;
    @BindView(R.id.custom17_cv) CustomView17 customView17;
    @BindView(R.id.custom18_cv) CustomView18 customView18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view12_to18);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.custom13_btn,R.id.custom14_btn,R.id.custom15_btn,R.id.custom16_btn,
            R.id.custom17_btn,R.id.custom18_btn})
    public void onClick(Button btn){
        switch (btn.getId()){
            case R.id.custom13_btn:
                customView13.changeColorFilter();
                break;

            case R.id.custom14_btn:
                customView14.setVisibility(View.VISIBLE);
                customView15.setVisibility(View.GONE);
                customView16.setVisibility(View.GONE);
                customView17.setVisibility(View.GONE);
                customView14.changeBackground();
                break;

            case R.id.custom15_btn:
                customView14.setVisibility(View.GONE);
                customView15.setVisibility(View.VISIBLE);
                customView16.setVisibility(View.GONE);
                customView17.setVisibility(View.GONE);
                break;

            case R.id.custom16_btn:
                customView14.setVisibility(View.GONE);
                customView15.setVisibility(View.GONE);
                customView16.setVisibility(View.VISIBLE);
                customView17.setVisibility(View.GONE);
                break;

            case R.id.custom17_btn:
                customView14.setVisibility(View.GONE);
                customView15.setVisibility(View.GONE);
                customView16.setVisibility(View.GONE);
                customView17.setVisibility(View.VISIBLE);
                break;

            case R.id.custom18_btn:
                customView18.restore();
                break;
        }

    }
}
