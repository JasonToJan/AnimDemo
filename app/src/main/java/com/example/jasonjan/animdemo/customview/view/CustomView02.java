package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * tips :
 * *
 * time : 18-10-19下午3:03
 * owner: jasonjan
 */
public class CustomView02 extends View {

    private Paint mPaint;

    public CustomView02(Context context){
        this(context,null);
    }

    public CustomView02(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public CustomView02(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //模式相关
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);//打开抗锯齿模式

        //风格相关
        mPaint.setStyle(Paint.Style.FILL);

        //颜色相关
        mPaint.setColor(Color.argb(255, 255, 128, 103));

        //粗细相关
        mPaint.setStrokeWidth(10);

        //初始值相关

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //图形绘制
        canvas.drawCircle(250,250,200,mPaint);
    }
}
