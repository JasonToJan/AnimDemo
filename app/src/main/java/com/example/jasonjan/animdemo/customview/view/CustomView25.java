package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * tips : 路径效果
 * *
 * time : 18-10-20上午10:54
 * owner: jasonjan
 */
public class CustomView25 extends View {

    private Paint mPaint;
    private PathEffect[] mEffects;// 路径效果数组
    private Path mPath;// 路径对象
    private float mPhase=0;//偏移值

    public CustomView25(Context context) {
        this(context,null);
        init();
    }

    public CustomView25(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView25(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //画笔配置相关
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.DKGRAY);

        //初始化路径相关
        mPath=new Path();
        mPath.moveTo(0,0);
        for (int i = 0; i <= 30; i++) {
            mPath.lineTo(i * 35, (float) (Math.random() * 100));
        }

        //初始化数组相关
        mEffects = new PathEffect[7];
        mEffects[0] = null;
        mEffects[1] = new CornerPathEffect(10);
        mEffects[2] = new DiscretePathEffect(3.0F, 5.0F);
        mEffects[3] = new DashPathEffect(new float[] { 20, 10, 5, 10 }, mPhase);
        Path path = new Path();
        path.addRect(0, 0, 8, 8, Path.Direction.CCW);
        mEffects[4] = new PathDashPathEffect(path, 12, mPhase, PathDashPathEffect.Style.ROTATE);
        mEffects[5] = new ComposePathEffect(mEffects[2], mEffects[4]);
        mEffects[6] = new SumPathEffect(mEffects[4], mEffects[3]);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制相关
        for (int i = 0; i < mEffects.length; i++) {
            mPaint.setPathEffect(mEffects[i]);
            canvas.drawPath(mPath, mPaint);
            canvas.translate(0, 250); //每绘制一条将画布向下平移250个像素
        }
        mPhase += 1;//刷新偏移值
        invalidate();
    }

}
