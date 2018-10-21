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

import com.example.jasonjan.animdemo.R;

/**
 * tips : 图形交并集计算
 * *
 * time : 18-10-19下午5:12
 * owner: jasonjan
 */
public class CustomView17 extends View {

    private Paint mPaint;
    private Bitmap bitmapSrc;
    private PorterDuffXfermode porterDuffXfermode;// 图形混合模式

    public CustomView17(Context context){
        this(context,null);
    }

    public CustomView17(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public CustomView17(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //模式相关
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SCREEN);//实例化混合模式#滤色模式

        //初始化资源相关
        bitmapSrc=BitmapFactory.decodeResource(context.getResources(),R.drawable.test1);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //背景相关
        canvas.drawColor(Color.BLUE);

        //矩形配置相关
        Rect srcRect=new Rect(0,0,bitmapSrc.getWidth(),bitmapSrc.getHeight());
        Rect dstRect=new Rect(15,15,1065,1050*bitmapSrc.getHeight()/bitmapSrc.getWidth()+15);

        //图层相关
        int sc = canvas.saveLayer(0, 0, 1080, 1920, null, Canvas.ALL_SAVE_FLAG);

        //绘制相关
        canvas.drawColor(0xcc1c093e);//绘制一层带透明色的颜色
        mPaint.setXfermode(porterDuffXfermode);//设置混合模式
        canvas.drawBitmap(bitmapSrc,srcRect,dstRect,mPaint);

        // 还原相关
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }



}
