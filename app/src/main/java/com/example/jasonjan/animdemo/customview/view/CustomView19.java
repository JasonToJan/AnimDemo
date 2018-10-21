package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.util.LogHelper;

/**
 * tips : Paint绘制相关
 * *
 * time : 18-10-20上午9:07
 * owner: jasonjan
 */
public class CustomView19 extends View {

    private static final String TEXT = "ap爱哥ξτβбпшㄎㄊěǔぬも┰┠№＠↓";
    private Paint textPaint,linePaint;// 画笔
    private Paint.FontMetrics mFontMetrics;// 文本测量对象
    private boolean isCenter;
    private boolean isClick;

    public CustomView19(Context context) {
        this(context,null);
        init();
    }

    public CustomView19(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView19(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //画笔相关
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(50);
        textPaint.setColor(Color.BLACK);
        textPaint.setTypeface(Typeface.SERIF);//字体

        //画笔相关#画线用的
        linePaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(1);
        linePaint.setColor(Color.RED);

        //测试相关
        mFontMetrics=textPaint.getFontMetrics();
        LogHelper.d("ascent:"+mFontMetrics.ascent+"\ntop:"+mFontMetrics.top +"\nleading:"+mFontMetrics.leading
                +"\ndescent:"+mFontMetrics.descent+"\nbottom:"+mFontMetrics.bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isCenter){
            //坐标相关
            int baseX = (int) (getWidth() / 2 - textPaint.measureText(TEXT) / 2);//Baseline的起点x坐标
            int baseY = (int) ((getHeight() / 2) - ((textPaint.descent() + textPaint.ascent()) / 2));

            //绘制相关
            canvas.drawText(TEXT,baseX,baseY,textPaint);
            canvas.drawLine(0,getHeight()/2,getWidth(),getHeight()/2,linePaint);

        }else{
            //绘制文本相关
            canvas.drawText(TEXT,0,Math.abs(mFontMetrics.top),textPaint);
        }
    }

    public String getText(){
        if(isClick){
            isClick=false;
            return "点击获取参数信息";
        }else{
            isClick=true;
            return "ascent:"+mFontMetrics.ascent+"\ntop:"+mFontMetrics.top +"\nleading:"+mFontMetrics.leading
                    +"\ndescent:"+mFontMetrics.descent+"\nbottom:"+mFontMetrics.bottom;
        }
    }

    public void doTextCenter(){
        if(isCenter){
            isCenter=false;
            invalidate();
        }else{
            isCenter=true;
            invalidate();
        }
    }
}
