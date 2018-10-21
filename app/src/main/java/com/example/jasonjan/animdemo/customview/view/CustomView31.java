package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.DemoApp;
import com.example.jasonjan.animdemo.R;

/**
 * tips : 图片倒影效果
 * *
 * time : 18-10-20下午6:46
 * owner: jasonjan
 */
public class CustomView31 extends View {

    private Bitmap mSrcBitmap,tempBitmap,mRefBitmap;// 位图
    private Paint mPaint;// 画笔
    private PorterDuffXfermode mXfermode;// 混合模式

    public CustomView31(Context context) {
        this(context,null);
        init();
    }

    public CustomView31(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView31(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //初始化数值相关
        Matrix matrix = new Matrix();
        matrix.setScale(1F, -1F);

        //混合模式相关
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

        //获取资源相关
        mSrcBitmap=BitmapFactory.decodeResource(DemoApp.getInstance().getResources(), R.drawable.rita5);
        tempBitmap= Bitmap.createScaledBitmap(mSrcBitmap, mSrcBitmap.getWidth()/2, mSrcBitmap.getHeight()/2, true);
        mRefBitmap = Bitmap.createBitmap(tempBitmap, 0, 0, tempBitmap.getWidth(), tempBitmap.getHeight(), matrix, true);

        //画笔相关
        mPaint = new Paint();
        mPaint.setShader(new LinearGradient(15, 15 + tempBitmap.getHeight(), 15,
                15 + tempBitmap.getHeight() + tempBitmap.getHeight() / 4, 0xAA000000,
                Color.TRANSPARENT, Shader.TileMode.CLAMP));
        mPaint.setXfermode(mXfermode);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制相关
        canvas.drawBitmap(tempBitmap, 15, 15, null);
        int sc = canvas.saveLayer(15, 15 + tempBitmap.getHeight(), 15 + mRefBitmap.getWidth(), 15 + tempBitmap.getHeight() * 2, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawBitmap(mRefBitmap, 15, 15 + tempBitmap.getHeight(), null);
        canvas.drawRect(15, 15 + tempBitmap.getHeight(), 15 + mRefBitmap.getWidth(), 15 + tempBitmap.getHeight() * 2, mPaint);

        //重置相关
        canvas.restoreToCount(sc);
    }


}
