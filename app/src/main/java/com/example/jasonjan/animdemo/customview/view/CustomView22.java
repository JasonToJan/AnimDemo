package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * tips : 阴影效果
 * *
 * time : 18-10-20上午10:54
 * owner: jasonjan
 */
public class CustomView22 extends View {

    private Paint mPaint;

    public CustomView22(Context context) {
        this(context,null);
        init();
    }

    public CustomView22(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView22(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //画笔配置相关
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF603811);
        mPaint.setMaskFilter(new BlurMaskFilter(20, BlurMaskFilter.Blur.SOLID));//设置画笔遮罩滤镜

        //其他相关
        setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭该View的硬加速

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制相关
        canvas.drawColor(Color.WHITE);
        canvas.drawRect(310,100,710,400,mPaint);
    }
}
