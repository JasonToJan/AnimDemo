package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.DemoApp;
import com.example.jasonjan.animdemo.R;

/**
 * tips : 图片梦幻效果
 * *
 * time : 18-10-20下午6:46
 * owner: jasonjan
 */
public class CustomView33 extends View {

    private Bitmap mSrcBitmap,tempBitmap,darkCornerBitmap;//最后一个是暗角
    private Canvas cornerCanvas;
    private Paint mPaint,mShaderPaint;// 画笔
    private PorterDuffXfermode mXfermode;// 混合模式
    private RadialGradient radialGradient;//径向渐变
    private boolean isClick;
    private boolean isClickFocuse;
    private boolean isDarkCorner;

    public CustomView33(Context context) {
        this(context,null);
        init();
    }

    public CustomView33(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView33(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //混合模式相关
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SCREEN);

        //获取资源相关
        mSrcBitmap=BitmapFactory.decodeResource(DemoApp.getInstance().getResources(), R.drawable.rita5);
        tempBitmap= Bitmap.createScaledBitmap(mSrcBitmap, mSrcBitmap.getWidth()/2, mSrcBitmap.getHeight()/2, true);
        darkCornerBitmap = Bitmap.createBitmap(tempBitmap.getWidth(), tempBitmap.getHeight(), Bitmap.Config.ARGB_8888);//暗角的Bitmap

        //画笔相关
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.WHITE);

        //绘制相关
        int sc = canvas.saveLayer(15, 15, tempBitmap.getWidth(),  tempBitmap.getHeight(), null, Canvas.ALL_SAVE_FLAG);
        canvas.drawColor(0xcc1c093e);
        canvas.drawBitmap(tempBitmap, 15, 15 , mPaint);

        //重置相关
        canvas.restoreToCount(sc);

        //是否突出重点
        if(isClickFocuse){
            canvas.drawRect(15, 15, 15 + tempBitmap.getWidth(), 15+ tempBitmap.getHeight(), mShaderPaint);
        }

        if(isDarkCorner){
            canvas.drawBitmap(darkCornerBitmap,15,15,null);
        }
    }

    public void setColorFilter(){
        if(isClick){
            isClick=false;
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
            invalidate();
        }else{
            isClick=true;
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setXfermode(mXfermode);
            mPaint.setColorFilter(new ColorMatrixColorFilter(new float[] {
                    0.8587F, 0.2940F, -0.0927F, 0,
                    6.79F,   0.0821F, 0.9145F,  0.0634F,
                    0,       6.79F,   0.2019F,  0.1097F,
                    0.7483F, 0,       6.79F,    0,
                    0,       0,       1,        0 }));
            invalidate();
        }
    }

    public void setFocusePoint(){
        if(isClickFocuse){
            isClickFocuse=false;
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
            invalidate();
        }else{
            isClickFocuse=true;
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setXfermode(mXfermode);
            mPaint.setColorFilter(new ColorMatrixColorFilter(new float[] {
                    0.8587F, 0.2940F, -0.0927F, 0,
                    6.79F,   0.0821F, 0.9145F,  0.0634F,
                    0,       6.79F,   0.2019F,  0.1097F,
                    0.7483F, 0,       6.79F,    0,
                    0,       0,       1,        0 }));
            mShaderPaint = new Paint();
            mShaderPaint.setShader(new RadialGradient(500, 500, tempBitmap.getHeight() * 7 / 8, Color.TRANSPARENT, Color.BLACK, Shader.TileMode.CLAMP));
            invalidate();
        }
    }

    public void setDarkCorner(){
        if(isDarkCorner){
            isDarkCorner=false;
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
            invalidate();
        }else{
            isDarkCorner=true;

            //画笔设置
            mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setXfermode(mXfermode);
            mPaint.setColorFilter(new ColorMatrixColorFilter(new float[] {
                    0.8587F, 0.2940F, -0.0927F, 0,
                    6.79F,   0.0821F, 0.9145F,  0.0634F,
                    0,       6.79F,   0.2019F,  0.1097F,
                    0.7483F, 0,       6.79F,    0,
                    0,       0,       1,        0 }));

            //暗角设置
            if(cornerCanvas==null){
                cornerCanvas = new Canvas(darkCornerBitmap);
            }
            cornerCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//清屏，关键一步
            float radiu = cornerCanvas.getHeight() * (2F / 3F);
            RadialGradient radialGradient = new RadialGradient(cornerCanvas.getWidth() / 2F,
                    cornerCanvas.getHeight() / 2F, radiu,
                    new int[] { 0, 0, 0xAA000000 },
                    new float[] { 0F, 0.7F, 1.0F }, Shader.TileMode.CLAMP);
            Matrix matrix = new Matrix();
            matrix.setScale(cornerCanvas.getWidth() / (radiu * 2F), 1.0F);
            matrix.preTranslate(((radiu * 2F) - cornerCanvas.getWidth()) / 2F, 0);
            radialGradient.setLocalMatrix(matrix);
            mShaderPaint = new Paint();
            mShaderPaint.setShader(radialGradient);
            cornerCanvas.drawRect(0, 0, cornerCanvas.getWidth(), cornerCanvas.getHeight(), mShaderPaint);
            invalidate();

        }


    }


}
