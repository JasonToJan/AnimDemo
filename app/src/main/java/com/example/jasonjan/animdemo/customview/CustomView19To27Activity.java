package com.example.jasonjan.animdemo.customview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jasonjan.animdemo.R;
import com.example.jasonjan.animdemo.customview.view.CustomView19;
import com.example.jasonjan.animdemo.customview.view.CustomView20;
import com.example.jasonjan.animdemo.customview.view.CustomView21;
import com.example.jasonjan.animdemo.customview.view.CustomView23;
import com.example.jasonjan.animdemo.customview.view.CustomView24;
import com.example.jasonjan.animdemo.customview.view.CustomView27;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CustomView19To27Activity extends AppCompatActivity {

    @BindView(R.id.custom19_cv) CustomView19 customView19;
    @BindView(R.id.custom19_tv) TextView textView19;
    @BindView(R.id.custom19_btn1) Button button19_1;
    @BindView(R.id.custom20_cv) CustomView20 customView20;
    @BindView(R.id.custom21_cv) CustomView21 customView21;
    @BindView(R.id.custom23_cv) CustomView23 customView23;
    @BindView(R.id.custom24_cv) CustomView24 customView24;
    @BindView(R.id.custom27_cv) CustomView27 customView27;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view19_to27);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.custom19_tv,R.id.custom19_btn1,R.id.custom20_btn,R.id.custom21_btn,
            R.id.custom23_btn,R.id.custom24_btn,R.id.custom27_btn})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.custom19_tv:
                textView19.setText(customView19.getText());
                break;

            case R.id.custom19_btn1:
                customView19.doTextCenter();
                break;

            case R.id.custom20_btn:
                customView20.doNewLine();
                break;

            case R.id.custom21_btn:
                customView21.doChangeTypeface();
                break;

            case R.id.custom23_btn:
                customView23.doMakeShadow();
                break;

            case R.id.custom24_btn:
                customView24.doMakeEmboss();
                break;

            case R.id.custom27_btn:
                customView27.doMakeShadow();
                break;
        }

    }
}
