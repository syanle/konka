package com.cyanogenmod.trebuchet;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.android.internal.util.XmlUtils;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;
import java.io.IOException;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParserException;

public class AppInfoManager {
    private static final String TAG = "AppInfoManager";
    private static Context mContext;
    private static AppInfoManager mUniqueInstance;
    private static Bitmap sDefaultBackground;
    ArrayList<AppInfoData> mSystemApps = new ArrayList<>();

    class AppInfoData {
        String mAppName;
        String mClassName;
        Bitmap mMetroIcon;
        String mPackageName;

        private AppInfoData(String packageName, String className, String appName, Bitmap metroIcon) {
            this.mPackageName = packageName;
            this.mClassName = className;
            this.mAppName = appName;
            this.mMetroIcon = metroIcon;
        }

        /* synthetic */ AppInfoData(AppInfoManager appInfoManager, String str, String str2, String str3, Bitmap bitmap, AppInfoData appInfoData) {
            this(str, str2, str3, bitmap);
        }
    }

    private AppInfoManager() {
        loadConfigData();
        for (int i = 0; i < this.mSystemApps.size(); i++) {
            AppInfoData data = (AppInfoData) this.mSystemApps.get(i);
            if (data != null) {
                Log.d(TAG, "packageName = " + data.mPackageName + ",className = " + data.mClassName + ",appName = " + data.mAppName + ",Icon = " + data.mMetroIcon);
            }
        }
    }

