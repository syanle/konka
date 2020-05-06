package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnHoverListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import com.cyanogenmod.trebuchet.AppsCustomizeView.ContentType;
import com.cyanogenmod.trebuchet.preference.PreferencesProvider.Interface.Drawer.Indicator;
import com.konka.ios7launcher.R;

public class AppsCustomizeTabHost extends TabHost implements LauncherTransitionable, OnTabChangeListener, OnHoverListener, OnFocusChangeListener {
    private static final String APPS_TAB_TAG = "APPS";
    static final String LOG_TAG = "AppsCustomizeTabHost";
    private static final String WIDGETS_TAB_TAG = "WIDGETS";
    private FrameLayout mAnimationBuffer;
    private AppsCustomizeView mAppsCustomizePane;
    private LinearLayout mContent;
    private View mCurFocusView;
    private boolean mFadeScrollingIndicator;
    private boolean mInTransition;
    private Launcher mLauncher;
    private Animator mLauncherTransition;
    private final LayoutInflater mLayoutInflater;
    private boolean mResetAfterTransition;
    private final Resources mResources;
    private boolean mSuppressContentCallback = false;
    /* access modifiers changed from: private */
    public ViewGroup mTabs;
    /* access modifiers changed from: private */
    public ViewGroup mTabsContainer;

