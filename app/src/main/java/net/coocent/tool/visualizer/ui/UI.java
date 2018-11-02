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
package net.coocent.tool.visualizer.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasonjan.animdemo.R;

import net.coocent.tool.visualizer.util.ColorUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormatSymbols;
import java.util.Locale;



//
//Unit conversions are based on:
//http://grepcode.com/file/repository.grepcode.com/java/ext/com.google.android/android/2.3.3_r1/android/util/TypedValue.java
//
@TargetApi(13)
public final class UI implements DialogInterface.OnShowListener, Animation.AnimationListener {

	public static int index = 0;
	//public static boolean hasInit = false;
	
	public static final int STATE_PRESSED = 1;
	public static final int STATE_FOCUSED = 2;
	public static final int STATE_CURRENT = 4;
	public static final int STATE_SELECTED = 8;
	public static final int STATE_MULTISELECTED = 16;
	
	public static final int LOCALE_NONE = 0;
	public static final int LOCALE_US = 1;
	public static final int LOCALE_PTBR = 2;
	public static final int LOCALE_RU = 3;
	public static final int LOCALE_UK = 4;
	public static final int LOCALE_ES = 5;
	public static final int LOCALE_DE = 6;
	public static final int LOCALE_FR = 7;
	public static final int LOCALE_MAX = 7;
	
	public static final int THEME_FPLAY = 3;

	public static final int TRANSITION_NONE = 0;
	public static final int TRANSITION_FADE = 1;
	public static final int TRANSITION_ZOOM = 2;
	public static final int TRANSITION_DISSOLVE = 3;
	public static final int TRANSITION_DURATION_FOR_ACTIVITIES = 300;
	public static final int TRANSITION_DURATION_FOR_VIEWS = 300;

	public static final int MSG_ADD = 0x0001;
	public static final int MSG_PLAY = 0x0002;
	
	public static final String ICON_PREV = "<";
	public static final String ICON_PLAY = "P";
	public static final String ICON_PAUSE = "|";
	public static final String ICON_NEXT = ">";
	public static final String ICON_MENU = "N";
	public static final String ICON_LIST = "l";
	public static final String ICON_MOVE = "M";
	public static final String ICON_REMOVE = "R";
	public static final String ICON_UP = "^";
	public static final String ICON_GOBACK = "_";
	public static final String ICON_SAVE = "S";
	public static final String ICON_LOAD = "L";
	public static final String ICON_FAVORITE_ON = "#";
	public static final String ICON_FAVORITE_OFF = "*";
	public static final String ICON_ADD = "A";
	public static final String ICON_HOME = "H";
	public static final String ICON_LINK = "U";
	public static final String ICON_EQUALIZER = "E";
	public static final String ICON_SETTINGS = "s";
	public static final String ICON_VISUALIZER = "V";
	public static final String ICON_EXIT = "X";
	public static final String ICON_VOLUME0 = "0";
	public static final String ICON_VOLUME1 = "1";
	public static final String ICON_VOLUME2 = "2";
	public static final String ICON_VOLUME3 = "3";
	public static final String ICON_VOLUME4 = "4";
	public static final String ICON_DECREASE_VOLUME = "-";
	public static final String ICON_INCREASE_VOLUME = "+";
	public static final String ICON_SEARCH = "F";
	public static final String ICON_INFORMATION = "I";
	public static final String ICON_QUESTION = "?";
	public static final String ICON_EXCLAMATION = "!";
	public static final String ICON_SHUFFLE = "h";
	public static final String ICON_REPEAT = "t";
	public static final String ICON_DELETE = "D";
	public static final String ICON_RADIOCHK = "x";
	public static final String ICON_RADIOUNCHK = "o";
	public static final String ICON_OPTCHK = "q";
	public static final String ICON_OPTUNCHK = "Q";
	public static final String ICON_GRIP = "G";
	public static final String ICON_FPLAY = "7";
	public static final String ICON_SLIDERTOP = "\"";
	public static final String ICON_SLIDERBOTTOM = "\'";
	public static final String ICON_RIGHT = "6";
	public static final String ICON_FADE = ";";
	public static final String ICON_DIAL = ":";
	public static final String ICON_HEADSET = "{";
	public static final String ICON_TRANSPARENT = "}";
	public static final String ICON_FLAT = "/";
	public static final String ICON_PALETTE = "[";
	public static final String ICON_LANGUAGE = "]";
	public static final String ICON_THEME = "\\";
	public static final String ICON_SPACELIST = "=";
	public static final String ICON_SPACEHEADER = "~";
	public static final String ICON_ORIENTATION = "@";
	public static final String ICON_DYSLEXIA = "$";
	public static final String ICON_SCREEN = "`";
	public static final String ICON_CLOCK = ".";
	public static final String ICON_DIVIDER = ",";
	public static final String ICON_MIC = "m";
	public static final String ICON_ALBUMART = "B";
	public static final String ICON_ALBUMART_OFF = "b";
	public static final String ICON_BLUETOOTH = "W";
	public static final String ICON_RADIO = "w";
	public static final String ICON_HAND = "a";
	public static final String ICON_SCROLLBAR = "c";
	public static final String ICON_SD = "d";
	public static final String ICON_FOLDER = "f";
	public static final String ICON_USB = "u";
	public static final String ICON_SEEKBAR = "5";
	public static final String ICON_TRANSITION = "%";
	public static final String ICON_REPEATONE = "y";
	public static final String ICON_ROOT = "(";
	public static final String ICON_3DPAN = ")";
	public static final String ICON_REPEATNONE = "Y";

	public static final int KEY_UP = KeyEvent.KEYCODE_DPAD_UP;
	public static final int KEY_DOWN = KeyEvent.KEYCODE_DPAD_DOWN;
	public static final int KEY_LEFT = KeyEvent.KEYCODE_DPAD_LEFT;
	public static final int KEY_RIGHT = KeyEvent.KEYCODE_DPAD_RIGHT;
	public static final int KEY_ENTER = KeyEvent.KEYCODE_DPAD_CENTER;
	public static final int KEY_DEL = KeyEvent.KEYCODE_FORWARD_DEL;
	public static final int KEY_EXTRA = KeyEvent.KEYCODE_SPACE;
	public static final int KEY_HOME = KeyEvent.KEYCODE_MOVE_HOME;
	public static final int KEY_END = KeyEvent.KEYCODE_MOVE_END;
	public static final int KEY_PAGE_UP = KeyEvent.KEYCODE_PAGE_UP;
	public static final int KEY_PAGE_DOWN = KeyEvent.KEYCODE_PAGE_DOWN;

	public static final int IDX_COLOR_WINDOW = 0;
	public static final int IDX_COLOR_CONTROL_MODE = 1;
	public static final int IDX_COLOR_VISUALIZER = 2;
	public static final int IDX_COLOR_LIST = 3;
	public static final int IDX_COLOR_MENU = 4;
	public static final int IDX_COLOR_MENU_ICON = 5;
	public static final int IDX_COLOR_MENU_BORDER = 6;
	public static final int IDX_COLOR_DIVIDER = 7;
	public static final int IDX_COLOR_HIGHLIGHT = 8;
	public static final int IDX_COLOR_TEXT_HIGHLIGHT = 9;
	public static final int IDX_COLOR_TEXT = 10;
	public static final int IDX_COLOR_TEXT_DISABLED = 11;
	public static final int IDX_COLOR_TEXT_LISTITEM = 12;
	public static final int IDX_COLOR_TEXT_LISTITEM_SECONDARY = 13;
	public static final int IDX_COLOR_TEXT_SELECTED = 14;
	public static final int IDX_COLOR_TEXT_MENU = 15;
	public static final int IDX_COLOR_SELECTED_GRAD_LT = 16;
	public static final int IDX_COLOR_SELECTED_GRAD_DK = 17;
	public static final int IDX_COLOR_SELECTED_BORDER = 18;
	public static final int IDX_COLOR_SELECTED_PRESSED = 19;
	public static final int IDX_COLOR_FOCUSED_GRAD_LT = 20;
	public static final int IDX_COLOR_FOCUSED_GRAD_DK = 21;
	public static final int IDX_COLOR_FOCUSED_BORDER = 22;
	public static final int IDX_COLOR_FOCUSED_PRESSED = 23;
	
