//
// FPlayAndroid is distributed under the FreeBSD License
//
// Copyright (c) 2013-2014, Carlos Rafael Gimenes das Neves
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice, this
//    list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright notice,
//    this list of conditions and the following disclaimer in the documentation
//    and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
// ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
// WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
// DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
// ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
// (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
// LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
// ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
// (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
// SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
//
// The views and conclusions contained in the software and documentation are those
// of the authors and should not be interpreted as representing official policies,
// either expressed or implied, of the FreeBSD Project.
//
// https://github.com/carlosrafaelgn/FPlayAndroid
//
package net.coocent.tool.visualizer;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.net.Uri;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.ViewDebug.ExportedProperty;
import android.view.WindowManager;

import com.example.jasonjan.animdemo.R;

import net.coocent.tool.visualizer.ui.ColorDrawable;
import net.coocent.tool.visualizer.ui.TextIconDrawable;
import net.coocent.tool.visualizer.ui.UI;
import net.coocent.tool.visualizer.util.ArraySorter;
import net.coocent.tool.visualizer.util.MainHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL10;


@TargetApi(11)
public final class OpenGLVisualizerJni extends GLSurfaceView implements GLSurfaceView.Renderer, GLSurfaceView.EGLContextFactory, GLSurfaceView.EGLWindowSurfaceFactory, Visualizer, MenuItem.OnMenuItemClickListener, MainHandler.Callback {

	/**
	 * OpenGL错误代号
	 */
	private static final int MSG_OPENGL_ERROR = 0x0600;
	/**
	 * 选择图片代号
	 */
	private static final int MSG_CHOOSE_IMAGE = 0x0601;
	/**
	 * GL版本号，默认为-1
	 */
	private static int GLVersion = -1;
	/**
	 * 类型
	 */
	private final int type;
	/**
	 * 波形数组
	 */
	private byte[] waveform;
	/**
	 * 是否支持，是否警告，是否可以渲染
	 */
	private volatile boolean supported, alerted, okToRender;
	/**
	 * 错误代号
	 */
	private volatile int error;
	/**
	 * 选择的uri
	 */
	private volatile Uri selectedUri;
	/**
	 * 是否在浏览中
	 */
	private boolean browsing;
	/**
	 * 颜色索引，速度，视图宽度，视图高度，间隔，上升速度，忽略输入
	 */
	private int colorIndex, speed, viewWidth, viewHeight, diffusion, riseSpeed, ignoreInput;
	/**
	 * EGL配置
	 */
	private EGLConfig config;
	/**
	 * 活动
	 */
	private Activity activity;
	/**
	 * 窗口管理员
	 */
	private WindowManager windowManager;
	/**
	 * OpenGL重力感应
	 */
	private OpenGLVisualizerSensorManager sensorManager;
	/**
	 * 照相机实例
	 */
	private Camera camera;
	/**
	 * 照相机文本视图
	 */
	private SurfaceTexture cameraTexture;
	/**
	 * 相机原生方向
	 */
	private int cameraNativeOrientation;
	/**
	 * 相机准备好
	 */
	private volatile boolean cameraOK;
	/**
	 * 不支持的时候回调到调用者出移除视图
	 */
	private NotSupportListener notSupportListener;
	
