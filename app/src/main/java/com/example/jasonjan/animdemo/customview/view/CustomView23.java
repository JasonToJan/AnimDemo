package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.DemoApp;
import com.example.jasonjan.animdemo.R;

/**
 * tips : 图片阴影效果
 * *
 * time : 18-10-20上午10:54
 * owner: jasonjan
 */
public class CustomView23 extends View {

    private Paint mPaint;
    private Bitmap srcBitmap,shadowBitmap;//位图和阴影位图
    private boolean isShadow;

    public CustomView23(Context context) {
        this(context,null);
        init();
    }

    public CustomView23(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView23(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

        //画笔配置相关
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setColor(Color.DKGRAY);
        mPaint.setMaskFilter(new BlurMaskFilter(10, BlurMaskFilter.Blur.NORMAL));//设置画笔遮罩滤镜

        //其他相关
        setLayerType(LAYER_TYPE_SOFTWARE, null);//关闭该View的硬加速

        //获取资源相关
        srcBitmap = BitmapFactory.decodeResource(DemoApp.getInstance().getResources(), R.drawable.rita3);
        shadowBitmap = srcBitmap.extractAlpha();//获取位图Alpha通道

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //矩形配置相关
        Rect srcRect=new Rect(0,0,srcBitmap.getWidth(),srcBitmap.getHeight());
        Rect dstRect=new Rect(15,15,1065,1050*srcBitmap.getHeight()/srcBitmap.getWidth()+15);

        if(isShadow){
            //绘制相关
            canvas.drawBitmap(shadowBitmap,srcRect,dstRect,mPaint);//先绘制阴影
            canvas.drawBitmap(srcBitmap,srcRect,dstRect,null);
        }else{
            canvas.drawBitmap(srcBitmap,srcRect,dstRect,null);
        }
    }

    public void doMakeShadow(){
        if(isShadow){
            isShadow=false;
            invalidate();
        }else{
            isShadow=true;
            invalidate();
        }
    }
}
