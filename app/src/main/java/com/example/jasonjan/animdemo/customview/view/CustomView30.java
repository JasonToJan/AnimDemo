package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.jasonjan.animdemo.R;

/**
 * tips : 渐变
 * *
 * time : 18-10-20下午6:46
 * owner: jasonjan
 */
public class CustomView30 extends View {

    private Paint mFillPaint;// 填充和描边的画笔
    private LinearGradient mLinearGradient;
    private Rect rect;
    private boolean isClick;
    private boolean isChangeStyle;
    private boolean isChangeStyle2;

    public CustomView30(Context context) {
        this(context,null);
        init();
    }

    public CustomView30(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView30(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //初始化矩形
        rect=new Rect(15,15,1065,1065);

        //画笔相关
        mFillPaint = new Paint();
        mLinearGradient = new LinearGradient(15,15,1065,1065,Color.RED,Color.YELLOW,Shader.TileMode.REPEAT);
        mFillPaint.setShader(mLinearGradient);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制相关
        canvas.drawColor(Color.WHITE);
        canvas.drawRect(rect, mFillPaint);

    }

    public void changeCoordinate(){
        if(isClick){
            isClick=false;
            mFillPaint = new Paint();
            mLinearGradient = new LinearGradient(15,15,1065,1065,Color.RED,Color.YELLOW,Shader.TileMode.REPEAT);
            mFillPaint.setShader(mLinearGradient);
            invalidate();
        }else{
            isClick=true;
            mFillPaint.setShader(new LinearGradient(15,15,500,500,Color.RED,Color.YELLOW,Shader.TileMode.REPEAT));
            invalidate();
        }
    }
    
    public void changeStyle(){
        if(isChangeStyle){
            isChangeStyle=false;
            mFillPaint = new Paint();
            mLinearGradient = new LinearGradient(15,15,1065,1065,Color.RED,Color.YELLOW,Shader.TileMode.REPEAT);
            mFillPaint.setShader(mLinearGradient);
            invalidate();
        }else{
            isChangeStyle=true;
            mFillPaint.setShader(new LinearGradient(15,15,1065,1065,
                    new int[] { Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE },
                    new float[] { 0, 0.1F, 0.5F, 0.7F, 0.8F }, Shader.TileMode.MIRROR));
            invalidate();
        }
    }

    public void changeStyle2(){
        if(isChangeStyle2){
            isChangeStyle2=false;
            mFillPaint = new Paint();
            mLinearGradient = new LinearGradient(15,15,1065,1065,Color.RED,Color.YELLOW,Shader.TileMode.REPEAT);
            mFillPaint.setShader(mLinearGradient);
            invalidate();
        }else{
            isChangeStyle2=true;
            mFillPaint.setShader(new LinearGradient(15,15,1065,1065,
                    new int[] { Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE },
                   null, Shader.TileMode.MIRROR));
            invalidate();
        }
    }

}
