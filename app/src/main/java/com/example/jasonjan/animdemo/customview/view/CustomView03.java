package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * tips :
 * *
 * time : 18-10-19下午3:03
 * owner: jasonjan
 */
public class CustomView03 extends View {

    private Paint mPaint;

    public CustomView03(Context context){
        this(context,null);
    }

    public CustomView03(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public CustomView03(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //初始值相关
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                0.5F, 0, 0, 0, 0,
                0, 0.5F, 0, 0, 0,
                0, 0, 0.5F, 0, 0,
                0, 0, 0, 1, 0,
        });

        //模式相关
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);//打开抗锯齿模式

        //风格相关
        mPaint.setStyle(Paint.Style.FILL);

        //颜色相关
        mPaint.setColor(Color.argb(255, 255, 128, 103));
        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        //粗细相关
        mPaint.setStrokeWidth(10);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //图形绘制
        canvas.drawCircle(250,250,200,mPaint);
    }
}
