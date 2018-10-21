package com.example.jasonjan.animdemo.rocket;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.jasonjan.animdemo.R;

public class RocketActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rocket);
        btn = (Button) findViewById(R.id.getup);
        btn.setOnClickListener(this);
    }

    private void launcherTheRocket() {

        //火箭动画
        final View rocket = findViewById(R.id.rocket);
        Animation rocketAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rocket);
        rocketAnimation.setAnimationListener(new VisibilityAnimationListener(rocket));
        rocket.startAnimation(rocketAnimation);

        //云层动画
        final View cloud = findViewById(R.id.cloud);
        Animation cloudAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cloud);
        cloudAnimation.setAnimationListener(new VisibilityAnimationListener(cloud));
        cloud.startAnimation(cloudAnimation);

        //轨迹动画
        final View launcher = findViewById(R.id.launcher);
        Animation launcherAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.launcher);
        launcherAnimation.setAnimationListener(new VisibilityAnimationListener(launcher));
        launcher.startAnimation(launcherAnimation);

    }


    public class VisibilityAnimationListener implements Animation.AnimationListener {

        private View mVisibilityView;

        public VisibilityAnimationListener(View view) {
            mVisibilityView = view;
        }

        public void setVisibilityView(View view) {
            mVisibilityView = view;
        }

        //动画开始
        @Override
        public void onAnimationStart(Animation animation) {
            Log.i("START", "...");
            if (mVisibilityView != null) {
                mVisibilityView.setVisibility(View.VISIBLE);
                // mVisibilityView.setVisibility(View.GONE);
            }

        }

        //动画结束
        @Override
        public void onAnimationEnd(Animation animation) {
            Log.i("END", "...");
            if (mVisibilityView != null) {
                mVisibilityView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }
    }

    @Override
    public void onClick(View v) {
        //启动
        launcherTheRocket();
    }

}
