package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
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
public class CustomView15 extends View {

    private Paint mPaint;
    private Bitmap bitmapDis,bitmapSrc;
    private PorterDuffXfermode porterDuffXfermode;// 图形混合模式

    public CustomView15(Context context){
        this(context,null);
    }

    public CustomView15(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public CustomView15(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //模式相关
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);//实例化混合模式

        //初始化资源相关
        bitmapDis=BitmapFactory.decodeResource(context.getResources(),R.drawable.test1);
        bitmapSrc=BitmapFactory.decodeResource(context.getResources(),R.drawable.test2);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //背景相关
        canvas.drawColor(Color.YELLOW);

        //矩形配置相关
        Rect srcRect=new Rect(0,0,bitmapSrc.getWidth(),bitmapSrc.getHeight());
        Rect dstRect=new Rect(15,15,1065,1050*bitmapSrc.getHeight()/bitmapSrc.getWidth()+15);

        //图层相关
        int sc = canvas.saveLayer(0, 0, 1080, 1920, null, Canvas.ALL_SAVE_FLAG);

        //绘制相关
        canvas.drawBitmap(bitmapDis, srcRect, dstRect, mPaint);//绘制原来的图
        mPaint.setXfermode(porterDuffXfermode);//设置混合模式
        canvas.drawBitmap(bitmapSrc,srcRect,dstRect,mPaint);//只绘制原图的掩码图区域，间接屏蔽其它杂物

        // 还原相关
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }



}
