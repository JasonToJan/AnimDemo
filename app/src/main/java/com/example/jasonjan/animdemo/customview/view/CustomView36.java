package com.example.jasonjan.animdemo.customview.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * tips : 自定义动画ListView
 * *
 * time : 18-11-4下午10:06
 * owner: jasonjan
 */
public class CustomView36 extends ListView {

    /**
     * 照相机实例
     */
    private Camera mCamera;
    /**
     * 矩阵实例
     */
    private Matrix mMatrix;

    public CustomView36(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCamera=new Camera();
        mMatrix=new Matrix();
    }

    public CustomView36(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //相机配置
        mCamera.save();
        mCamera.rotate(30,0,0);
        mCamera.getMatrix(mMatrix);

        //矩阵配置
        mMatrix.preTranslate(-getWidth()/2,-getHeight()/2);
        mMatrix.postTranslate(getWidth()/2,getHeight()/2);
        canvas.concat(mMatrix);

        //恢复相关
        mCamera.restore();//恢复保存状态
    }
}