    public AppsCustomizeTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mResources = context.getResources();
        this.mLauncher = (Launcher) context;
        this.mFadeScrollingIndicator = Indicator.getFadeScrollingIndicator(context);
    }

    private void setContentTypeImmediate(ContentType type) {
        this.mAppsCustomizePane.hideIndicator(false);
        this.mAppsCustomizePane.setContentType(type);
    }

    /* access modifiers changed from: 0000 */
    public void selectAppsTab() {
        setContentTypeImmediate(ContentType.Apps);
        setCurrentTabByTag(APPS_TAB_TAG);
    }

    /* access modifiers changed from: 0000 */
    public void selectWidgetsTab() {
        setContentTypeImmediate(ContentType.Widgets);
        this.mAppsCustomizePane.setCurrentToWidgets();
        setCurrentTabByTag(WIDGETS_TAB_TAG);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        AppsCustomizeView appsCustomizePane = (AppsCustomizeView) findViewById(R.id.apps_customize_pane_content);
        this.mTabsContainer = (ViewGroup) findViewById(R.id.tabs_container);
        this.mAppsCustomizePane = appsCustomizePane;
        this.mAnimationBuffer = (FrameLayout) findViewById(R.id.animation_buffer);
        this.mContent = (LinearLayout) findViewById(R.id.apps_customize_content);
        AppsCustomizeTabKeyEventListener keyListener = new AppsCustomizeTabKeyEventListener();
        View appLatestButton = findViewById(R.id.applist_latest);
        appLatestButton.setOnKeyListener(keyListener);
        appLatestButton.setOnHoverListener(this);
        appLatestButton.setOnFocusChangeListener(this);
        appLatestButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_latest_app));
        View marketButton = findViewById(R.id.market_button);
        marketButton.setOnKeyListener(keyListener);
        marketButton.setOnHoverListener(this);
        marketButton.setOnFocusChangeListener(this);
        marketButton.setTag(R.id.hint_text, this.mResources.getString(R.string.hint_market));
        TextView versionTextView = (TextView) findViewById(R.id.release_version);
        try {
            versionTextView.setText(this.mLauncher.getString(R.string.versionFormatter, new Object[]{this.mLauncher.getPackageManager().getPackageInfo(this.mLauncher.getPackageName(), 0).versionName, Integer.valueOf(this.mLauncher.getPackageManager().getPackageInfo(this.mLauncher.getPackageName(), 0).versionCode)}));
        } catch (NameNotFoundException e) {
            Log.e(LOG_TAG, "Can't find version info:", e);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean remeasureTabWidth = this.mTabs != null && this.mTabs.getLayoutParams().width <= 0;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (remeasureTabWidth) {
            int contentWidth = ((View) this.mAppsCustomizePane).getMeasuredWidth();
            if (contentWidth > 0 && this.mTabs.getLayoutParams().width != contentWidth) {
                this.mTabs.getLayoutParams().width = contentWidth;
                post(new Runnable() {
                    public void run() {
                        AppsCustomizeTabHost.this.mTabs.requestLayout();
                        AppsCustomizeTabHost.this.mTabsContainer.setAlpha(1.0f);
                    }
                });
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (event.getY() < ((float) ((View) this.mAppsCustomizePane).getBottom())) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void onTabChanged(String tabId) {
        ContentType type = getContentTypeForTabTag(tabId);
        if (this.mSuppressContentCallback) {
            this.mSuppressContentCallback = false;
        } else {
            this.mAppsCustomizePane.onTabChanged(type);
        }
    }

    private void showHint(View v) {
        ((DragLayer) getParent()).showDelayedHint(v, (String) v.getTag(R.id.hint_text), 1000, 1000);
    }

    private void hideHint(View v) {
        ((DragLayer) getParent()).hideHint(v);
    }

    public boolean onHover(View v, MotionEvent event) {
        if (this.mLauncher.isWorkspaceVisible()) {
            return false;
        }
        switch (event.getAction()) {
            case 9:
                v.onHoverEvent(event);
                showHint(v);
                return true;
            case 10:
                v.onHoverEvent(event);
                hideHint(v);
                return true;
            default:
                return false;
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            hideHint(v);
        } else {
            showHint(v);
        }
    }

    public void setCurrentTabFromContent(ContentType type) {
        this.mSuppressContentCallback = true;
        setCurrentTabByTag(getTabTagForContentType(type));
    }

    public ContentType getContentTypeForTabTag(String tag) {
        if (tag.equals(APPS_TAB_TAG)) {
            return ContentType.Apps;
        }
        if (tag.equals(WIDGETS_TAB_TAG)) {
            return ContentType.Widgets;
        }
        return ContentType.Apps;
    }

    public String getTabTagForContentType(ContentType type) {
        if (type == ContentType.Apps) {
            return APPS_TAB_TAG;
        }
        if (type == ContentType.Widgets) {
            return WIDGETS_TAB_TAG;
        }
        return APPS_TAB_TAG;
    }

    public int getDescendantFocusability() {
        if (getVisibility() != 0) {
            return 393216;
        }
        return super.getDescendantFocusability();
    }

    /* access modifiers changed from: 0000 */
    public void reset() {
        if (this.mInTransition) {
            this.mResetAfterTransition = true;
        } else {
            this.mAppsCustomizePane.reset();
        }
    }

    private void enableAndBuildHardwareLayer() {
        if (isHardwareAccelerated()) {
            setLayerType(2, null);
            buildLayer();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mLauncherTransition != null) {
            enableAndBuildHardwareLayer();
            this.mLauncherTransition.start();
            this.mLauncherTransition = null;
        }
    }

    public boolean onLauncherTransitionStart(Launcher l, Animator animation, boolean toWorkspace) {
        boolean animated;
        this.mInTransition = true;
        boolean delayLauncherTransitionUntilLayout = false;
        if (animation != null) {
            animated = true;
        } else {
            animated = false;
        }
        this.mLauncherTransition = null;
        if (animated && this.mContent.getVisibility() == 8) {
            this.mLauncherTransition = animation;
            delayLauncherTransitionUntilLayout = true;
        }
        this.mContent.setVisibility(0);
        if (!toWorkspace) {
            this.mAppsCustomizePane.loadContent(true);
        }
        if (animated && !delayLauncherTransitionUntilLayout) {
            enableAndBuildHardwareLayer();
        }
        if (!toWorkspace) {
            this.mAppsCustomizePane.showIndicator(false);
        }
        if (this.mResetAfterTransition) {
            this.mAppsCustomizePane.reset();
            this.mResetAfterTransition = false;
        }
        return delayLauncherTransitionUntilLayout;
    }

    public void onLauncherTransitionEnd(Launcher l, Animator animation, boolean toWorkspace) {
        this.mInTransition = false;
        if (animation != null) {
            setLayerType(0, null);
        }
        if (!toWorkspace) {
            l.dismissWorkspaceCling(null);
            this.mAppsCustomizePane.showAllAppsCling();
            this.mAppsCustomizePane.loadContent();
            if (!LauncherApplication.isScreenLarge() && this.mFadeScrollingIndicator) {
                this.mAppsCustomizePane.hideIndicator(false);
            }
            ViewGroup contents = (ViewGroup) findViewById(16908305);
            View appLatestButton = findViewById(R.id.applist_latest);
            View marketButton = findViewById(R.id.market_button);
            if (!contents.hasFocus() && !appLatestButton.hasFocus() && !marketButton.hasFocus()) {
                contents.requestFocus();
            }
        }
    }

    public void onPause() {
        ViewGroup contents = (ViewGroup) findViewById(16908305);
        View appLatestButton = findViewById(R.id.applist_latest);
        View marketButton = findViewById(R.id.market_button);
        if (appLatestButton.isFocused()) {
            this.mCurFocusView = appLatestButton;
        } else if (marketButton.isFocused()) {
            this.mCurFocusView = marketButton;
        } else if (contents.hasFocus()) {
            this.mCurFocusView = this.mAppsCustomizePane.getCurSelectView();
        } else {
            this.mCurFocusView = contents;
        }
    }

    public void onResume() {
        if (getVisibility() == 0) {
            this.mContent.setVisibility(0);
            this.mAppsCustomizePane.loadContent(true);
            this.mAppsCustomizePane.loadContent();
            if (this.mCurFocusView != null) {
                this.mCurFocusView.requestFocus();
            }
        }
    }

    public void onTrimMemory() {
        this.mContent.setVisibility(8);
        this.mAppsCustomizePane.clearAllWidgetPreviews();
    }

    /* access modifiers changed from: 0000 */
    public boolean isTransitioning() {
        return this.mInTransition;
    }
}
