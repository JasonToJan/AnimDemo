package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.DemoApp;
import com.example.jasonjan.animdemo.R;

/**
 * tips : 图形阴影
 * *
 * time : 18-10-20上午10:54
 * owner: jasonjan
 */
public class CustomView27 extends View {

    private Paint mPaint;
    private boolean isShadow;

    public CustomView27(Context context) {
        this(context,null);
        init();
    }

    public CustomView27(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView27(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //画笔配置相关
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        //其他相关
        setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭该View的硬加速

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制相关
        canvas.drawRect(100, 100, 600, 600, mPaint);

    }

    public void doMakeShadow(){
        if(isShadow){
            mPaint=null;
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.FILL);
            isShadow=false;
            invalidate();
        }else{
            isShadow=true;
            mPaint.setShadowLayer(10, 3, 3, Color.DKGRAY);
            invalidate();
        }
    }
}
