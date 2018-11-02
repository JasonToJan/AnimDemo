package net.coocent.tool.visualizer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

/**
 * 提供VisualizerView
 * 根据参数，提供不同类型的频谱效果
 * */
@TargetApi(9)
public class VisualViewProvider {

	private Activity mActivity;
	private int type = VisualParams.TYPE_SPECTRUM;
	private int audioSessionID = -1;
	private int width = 500;
	private int height = 500;
	private Visualizer mVisualizer;
	private FxVisualizer fxVisualizer;

	private VisualViewProvider(){

	}

	/**
	 * Constructor
	 * @param activity 当前运行的Activity
	 * @param _audioSessionID 当前播放器的sessionID
	 * @param _width 所要展示的宽度
	 * @param _height 所要展示的高度
	 * */
	public VisualViewProvider(Activity activity, int _audioSessionID, int _width, int _height){
		mActivity = activity;
		audioSessionID = _audioSessionID;
		width = _width;
		height = _height;
	}

	/**
	 * @param index 频谱的类型
	 * */
	public VisualViewProvider setType(int _type){
		type = _type;
		return this;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 根据type构建一个view,直接添加即可
	 * */
	public View build(){

		if(mVisualizer != null){
			mVisualizer.release();
			mVisualizer = null;
		}

		if(fxVisualizer != null){
			fxVisualizer.destroy();
			fxVisualizer = null;
		}

		if(type == VisualParams.TYPE_CLASSIC){
			mVisualizer = new SimpleVisualizerJni(mActivity);
		} else {
			mVisualizer = new OpenGLVisualizerJni(mActivity, true, type);
		}
		fxVisualizer = new FxVisualizer(mActivity.getApplication(),
				mVisualizer, audioSessionID);

		RelativeLayout.LayoutParams ip;
		final Point pt = mVisualizer.getDesiredSize(width, height);
		ip = new RelativeLayout.LayoutParams(pt.x, pt.y);
		ip.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		((View)mVisualizer).setLayoutParams(ip);

		mActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		/**
		 * 检测当前是否有播放器在播放音乐
		 * 如果有  则进行频谱的展示
		 *
		 AudioManager audioMgr = (AudioManager) mActivity.getSystemService(Context.AUDIO_SERVICE);
		 if(audioMgr.isMusicActive()) {
		 start();
		 }*/
		return (View)mVisualizer;
	}

	/**
	 * 播放器开始播放
	 * */
	public void start(){
		if(fxVisualizer != null){
			fxVisualizer.start();
		}
	}

	/**
	 * 播放器暂停播放
	 * */
	public void pause(){
		if(fxVisualizer != null){
			fxVisualizer.pause();
		}
	}

	/**
	 * 播放器停止播放
	 * */
	public void stop(){
		if(fxVisualizer != null){
			fxVisualizer.stop();
		}
	}

	/**
	 * 释放资源
	 * */
	public void release() {
		if(mVisualizer != null){
			mVisualizer.cancelLoading();
			mVisualizer.release();
		}
		if(fxVisualizer != null){
			fxVisualizer.destroy();
		}
		mActivity = null;
	}
}