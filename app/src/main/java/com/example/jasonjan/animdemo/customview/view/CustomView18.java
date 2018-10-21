package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.jasonjan.animdemo.DemoApp;
import com.example.jasonjan.animdemo.R;

/**
 * tips : 图形交并集计算
 * *
 * time : 18-10-19下午5:12
 * owner: jasonjan
 */
public class CustomView18 extends View {

    private Paint mPaint;
    private Bitmap fgBitmap,bgBitmap;//前景图片和背景图片
    private static final int MIN_MOVE_DIS = 5;// 最小的移动距离：如果我们手指在屏幕上的移动距离小于此值则不会绘制
    private Path mPath;// 橡皮擦绘制路径
    private float preX, preY;// 记录上一个触摸事件的位置坐标
    private Canvas mCanvas;// 绘制橡皮擦路径的画布

    public CustomView18(Context context){
        this(context,null);
    }

    public CustomView18(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public CustomView18(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //初始化路径相关
        mPath=new Path();

        //初始化画笔相关
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setARGB(128,255,0,0);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setStyle(Paint.Style.STROKE);//风格为描边
        mPaint.setStrokeJoin(Paint.Join.ROUND);//路径结合处样式
        mPaint.setStrokeCap(Paint.Cap.ROUND);//笔触类型
        mPaint.setStrokeWidth(50);//描边宽度

        //初始化资源相关
        fgBitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);
        bgBitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.rita1);
        fgBitmap = Bitmap.createScaledBitmap(fgBitmap, 1080, 1920, true);
        bgBitmap = Bitmap.createScaledBitmap(bgBitmap, 1080, 1920, true);

        //初始化画布相关
        mCanvas=new Canvas(fgBitmap);
        mCanvas.drawColor(0xFF808080);//中性灰


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //背景相关
        canvas.drawBitmap(bgBitmap, 0, 0, null);

        //绘制相关
        canvas.drawBitmap(fgBitmap, 0, 0, null);
        mCanvas.drawPath(mPath, mPaint);//最关键的绘制路径，实现和用户交互

    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x=event.getX();
        float y=event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:// 手指接触屏幕重置路径
                mPath.reset();
                mPath.moveTo(x, y);
                preX = x;
                preY = y;
                break;

            case MotionEvent.ACTION_MOVE:// 手指移动时连接路径
                float dx = Math.abs(x - preX);
                float dy = Math.abs(y - preY);
                if (dx >= MIN_MOVE_DIS || dy >= MIN_MOVE_DIS) {
                    mPath.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
                    preX = x;
                    preY = y;
                }
                break;
        }

        invalidate();
        return true;//消费该事件
    }

    public void restore(){
        //置空相关
        mCanvas=null;
        mPath=null;

        fgBitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);
        mCanvas=new Canvas(fgBitmap);
        mCanvas.drawColor(0xFF808080);//中性灰
        mPath=new Path();
        invalidate();
    }
}
