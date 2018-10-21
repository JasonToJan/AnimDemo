package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.DemoApp;
import com.example.jasonjan.animdemo.R;
import com.example.jasonjan.animdemo.util.ScreenUtils;

/**
 * tips : 浮雕效果
 * *
 * time : 18-10-20上午10:54
 * owner: jasonjan
 */
public class CustomView24 extends View {

    private Paint mPaint,normalPaint;
    private PointF[] mPointFs;// 存储各个巧克力坐上坐标的点
    private boolean isEmboss;

    public CustomView24(Context context) {
        this(context,null);
        init();
    }

    public CustomView24(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView24(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //画笔1配置相关
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(0xFF603811);
        mPaint.setMaskFilter(new EmbossMaskFilter(new float[] { 1, 1, 1F }, 0.1F, 10F, 20F));

        //画笔2配置相关
        normalPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        normalPaint.setStyle(Paint.Style.FILL);
        normalPaint.setColor(0xFF603811);

        //其他相关
        setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭该View的硬加速

        //坐标点初始化
        mPointFs=new PointF[8];
        int width=800/2;
        int height=1200/4;
        float temp=0;//单个物体Y轴坐标
        for(int i=0;i<8;i++){
            if(i%2==0){
                temp = i * height / 2F;
                mPointFs[i] = new PointF(100, temp);
            }else{
                mPointFs[i] = new PointF(width+100, temp);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(Color.GRAY);

        //绘制相关
        if(isEmboss){
            for (int i = 0; i < 8; i++) {
                canvas.drawRect(mPointFs[i].x, mPointFs[i].y, mPointFs[i].x + 400, mPointFs[i].y + 300, mPaint);
            }
        }else{
            for (int i = 0; i < 8; i++) {
                canvas.drawRect(mPointFs[i].x, mPointFs[i].y, mPointFs[i].x + 400, mPointFs[i].y + 300, normalPaint);
            }
        }
    }

    public void doMakeEmboss(){
        if(isEmboss){
            isEmboss=false;
            invalidate();
        }else{
            isEmboss=true;
            invalidate();
        }
    }
}