	public static int color_btn;
	public static int color_window;
	public static int color_control_mode;
	public static int color_visualizer, color_visualizer565;
	public static int color_list;
	public static int color_list_bg;
	public static int color_list_original;
	public static int color_menu;
	public static int color_menu_icon;
	public static int color_menu_border;
	public static int color_divider;
	public static int color_divider_pressed;
	public static int color_highlight;
	public static int color_text_highlight;
	public static int color_text;
	public static int color_text_disabled;
	public static int color_text_listitem;
	public static int color_text_listitem_secondary;
	public static int color_text_selected;
	public static int color_text_menu;
	public static int color_text_title;
	public static int color_selected;
	public static int color_selected_multi;
	public static int color_selected_grad_lt;
	public static int color_selected_grad_dk;
	public static int color_selected_border;
	public static int color_selected_pressed;
	public static int color_selected_pressed_border;
	public static int color_focused;
	public static int color_focused_grad_lt;
	public static int color_focused_grad_dk;
	public static int color_focused_border;
	public static int color_focused_pressed;
	public static int color_focused_pressed_border;
	public static int color_glow;
	
	public static BgColorStateList colorState_text_reactive;
	/*public static BgColorStateList colorState_text_white_reactive;
	public static BgColorStateList colorState_text_menu_reactive;
	public static BgColorStateList colorState_text_static;
	public static BgColorStateList colorState_text_listitem_static;
	public static BgColorStateList colorState_text_listitem_reactive;
	public static BgColorStateList colorState_text_listitem_secondary_static;
	public static BgColorStateList colorState_text_selected_static;
	public static BgColorStateList colorState_text_title_static;
	public static BgColorStateList colorState_highlight_static;
	public static BgColorStateList colorState_text_highlight_static;
	public static BgColorStateList colorState_text_highlight_reactive;
	public static BgColorStateList colorState_text_control_mode_reactive;
	public static BgColorStateList colorState_text_visualizer_static;
	public static BgColorStateList colorState_text_visualizer_reactive;*/
	
	public static Typeface iconsTypeface;

	public static final class DisplayInfo {
		public int usableScreenWidth, usableScreenHeight, screenWidth, screenHeight;
		public boolean isLandscape, isLargeScreen, isLowDpiScreen;
		public DisplayMetrics displayMetrics;
		
		private void initializeScreenDimensions(Display display, DisplayMetrics outDisplayMetrics) {
			display.getMetrics(outDisplayMetrics);
			screenWidth = outDisplayMetrics.widthPixels;
			screenHeight = outDisplayMetrics.heightPixels;
			usableScreenWidth = screenWidth;
			usableScreenHeight = screenHeight;
		}
		
		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		private void initializeScreenDimensions14(Display display, DisplayMetrics outDisplayMetrics) {
			try {
				screenWidth = (Integer)Display.class.getMethod("getRawWidth").invoke(display);
				screenHeight = (Integer)Display.class.getMethod("getRawHeight").invoke(display);
			} catch (Throwable ex) {
				initializeScreenDimensions(display, outDisplayMetrics);
				return;
			}
			display.getMetrics(outDisplayMetrics);
			usableScreenWidth = outDisplayMetrics.widthPixels;
			usableScreenHeight = outDisplayMetrics.heightPixels;
		}
		
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
		private void initializeScreenDimensions17(Display display, DisplayMetrics outDisplayMetrics) {
			display.getMetrics(outDisplayMetrics);
			usableScreenWidth = outDisplayMetrics.widthPixels;
			usableScreenHeight = outDisplayMetrics.heightPixels;
			display.getRealMetrics(outDisplayMetrics);
			screenWidth = outDisplayMetrics.widthPixels;
			screenHeight = outDisplayMetrics.heightPixels;
		}
		
		public void getInfo(Context context) {
			final Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			displayMetrics = new DisplayMetrics();
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
				initializeScreenDimensions17(display, displayMetrics);
			else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
				initializeScreenDimensions14(display, displayMetrics);
			else
				initializeScreenDimensions(display, displayMetrics);
			//improved detection for tablets, based on:
			//http://developer.android.com/guide/practices/screens_support.html#DeclaringTabletLayouts
			//(There is also the solution at http://stackoverflow.com/questions/11330363/how-to-detect-device-is-android-phone-or-android-tablet
			//but the former link says it is deprecated...)
			//*** I decided to treat screens >= 500dp as large screens because there are
			//lots of 7" phones/tablets with resolutions starting at around 533dp ***
			final int _500dp = (int)((500.0f * displayMetrics.density) + 0.5f);
			isLandscape = (screenWidth >= screenHeight);
			
			isLargeScreen = ((screenWidth >= _500dp) && (screenHeight >= _500dp));
			isLowDpiScreen = (displayMetrics.densityDpi < 160);
		}
	}
	
	private static final class Gradient {
		private static final Gradient[] gradients = new Gradient[16];
		private static int pos, count;
		public final boolean focused, vertical;
		public final int size;
		public final LinearGradient gradient;
		
		private Gradient(boolean focused, boolean vertical, int size) {
			this.focused = focused;
			this.vertical = vertical;
			this.size = size;
			this.gradient = (focused ? new LinearGradient(0, 0, (vertical ? size : 0), (vertical ? 0 : size), color_focused_grad_lt, color_focused_grad_dk, Shader.TileMode.CLAMP) :
				new LinearGradient(0, 0, (vertical ? size : 0), (vertical ? 0 : size), color_selected_grad_lt, color_selected_grad_dk, Shader.TileMode.CLAMP));
		}
		
		public static void purgeAll() {
			for (int i = gradients.length - 1; i >= 0; i--)
				gradients[i] = null;
			pos = 0;
			count = 0;
		}
		
		public static LinearGradient getGradient(boolean focused, boolean vertical, int size) {
			//a LRU algorithm could be implemented here...
			for (int i = count - 1; i >= 0; i--) {
				if (gradients[i].size == size && gradients[i].focused == focused && gradients[i].vertical == vertical)
					return gradients[i].gradient;
			}
			if (count < 16) {
				pos = count;
				count++;
			} else {
				pos = (pos + 1) & 15;
			}
			final Gradient g = new Gradient(focused, vertical, size);
			gradients[pos] = g;
			return g.gradient;
		}
	}
	
	public static final Rect rect = new Rect();
	public static final RectF rectF = new RectF();
	public static char decimalSeparator;
	public static boolean hasTouch, isLandscape, isTV, isLargeScreen, isLowDpiScreen, deviceSupportsAnimations, is3D, isDividerVisible, isVerticalMarginLarge, keepScreenOn, doubleClickMode,
		marqueeTitle, blockBackKey, widgetTransparentBg, backKeyAlwaysReturnsToPlayerWhenBrowsing, wrapAroundList, extraSpacing, albumArt, visualizerPortrait,
		scrollBarToTheLeft, expandSeekBar, notFullscreen, controlsToTheLeft, hasBorders, placeTitleAtTheBottom;
	public static int _1dp, _4dp, _22sp, _18sp, _14sp, _22spBox, defaultCheckIconSize, _18spBox, _14spBox, _22spYinBox, _18spYinBox, _14spYinBox, _LargeItemsp, _LargeItemspBox, _LargeItemspYinBox, controlLargeMargin, controlMargin, controlSmallMargin, controlXtraSmallMargin, dialogTextSize, dialogMargin, dialogDropDownVerticalMargin, verticalMargin, menuMargin,
		strokeSize, thickDividerSize, defaultControlContentsSize, defaultControlSize, usableScreenWidth, usableScreenHeight, screenWidth, screenHeight, densityDpi, forcedOrientation, msgs, msgStartup, widgetTextColor, widgetIconColor, lastVersionCode, browserScrollBarType, songListScrollBarType;
	public static int[] lastViewCenterLocation = new int[2];
	public static Bitmap icPrev, icPlay, icPause, icNext, icPrevNotif, icPlayNotif, icPauseNotif, icNextNotif, icExitNotif;
	public static byte[] customColors;
	//I know this is not the "proper" way of doing this... But this is the best way
	//to save memory and prevent a few memory leaks (considering this class is only
	//going to be used in this kind of project)
	/*public static ActivityItemView songActivity;
	public static ActivityBrowserView browserActivity;*/
	public static AccessibilityManager accessibilityManager;
	
	private static int currentLocale, createdWidgetIconColor;
	private static boolean alternateTypefaceActive, fullyInitialized;
	private static Toast internalToast;

	//These guys used to be private, but I decided to make them public, even though they still have
	//their setters, after I found out ProGuard does not inline static setters (or at least I have
	//not been able to figure out a way to do so....)
	public static boolean isUsingAlternateTypeface, isFlat;
	public static int forcedLocale, theme, transition;
	
