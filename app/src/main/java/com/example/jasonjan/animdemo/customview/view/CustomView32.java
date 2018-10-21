package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

/**
 * tips : 梯度渐变
 * *
 * time : 18-10-20下午6:46
 * owner: jasonjan
 */
public class CustomView32 extends View {

    private Paint mFillPaint;// 填充和描边的画笔
    private SweepGradient mSweepGradient;
    private RadialGradient mRadialGradient;
    private Rect rect;
    private boolean isSweep;
    private boolean isRadial;

    public CustomView32(Context context) {
        this(context,null);
        init();
    }

    public CustomView32(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView32(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //初始化矩形
        rect=new Rect(15,15,1065,1065);

        //画笔相关
        mFillPaint = new Paint();
        mSweepGradient = new SweepGradient(500,500,Color.RED,Color.YELLOW);
        mFillPaint.setShader(mSweepGradient);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制相关
        canvas.drawColor(Color.WHITE);
        canvas.drawRect(rect, mFillPaint);

    }

    public void changeSweepGradient(){
        if(isSweep){
            isSweep=false;
            mFillPaint = new Paint();
            mSweepGradient = new SweepGradient(500,500,Color.RED,Color.YELLOW);
            mFillPaint.setShader(mSweepGradient);
            invalidate();
        }else{
            isSweep=true;
            mFillPaint.setShader(new SweepGradient(1000, 1000, new int[] { Color.GREEN, Color.WHITE, Color.GREEN }, null));
            invalidate();
        }
    }
    
    public void changeRadialGradient(){
        if(isRadial){
            isRadial=false;
            mFillPaint = new Paint();
            mRadialGradient = new RadialGradient(100,100,450,Color.RED,Color.YELLOW,Shader.TileMode.CLAMP);
            mFillPaint.setShader(mRadialGradient);
            invalidate();
        }else{
            isRadial=true;
            mFillPaint = new Paint();
            mRadialGradient = new RadialGradient(100,100,450,Color.BLUE,Color.WHITE,Shader.TileMode.REPEAT);
            mFillPaint.setShader(mRadialGradient);
            invalidate();
        }
    }

}