    public static synchronized void createInstance(Context context) {
        synchronized (AppInfoManager.class) {
            if (mUniqueInstance == null) {
                mContext = context;
                mUniqueInstance = new AppInfoManager();
                Bitmap bg = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_ios_app_icon_background);
                sDefaultBackground = bg != null ? bg.copy(Config.ARGB_8888, true) : null;
            }
        }
    }

    public static synchronized AppInfoManager getInstance() {
        AppInfoManager appInfoManager;
        synchronized (AppInfoManager.class) {
            if (mUniqueInstance == null && mContext != null) {
                createInstance(mContext);
            }
            appInfoManager = mUniqueInstance;
        }
        return appInfoManager;
    }

    private void loadConfigData() {
        XmlResourceParser parser = mContext.getResources().getXml(R.xml.default_system_app_ios7_style_icon);
        AttributeSet attrs = Xml.asAttributeSet(parser);
        try {
            XmlUtils.beginDocument(parser, "appIOS7Icons");
            int depth = parser.getDepth();
            while (true) {
                int type = parser.next();
                if ((type == 3 && parser.getDepth() <= depth) || type == 1) {
                    return;
                }
                if (type == 2 && parser.getName().equals("appIOS7Icon")) {
                    TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.appIOS7Icon);
                    String targetPackageName = a.getString(0);
                    String targetClassName = a.getString(1);
                    String targetAppName = a.getString(2);
                    Bitmap targetIcon = null;
                    Drawable drawable = null;
                    try {
                        drawable = a.getDrawable(3);
                    } catch (NotFoundException e) {
                    }
                    if (drawable != null) {
                        targetIcon = ((BitmapDrawable) drawable).getBitmap();
                    }
                    this.mSystemApps.add(new AppInfoData(this, targetPackageName, targetClassName, targetAppName, targetIcon, null));
                    a.recycle();
                }
            }
        } catch (XmlPullParserException e2) {
            e2.printStackTrace();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
    }

    public void setIOS7Icon(ShortcutInfo info, IconCache cache) {
        Bitmap targetIcon = getIOS7Icon(info.intent);
        if (targetIcon != null) {
            info.setIcon(targetIcon);
            Log.e("ioslauncher", "targetIcon is ok");
            Log.e(TAG, "system app title = " + info.title);
            return;
        }
        Log.e("ioslauncher", "targetIcon is null");
        Bitmap mIcon = info.getIcon(cache);
        if (sDefaultBackground != null && mIcon != null) {
            Bitmap icon = Utilities.ComposeTwoBitmap(sDefaultBackground, mIcon);
            if (icon != null) {
                info.setIcon(icon);
            }
        }
    }

    private Bitmap getIOS7Icon(Intent intent) {
        String packageName = null;
        String className = null;
        if (intent == null) {
            return null;
        }
        ComponentName componentName = intent.getComponent();
        if (componentName != null) {
            packageName = componentName.getPackageName();
            className = componentName.getClassName();
        }
        Log.d(TAG, "packageName = " + packageName + "className = " + className);
        if (packageName == null) {
            return null;
        }
        if (className != null) {
            for (int i = 0; i < this.mSystemApps.size(); i++) {
                AppInfoData data = (AppInfoData) this.mSystemApps.get(i);
                if (data.mClassName != null && className.equals(data.mClassName)) {
                    return data.mMetroIcon;
                }
            }
        }
        if (packageName == null) {
            return null;
        }
        if (packageName.equals(MessageManager.MESSAGE_PACKAGE_NAME)) {
            Log.d(TAG, "message package");
            return setIconCornerNumber(mContext, R.drawable.com_konka_message, MessageManager.getInstance().getMessageUnReadNumber());
        } else if (packageName.equals("com.konka.market.main")) {
            Log.d(TAG, "market package");
            return setIconCornerNumber(mContext, R.drawable.com_konka_market_main, MarketManager.getInstance().getMarketUgradeNumber());
        } else {
            for (int i2 = 0; i2 < this.mSystemApps.size(); i2++) {
                AppInfoData data2 = (AppInfoData) this.mSystemApps.get(i2);
                if (packageName.equals(data2.mPackageName)) {
                    Log.d(TAG, "package:" + data2.mPackageName + "\ticonWidth:" + data2.mMetroIcon.getWidth());
                    return data2.mMetroIcon;
                }
            }
            return null;
        }
    }

    public Bitmap setIconCornerNumber(Context context, int resId, int number) {
        int rectWidth;
        Drawable drawable = context.getResources().getDrawable(resId);
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        Bitmap bmp = Bitmap.createBitmap(drawableWidth, drawableHeight, drawable.getOpacity() != -1 ? Config.ARGB_8888 : Config.RGB_565);
        Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, drawableWidth, drawableHeight);
        drawable.draw(canvas);
        Log.d(TAG, "messageicon width:" + bmp.getWidth());
        bmp.setDensity(0);
        if (number > 0) {
            int circleRadius = DensityUtil.dp2px(context, 13.0f);
            int circleX = drawable.getIntrinsicWidth() - circleRadius;
            int circleY = circleRadius;
            int roundRadius = DensityUtil.dp2px(context, 8.0f);
            int rectDx = DensityUtil.dp2px(context, 6.0f);
            int rectHeight = circleRadius * 2;
            int length = ((int) Math.log10((double) number)) + 1;
            int numSize = DensityUtil.sp2px(context, 19.0f);
            String numStr = new StringBuilder(String.valueOf(number)).toString();
            if (number < 10) {
                Paint paintCircleBg = new Paint();
                paintCircleBg.setStyle(Style.FILL);
                paintCircleBg.setColor(-65536);
                paintCircleBg.setAntiAlias(true);
                paintCircleBg.setFilterBitmap(true);
                paintCircleBg.setShadowLayer(2.0f, FlyingIcon.ANGULAR_VMIN, 1.0f, -12303292);
                canvas.drawCircle((float) circleX, (float) circleY, (float) circleRadius, paintCircleBg);
                rectWidth = circleRadius * 2;
            } else if (number < 99) {
                rectWidth = (circleRadius * 2) + (rectDx * length);
                RectF rectF = new RectF((float) (drawableWidth - rectWidth), FlyingIcon.ANGULAR_VMIN, (float) drawableWidth, (float) rectHeight);
                Paint paintRectBg = new Paint();
                paintRectBg.setStyle(Style.FILL);
                paintRectBg.setColor(-65536);
                paintRectBg.setAntiAlias(true);
                paintRectBg.setFilterBitmap(true);
                paintRectBg.setShadowLayer(2.0f, FlyingIcon.ANGULAR_VMIN, 1.0f, -12303292);
                canvas.drawRoundRect(rectF, (float) roundRadius, (float) roundRadius, paintRectBg);
            } else {
                rectWidth = (circleRadius * 2) + (rectDx * length);
                RectF rectF2 = new RectF((float) (drawableWidth - rectWidth), FlyingIcon.ANGULAR_VMIN, (float) drawableWidth, (float) rectHeight);
                Paint paintRectBg2 = new Paint();
                paintRectBg2.setStyle(Style.FILL);
                paintRectBg2.setColor(-65536);
                paintRectBg2.setAntiAlias(true);
                paintRectBg2.setFilterBitmap(true);
                paintRectBg2.setShadowLayer(2.0f, FlyingIcon.ANGULAR_VMIN, 1.0f, -12303292);
                canvas.drawRoundRect(rectF2, (float) roundRadius, (float) roundRadius, paintRectBg2);
                numStr = new StringBuilder(String.valueOf(99)).append("+").toString();
            }
            Paint paintNum = new Paint();
            Rect numRect = new Rect();
            paintNum.setTextSize((float) numSize);
            paintNum.getTextBounds(numStr, 0, numStr.length(), numRect);
            int numX = (drawableWidth - rectWidth) + ((rectWidth - numRect.width()) / 2);
            int numY = rectHeight - ((rectHeight - numRect.height()) / 2);
            if (numStr.startsWith("1")) {
                numX -= DensityUtil.dp2px(context, 2.0f);
            }
            paintNum.setColor(-1);
            paintNum.setAntiAlias(true);
            paintNum.setFilterBitmap(true);
            paintNum.setTypeface(Typeface.DEFAULT_BOLD);
            canvas.drawText(numStr, (float) numX, (float) numY, paintNum);
        }
        return bmp;
    }

    private boolean isDefaultSystemApp(Intent intent) {
        String packageName = null;
        if (intent == null) {
            return false;
        }
        PackageManager packageManager = mContext.getPackageManager();
        ComponentName componentName = intent.getComponent();
        if (componentName != null) {
            packageName = componentName.getPackageName();
        }
        if (packageName == null) {
            return false;
        }
        for (int i = 0; i < this.mSystemApps.size(); i++) {
            if (packageName.equals(((AppInfoData) this.mSystemApps.get(i)).mPackageName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDefaultSystemApp(String packageName) {
        if (packageName != null) {
            for (int i = 0; i < this.mSystemApps.size(); i++) {
                if (packageName.equals(((AppInfoData) this.mSystemApps.get(i)).mPackageName)) {
                    return true;
                }
            }
        }
        return false;
    }
}
