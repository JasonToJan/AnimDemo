package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.util.AttributeSet;
import android.view.View;

/**
 * tips : 路径效果
 * *
 * time : 18-10-20上午10:54
 * owner: jasonjan
 */
public class CustomView26 extends View {

    private Paint mPaint;
    private Path mPath;// 路径对象
    private int screenW, screenH;// 屏幕宽高
    private float x, y;// 路径初始坐标
    private float initScreenW;// 屏幕初始宽度
    private float initX;// 初始X轴坐标
    private float transX=0, moveX;// 画布移动的距离
    private boolean isCanvasMove=false;// 画布是否需要平移

    public CustomView26(Context context) {
        this(context,null);
        init();
    }

    public CustomView26(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView26(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //画笔配置相关
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(5);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setShadowLayer(7, 0, 0, Color.GREEN);

        //初始化路径相关
        mPath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //设定数据值相关
        screenW = w;
        screenH = h;
        x = 0;
        y = (screenH / 2) + (screenH / 4) + (screenH / 10);
        initScreenW = screenW;
        initX = ((screenW / 2) + (screenW / 4));
        moveX = (screenW / 24);

        //路径相关
        mPath.moveTo(x, y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制背景相关
        canvas.drawColor(Color.BLACK);
        canvas.translate(-transX, 0);

        //路径相关
        mPath.lineTo(x, y);

        //改变xy的值
        calCoors();

        //绘制路径相关
        canvas.drawPath(mPath, mPaint);
        invalidate();
    }

    private void calCoors(){
        if (isCanvasMove == true) {
            transX += 4;
        }

        if (x < initX) {
            x += 8;
        } else {
            if (x < initX + moveX) {
                x += 2;
                y -= 8;
            } else {
                if (x < initX + (moveX * 2)) {
                    x += 2;
                    y += 14;
                } else {
                    if (x < initX + (moveX * 3)) {
                        x += 2;
                        y -= 12;
                    } else {
                        if (x < initX + (moveX * 4)) {
                            x += 2;
                            y += 6;
                        } else {
                            if (x < initScreenW) {
                                x += 8;
                            } else {
                                isCanvasMove = true;
                                initX = initX + initScreenW;
                            }
                        }
                    }
                }
            }

        }
    }

}
