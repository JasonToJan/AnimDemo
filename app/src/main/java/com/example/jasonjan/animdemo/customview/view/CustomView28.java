package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.DemoApp;
import com.example.jasonjan.animdemo.R;

/**
 * tips : BitmapShader用法
 * *
 * time : 18-10-20下午6:46
 * owner: jasonjan
 */
public class CustomView28 extends View {

    private Paint mPaint;
    private int bitmapWidth,bitmapHeight;

    public CustomView28(Context context) {
        this(context,null);
        init();
    }

    public CustomView28(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView28(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //画笔相关
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        //获取资源相关
        Bitmap bitmapSrc=BitmapFactory.decodeResource(DemoApp.getInstance().getResources(),R.drawable.picture1);

        //默认值设置
        bitmapWidth=bitmapSrc.getWidth()/2;
        bitmapHeight=bitmapSrc.getHeight()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制相关
        canvas.drawRect(15,15,1065,1800,mPaint);
    }

    public void do2Clamp(){
        mPaint=null;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        Bitmap bitmapSrc=BitmapFactory.decodeResource(DemoApp.getInstance().getResources(),R.drawable.picture1);
        mPaint.setShader(new BitmapShader(bitmapSrc,Shader.TileMode.CLAMP,Shader.TileMode.CLAMP));
        invalidate();
    }

    public void do2Mirror(){
        mPaint=null;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        Bitmap bitmapSrc=BitmapFactory.decodeResource(DemoApp.getInstance().getResources(),R.drawable.picture1);
        mPaint.setShader(new BitmapShader(bitmapSrc,Shader.TileMode.MIRROR,Shader.TileMode.MIRROR));
        invalidate();
    }

    public void do2Repeat(){
        mPaint=null;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        Bitmap bitmapSrc=BitmapFactory.decodeResource(DemoApp.getInstance().getResources(),R.drawable.picture1);
        mPaint.setShader(new BitmapShader(bitmapSrc,Shader.TileMode.REPEAT,Shader.TileMode.REPEAT));
        invalidate();
    }

    public void doClampAndMirror(){
        mPaint=null;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        Bitmap bitmapSrc=BitmapFactory.decodeResource(DemoApp.getInstance().getResources(),R.drawable.picture1);
        mPaint.setShader(new BitmapShader(bitmapSrc,Shader.TileMode.CLAMP,Shader.TileMode.MIRROR));
        invalidate();
    }

    public void doMirrorAndClamp(){
        mPaint=null;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        Bitmap bitmapSrc=BitmapFactory.decodeResource(DemoApp.getInstance().getResources(),R.drawable.picture1);
        mPaint.setShader(new BitmapShader(bitmapSrc,Shader.TileMode.MIRROR,Shader.TileMode.CLAMP));
        invalidate();
    }
}