	public OpenGLVisualizerJni(Activity activity, boolean landscape, int _type) {
		super(activity.getApplicationContext());
		this.activity = activity;
		final Context context = activity.getApplicationContext();
		this.notSupportListener=notSupportListener;

		//初始化数值
		final int t = _type;
		type = ((t < VisualParams.TYPE_LIQUID || t > VisualParams.TYPE_HORN) ? VisualParams.TYPE_SPECTRUM : t);
		waveform = new byte[Visualizer.CAPTURE_SIZE];
		colorIndex = 0;
		speed = ((type == VisualParams.TYPE_LIQUID) ? 0 : 2);
		diffusion = ((type == VisualParams.TYPE_IMMERSIVE_PARTICLE_VR) ? 3 : 1);
		riseSpeed = ((type == VisualParams.TYPE_IMMERSIVE_PARTICLE_VR) ? 3 : 2);
		ignoreInput = 0;
		if (landscape) {
			viewWidth = 1024;
			viewHeight = 512;
		} else {
			viewWidth = 512;
			viewHeight = 1024;
		}

		//是否支持OpenGL
		if (GLVersion != -1) {
			supported = (GLVersion >= 0x00020000);
			if (!supported)
				MainHandler.sendMessage(this, MSG_OPENGL_ERROR);
		}

		//获取窗口管理器实例
		try {
			windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		} catch (Throwable ex) {
			windowManager = null;
		}

		//生产重力感应实例
		if (type == VisualParams.TYPE_HORN || type == VisualParams.TYPE_IMMERSIVE_PARTICLE 
				|| type == VisualParams.TYPE_IMMERSIVE_PARTICLE_VR) {
			sensorManager = new OpenGLVisualizerSensorManager(context, false);
			if (!sensorManager.isCapable) {
				sensorManager = null;
			} else {
				sensorManager.start();
				CharSequence originalText = activity.getResources().getString(R.string.move_device);
				final int iconIdx = originalText.toString().indexOf('\u21BA');
				if (iconIdx >= 0) {
					final SpannableStringBuilder txt = new SpannableStringBuilder(originalText);
					txt.setSpan(new ImageSpan(new TextIconDrawable(UI.ICON_3DPAN, UI.color_text, UI._22sp, 0), DynamicDrawableSpan.ALIGN_BASELINE), iconIdx, iconIdx + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					originalText = txt;
				}
				if(type == VisualParams.TYPE_IMMERSIVE_PARTICLE 
						|| type == VisualParams.TYPE_IMMERSIVE_PARTICLE_VR){
					if( PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("SHOW_VISUALIZER_TIPS",false)==false) {
						UI.customToast(activity, originalText, true, UI._22sp, UI.color_btn, new ColorDrawable(0x7f000000 | (UI.color_visualizer565 & 0x00ffffff)));
						PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean("SHOW_VISUALIZER_TIPS",true).commit();
					}
				}
			}
		}
		
		//http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.3_r1/android/opengl/GLSurfaceView.java
		//getHolder().setFormat(PixelFormat.RGB_565);
		//setEGLContextClientVersion(2);
		//setEGLConfigChooser(5, 6, 5, 0, 0, 0);

		//设置GLSurfaceView相关属性
		setEGLContextFactory(this);
		setEGLWindowSurfaceFactory(this);
		setRenderer(this);
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			setPreserveEGLContext();
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setPreserveEGLContext() {
		setPreserveEGLContextOnPause(false);
	}

	/**
	 * 创建窗口Surface
	 * @param egl
	 * @param display
	 * @param config
	 * @param native_window
	 * @return
	 */
	@Override
	public EGLSurface createWindowSurface(EGL10 egl, EGLDisplay display, EGLConfig config, Object native_window) {
		try {
			return egl.eglCreateWindowSurface(display, (this.config != null) ? this.config : config, native_window, null);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 销毁Surface
	 * @param egl
	 * @param display
	 * @param surface
	 */
	@Override
	public void destroySurface(EGL10 egl, EGLDisplay display, EGLSurface surface) {
		if (egl != null && display != null && surface != null)
			egl.eglDestroySurface(display, surface);
	}

	/**
	 * 创建Context
	 * @param egl
	 * @param display
	 * @param config
	 * @return
	 */
	@Override
	public EGLContext createContext(final EGL10 egl, final EGLDisplay display, EGLConfig config) {

		//https://www.khronos.org/registry/egl/sdk/docs/man/html/eglChooseConfig.xhtml
		//https://www.khronos.org/registry/egl/sdk/docs/man/html/eglCreateContext.xhtml

		egl.eglMakeCurrent(display, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
		this.config = null;
		//EGL_FALSE = 0
		//EGL_TRUE = 1
		//EGL_OPENGL_ES2_BIT = 4
		//EGL_CONTEXT_CLIENT_VERSION = 0x3098
		final EGLConfig[] configs = new EGLConfig[64], selectedConfigs = new EGLConfig[64];
		final int[] num_config = { 0 }, value = new int[1];
		final int[] none = { EGL10.EGL_NONE };
		final int[] v2 = { 0x3098, 2, EGL10.EGL_NONE };
		int selectedCount = 0;
		if (egl.eglGetConfigs(display, configs, 32, num_config) && num_config[0] > 0) {
			for (int i = 0; i < num_config[0]; i++) {
				egl.eglGetConfigAttrib(display, configs[i], EGL10.EGL_RENDERABLE_TYPE, value);
				if ((value[0] & 4) == 0) continue;
				egl.eglGetConfigAttrib(display, configs[i], EGL10.EGL_SURFACE_TYPE, value);
				if ((value[0] & EGL10.EGL_WINDOW_BIT) == 0) continue;
				//egl.eglGetConfigAttrib(display, configs[i], EGL10.EGL_COLOR_BUFFER_TYPE, value);
				//if (value[0] != EGL10.EGL_RGB_BUFFER) continue;
				egl.eglGetConfigAttrib(display, configs[i], EGL10.EGL_RED_SIZE, value);
				if (value[0] < 4) continue;
				egl.eglGetConfigAttrib(display, configs[i], EGL10.EGL_GREEN_SIZE, value);
				if (value[0] < 4) continue;
				egl.eglGetConfigAttrib(display, configs[i], EGL10.EGL_BLUE_SIZE, value);
				if (value[0] < 4) continue;
				selectedConfigs[selectedCount++] = configs[i];
			}
		}
		if (selectedCount == 0) {
			supported = false;
			MainHandler.sendMessage(this, MSG_OPENGL_ERROR);
			return egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, none);
		}
		ArraySorter.sort(selectedConfigs, 0, selectedCount, new ArraySorter.Comparer<EGLConfig>() {
			@Override
			public int compare(EGLConfig a, EGLConfig b) {
				int x;
				egl.eglGetConfigAttrib(display, a, EGL10.EGL_COLOR_BUFFER_TYPE, value);
				x = value[0];
				egl.eglGetConfigAttrib(display, b, EGL10.EGL_COLOR_BUFFER_TYPE, value);
				//prefer rgb buffers
				if (x != value[0])
					return (x == EGL10.EGL_RGB_BUFFER) ? -1 : 1;
				egl.eglGetConfigAttrib(display, a, EGL10.EGL_NATIVE_RENDERABLE, value);
				x = value[0];
				egl.eglGetConfigAttrib(display, b, EGL10.EGL_NATIVE_RENDERABLE, value);
				//prefer native configs
				if (x != value[0])
					return (x != 0) ? -1 : 1;
				egl.eglGetConfigAttrib(display, a, EGL10.EGL_SAMPLE_BUFFERS, value);
				x = value[0];
				egl.eglGetConfigAttrib(display, b, EGL10.EGL_SAMPLE_BUFFERS, value);
				//prefer smaller values
				if (x != value[0])
					return (x - value[0]);
				egl.eglGetConfigAttrib(display, a, EGL10.EGL_SAMPLES, value);
				x = value[0];
				egl.eglGetConfigAttrib(display, b, EGL10.EGL_SAMPLES, value);
				//prefer smaller values
				if (x != value[0])
					return (x - value[0]);
				egl.eglGetConfigAttrib(display, a, EGL10.EGL_BUFFER_SIZE, value);
				x = value[0];
				egl.eglGetConfigAttrib(display, b, EGL10.EGL_BUFFER_SIZE, value);
				//prefer smaller values
				if (x != value[0])
					return (x - value[0]);
				egl.eglGetConfigAttrib(display, a, EGL10.EGL_DEPTH_SIZE, value);
				x = value[0];
				egl.eglGetConfigAttrib(display, b, EGL10.EGL_DEPTH_SIZE, value);
				//prefer smaller values
				if (x != value[0])
					return (x - value[0]);
				egl.eglGetConfigAttrib(display, a, EGL10.EGL_STENCIL_SIZE, value);
				x = value[0];
				egl.eglGetConfigAttrib(display, b, EGL10.EGL_STENCIL_SIZE, value);
				//prefer smaller values
				if (x != value[0])
					return (x - value[0]);
				egl.eglGetConfigAttrib(display, a, EGL10.EGL_ALPHA_MASK_SIZE, value);
				x = value[0];
				egl.eglGetConfigAttrib(display, b, EGL10.EGL_ALPHA_MASK_SIZE, value);
				//prefer smaller values
				if (x != value[0])
					return (x - value[0]);
				egl.eglGetConfigAttrib(display, a, EGL10.EGL_ALPHA_SIZE, value);
				x = value[0];
				egl.eglGetConfigAttrib(display, b, EGL10.EGL_ALPHA_SIZE, value);
				//prefer smaller values
				if (x != value[0])
					return (x - value[0]);
				egl.eglGetConfigAttrib(display, a, EGL10.EGL_CONFIG_ID, value);
				x = value[0];
				egl.eglGetConfigAttrib(display, b, EGL10.EGL_CONFIG_ID, value);
				//prefer smaller values
				return (x - value[0]);
			}
		});
		//according to this:
		//http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.3_r1/android/opengl/GLSurfaceView.java#941
		//the native_window parameter in cretaeWindowSurface is this SurfaceHolder 
		final SurfaceHolder holder = getHolder();
		for (int i = 0; i < selectedCount; i++) {
			final int r, g, b;
			egl.eglGetConfigAttrib(display, selectedConfigs[i], EGL10.EGL_BUFFER_SIZE, value);
			egl.eglGetConfigAttrib(display, selectedConfigs[i], EGL10.EGL_SAMPLE_BUFFERS, value);
			egl.eglGetConfigAttrib(display, selectedConfigs[i], EGL10.EGL_SAMPLES, value);
			egl.eglGetConfigAttrib(display, selectedConfigs[i], EGL10.EGL_DEPTH_SIZE, value);
			egl.eglGetConfigAttrib(display, selectedConfigs[i], EGL10.EGL_STENCIL_SIZE, value);
			egl.eglGetConfigAttrib(display, selectedConfigs[i], EGL10.EGL_ALPHA_SIZE, value);
			egl.eglGetConfigAttrib(display, selectedConfigs[i], EGL10.EGL_ALPHA_MASK_SIZE, value);
			egl.eglGetConfigAttrib(display, selectedConfigs[i], EGL10.EGL_RED_SIZE, value);
			r = value[0];
			egl.eglGetConfigAttrib(display, selectedConfigs[i], EGL10.EGL_GREEN_SIZE, value);
			g = value[0];
			egl.eglGetConfigAttrib(display, selectedConfigs[i], EGL10.EGL_BLUE_SIZE, value);
			b = value[0];
			if (r != 8 || g != 8 || b != 8) {
				if (r != 5 || g != 6 || b != 5)
					continue;
			}
			EGLSurface surface = null;
			try {
				this.config = selectedConfigs[i];
				EGLContext ctx = egl.eglCreateContext(display, this.config, EGL10.EGL_NO_CONTEXT, v2);
				if (ctx == null || ctx == EGL10.EGL_NO_CONTEXT)
					ctx = egl.eglCreateContext(display, this.config, EGL10.EGL_NO_CONTEXT, none);
				if (ctx != null && ctx != EGL10.EGL_NO_CONTEXT) {
					//try to create a surface and make it current successfully
					//before confirming this is the right config/context
					try{
						holder.setFormat((r == 5) ? PixelFormat.RGB_565 : PixelFormat.RGBA_8888);
						surface = egl.eglCreateWindowSurface(display, this.config, holder, null);
					}catch (Exception e){
						e.printStackTrace();
						return null;
					}

					if (surface != null && surface != EGL10.EGL_NO_SURFACE) {
						//try to make current
						if (egl.eglMakeCurrent(display, surface, surface, ctx)) {
							//yes! this combination works!!!
							return ctx;
						}
						this.config = null;
					}
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
			} finally {
				egl.eglMakeCurrent(display, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
				if (surface != null && surface != EGL10.EGL_NO_SURFACE)
					egl.eglDestroySurface(display, surface);
			}
		}
		this.config = null;
		supported = false;
		MainHandler.sendMessage(this, MSG_OPENGL_ERROR);
		return egl.eglCreateContext(display, config, EGL10.EGL_NO_CONTEXT, none);
	}

	/**
	 * 销毁Context
	 * @param egl
	 * @param display
	 * @param context
	 */
	@Override
	public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
		if (egl != null && display != null && context != null)
			egl.eglDestroyContext(display, context);
	}

	/**
	 * 释放相机
	 */
	private void releaseCamera() {
		if (camera != null) {
			try {
				camera.stopPreview();
			} catch (Throwable ex2) {
				ex2.printStackTrace();
			}
			try {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
					camera.setPreviewTexture(null);
			} catch (Throwable ex2) {
				ex2.printStackTrace();
			}
			try {
				camera.release();
			} catch (Throwable ex2) {
				ex2.printStackTrace();
			}
			camera = null;
			cameraOK = false;
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (cameraTexture != null) {
				cameraTexture.setOnFrameAvailableListener(null);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
					cameraTexture.release();
				cameraTexture = null;
			}
		}
	}

	/**
	 * 创建Surface完成
	 * Runs on a SECONDARY thread (A)
	 * @param gl
	 * @param config
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		if (type == VisualParams.TYPE_SPECTRUM)
			SimpleVisualizerJni.commonSetColorIndex(colorIndex);
		SimpleVisualizerJni.commonSetSpeed(speed);
		if (GLVersion == -1) {
			supported = true;
			try {
				//https://www.khronos.org/opengles/sdk/docs/man/xhtml/glGetString.xml
				final String version = gl.glGetString(GL10.GL_VERSION).toLowerCase(Locale.US);
				if (version.indexOf("opengl es ") == 0 &&
					version.length() > 12 &&
					version.charAt(10) >= '0' &&
					version.charAt(10) <= '9' ) {
					final int len = version.length();
					GLVersion = 0;
					int i = 10;
					char c = 0;
					for (; i < len; i++) {
						c = version.charAt(i);
						if (c < '0' || c > '9')
							break;
						GLVersion = (GLVersion << 4) | ((c - '0') << 16);
					}
					if (GLVersion == 0) {
						GLVersion = -1;
					} else {
						if (c == '.') {
							i++;
							int shift = 12;
							for (; i < len; i++) {
								if (shift < 0)
									break;
								c = version.charAt(i);
								if (c < '0' || c > '9')
									break;
								GLVersion |= ((c - '0') << shift);
								shift -= 4;
							}
						}
						supported = (GLVersion >= 0x00020000);
						if (!supported)
							MainHandler.sendMessage(this, MSG_OPENGL_ERROR);
					}
				}
			} catch (Throwable ex) {
				GLVersion = -1;
				ex.printStackTrace();
			}
			if (GLVersion == -1) {
				//if the method above fails, try to get opengl version the hard way!
				Process ifc = null;
				BufferedReader bis = null;
				try {
					ifc = Runtime.getRuntime().exec("getprop ro.opengles.version");
					bis = new BufferedReader(new InputStreamReader(ifc.getInputStream()));
					String line = bis.readLine();
					GLVersion = Integer.parseInt(line);
					supported = (GLVersion >= 0x00020000);
					if (!supported)
						MainHandler.sendMessage(this, MSG_OPENGL_ERROR);
				} catch (Throwable ex) {
					ex.printStackTrace();
				} finally {
					try {
						if (bis != null)
							bis.close();
					} catch (Throwable ex) {
						ex.printStackTrace();
					}
					try {
						if (ifc != null)
							ifc.destroy();
					} catch (Throwable ex) {
						ex.printStackTrace();
					}
				}
			}
		}
		if (!supported)
			return;
		if (type == VisualParams.TYPE_IMMERSIVE_PARTICLE_VR) {
			final String extensions = GLES20.glGetString(GLES20.GL_EXTENSIONS);
			if (!extensions.contains("OES_EGL_image_external")) {
				error = 0;
				supported = false;
				MainHandler.sendMessage(this, MSG_OPENGL_ERROR);
				return;
			}
			synchronized (this) {
				try {
					int cameraId = -1;
					final Camera.CameraInfo info = new Camera.CameraInfo();
					final int numberOfCameras = Camera.getNumberOfCameras();
					for (int i = 0; i < numberOfCameras; i++) {
						Camera.getCameraInfo(i, info);
						if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
							cameraId = i;
							break;
						}
					}
					if (cameraId == -1) {
						Camera.getCameraInfo(0, info);
						cameraId = 0;
					}
					camera = ((cameraId >= 0) ? Camera.open(cameraId) : null);
					cameraNativeOrientation = info.orientation;
				} catch (Throwable ex) {
					if (camera != null) {
						camera.release();
						camera = null;
					}
				}
				if (camera == null) {
					error = 0;
					supported = false;
					MainHandler.sendMessage(this, MSG_OPENGL_ERROR);
					return;
				}
			}
		}
		if ((error = SimpleVisualizerJni.glOnSurfaceCreated(Color.BLACK, type, 720, 1280, (1 < 2) ? 1 : 0, (sensorManager != null && sensorManager.hasGyro) ? 1 : 0)) != 0) {

			supported = false;
			MainHandler.sendMessage(this, MSG_OPENGL_ERROR);
			
		} else if (type == VisualParams.TYPE_IMMERSIVE_PARTICLE_VR && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			
			synchronized (this) {
				cameraTexture = new SurfaceTexture(SimpleVisualizerJni.glGetOESTexture());
				cameraTexture.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
					@Override
					public void onFrameAvailable(SurfaceTexture surfaceTexture) {
						cameraOK = true;
					}
				});
				try {
					camera.setPreviewTexture(cameraTexture);
				} catch (Throwable ex) {
					releaseCamera();
					error = 0;
					supported = false;
					MainHandler.sendMessage(this, MSG_OPENGL_ERROR);
				}
			}
		}
		if(sensorManager != null && (type == VisualParams.TYPE_IMMERSIVE_PARTICLE
				|| type == VisualParams.TYPE_IMMERSIVE_PARTICLE_VR)){
			sensorManager.reset();
			sensorManager.register();
		}
	}

	/**
	 * Surface发生改变
	 * Runs on a SECONDARY thread (A)
	 * @param gl
	 * @param width
	 * @param height
	 */
	@Override
	@SuppressWarnings("deprecation")
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		if (!supported)
			return;
		//http://developer.download.nvidia.com/tegra/docs/tegra_android_accelerometer_v5f.pdf
		int rotation;
		if (windowManager != null) {
			try {
				rotation = windowManager.getDefaultDisplay().getRotation();
			} catch (Throwable ex) {
				//silly assumption for phones....
				rotation = ((width >= height) ? Surface.ROTATION_90 : Surface.ROTATION_0);
			}
		} else {
			//silly assumption for phones....
			rotation = ((width >= height) ? Surface.ROTATION_90 : Surface.ROTATION_0);
		}
		
		int cameraPreviewW = 0, cameraPreviewH = 0;
		if (camera != null) {
			synchronized (this) {
				try {
					int degrees = 0;
					switch (rotation) {
					case Surface.ROTATION_0:
						degrees = 0;
						break;
					case Surface.ROTATION_90:
						degrees = 90;
						break;
					case Surface.ROTATION_180:
						degrees = 180;
						break;
					case Surface.ROTATION_270:
						degrees = 270;
						break;
					}
					
					final int cameraDisplayOrientation = (cameraNativeOrientation - degrees + 360) % 360;
					camera.setDisplayOrientation(cameraDisplayOrientation);
					final Camera.Parameters parameters = camera.getParameters();

					//try to find the ideal preview size...
					final List<Camera.Size> localSizes = parameters.getSupportedPreviewSizes();
					int largestW = 0, largestH = 0;
					float smallestRatioError = 10000.0f;
					final float viewRatio = (float)width / (float)height;
					if (cameraDisplayOrientation == 0 || cameraDisplayOrientation == 180) {
						for (int i = localSizes.size() - 1; i >= 0; i--) {
							final int w = localSizes.get(i).width, h = localSizes.get(i).height;
							final float ratioError = Math.abs(((float)w / (float)h) - viewRatio);
							if (w < width && h < height && w >= largestW && h >= largestH && ratioError <= (smallestRatioError + 0.001f)) {
								smallestRatioError = ratioError;
								largestW = w;
								largestH = h;
								cameraPreviewW = w;
								cameraPreviewH = h;
							}
						}
					} else {
						//getSupportedPreviewSizes IS NOT AFFECTED BY setDisplayOrientation
						//therefore, w and h MUST BE SWAPPED in 3 places
						for (int i = localSizes.size() - 1; i >= 0; i--) {
							final int w = localSizes.get(i).width, h = localSizes.get(i).height;
							//SWAP HERE
							final float ratioError = Math.abs(((float)h / (float)w) - viewRatio);
							if (h < width && w < height //SWAP HERE
								&& w >= largestW && h >= largestH && ratioError <= (smallestRatioError + 0.001f)) {
								smallestRatioError = ratioError;
								largestW = w;
								largestH = h;
								//SWAP HERE
								cameraPreviewW = h;
								cameraPreviewH = w;
							}
						}
					}
					if (largestW == 0) {
						largestW = localSizes.get(0).width;
						largestH = localSizes.get(0).height;
					}

					parameters.setPreviewSize(largestW, largestH);

					final List<String> localFocusModes = parameters.getSupportedFocusModes();
					if (localFocusModes != null) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH &&
							localFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
						} else if (localFocusModes.contains(Camera.Parameters.FOCUS_MODE_EDOF)) {
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_EDOF);
						} else if (localFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
							parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
						}
					}

					final List<String> localWhiteBalance = parameters.getSupportedWhiteBalance();
					if (localWhiteBalance != null && localWhiteBalance.contains(Camera.Parameters.WHITE_BALANCE_AUTO))
						parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);

					camera.setParameters(parameters);
					camera.startPreview();
					
				} catch (Throwable ex) {
					releaseCamera();
				}
			}
		}

		viewWidth = width;
		viewHeight = height;
		SimpleVisualizerJni.glOnSurfaceChanged(width, height, rotation, cameraPreviewW, cameraPreviewH, (1 < 2) ? 1 : 0);
		okToRender = true;
	}

