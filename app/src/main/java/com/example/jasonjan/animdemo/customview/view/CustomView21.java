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

import com.example.jasonjan.animdemo.DemoApp;

/**
 * tips : Paint绘制相关
 * *
 * time : 18-10-20上午9:07
 * owner: jasonjan
 */
public class CustomView21 extends View {

    private static final String TEXT ="大家都说我的性子很慢，其实我也可以很快 。" +
            "比如，后面有狗追我，或者，你在前面等我。 ";

    private TextPaint mTextPaint,specialPaint;// 画笔
    private StaticLayout mStaticLayout;// 文本布局
    private boolean isChangeTypeface;

    public CustomView21(Context context) {
        this(context,null);
        init();
    }

    public CustomView21(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView21(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        
        //字体相关
        Typeface typeface=Typeface.createFromAsset(DemoApp.getInstance().getAssets(),"typeface1.ttf");

        //画笔1相关
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(50);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSkewX(-0.25f);//设置文字倾斜

        //画笔2相关
        specialPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        specialPaint.setTextSize(50);
        specialPaint.setColor(Color.BLACK);
        specialPaint.setTypeface(typeface);

        //布局相关
        mStaticLayout = new StaticLayout(TEXT, mTextPaint, getWidth(),
                Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mStaticLayout.draw(canvas);

    }

    public void doChangeTypeface(){
        if(isChangeTypeface){
            isChangeTypeface=false;
            mStaticLayout = new StaticLayout(TEXT, mTextPaint, getWidth(),
                    Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            invalidate();
        }else{
            isChangeTypeface=true;
            mStaticLayout = new StaticLayout(TEXT, specialPaint, getWidth(),
                    Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, false);
            invalidate();
        }
    }
}
