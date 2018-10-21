package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * tips :
 * *
 * time : 18-10-19上午11:51
 * owner: jasonjan
 */
public class CustomView01 extends View implements Runnable{

    private Paint mPaint;
    private int radiu;
    private static volatile boolean isStop=false;

    public CustomView01(Context context){
        this(context,null);
    }

    public CustomView01(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public CustomView01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //模式相关
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);//打开抗锯齿模式

        //风格相关
        mPaint.setStyle(Paint.Style.STROKE);

        //颜色相关
        mPaint.setColor(Color.RED);

        //粗细相关
        mPaint.setStrokeWidth(10);

        //初始值相关
        radiu=200;//半径

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //绘制圆环
        /*canvas.drawCircle( ScreenUtils.getScreenWidth(DemoApp.getInstance())/2,
                ScreenUtils.getScreenHeight(DemoApp.getInstance())/2,200,mPaint);*/
        canvas.drawCircle( 250, 250,radiu,mPaint);

    }

    public synchronized void setRadiu(int radiu){
        this.radiu=radiu;
        invalidate();//重写执行onDraw
    }

    public synchronized void setIsStop(boolean isStop_){
        isStop=isStop_;
        setRadiu(200);
    }

    @Override
    public void run() {
        while (true&&!isStop){
            try{
                if(radiu<=200){
                    radiu+=10;
                    //invalidate();无法在子线程中更新UI,故这种方法错误
                    postInvalidate();
                }else{
                    radiu=0;
                }
                Thread.sleep(40);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
