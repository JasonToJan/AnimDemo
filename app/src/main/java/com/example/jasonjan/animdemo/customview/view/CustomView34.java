package com.example.jasonjan.animdemo.customview.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.R;
import com.example.jasonjan.animdemo.util.LogHelper;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * tips :
 * *
 * time : 18-10-21下午1:48
 * owner: jasonjan
 */
public class CustomView34 extends View {

    private Paint mPaint;// 画笔
    private Bitmap srcBitmap;
    private boolean isZero;
    private boolean is1And4;
    private boolean is1And2;


    public CustomView34(Context context){
        this(context,null);
        init();
    }

    public CustomView34(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){

        // 获取位图
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rita6);
        srcBitmap=Bitmap.createScaledBitmap(bitmap,bitmap.getWidth()/2,bitmap.getHeight()/2,false);

        //设置Shader相关
        BitmapShader bitmapShader = new BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Matrix matrix = new Matrix();// 实例一个矩阵对象
        matrix.setTranslate(srcBitmap.getWidth(), srcBitmap.getHeight());  // 设置矩阵变换
        bitmapShader.setLocalMatrix(matrix); // 设置Shader的变换矩阵

        // 实例化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setShader(bitmapShader);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // 绘制矩形
        canvas.drawRect(15,15,15+srcBitmap.getWidth(),15+srcBitmap.getHeight()+400, mPaint);
    }

    public void setZero(){
        if(isZero){
            isZero=false;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            invalidate();
        }else{
            isZero=true;
            BitmapShader bitmapShader = new BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();// 实例一个矩阵对象
            matrix.setTranslate(0, 0);  // 设置矩阵变换
            bitmapShader.setLocalMatrix(matrix); // 设置Shader的变换矩阵
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(bitmapShader);

            //测试相关
            float[] fs=new float[9];
            matrix.getValues(fs);
            LogHelper.d(Arrays.toString(fs));

            invalidate();
        }
    }

    public void set1And4(){
        if(is1And4){
            is1And4=false;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            invalidate();
        }else{
            is1And4=true;
            BitmapShader bitmapShader = new BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();// 实例一个矩阵对象
            matrix.setTranslate(srcBitmap.getWidth()/4, srcBitmap.getHeight()/4);  // 设置矩阵变换
            bitmapShader.setLocalMatrix(matrix); // 设置Shader的变换矩阵
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(bitmapShader);

            //测试相关
            float[] fs=new float[9];
            matrix.getValues(fs);
            LogHelper.d(Arrays.toString(fs));

            invalidate();
        }
    }

    public void set1And2(){
        if(is1And2){
            is1And2=false;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            invalidate();
        }else{
            is1And2=true;
            BitmapShader bitmapShader = new BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();// 实例一个矩阵对象
            matrix.setTranslate(srcBitmap.getWidth()/2, srcBitmap.getHeight()/2);  // 设置矩阵变换
            bitmapShader.setLocalMatrix(matrix); // 设置Shader的变换矩阵
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(bitmapShader);

            //测试相关
            float[] fs=new float[9];
            matrix.getValues(fs);
            LogHelper.d(Arrays.toString(fs));

            invalidate();
        }
    }

    public void setRotate20(){
        if(is1And2){
            is1And2=false;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            invalidate();
        }else{
            is1And2=true;
            BitmapShader bitmapShader = new BitmapShader(srcBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

            //矩阵变换
            Matrix matrix = new Matrix();// 实例一个矩阵对象
            matrix.preRotate(20);//前乘20，matrix.postRotate为后乘
            matrix.postScale(0.8f, 0.8f);//缩放
            bitmapShader.setLocalMatrix(matrix); // 设置Shader的变换矩阵


            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setShader(bitmapShader);

            //测试相关
            float[] fs=new float[9];
            matrix.getValues(fs);
            LogHelper.d(Arrays.toString(fs));

            invalidate();
        }
    }
}
