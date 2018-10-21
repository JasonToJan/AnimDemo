package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.example.jasonjan.animdemo.R;
import com.example.jasonjan.animdemo.util.LogHelper;

/**
 * tips :
 * *
 * time : 18-10-19下午3:03
 * owner: jasonjan
 */
public class CustomView11 extends View {

    private Paint mPaint;
    private Bitmap bitmap;

    public CustomView11(Context context){
        this(context,null);
    }

    public CustomView11(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        init(context);
    }

    public CustomView11(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        //初始值相关#高饱和度效果
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1.438F, -0.122F, -0.016F, 0, -0.03F,
                -0.062F, 1.378F, -0.016F, 0, 0.05F,
                -0.062F, -0.122F, 1.483F, 0, -0.02F,
                0, 0, 0, 1, 0,
        });

        //模式相关
        mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);//打开抗锯齿模式

        //风格相关
        //mPaint.setStyle(Paint.Style.FILL);

        //颜色相关
        mPaint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));

        //粗细相关
        //mPaint.setStrokeWidth(10);

        //初始化资源相关
        bitmap=BitmapFactory.decodeResource(context.getResources(),R.drawable.rita1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //矩形配置相关
        Rect srcRect=new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
        Rect dstRect=new Rect(15,15,1065,1050*bitmap.getHeight()/bitmap.getWidth()+15);

        //绘制位图
        canvas.drawBitmap(bitmap,srcRect,dstRect,mPaint);
    }
}
