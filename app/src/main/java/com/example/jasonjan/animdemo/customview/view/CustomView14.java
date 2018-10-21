package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.DemoApp;
import com.example.jasonjan.animdemo.R;

/**
 * tips : 图形交并集计算
 * *
 * time : 18-10-19下午5:12
 * owner: jasonjan
 */
public class CustomView14 extends View {

    private Paint mPaint;
    private Bitmap bitmap;
    private boolean isClick;

    public CustomView14(Context context){
        this(context,null);
    }

    public CustomView14(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public CustomView14(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //模式相关
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        //初始化资源相关
        bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.test1);

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

    public void changeBackground(){
        if(isClick){
            isClick=false;
            bitmap.recycle();
            bitmap=BitmapFactory.decodeResource(DemoApp.getInstance().getResources(),R.drawable.test1);
            invalidate();
        }else{
            isClick=true;
            bitmap.recycle();
            bitmap=BitmapFactory.decodeResource(DemoApp.getInstance().getResources(),R.drawable.test2);
            invalidate();
        }
    }

}
