package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.util.LogHelper;

/**
 * tips : Paint绘制相关
 * *
 * time : 18-10-20上午9:07
 * owner: jasonjan
 */
public class CustomView20 extends View {

    private static final String TEXT ="This is used by widgets to control text layout. " +
            "You should not need to use this class directly unless you are implementing " +
            "your own widget or custom display object," +
            " or would be tempted to call Canvas.drawText() directly.";

    private TextPaint mTextPaint;// 画笔
    private StaticLayout mStaticLayout;// 文本布局
    private boolean isNewLine;

    public CustomView20(Context context) {
        this(context,null);
        init();
    }

    public CustomView20(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView20(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //画笔相关
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(50);
        mTextPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isNewLine){
            //文本布局绘制相关
            mStaticLayout = new StaticLayout(TEXT, mTextPaint, canvas.getWidth(),
                    Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            mStaticLayout.draw(canvas);
        }else{
            canvas.drawText(TEXT,0,100,mTextPaint);
        }
    }

    public void doNewLine(){
        if(isNewLine){
            isNewLine=false;
            invalidate();
        }else{
            isNewLine=true;
            invalidate();
        }
    }
}