	/**
	 * Surface销毁
	 * Runs on the MAIN thread
	 * @param holder
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//is it really necessary to call any cleanup code for OpenGL in Android??????
		okToRender = false;
		super.surfaceDestroyed(holder);
		//some times surfaceDestroyed is called, but after resuming,
		//onSurfaceCreated is not called, only onSurfaceChanged is! :/
	}

	/**
	 * 加载图片
	 */
	private void loadBitmap() {
		if (activity == null || selectedUri == null)
			return;
		InputStream input = null;
		Bitmap bitmap = null;
		try {
			input = activity.getContentResolver().openInputStream(selectedUri);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			bitmap = BitmapFactory.decodeStream(input, null, opts);
			input.close();
			input = null;

			final int maxDim = Math.max(320, Math.min(1024, Math.max(viewWidth, viewHeight)));

			opts.inSampleSize = 1;
			int largest = ((opts.outWidth >= opts.outHeight) ? opts.outWidth : opts.outHeight);
			while (largest > maxDim) {
				opts.inSampleSize <<= 1;
				largest >>= 1;
			}

			input = activity.getContentResolver().openInputStream(selectedUri);
			opts.inJustDecodeBounds = false;
			opts.inPreferredConfig = Bitmap.Config.RGB_565;
			opts.inDither = true;
			bitmap = BitmapFactory.decodeStream(input, null, opts);

			if (bitmap != null) {
				if (opts.outWidth != opts.outHeight && ((opts.outWidth > opts.outHeight) != (viewWidth > viewHeight))) {
					//rotate the image 90 degress
					final Matrix matrix = new Matrix();
					matrix.postRotate(-90);
					final Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
					if (bitmap != newBitmap && newBitmap != null) {
						bitmap.recycle();
						bitmap = newBitmap;
					}
					System.gc();
				}
				SimpleVisualizerJni.glLoadBitmapFromJava(bitmap);
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			}
			if (bitmap != null)
				bitmap.recycle();
			System.gc();
		}
	}

