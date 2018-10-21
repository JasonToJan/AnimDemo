package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.R;

/**
 * tips :
 * *
 * time : 18-10-19下午5:12
 * owner: jasonjan
 */
public class CustomView12 extends View {

    private Paint mPaint;
    private Bitmap bitmap;
    private boolean isClick;

    public CustomView12(Context context){
        this(context,null);
    }

    public CustomView12(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public CustomView12(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //模式相关
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);//打开抗锯齿模式

        //风格相关
        //mPaint.setStyle(Paint.Style.FILL);

        //颜色相关
        //mPaint.setColorFilter(new LightingColorFilter(0xFFFF00FF, 0x00000000));
        //如果我们想增加红色的值，那么LightingColorFilter(0xFFFFFFFF, 0x00XX0000)就好，其中XX取值为00至FF

        //粗细相关
        //mPaint.setStrokeWidth(10);

        //初始化资源相关
        bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.star);

        //初始化点击事件相关
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClick) {
                    // 如果已经被点击了则点击时设置颜色过滤为空还原本色
                    mPaint.setColorFilter(null);
                    isClick = false;
                } else {
                    // 如果未被点击则点击时设置颜色过滤后为黄色
                    mPaint.setColorFilter(new LightingColorFilter(0xFFFFFFFF, 0X00FFFF00));
                    isClick = true;
                }

                //重绘
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制位图
        canvas.drawBitmap(bitmap,100,20,mPaint);
    }
}
