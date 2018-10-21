package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
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
public class CustomView13 extends View {

    private Paint mPaint;
    private Bitmap bitmap;
    private boolean isClick;

    public CustomView13(Context context){
        this(context,null);
    }

    public CustomView13(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public CustomView13(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //模式相关
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);//打开抗锯齿模式

        //风格相关
        //mPaint.setStyle(Paint.Style.FILL);

        //颜色相关#混合模式#变成暗红色
        //mPaint.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN));

        //粗细相关
        //mPaint.setStrokeWidth(10);

        //初始化资源相关
        bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.rita2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //矩形配置相关
        Rect srcRect=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        Rect dstRect=new Rect(15,15,1065,1050*bitmap.getHeight()/bitmap.getWidth()+15);

        //绘制位图
        canvas.drawBitmap(bitmap,srcRect,dstRect,mPaint);
    }

    public void changeColorFilter(){
        if(isClick){
            isClick=false;
            mPaint.setColorFilter(null);
            invalidate();
        }else{
            isClick=true;
            mPaint.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN));
            invalidate();
        }
    }
}