	public static float density, scaledDensity, xdpi_1_72;
	
	public static final Paint fillPaint;
	public static final TextPaint textPaint;
	private static PorterDuffColorFilter glowFilter;
	//private static final PorterDuffColorFilter edgeFilter;
	
	static {
		fillPaint = new Paint();
		fillPaint.setDither(false);
		fillPaint.setAntiAlias(false);
		fillPaint.setStyle(Paint.Style.FILL);
		textPaint = new TextPaint();
		textPaint.setDither(false);
		textPaint.setAntiAlias(true);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setTypeface(Typeface.DEFAULT);
		textPaint.setTextAlign(Paint.Align.LEFT);
		textPaint.setColor(color_text);
		textPaint.measureText("FPlay");
		//hide the edge!!! ;)
		//if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
		//  edgeFilter = new PorterDuffColorFilter(0, PorterDuff.Mode.CLEAR);
		animationInterpolator = new Interpolator() {
			@Override
			public float getInterpolation(float input) {
				//faster version of AccelerateDecelerateInterpolator using this sine approximation:
				//http://forum.devmaster.net/t/fast-and-accurate-sine-cosine/9648
				//we use the result of sin in the range -PI/2 to PI/2
				//input = (input * 3.14159265f) - 1.57079632f;
				//return 0.5f + (0.5f * ((1.27323954f * input) - (0.40528473f * input * (input < 0.0f ? -input : input))));

				//even faster version! using the Hermite interpolation (GLSL's smoothstep)
				//https://www.opengl.org/sdk/docs/man/html/smoothstep.xhtml
				return (input * input * (3.0f - (2.0f * input)));
			}
		};
	}
	
	public static String formatIntAsFloat(int number, boolean useTwoDecimalPlaces, boolean removeDecimalPlacesIfExact) {
		int dec;
		if (useTwoDecimalPlaces) {
			dec = number % 100;
			number /= 100;
		} else {
			dec = number % 10;
			number /= 10;
		}
		if (removeDecimalPlacesIfExact && dec == 0)
			return Integer.toString(number);
		if (dec < 0)
			dec = -dec;
		return Integer.toString(number) + decimalSeparator + ((useTwoDecimalPlaces && (dec < 10)) ? ("0" + Integer.toString(dec)) : Integer.toString(dec));
	}
	
	public static void formatIntAsFloat(StringBuilder sb, int number, boolean useTwoDecimalPlaces, boolean removeDecimalPlacesIfExact) {
		int dec;
		if (useTwoDecimalPlaces) {
			dec = number % 100;
			number /= 100;
		} else {
			dec = number % 10;
			number /= 10;
		}
		sb.append(number);
		if (!removeDecimalPlacesIfExact || dec != 0) {
			if (dec < 0)
				dec = -dec;
			sb.append(decimalSeparator);
			if (useTwoDecimalPlaces && (dec < 10))
				sb.append('0');
			sb.append(dec);
		}
	}

	public static void setUsingAlternateTypeface(Context context, boolean useAlternateTypeface) {
		UI.isUsingAlternateTypeface = false;
		alternateTypefaceActive = false;
		//Font Metrics in Java OR How, the hell, Should I Position This Font?!
		//http://blog.evendanan.net/2011/12/Font-Metrics-in-Java-OR-How-the-hell-Should-I-Position-This-Font
		textPaint.setTextSize(_22sp);
		final Paint.FontMetrics fm = textPaint.getFontMetrics();
		_22spBox = (int)(fm.descent - fm.ascent + 0.5f);
		_22spYinBox = _22spBox - (int)(fm.descent);
		textPaint.setTextSize(_18sp);
		textPaint.getFontMetrics(fm);
		_18spBox = (int)(fm.descent - fm.ascent + 0.5f);
		_18spYinBox = _18spBox - (int)(fm.descent);
		textPaint.setTextSize(_14sp);
		textPaint.getFontMetrics(fm);
		_14spBox = (int)(fm.descent - fm.ascent + 0.5f);
		_14spYinBox = _14spBox - (int)(fm.descent);
		if (isLargeScreen) {
			_LargeItemsp = _22sp;
			_LargeItemspBox = _22spBox;
			_LargeItemspYinBox = _22spYinBox;
		} else {
			_LargeItemsp = _18sp;
			_LargeItemspBox = _18spBox;
			_LargeItemspYinBox = _18spYinBox;
		}
	}
	
	public static Locale getLocaleFromCode(int localeCode) {
		final Locale l = Locale.getDefault();
		switch (localeCode) {
		case LOCALE_US:
			if (!"en".equals(l.getLanguage()))
				return Locale.US;
			break;
		case LOCALE_PTBR:
			if (!"pt".equals(l.getLanguage()))
				return new Locale("pt", "BR");
			break;
		case LOCALE_RU:
			if (!"ru".equals(l.getLanguage()))
				return new Locale("ru", "RU");
			break;
		case LOCALE_UK:
			if (!"uk".equals(l.getLanguage()))
				return new Locale("uk");
			break;
		case LOCALE_ES:
			if (!"es".equals(l.getLanguage()))
				return new Locale("es");
			break;
		case LOCALE_DE:
			if (!"de".equals(l.getLanguage()))
				return new Locale("de");
			break;
		case LOCALE_FR:
			if (!"fr".equals(l.getLanguage()))
				return new Locale("fr");
			break;
		}
		return l;
	}
	
	public static String getLocaleDescriptionFromCode(Context context, int localeCode) {
		switch (localeCode) {
		case LOCALE_US:
			return "English";
		case LOCALE_PTBR:
			return "Português (Brasil)";
		case LOCALE_RU:
			return "Русский";
		case LOCALE_UK:
			return "Українська";
		case LOCALE_ES:
			return "Español";
		case LOCALE_DE:
			return "Deutsch";
		case LOCALE_FR:
			return "Français";
		}
		return context.getText(R.string.standard_language).toString();
	}
	
