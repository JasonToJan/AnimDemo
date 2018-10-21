package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.jasonjan.animdemo.DemoApp;
import com.example.jasonjan.animdemo.R;

/**
 * tips : 开场动画
 * *
 * time : 18-10-20下午6:46
 * owner: jasonjan
 */
public class CustomView29 extends View {

    private Paint mFillPaint, mStrokePaint;// 填充和描边的画笔
    private BitmapShader mBitmapShader;// Bitmap着色器
    private float posX, posY;// 触摸点的XY坐标

    public CustomView29(Context context) {
        this(context,null);
        init();
    }

    public CustomView29(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView29(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //画笔1相关
        mStrokePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mStrokePaint.setColor(Color.RED);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(5);

        //画笔2相关
        mFillPaint = new Paint();
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.picture2);
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
        mFillPaint.setShader(mBitmapShader);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制相关
        canvas.drawColor(Color.DKGRAY);
        canvas.drawCircle(posX, posY, 300, mFillPaint);
        canvas.drawCircle(posX, posY, 300, mStrokePaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            posX = event.getX();
            posY = event.getY();

            invalidate();
        }
        return true;
    }
}