	/**
	 * 开始画画
	 * Runs on a SECONDARY thread (A)
	 * @param gl
	 */
	@Override
	public void onDrawFrame(GL10 gl) {
		if (okToRender) {
			if (selectedUri != null) {
				loadBitmap();
				selectedUri = null;
			}
			if (cameraOK && cameraTexture != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				synchronized (this) {
					if (cameraTexture != null)
						cameraTexture.updateTexImage();
				}
			}
			SimpleVisualizerJni.glDrawFrame();
		}
	}

	/**
	 * 消息回调
	 * @param msg
	 * @return
	 */
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_OPENGL_ERROR:
			if (!alerted) {
				alerted = true;
				if (activity == null)
					break;
				final Context ctx = activity.getApplication();
				UI.toast(ctx, ctx.getText(R.string.sorry) + " " + ((error != 0) ? "OpenGL error" + ": " + error : ctx.getText(R.string.opengl_not_supported).toString()) + " :(");
			}
			break;
		case MSG_CHOOSE_IMAGE:
			chooseImage();
			break;
		}
		return true;
	}
	
	@Override
	@ExportedProperty(category = "drawing")
	public final boolean isOpaque() {
		return true;
	}

	/**
	 * 选择图片
	 */
	private void chooseImage() {
		//Based on: http://stackoverflow.com/a/20177611/3569421
		//Based on: http://stackoverflow.com/a/4105966/3569421
		if (activity != null && selectedUri == null && !browsing && okToRender) {
			browsing = true;
			final Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			activity.startActivityForResult(intent, 1234);
		}
	}

	/**
	 * 菜单点击事件
	 * @param item
	 * @return
	 */
	@Override
	public boolean onMenuItemClick(MenuItem item) {
		final int id = item.getItemId();
		switch (id) {
		case VisualParams.MNU_COLOR:
			colorIndex = ((colorIndex == 0) ? 257 : 0);
			SimpleVisualizerJni.commonSetColorIndex(colorIndex);
			break;
		case VisualParams.MNU_SPEED0:
		case VisualParams.MNU_SPEED1:
		case VisualParams.MNU_SPEED2:
			speed = id - VisualParams.MNU_SPEED0;
			SimpleVisualizerJni.commonSetSpeed(speed);
			break;
		case VisualParams.MNU_DIFFUSION0:
		case VisualParams.MNU_DIFFUSION1:
		case VisualParams.MNU_DIFFUSION2:
		case VisualParams.MNU_DIFFUSION3:
			diffusion = id - VisualParams.MNU_DIFFUSION0;
			SimpleVisualizerJni.glSetImmersiveCfg(diffusion, -1);
			break;
		case VisualParams.MNU_RISESPEED0:
		case VisualParams.MNU_RISESPEED1:
		case VisualParams.MNU_RISESPEED2:
		case VisualParams.MNU_RISESPEED3:
			riseSpeed = id - VisualParams.MNU_RISESPEED0;
			SimpleVisualizerJni.glSetImmersiveCfg(-1, riseSpeed);
			break;
		case VisualParams.MNU_CHOOSE_IMAGE:
			chooseImage();
			break;
		}
		return true;
	}

	/**
	 * Activity回调
	 * Runs on the MAIN thread
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1234) {
			browsing = false;
			if (resultCode == Activity.RESULT_OK)
				selectedUri = data.getData();
		}
	}

	public void onActivityPause() {
		
	}

	public void onActivityResume() {
		
	}

	/**
	 * 创建菜单
	 * @param menu
	 */
	@Override
	public void onCreateContextMenu(ContextMenu menu) {
		if (activity == null)
			return;
		final Context ctx = activity.getApplication();
		Menu s;
		switch (type) {
		case VisualParams.TYPE_LIQUID:
			UI.separator(menu, 1, 0);
			menu.add(1, VisualParams.MNU_CHOOSE_IMAGE, 1, "Choose image")
				.setOnMenuItemClickListener(this)
				.setIcon(new TextIconDrawable(UI.ICON_PALETTE));
			break;
		case VisualParams.TYPE_SPIN:
		case VisualParams.TYPE_PARTICLE:
			break;
		case VisualParams.TYPE_HORN:
		case VisualParams.TYPE_IMMERSIVE_PARTICLE:
			UI.separator(menu, 1, 0);
		case VisualParams.TYPE_IMMERSIVE_PARTICLE_VR:
			s = menu.addSubMenu(1, 0, 1, "Diffusion" + "\u2026")
				.setIcon(new TextIconDrawable(UI.ICON_SETTINGS));
			UI.prepare(s);
			s.add(0, VisualParams.MNU_DIFFUSION0, 0, "Diffusion" + ": 0")
				.setOnMenuItemClickListener(this)
				.setIcon(new TextIconDrawable((diffusion == 0) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
			s.add(0, VisualParams.MNU_DIFFUSION1, 1, "Diffusion" + ": 1")
				.setOnMenuItemClickListener(this)
				.setIcon(new TextIconDrawable((diffusion != 0 && diffusion != 2 && diffusion != 3) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
			s.add(0, VisualParams.MNU_DIFFUSION2, 2, "Diffusion" + ": 2")
				.setOnMenuItemClickListener(this)
				.setIcon(new TextIconDrawable((diffusion == 2) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
			s.add(0, VisualParams.MNU_DIFFUSION3, 3, "Diffusion" + ": 3")
				.setOnMenuItemClickListener(this)
				.setIcon(new TextIconDrawable((diffusion == 3) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
			s = menu.addSubMenu(1, 0, 2, "Speed" + "\u2026")
				.setIcon(new TextIconDrawable(UI.ICON_SETTINGS));
			UI.prepare(s);
			s.add(0, VisualParams.MNU_RISESPEED0, 0, "Speed" + ": 0")
				.setOnMenuItemClickListener(this)
				.setIcon(new TextIconDrawable((riseSpeed == 0) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
			s.add(0, VisualParams.MNU_RISESPEED1, 1, "Speed" + ": 1")
				.setOnMenuItemClickListener(this)
				.setIcon(new TextIconDrawable((riseSpeed != 0 && riseSpeed != 2 && riseSpeed != 3) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
			s.add(0, VisualParams.MNU_RISESPEED2, 2, "Speed" + ": 2")
				.setOnMenuItemClickListener(this)
				.setIcon(new TextIconDrawable((riseSpeed == 2) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
			s.add(0, VisualParams.MNU_RISESPEED3, 3, "Speed" + ": 3")
				.setOnMenuItemClickListener(this)
				.setIcon(new TextIconDrawable((riseSpeed == 3) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
			break;
		default:
			UI.separator(menu, 1, 0);
			menu.add(1, VisualParams.MNU_COLOR, 1, (colorIndex == 0) ? "Green" : "Blue")
				.setOnMenuItemClickListener(this)
				.setIcon(new TextIconDrawable(UI.ICON_PALETTE));
			break;
		}
		UI.separator(menu, 2, 0);
		menu.add(2, VisualParams.MNU_SPEED0, 1, "Sustain:" + " 3")
			.setOnMenuItemClickListener(this)
			.setIcon(new TextIconDrawable((speed != 1 && speed != 2) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
		menu.add(2, VisualParams.MNU_SPEED1, 2, "Sustain:" + " 2")
			.setOnMenuItemClickListener(this)
			.setIcon(new TextIconDrawable((speed == 1) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
		menu.add(2, VisualParams.MNU_SPEED2, 3, "Sustain:" + " 1")
			.setOnMenuItemClickListener(this)
			.setIcon(new TextIconDrawable((speed == 2) ? UI.ICON_RADIOCHK : UI.ICON_RADIOUNCHK));
	}
	
	//Runs on the MAIN thread
	@Override
	public void onClick() {
	}

	//Runs on the MAIN thread (returned value MUST always be the same)
	@Override
	public boolean isFullscreen() {
		return true;
	}

	//Runs on the MAIN thread (called only if isFullscreen() returns false)
	public Point getDesiredSize(int availableWidth, int availableHeight) {
		return new Point(availableWidth, availableHeight);
	}

	//Runs on the MAIN thread (returned value MUST always be the same)
	@Override
	public boolean requiresHiddenControls() {
		return true;
	}

	//Runs on ANY thread
	@Override
	public int requiredDataType() {
		return DATA_FFT;
	}

	//Runs on ANY thread
	@Override
	public int requiredOrientation() {
		return (type == VisualParams.TYPE_IMMERSIVE_PARTICLE_VR ? ORIENTATION_LANDSCAPE : ORIENTATION_NONE);
	}

	//Runs on a SECONDARY thread (B)
	@Override
	public void load(Context context) {
		SimpleVisualizerJni.commonCheckNeonMode();
	}
	
	//Runs on ANY thread
	@Override
	public boolean isLoading() {
		return false;
	}
	
	//Runs on ANY thread
	@Override
	public void cancelLoading() {
		
	}
	
	//Runs on the MAIN thread
	@Override
	public void configurationChanged(boolean landscape) {
		return;
	}

	/**
	 * 绘制窗口
	 * Runs on a SECONDARY thread (B)
	 * @param visualizer
	 */
	@Override
	public void processFrame(android.media.audiofx.Visualizer visualizer) {
		if (okToRender) {
			//We use ignoreInput, because sampling 1024, 60 times a seconds,
			//is useless, as there are only 44100 or 48000 samples in one second
			if (ignoreInput == 0) {
				//WE MUST NEVER call any method from net.coocent.tool.net.coocent.tool.visualizer
				//while the player is not actually playing
				if (visualizer == null)
					Arrays.fill(waveform, 0, 1024, (byte)0x80);
				else
					visualizer.getWaveForm(waveform);
			}
			SimpleVisualizerJni.commonProcess(waveform, ignoreInput | DATA_FFT);
			ignoreInput ^= IGNORE_INPUT;
			//requestRender();
		}
	}

	/**
	 * 释放资源
	 * Runs on a SECONDARY thread (B)
	 */
	@Override
	public void release() {
		if (sensorManager != null)
			sensorManager.unregister();
		waveform = null;
		synchronized (this) {
			releaseCamera();
		}
	}

	/**
	 * 释放视图
	 * Runs on the MAIN thread (AFTER Visualizer.release())
	 */
	@Override
	public void releaseView() {
		windowManager = null;
		if (sensorManager != null) {
			sensorManager.release();
			sensorManager = null;
		}
		activity = null;
		SimpleVisualizerJni.glReleaseView();
	}
}