	public static int getCurrentLocale(Context context) {
		try {
			final String l = context.getResources().getConfiguration().locale.getLanguage().toLowerCase(Locale.US);
			if ("pt".equals(l))
				return LOCALE_PTBR;
			if ("ru".equals(l))
				return LOCALE_RU;
			if ("uk".equals(l))
				return LOCALE_UK;
			if ("es".equals(l))
				return LOCALE_ES;
			if ("de".equals(l))
				return LOCALE_DE;
			if ("fr".equals(l))
				return LOCALE_FR;
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return LOCALE_US;
	}
	
	public static boolean isCurrentLocaleCyrillic() {
		return ((currentLocale == LOCALE_RU) || (currentLocale == LOCALE_UK));
	}

	private static void updateDecimalSeparator() {
		try {
			final DecimalFormatSymbols d = new DecimalFormatSymbols(getLocaleFromCode(currentLocale));
			decimalSeparator = d.getDecimalSeparator();
		} catch (Throwable ex) {
			decimalSeparator = '.';
		}
	}

	public static void reapplyForcedLocale(Context context, Activity activityContext) {
		setForcedLocale(context, activityContext, forcedLocale, false);
	}

	public static boolean setForcedLocale(Context context, Activity activityContext, int localeCode, boolean reloadEmptyListString) {
		if (localeCode < 0 || localeCode > LOCALE_MAX)
			localeCode = LOCALE_NONE;
		if (forcedLocale == 0 && localeCode == 0) {
			currentLocale = getCurrentLocale(context);
			updateDecimalSeparator();
			return false;
		}
		final boolean wasCyrillic = isCurrentLocaleCyrillic();
		try {
			final Locale l = getLocaleFromCode(localeCode);
			final Resources res = context.getResources();
			Configuration cfg = new Configuration();
			cfg.locale = l;
			res.getConfiguration().updateFrom(cfg);
			res.updateConfiguration(res.getConfiguration(), res.getDisplayMetrics());
			forcedLocale = localeCode;
			currentLocale = ((localeCode == 0) ? getCurrentLocale(context) : localeCode);
			if (activityContext != null) {
				final Resources res2 = activityContext.getResources();
				if (res != res2) {
					res2.getConfiguration().updateFrom(cfg);
					res2.updateConfiguration(res2.getConfiguration(), res2.getDisplayMetrics());
				}
			}
		} catch (Throwable ex) {
			currentLocale = getCurrentLocale(context);
		}
		updateDecimalSeparator();
		if (fullyInitialized && isUsingAlternateTypeface && wasCyrillic != isCurrentLocaleCyrillic()) {
			setUsingAlternateTypeface(context, true);
			return true;
		}
		return false;
	}
	
	public static void initialize(Context context, Activity activityContext) {
		
		accessibilityManager = (AccessibilityManager)context.getSystemService(Context.ACCESSIBILITY_SERVICE);
		if (iconsTypeface == null)
			iconsTypeface = Typeface.createFromAsset(context.getAssets(), "fonts/icons.ttf");
		if (!fullyInitialized) {
			try {
				isTV = ((((UiModeManager)context.getSystemService(Context.UI_MODE_SERVICE)).getCurrentModeType() & Configuration.UI_MODE_TYPE_TELEVISION) != 0);
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
			try {
				hasTouch = context.getPackageManager().hasSystemFeature("android.hardware.touchscreen");
			} catch (Throwable ex) {
				hasTouch = true;
				ex.printStackTrace();
			}
			fullyInitialized = true;
		}
		final DisplayInfo info = new DisplayInfo();
		info.getInfo(context);
		density = info.displayMetrics.density;
		densityDpi = info.displayMetrics.densityDpi;
		scaledDensity = info.displayMetrics.scaledDensity;
		xdpi_1_72 = info.displayMetrics.xdpi * (1.0f / 72.0f);
		screenWidth = info.screenWidth;
		screenHeight = info.screenHeight;
		usableScreenWidth = info.usableScreenWidth;
		usableScreenHeight = info.usableScreenHeight;
		isLargeScreen = (isTV || info.isLargeScreen);
		int orientation = activityContext.getResources().getConfiguration().orientation;
		isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE;
		
		isLowDpiScreen = info.isLowDpiScreen;
		//let's do some guessing here... :/
		deviceSupportsAnimations = ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) || ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) && (density >= 1.5f || isLargeScreen)));

		//apparently, the display metrics returned by Resources.getDisplayMetrics()
		//is not the same as the one returned by Display.getMetrics()/getRealMetrics()
		final float sd = context.getResources().getDisplayMetrics().scaledDensity;
		if (sd > 0)
			scaledDensity = sd;
		else if (scaledDensity <= 0)
			scaledDensity = 1.0f;
		
		_1dp = dpToPxI(1);
		strokeSize = (_1dp + 1) >> 1;
		thickDividerSize = dpToPxI(1.5f);// ((_1dp >= 2) ? _1dp : 2);
		if (thickDividerSize < 2) thickDividerSize = 2;
		if (thickDividerSize <= _1dp) thickDividerSize = _1dp + 1;
		_4dp = dpToPxI(4);
		_22sp = spToPxI(22);
		_18sp = spToPxI(18);
		_14sp = spToPxI(14);
		controlLargeMargin = dpToPxI(16);
		controlMargin = controlLargeMargin >> 1;
		controlSmallMargin = controlLargeMargin >> 2;
		controlXtraSmallMargin = controlLargeMargin >> 3;
		menuMargin = controlMargin;
		if (isLargeScreen || !isLowDpiScreen) {
			dialogTextSize = _18sp;
			dialogMargin = controlMargin;
			menuMargin += controlSmallMargin;
		} else {
			dialogTextSize = _14sp;
			dialogMargin = controlMargin >> 1;
		}
		dialogDropDownVerticalMargin = (dialogMargin * 3) >> 1;
		defaultControlContentsSize = dpToPxI(32);
		defaultControlSize = defaultControlContentsSize + (controlMargin << 1);
		defaultCheckIconSize = dpToPxI(24); //both descent and ascent of iconsTypeface are 0!
		if (!setForcedLocale(context, activityContext, forcedLocale, false))
			setUsingAlternateTypeface(context, isUsingAlternateTypeface);
		setVerticalMarginLarge(isVerticalMarginLarge);
	}
	
	public static void prepareWidgetPlaybackIcons(Context context) {
		if (widgetIconColor != createdWidgetIconColor) {
			if (icPrev != null) {
				icPrev.recycle();
				icPrev = null;
			}
			if (icPlay != null) {
				icPlay.recycle();
				icPlay = null;
			}
			if (icPause != null) {
				icPause.recycle();
				icPause = null;
			}
			if (icNext != null) {
				icNext.recycle();
				icNext = null;
			}
		}
		if (icPrev != null)
			return;
		if (iconsTypeface == null)
			initialize(context, null);
		final Canvas c = new Canvas();
		textPaint.setTypeface(iconsTypeface);
		textPaint.setColor(widgetIconColor);
		textPaint.setTextSize(defaultControlContentsSize);
		icPrev = Bitmap.createBitmap(defaultControlContentsSize, defaultControlContentsSize, Bitmap.Config.ARGB_8888);
		c.setBitmap(icPrev);
		c.drawText(ICON_PREV, 0, defaultControlContentsSize, textPaint);
		icPlay = Bitmap.createBitmap(defaultControlContentsSize, defaultControlContentsSize, Bitmap.Config.ARGB_8888);
		c.setBitmap(icPlay);
		c.drawText(ICON_PLAY, 0, defaultControlContentsSize, textPaint);
		icPause = Bitmap.createBitmap(defaultControlContentsSize, defaultControlContentsSize, Bitmap.Config.ARGB_8888);
		c.setBitmap(icPause);
		c.drawText(ICON_PAUSE, 0, defaultControlContentsSize, textPaint);
		icNext = Bitmap.createBitmap(defaultControlContentsSize, defaultControlContentsSize, Bitmap.Config.ARGB_8888);
		c.setBitmap(icNext);
		c.drawText(ICON_NEXT, 0, defaultControlContentsSize, textPaint);
		createdWidgetIconColor = widgetIconColor;
		//reset to the original state
	//	textPaint.setTypeface(defaultTypeface);
		textPaint.setColor(color_text);
		textPaint.measureText("FPlay");
	}
	
	public static float pxToDp(float px) {
		return px / density;
	}
	
	public static float pxToSp(float px) {
		return px / scaledDensity;
	}
	
	public static float pxToPt(float px) {
		return px / xdpi_1_72;
	}
	
	public static float dpToPx(float dp) {
		return dp * density;
	}
	
	public static float spToPx(float sp) {
		return sp * scaledDensity;
	}
	
	public static float ptToPx(float pt) {
		return pt * xdpi_1_72;
	}
	
	public static int dpToPxI(float dp) {
		return (int)((dp * density) + 0.5f);
	}
	
	public static int spToPxI(float sp) {
		return (int)((sp * scaledDensity) + 0.5f);
	}
	
	public static int ptToPxI(float pt) {
		return (int)((pt * xdpi_1_72) + 0.5f);
	}
	
	public static CharSequence getThemeColorDescription(Context context, int idx) {
		switch (idx) {
		case 0:
			return context.getText(R.string.window_background);
		case 1:
			return context.getText(R.string.control_mode_background);
		case 2:
			return context.getText(R.string.visualizer_background);
		case 3:
		case 4:
			return context.getText(R.string.background);
		case 5:
			return context.getText(R.string.icon);
		case 6:
		case 18:
		case 22:
			return context.getText(R.string.border);
		case 7:
			return context.getText(R.string.divider);
		case 8:
			return context.getText(R.string.highlight_background);
		case 9:
			return context.getText(R.string.highlight_text);
		case 10:
			return context.getText(R.string.window_text);
		case 11:
			return context.getText(R.string.window_text_disabled);
		case 12:
		case 14:
		case 15:
			return context.getText(R.string.text);
		case 13:
			return context.getText(R.string.text_secondary);
		case 16:
		case 20:
			return context.getText(R.string.top_background);
		case 17:
		case 21:
			return context.getText(R.string.bottom_background);
		case 19:
		case 23:
			return context.getText(R.string.pressed_background);
		}
		return context.getText(R.string.no_info);
	}
	
	public static void serializeThemeColor(byte[] colors, int idx, int color) {
		colors[idx] = (byte)color;
		colors[idx + 1] = (byte)(color >>> 8);
		colors[idx + 2] = (byte)(color >>> 16);
	}
	
	public static int deserializeThemeColor(byte[] colors, int idx) {
		return 0xff000000 | (int)(colors[idx] & 0xff) | (int)((colors[idx + 1] & 0xff) << 8) | (int)((colors[idx + 2] & 0xff) << 16);
	}
	
	public static byte[] serializeThemeToArray() {
		final byte[] colors = new byte[72];
		serializeThemeColor(colors, 3 * IDX_COLOR_WINDOW, color_window);
		serializeThemeColor(colors, 3 * IDX_COLOR_CONTROL_MODE, color_control_mode);
		serializeThemeColor(colors, 3 * IDX_COLOR_VISUALIZER, color_visualizer);
		serializeThemeColor(colors, 3 * IDX_COLOR_LIST, color_list);
		serializeThemeColor(colors, 3 * IDX_COLOR_MENU, color_menu);
		serializeThemeColor(colors, 3 * IDX_COLOR_MENU_ICON, color_menu_icon);
		serializeThemeColor(colors, 3 * IDX_COLOR_MENU_BORDER, color_menu_border);
		serializeThemeColor(colors, 3 * IDX_COLOR_DIVIDER, color_divider);
		serializeThemeColor(colors, 3 * IDX_COLOR_HIGHLIGHT, color_highlight);
		serializeThemeColor(colors, 3 * IDX_COLOR_TEXT_HIGHLIGHT, color_text_highlight);
		serializeThemeColor(colors, 3 * IDX_COLOR_TEXT, color_text);
		serializeThemeColor(colors, 3 * IDX_COLOR_TEXT_DISABLED, color_text_disabled);
		serializeThemeColor(colors, 3 * IDX_COLOR_TEXT_LISTITEM, color_text_listitem);
		serializeThemeColor(colors, 3 * IDX_COLOR_TEXT_LISTITEM_SECONDARY, color_text_listitem_secondary);
		serializeThemeColor(colors, 3 * IDX_COLOR_TEXT_SELECTED, color_text_selected);
		serializeThemeColor(colors, 3 * IDX_COLOR_TEXT_MENU, color_text_menu);
		serializeThemeColor(colors, 3 * IDX_COLOR_SELECTED_GRAD_LT, color_selected_grad_lt);
		serializeThemeColor(colors, 3 * IDX_COLOR_SELECTED_GRAD_DK, color_selected_grad_dk);
		serializeThemeColor(colors, 3 * IDX_COLOR_SELECTED_BORDER, color_selected_border);
		serializeThemeColor(colors, 3 * IDX_COLOR_SELECTED_PRESSED, color_selected_pressed);
		serializeThemeColor(colors, 3 * IDX_COLOR_FOCUSED_GRAD_LT, color_focused_grad_lt);
		serializeThemeColor(colors, 3 * IDX_COLOR_FOCUSED_GRAD_DK, color_focused_grad_dk);
		serializeThemeColor(colors, 3 * IDX_COLOR_FOCUSED_BORDER, color_focused_border);
		serializeThemeColor(colors, 3 * IDX_COLOR_FOCUSED_PRESSED, color_focused_pressed);
		return colors;
	}
	
	public static void updateColorListBg() {
		if (is3D) {
			if (ColorUtils.relativeLuminance(color_list_original) >= 0.5) {
				color_list = color_list_original;
				color_list_bg = ColorUtils.blend(color_list_original, 0xff000000, 0.9286f);
			} else {
				color_list = ColorUtils.blend(color_list_original, 0xffffffff, 0.9286f);
				color_list_bg = color_list_original;
			}
		} else {
			color_list = color_list_original;
			color_list_bg = color_list_original;
		}
	}

	private static void finishLoadingTheme(boolean custom) {
		//create a "safe 565" version for the net.coocent.tool.net.coocent.tool.visualizer background
		//http://stackoverflow.com/questions/2442576/how-does-one-convert-16-bit-rgb565-to-24-bit-rgb888
		//first convert to 565
		int r = ((color_visualizer >> 16) & 0xff) >> 3;
		int g = ((color_visualizer >> 8) & 0xff) >> 2;
		int b = (color_visualizer & 0xff) >> 3;
		//now upscale back to 888
		r = (r << 3) | (r >> 2);
		g = (g << 2) | (g >> 3);
		b = (b << 3) | (b >> 2);
		color_visualizer565 = 0xff000000 | (r << 16) | (g << 8) | b;
		
		color_selected = ColorUtils.blend(color_selected_grad_lt, color_selected_grad_dk, 0.5f);
		color_focused = ColorUtils.blend(color_focused_grad_lt, color_focused_grad_dk, 0.5f);
		color_selected_multi = ColorUtils.blend(color_selected, color_list, 0.7f);
		color_selected_pressed_border = color_selected_border;
		color_focused_pressed_border = color_focused_border;
		
		colorState_text_reactive = new BgColorStateList(color_text, color_text_selected);
		
		color_divider_pressed = ColorUtils.blend(color_divider, color_text_listitem, 0.7f);
		if (!custom) {
			color_text_title = color_highlight;
			color_menu_border = color_selected_border;
		}
		color_list_original = color_list;
		updateColorListBg();
		color_glow = ((theme == THEME_FPLAY) ? color_text_listitem_secondary : ((ColorUtils.contrastRatio(color_window, color_list) >= 3.5) ? color_window : ((color_text_listitem_secondary != color_highlight) ? color_text_listitem_secondary : color_text_listitem)));
		//choose the color with a nice contrast against the list background to be the glow color
		//the color is treated as SRC, and the bitmap is treated as DST
		glowFilter = ((Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) ? new PorterDuffColorFilter(color_glow, PorterDuff.Mode.SRC_IN) : null);
	}
	
	public static void loadFPlayTheme() {
		color_btn = 0xffffffff;
		color_window = 0xff444abf;
		color_control_mode = 0xff000000;
		color_visualizer = 0xff000000;
		color_list = 0xfffcfcfc;
		color_menu = 0xfffcfcfc;
		color_menu_icon = 0xff555555;
		color_divider = 0xffc4c4c4;
		color_highlight = 0xffffcc66;
		color_text_highlight = 0xff000000;
		color_text = 0xff000000;
		color_text_disabled = 0xff8c8c8c;
		color_text_listitem = 0xff000000;
		color_text_listitem_secondary = 0xff353be0;
		color_text_selected = 0xff000000;
		color_text_menu = 0xff000000;
		color_selected_grad_lt = 0xffffdd99;
		color_selected_grad_dk = 0xffffbb33;
		color_selected_border = 0xffce9731;
		color_selected_pressed = 0xffffe5b5;
		color_focused_grad_lt = 0xffd6d8ff;
		color_focused_grad_dk = 0xffaaafff;
		color_focused_border = 0xff696dbf;
		color_focused_pressed = 0xffe5e6ff;
		finishLoadingTheme(false);
		color_menu_border = 0xffc4c4c4;
		color_text_title = color_text;
		_1dp = dpToPxI(1);
		strokeSize = (_1dp + 1) >> 1;
	//	colorState_text_title_static = colorState_text_static;
	}

	public static String getThemeString(Context context, int theme) {
		return "FPlay";
	}

	/*public static void setTheme(ActivityHost activityHost, int theme) {
		UI.theme = theme;
		Gradient.purgeAll();
		internalToast = null;
		switch (theme) {
		case THEME_CUSTOM:
			loadCustomTheme();
			break;
		case THEME_BLUE_ORANGE:
			loadBlueOrangeTheme();
			break;
		case THEME_BLUE:
			loadBlueTheme();
			break;
		case THEME_ORANGE:
			loadOrangeTheme();
			break;
		case THEME_LIGHT:
			loadLightTheme();
			break;
		case THEME_DARK_LIGHT:
			loadDarkLightTheme();
			break;
		case THEME_CREAMY:
			loadCreamyTheme();
			break;
		default:
			UI.theme = THEME_FPLAY;
			loadFPlayTheme();
			break;
		}
		if (activityHost != null)
			setAndroidThemeAccordingly(activityHost);
	}*/

	public static boolean isAndroidThemeLight() {
		return ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) && (ColorUtils.relativeLuminance(color_menu) >= 0.5));
	}

	/*public static void setAndroidThemeAccordingly(ActivityHost activityHost) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			setAndroidThemeAccordingly21(activityHost);
		else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
			setAndroidThemeAccordingly13(activityHost);
	}*/

	/*@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private static void setAndroidThemeAccordingly13(ActivityHost activityHost) {
		if (isAndroidThemeLight())
			activityHost.setTheme(android.R.style.Theme_Holo_Light_NoActionBar_Fullscreen);
		else
			activityHost.setTheme(android.R.style.Theme_Holo_NoActionBar_Fullscreen);
	}*/

	/*@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	private static void setAndroidThemeAccordingly21(ActivityHost activityHost) {
		if (isAndroidThemeLight())
			activityHost.setTheme(android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
		else
			activityHost.setTheme(android.R.style.Theme_Material_NoActionBar_Fullscreen);
		activityHost.updateSystemColors(true);
	}*/

	public static int getAndroidThemeColor(Context context, int style, int attribute) {
		final TypedArray array = context.getTheme().obtainStyledAttributes(style, new int[]{attribute});
		final int color = array.getColor(array.getIndex(0), 0);
		array.recycle();
		return color;
	}

	public static void setFlat(boolean flat) {
		isFlat = flat;
		Gradient.purgeAll();
	}

	public static void setTransition(int transition) {
		switch (transition) {
			case TRANSITION_DISSOLVE:
			case TRANSITION_ZOOM:
			case TRANSITION_FADE:
				UI.transition = transition;
				break;
			default:
				UI.transition = TRANSITION_NONE;
				break;
		}
	}

	/*public static String getTransitionString(Context context, int transition) {
		switch (transition) {
			case TRANSITION_DISSOLVE:
				return context.getText(R.string.dissolve).toString();
			case TRANSITION_ZOOM:
				return context.getText(R.string.zoom).toString();
			case TRANSITION_FADE:
				return context.getText(R.string.fade).toString();
			default:
				return context.getText(R.string.none).toString();
		}
	}*/

	/*public static void showNextStartupMsg(Activity activity) {
		if (msgStartup >= 26) {
			msgStartup = 26;
			return;
		}
		final int title = R.string.new_setting;
		msgStartup = 26;
		//final String content = activity.getText(R.string.startup_message).toString() + "!\n\n" + activity.getText(R.string.there_are_new_features).toString() + "\n- " + activity.getText(R.string.expand_seek_bar).toString() + "\n\n" + activity.getText(R.string.check_it_out).toString();
		//final String content = activity.getText(R.string.there_are_new_features).toString() + "\n- " + activity.getText(R.string.fullscreen).toString() + "\n- " + activity.getText(R.string.transition).toString() + "\n- " + activity.getText(R.string.color_theme).toString() + ": " + activity.getText(R.string.creamy).toString() + "\n\n" + activity.getText(R.string.check_it_out).toString();
		//final String content = activity.getText(R.string.startup_message).toString();
		//final String content = activity.getText(R.string.there_are_new_features).toString() + "\n- " + activity.getText(R.string.color_theme).toString() + ": FPlay\n\n" + activity.getText(R.string.net.coocent.tool.net.coocent.tool.visualizer).toString() + "! :D\n- Liquid Spectrum\n- Spinning Rainbow\n\n" + activity.getText(R.string.check_it_out).toString();
		//final String content = "- " + activity.getText(R.string.net.coocent.tool.net.coocent.tool.visualizer).toString() + ":\n" +  activity.getText(R.string.album_art).toString() + "\nInto the Particles! :D\n\n- " + activity.getText(R.string.color_theme).toString() + ":\nFPlay\n\n" + activity.getText(R.string.check_it_out).toString();
		final String content = activity.getText(R.string.there_are_new_features).toString() + "\n- " + activity.getText(R.string.accessibility) + "\n- 3D\n\n" + ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) ? activity.getText(R.string.net.coocent.tool.net.coocent.tool.visualizer) + ":\n- Into the Particles (VR)\n\n" : "") + activity.getText(R.string.startup_message).toString() + "\n- " + activity.getText(R.string.loudspeaker).toString() + "\n- " + activity.getText(R.string.earphones).toString() + "\n- " + activity.getText(R.string.bluetooth).toString() + "\n\n" + activity.getText(R.string.check_it_out).toString();
		prepareDialogAndShow((new AlertDialog.Builder(activity))
			.setTitle(activity.getText(title))
			.setView(createDialogView(activity, content))
			.setPositiveButton(R.string.got_it, null)
			.create());
	}*/

	public static void setVerticalMarginLarge(boolean isVerticalMarginLarge) {
		UI.isVerticalMarginLarge = isVerticalMarginLarge;
		UI.verticalMargin = (isVerticalMarginLarge ? controlLargeMargin : controlMargin);
	}

	/*public static boolean showMsg(Activity activity, int msg) {
		if ((msgs & msg) != 0)
			return false;
		int title, content;
		switch (msg) {
		case MSG_ADD:
			title = R.string.add;
			content = R.string.msg_add;
			break;
		case MSG_PLAY:
			title = R.string.play;
			content = R.string.msg_play;
			break;
		default:
			return false;
		}
		prepareDialogAndShow((new AlertDialog.Builder(activity))
			.setTitle(activity.getText(title))
			.setView(createDialogView(activity, activity.getText(content)))
			.setPositiveButton(R.string.got_it, null)
			.create());
		msgs |= msg;
		return true;
	}*/
	
	public static String ellipsizeText(String text, int size, int width, boolean truncateAtMiddle) {
		if (text == null)
			return "";
		if (width <= 1)
			return text;
		textPaint.setTextSize(size);
		return TextUtils.ellipsize(text, textPaint, width, truncateAtMiddle ? TruncateAt.MIDDLE : TruncateAt.END).toString();
	}
	
	public static int measureText(String text, int size) {
		if (text == null)
			return 0;
		textPaint.setTextSize(size);
		return (int)(textPaint.measureText(text) + 0.5f);
	}
	
	public static void drawText(Canvas canvas, String text, int color, int size, int x, int y) {
		textPaint.setColor(color);
		textPaint.setTextSize(size);
		canvas.drawText(text, x, y, textPaint);
	}

	public static void drawText(Canvas canvas, String text, int start, int end, int color, int size, int x, int y) {
		textPaint.setColor(color);
		textPaint.setTextSize(size);
		canvas.drawText(text, start, end, x, y, textPaint);
	}

	public static void fillRect(Canvas canvas, int fillColor) {
		fillPaint.setColor(fillColor);
		canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
	}

	public static void fillRect(Canvas canvas, Shader shader) {
		fillPaint.setShader(shader);
		canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
		fillPaint.setShader(null);
	}

	public static void strokeRect(Canvas canvas, int strokeColor, int thickness) {
		fillPaint.setColor(strokeColor);
		final int l = rect.left, t = rect.top, r = rect.right, b = rect.bottom;
		canvas.drawRect(l, t, r, t + thickness, fillPaint);
		canvas.drawRect(l, b - thickness, r, b, fillPaint);
		canvas.drawRect(l, t + thickness, l + thickness, b - thickness, fillPaint);
		canvas.drawRect(r - thickness, t + thickness, r, b - thickness, fillPaint);
	}
	
	public static int getBorderColor(int state) {
		if ((state & STATE_PRESSED) != 0)
			return (((state & STATE_FOCUSED) != 0) ? color_focused_pressed_border : color_selected_pressed_border);
		if ((state & STATE_FOCUSED) != 0)
			return color_focused_border;
		if ((state & STATE_SELECTED) != 0)
			return color_selected_border;
		return 0;
	}
	
	public static void drawBgBorderless(Canvas canvas, int state) {
		if ((state & ~STATE_CURRENT) != 0) {
			if ((state & STATE_PRESSED) != 0) {
				fillPaint.setColor(((state & STATE_FOCUSED) != 0) ? color_focused_pressed : color_selected_pressed);
			} else if ((state & (STATE_SELECTED | STATE_FOCUSED)) != 0) {
				if (isFlat) {
					fillPaint.setColor(((state & STATE_FOCUSED) != 0) ? color_focused : color_selected);
				} else {
					fillPaint.setShader(Gradient.getGradient((state & STATE_FOCUSED) != 0, false, rect.bottom));
					canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
					fillPaint.setShader(null);
					return;
				}
			} else { //if ((state & STATE_MULTISELECTED) != 0) {
				fillPaint.setColor(color_selected_multi);
			}
			canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
		}
	}

	public static void drawBgListItem2D(Canvas canvas, int state, boolean dividerAllowed, int dividerMarginLeft, int dividerMarginRight) {
		dividerAllowed &= isDividerVisible;
		if (dividerAllowed)
			rect.bottom -= strokeSize;
		if ((state & ~STATE_CURRENT) != 0) {
			if ((state & STATE_PRESSED) != 0) {
				fillPaint.setColor(((state & STATE_FOCUSED) != 0) ? color_focused_pressed : color_selected_pressed);
				canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
			} else if ((state & (STATE_SELECTED | STATE_FOCUSED)) != 0) {
				if (isFlat) {
					fillPaint.setColor(((state & STATE_FOCUSED) != 0) ? color_focused : color_selected);
					canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
				} else {
					fillPaint.setShader(Gradient.getGradient((state & STATE_FOCUSED) != 0, false, rect.bottom));
					canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
					fillPaint.setShader(null);
				}
			} else { //if ((state & STATE_MULTISELECTED) != 0) {
				fillPaint.setColor(color_selected_multi);
				canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
			}
		}
		if (dividerAllowed) {
			fillPaint.setColor(color_divider);
			canvas.drawRect(rect.left + dividerMarginLeft, rect.bottom, rect.right - dividerMarginRight, rect.bottom += strokeSize, fillPaint);
		}
	}

	public static void drawBgListItem(Canvas canvas, int state, boolean dividerAllowed, int leftMargin, int rightMargin) {
		if (!is3D) {
			drawBgListItem2D(canvas, state, dividerAllowed, leftMargin, rightMargin);
			return;
		}
		fillPaint.setColor(color_divider);
		//right shadow
		canvas.drawRect(rect.right - rightMargin, controlSmallMargin + strokeSize, rect.right - rightMargin + strokeSize, rect.bottom, fillPaint);
		//bottom shadow
		canvas.drawRect(leftMargin + strokeSize, rect.bottom - strokeSize, rect.right - rightMargin, rect.bottom, fillPaint);
		if ((state & ~STATE_CURRENT) != 0) {
			if ((state & STATE_PRESSED) != 0) {
				fillPaint.setColor(((state & STATE_FOCUSED) != 0) ? color_focused_pressed : color_selected_pressed);
			} else if ((state & (STATE_SELECTED | STATE_FOCUSED)) != 0) {
				if (isFlat) {
					fillPaint.setColor(((state & STATE_FOCUSED) != 0) ? color_focused : color_selected);
				} else {
					fillPaint.setShader(Gradient.getGradient((state & STATE_FOCUSED) != 0, false, rect.bottom - controlSmallMargin - strokeSize));
					canvas.drawRect(leftMargin, controlSmallMargin, rect.right - rightMargin, rect.bottom - strokeSize, fillPaint);
					fillPaint.setShader(null);
					return;
				}
			} else { //if ((state & STATE_MULTISELECTED) != 0) {
				fillPaint.setColor(color_selected_multi);
			}
		} else {
			fillPaint.setColor(color_list);
		}
		canvas.drawRect(leftMargin, controlSmallMargin, rect.right - rightMargin, rect.bottom - strokeSize, fillPaint);
	}

	public static void drawBg(Canvas canvas, int state) {
		if ((state & ~STATE_CURRENT) != 0) {
			if ((state & STATE_PRESSED) != 0) {
				fillPaint.setColor(((state & STATE_FOCUSED) != 0) ? color_focused_pressed : color_selected_pressed);
				canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
				if (hasBorders) strokeRect(canvas, ((state & STATE_FOCUSED) != 0) ? color_focused_pressed_border : color_selected_pressed_border, strokeSize);
			} else if ((state & (STATE_SELECTED | STATE_FOCUSED)) != 0) {
				if (isFlat) {
					fillPaint.setColor(((state & STATE_FOCUSED) != 0) ? color_focused : color_selected);
					canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
				} else {
					fillPaint.setShader(Gradient.getGradient((state & STATE_FOCUSED) != 0, false, rect.bottom));
					canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
					fillPaint.setShader(null);
				}
				if (hasBorders) strokeRect(canvas, ((state & STATE_FOCUSED) != 0) ? color_focused_border : color_selected_border, strokeSize);
			} else { //if ((state & STATE_MULTISELECTED) != 0) {
				fillPaint.setColor(color_selected_multi);
				canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, fillPaint);
			}
		}
	}
	
	public static int handleStateChanges(int state, boolean pressed, boolean focused, View view) {
		boolean r = false;
		final boolean op = ((state & STATE_PRESSED) != 0), of = ((state & STATE_FOCUSED) != 0);
		if (op != pressed) {
			if (pressed)
				state |= STATE_PRESSED;
			else
				state &= ~STATE_PRESSED;
			r = true;
		}
		if (of != focused) {
			if (focused)
				state |= STATE_FOCUSED;
			else
				state &= ~STATE_FOCUSED;
			r = true;
		}
		if (r)
			view.invalidate();
		return state;
	}
	
	public static void smallText(TextView view) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, _14sp);
		view.setTextColor(color_btn);
	//	view.setTypeface(defaultTypeface);
	}
	
	public static void smallTextAndColor(TextView view) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, _14sp);
		view.setTextColor(color_btn);
	//	view.setTypeface(defaultTypeface);
	}
	
	public static void mediumText(TextView view) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, _18sp);
		view.setTextColor(color_btn);
	//	view.setTypeface(defaultTypeface);
	}
	
	public static void mediumTextAndColor(TextView view) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, _18sp);
		view.setTextColor(color_btn);
	//	view.setTypeface(defaultTypeface);
	}
	
	public static void largeText(TextView view) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, _22sp);
	//	view.setTypeface(defaultTypeface);
	}
	
	public static void largeTextAndColor(TextView view) {
		view.setTextSize(TypedValue.COMPLEX_UNIT_PX, _22sp);
		view.setTextColor(color_btn);
	//	view.setTypeface(defaultTypeface);
	}
	
	public static int getViewPaddingForLargeScreen() {
		return ((usableScreenWidth < usableScreenHeight) ? usableScreenWidth : usableScreenHeight) / (isLandscape ? 5 : 10);
	}
	
	public static void prepareViewPaddingForLargeScreen(View view, int topPadding, int bottomPadding) {
		final int p = getViewPaddingForLargeScreen();
		view.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
		view.setPadding(p, topPadding, p, bottomPadding);
	}

	public static void toast(Context context, Throwable ex) {
		String s = ex.getMessage();
		if (s != null && s.length() > 0)
			s = "error " + s;
		else
			s = "error " + ex.getClass().getName();
		toast(context, s);
	}
	
	public static void toast(Context context, int resId) {
		toast(context, context.getText(resId));
	}
	
	@SuppressWarnings("deprecation")
	public static void prepareNotificationViewColors(TextView view) {
	//	view.setTextColor(colorState_text_highlight_static);
		view.setBackgroundDrawable(hasBorders ? new BorderDrawable(ColorUtils.blend(color_highlight, 0, 0.5f), color_highlight, strokeSize, strokeSize, strokeSize, strokeSize) : new ColorDrawable(color_highlight));
	}
	
	public static void toast(Context context, CharSequence text) {
		if (internalToast == null) {
			final Toast t = new Toast(context);
			final TextView v = new TextView(context);
			mediumText(v);
			prepareNotificationViewColors(v);
			v.setGravity(Gravity.CENTER);
			v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			v.setPadding(controlMargin, controlMargin, controlMargin, controlMargin);
			t.setView(v);
			t.setDuration(Toast.LENGTH_LONG);
			internalToast = t;
		}
		((TextView)internalToast.getView()).setText(text);
		internalToast.show();
	}

	@SuppressWarnings("deprecation")
	public static void customToast(Context context, CharSequence text, boolean longDuration, int textSize, int textColor, Drawable background) {
		final Toast t = new Toast(context);
		final TextView v = new TextView(context);
		v.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
	//	v.setTypeface(defaultTypeface);
		v.setTextColor(textColor);
		v.setBackgroundDrawable(background);
		v.setGravity(Gravity.CENTER);
		v.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		v.setPadding(controlMargin, controlMargin, controlMargin, controlMargin);
		v.setText(text);
		t.setView(v);
		t.setDuration(longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
		t.show();
	}

	public static void prepare(Menu menu) {
		final CustomContextMenu mnu = (CustomContextMenu)menu;
		try {
			mnu.setItemClassConstructor(BgButton.class.getConstructor(Context.class));
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		mnu.setBackground(new BorderDrawable(0xffc4c4c4, 0xfffcfcfc, strokeSize, strokeSize, strokeSize, strokeSize));
		mnu.setPadding(0);
		mnu.setItemTextSizeInPixels(_18sp);
		mnu.setItemTextColor(colorState_text_reactive);
		mnu.setItemPadding(menuMargin);
		mnu.setItemGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
	}
	
	public static void separator(Menu menu, int groupId, int order) {
		((CustomContextMenu)menu).addSeparator(groupId, order, color_menu_border, strokeSize, defaultCheckIconSize + menuMargin + menuMargin, controlXtraSmallMargin, menuMargin, controlXtraSmallMargin);
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void setNextFocusForwardId(View view, int nextFocusForwardId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
			view.setNextFocusForwardId(nextFocusForwardId);
	}
	
	public static View createDialogView(Context context, CharSequence messageOnly) {
		if (messageOnly == null) {
			final LinearLayout l = new LinearLayout(context);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
				removeSplitTouch(l);
			l.setOrientation(LinearLayout.VERTICAL);
			l.setPadding(dialogMargin, dialogMargin, dialogMargin, dialogMargin);
			l.setBaselineAligned(false);
			return l;
		}
		final TextView txt = new TextView(context);
		txt.setText(messageOnly);
		txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, dialogTextSize);
		txt.setPadding(dialogMargin << 1, dialogMargin << 1, dialogMargin << 1, dialogMargin << 1);
		return txt;
	}
	
	public static void storeViewCenterLocationForFade(View view) {
		if (view == null) {
			lastViewCenterLocation[0] = screenWidth >> 1;
			lastViewCenterLocation[1] = screenHeight >> 1;
		} else {
			view.getLocationOnScreen(lastViewCenterLocation);
			lastViewCenterLocation[0] += (view.getWidth() >> 1);
			lastViewCenterLocation[1] += (view.getHeight() >> 1);
		}
	}

	public static void prepareDialogAnimations(Dialog dialog) {
		final Window window = dialog.getWindow();
		if (window != null)
			window.setWindowAnimations(animationEnabled ? R.style.FadeAnimation : R.style.NoAnimation);
	}

	/*public static AlertDialog prepareDialogAndShow(AlertDialog dialog) {
		if (alternateTypefaceActive || Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//https://code.google.com/p/android/issues/detail?id=6360
			dialog.setOnShowListener(Player.theUI);
		}
		prepareDialogAnimations(dialog);
		dialog.show();
		return dialog;
	}*/

	private static void prepareDialogAndShowScanChildren(ViewGroup parent) {
		for (int i = parent.getChildCount(); i >= 0; i--) {
			final View v = parent.getChildAt(i);
			if (v instanceof ViewGroup)
				prepareDialogAndShowScanChildren((ViewGroup)v);
		//	else if (v instanceof TextView)
		//		((TextView)v).setTypeface(defaultTypeface);
		}
	}

	@Override
	public void onShow(DialogInterface dlg) {
		final AlertDialog dialog = ((dlg instanceof AlertDialog) ? (AlertDialog)dlg : null);
		if (dialog == null)
			return;
		Button btn;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ViewParent parent = null;
			if ((btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)) != null)
				parent = btn.getParent();
			else if ((btn = dialog.getButton(AlertDialog.BUTTON_NEUTRAL)) != null)
				parent = btn.getParent();
			else if ((btn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)) != null)
				parent = btn.getParent();
			if (parent != null && (parent instanceof ViewGroup))
				removeSplitTouch((ViewGroup)parent);
		}
		if (alternateTypefaceActive) {
			final View v = dialog.findViewById(android.R.id.content);
			if (v != null && (v instanceof ViewGroup)) {
				prepareDialogAndShowScanChildren((ViewGroup)v);
			} else {
				//at least try to change the buttons...
				/*if ((btn = dialog.getButton(AlertDialog.BUTTON_POSITIVE)) != null)
					btn.setTypeface(defaultTypeface);
				if ((btn = dialog.getButton(AlertDialog.BUTTON_NEUTRAL)) != null)
					btn.setTypeface(defaultTypeface);
				if ((btn = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)) != null)
					btn.setTypeface(defaultTypeface);*/
			}
		}
	}

	public static void removeInternalPaddingForEdgeEffect(AbsListView view) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
			return;
		try {
			final Method setOverScrollEffectPadding = AbsListView.class.getDeclaredMethod("setOverScrollEffectPadding", int.class, int.class);
			if (setOverScrollEffectPadding != null)
				setOverScrollEffectPadding.invoke(view, -view.getPaddingLeft(), -view.getPaddingRight());
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	public static void offsetTopEdgeEffect(View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			try {
				if (glowFilter == null) {
					final Field mEdgeGlow = ((view instanceof ScrollView) ? ScrollView.class : AbsListView.class).getDeclaredField("mEdgeGlowTop");
					if (mEdgeGlow != null) {
						mEdgeGlow.setAccessible(true);
						/*final BgEdgeEffect edgeEffect = (BgEdgeEffect)mEdgeGlow.get(view);
						if (edgeEffect != null)
							edgeEffect.mOffsetY = thickDividerSize;*/
					}
				}
			} catch (Throwable ex) {
				ex.printStackTrace();
			}
		}
	}

	@SuppressWarnings("deprecation")
	public static void disableEdgeEffect(Context context) {
		if (glowFilter == null)
			return;
		try {
			//http://evendanan.net/android/branding/2013/12/09/branding-edge-effect/
			Drawable drawable = context.getResources().getDrawable(context.getResources().getIdentifier("overscroll_glow", "drawable", "android"));
			if (drawable != null)
				drawable.setColorFilter(null);
			drawable = context.getResources().getDrawable(context.getResources().getIdentifier("overscroll_edge", "drawable", "android"));
			if (drawable != null)
				drawable.setColorFilter(null);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static void reenableEdgeEffect(Context context) {
		if (glowFilter == null)
			return;
		try {
			//http://evendanan.net/android/branding/2013/12/09/branding-edge-effect/
			Drawable drawable = context.getResources().getDrawable(context.getResources().getIdentifier("overscroll_glow", "drawable", "android"));
			if (drawable != null)
				drawable.setColorFilter(glowFilter);
			drawable = context.getResources().getDrawable(context.getResources().getIdentifier("overscroll_edge", "drawable", "android"));
			if (drawable != null)
				drawable.setColorFilter(glowFilter);//edgeFilter);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static void prepareControlContainer(View view, boolean topBorder, boolean bottomBorder) {
		final int t = (topBorder ? thickDividerSize : 0);
		final int b = (bottomBorder ? thickDividerSize : 0);
		view.setBackgroundDrawable(new BorderDrawable(color_highlight, color_window, 0, t, 0, b));
		if (extraSpacing)
			view.setPadding(controlMargin, controlMargin + t, controlMargin, controlMargin + b);
		else
			view.setPadding(0, t, 0, b);
	}

	@SuppressWarnings("deprecation")
	public static void prepareControlContainerWithoutRightPadding(View view, boolean topBorder, boolean bottomBorder) {
		final int t = (topBorder ? thickDividerSize : 0);
		final int b = (bottomBorder ? thickDividerSize : 0);
		view.setBackgroundDrawable(new BorderDrawable(color_highlight, color_window, 0, t, 0, b));
		if (extraSpacing)
			view.setPadding(controlMargin, controlMargin + t, 0, controlMargin + b);
		else
			view.setPadding(0, t, 0, b);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static void removeSplitTouch(ViewGroup viewGroup) {
		viewGroup.setMotionEventSplittingEnabled(false);
	}

	public static void announceAccessibilityText(CharSequence text) {
		if (accessibilityManager != null && accessibilityManager.isEnabled()) {
			final AccessibilityEvent e = AccessibilityEvent.obtain();
			//I couldn't make AccessibilityEvent.TYPE_ANNOUNCEMENT work... even on Android 16+
			e.setEventType(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
			e.setClassName("br.com.carlosrafaelgn.fplay.activity.ActivityHost");
			e.setPackageName("br.com.carlosrafaelgn.fplay");
			e.getText().add(text);
			accessibilityManager.sendAccessibilityEvent(e);
		}
	}

	public static boolean animationEnabled;
	public static final Interpolator animationInterpolator;
	private static int animationHideCount, animationShowCount;
	private static View[] animationViewsToHideAndShow;

	public static Animation animationCreateAlpha(float fromAlpha, float toAlpha) {
		final Animation animation = new AlphaAnimation(fromAlpha, toAlpha);
		animation.setDuration(TRANSITION_DURATION_FOR_VIEWS);
		animation.setInterpolator(animationInterpolator);
		animation.setRepeatCount(0);
		animation.setFillAfter(false);
		return animation;
	}

	public static void animationAddViewToHide(View view) {
		if (animationViewsToHideAndShow == null)
			animationViewsToHideAndShow = new View[32];
		else if (animationHideCount >= 16 && view != null && view.getVisibility() != View.GONE)
			return;
		animationViewsToHideAndShow[animationHideCount] = view;
		animationHideCount++;
	}

	public static void animationAddViewToShow(View view) {
		if (animationViewsToHideAndShow == null)
			animationViewsToHideAndShow = new View[32];
		else if (animationShowCount >= 16 && view != null && view.getVisibility() != View.VISIBLE)
			return;
		animationViewsToHideAndShow[16 + animationShowCount] = view;
		animationShowCount++;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}