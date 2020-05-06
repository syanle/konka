package greendroid.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.konka.ios7launcher.R;
import greendroid.widget.PagedView.OnPagedViewChangeListener;
import java.lang.ref.WeakReference;

public class PagedDialog extends Dialog implements OnPagedViewChangeListener {
    private static final String TAG = "PagedDialog";
    /* access modifiers changed from: private */
    public PagedAdapter mAdapter;
    private OnClickListener mButtonHandler;
    /* access modifiers changed from: private */
    public Button mButtonNegative;
    /* access modifiers changed from: private */
    public Message mButtonNegativeMessage;
    private CharSequence mButtonNegativeText;
    /* access modifiers changed from: private */
    public Button mButtonPositive;
    /* access modifiers changed from: private */
    public Message mButtonPositiveMessage;
    private CharSequence mButtonPositiveText;
    private int mContentHeight;
    private int mContentWidth;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private Drawable mIcon;
    private int mIconId;
    private ImageView mIconView;
    /* access modifiers changed from: private */
    public PageIndicator mPageIndicator;
    /* access modifiers changed from: private */
    public PagedView mPagedView;
    private ProgressBar mProgressBar;
    private CharSequence mTitleText;
    private TextView mTitleView;

    private static class AlertParams {
        public PagedAdapter mAdapter;
        public boolean mCancelable;
        public int mContentHeight;
        public int mContentWidth;
        public final Context mContext;
        public Drawable mIcon;
        public int mIconId = 0;
        public final LayoutInflater mInflater;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNegativeButtonText;
        public OnCancelListener mOnCancelListener;
        public OnKeyListener mOnKeyListener;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mPositiveButtonText;
        public CharSequence mTitle;

        public AlertParams(Context context) {
            this.mContext = context;
            this.mCancelable = true;
            this.mInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        }

        public void apply(PagedDialog dialog) {
            if (this.mTitle != null) {
                dialog.setTitle(this.mTitle);
            }
            if (this.mIcon != null) {
                dialog.setIcon(this.mIcon);
            }
            if (this.mIconId >= 0) {
                dialog.setIcon(this.mIconId);
            }
            if (this.mContentWidth > 0 && this.mContentHeight > 0) {
                dialog.setContentLayout(this.mContentWidth, this.mContentHeight);
            }
            if (this.mPositiveButtonText != null) {
                dialog.setButton(-1, this.mPositiveButtonText, this.mPositiveButtonListener, null);
            }
            if (this.mNegativeButtonText != null) {
                dialog.setButton(-2, this.mNegativeButtonText, this.mNegativeButtonListener, null);
            }
            if (this.mAdapter != null) {
                dialog.setAdapter(this.mAdapter);
            }
        }
    }

    public static class Builder {
        private final AlertParams P;
        private int mTheme;

        public Builder(Context context) {
            this.P = new AlertParams(context);
        }

        public Context getContext() {
            return this.P.mContext;
        }

        public Builder setTitle(int titleId) {
            this.P.mTitle = this.P.mContext.getText(titleId);
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.P.mTitle = title;
            return this;
        }

        public Builder setIcon(int iconId) {
            this.P.mIconId = iconId;
            return this;
        }

        public Builder setIcon(Drawable icon) {
            this.P.mIcon = icon;
            return this;
        }

        public Builder setIconAttribute(int attrId) {
            TypedValue out = new TypedValue();
            this.P.mContext.getTheme().resolveAttribute(attrId, out, true);
            this.P.mIconId = out.resourceId;
            return this;
        }

        public Builder setPositiveButton(int textId, DialogInterface.OnClickListener listener) {
            this.P.mPositiveButtonText = this.P.mContext.getText(textId);
            this.P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setPositiveButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.P.mPositiveButtonText = text;
            this.P.mPositiveButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(int textId, DialogInterface.OnClickListener listener) {
            this.P.mNegativeButtonText = this.P.mContext.getText(textId);
            this.P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setNegativeButton(CharSequence text, DialogInterface.OnClickListener listener) {
            this.P.mNegativeButtonText = text;
            this.P.mNegativeButtonListener = listener;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.P.mCancelable = cancelable;
            return this;
        }

        public Builder setOnCancelListener(OnCancelListener onCancelListener) {
            this.P.mOnCancelListener = onCancelListener;
            return this;
        }

        public Builder setOnKeyListener(OnKeyListener onKeyListener) {
            this.P.mOnKeyListener = onKeyListener;
            return this;
        }

        public Builder setAdapter(PagedAdapter adapter) {
            this.P.mAdapter = adapter;
            return this;
        }

        public Builder setContentLayout(int width, int height) {
            this.P.mContentWidth = width;
            this.P.mContentHeight = height;
            return this;
        }

        public PagedDialog create() {
            PagedDialog dialog = new PagedDialog(this.P.mContext);
            this.P.apply(dialog);
            dialog.setCancelable(this.P.mCancelable);
            if (this.P.mCancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            dialog.setOnCancelListener(this.P.mOnCancelListener);
            if (this.P.mOnKeyListener != null) {
                dialog.setOnKeyListener(this.P.mOnKeyListener);
            }
            return dialog;
        }

        public PagedDialog show() {
            PagedDialog dialog = create();
            dialog.show();
            return dialog;
        }
    }

    private static final class ButtonHandler extends Handler {
        private static final int MSG_DISMISS_DIALOG = 1;
        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            this.mDialog = new WeakReference<>(dialog);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -3:
                case -2:
                case PageIndicator.NO_ACTIVE_DOT /*-1*/:
                    ((DialogInterface.OnClickListener) msg.obj).onClick((DialogInterface) this.mDialog.get(), msg.what);
                    return;
                case 1:
                    ((DialogInterface) msg.obj).dismiss();
                    return;
                default:
                    return;
            }
        }
    }

    protected PagedDialog(Context context) {
        this(context, 0);
    }

    protected PagedDialog(Context context, int theme) {
        super(context, theme);
        this.mIconId = 0;
        this.mContentWidth = -1;
        this.mContentHeight = -1;
        this.mButtonHandler = new OnClickListener() {
            public void onClick(View v) {
                Message m = null;
                if (v == PagedDialog.this.mButtonPositive && PagedDialog.this.mButtonPositiveMessage != null) {
                    m = Message.obtain(PagedDialog.this.mButtonPositiveMessage);
                } else if (v == PagedDialog.this.mButtonNegative && PagedDialog.this.mButtonNegativeMessage != null) {
                    m = Message.obtain(PagedDialog.this.mButtonNegativeMessage);
                }
                if (m != null) {
                    m.sendToTarget();
                }
                PagedDialog.this.mHandler.obtainMessage(1, PagedDialog.this).sendToTarget();
            }
        };
        this.mHandler = new ButtonHandler(this);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.requestFeature(1);
        window.setFlags(131072, 131072);
        setContentView(R.layout.paged_dialog);
        if (this.mContentWidth > 0) {
            window.setLayout(this.mContentWidth + 32, -2);
        }
        boolean z = setupTitle((LinearLayout) findViewById(R.id.topPanel));
        setupContent();
        boolean hasButtons = setupButtons();
        View buttonPanel = window.findViewById(R.id.buttonPanel);
        if (!hasButtons) {
            buttonPanel.setVisibility(8);
            window.setCloseOnTouchOutsideIfNotSet(true);
        }
    }

    public void setShowProgress(boolean shown) {
        boolean z;
        int i = 0;
        if (this.mProgressBar != null) {
            int visibility = this.mProgressBar.getVisibility();
            if (visibility == 0) {
                z = true;
            } else {
                z = false;
            }
            if (z ^ shown) {
                ProgressBar progressBar = this.mProgressBar;
                if (visibility == 0) {
                    i = 8;
                }
                progressBar.setVisibility(i);
            }
        }
    }

    private boolean setupTitle(LinearLayout topPanel) {
        boolean hasTextTitle = !TextUtils.isEmpty(this.mTitleText);
        this.mIconView = (ImageView) findViewById(R.id.icon);
        if (hasTextTitle) {
            this.mTitleView = (TextView) findViewById(R.id.pagedTitle);
            this.mTitleView.setText(this.mTitleText);
            if (this.mIconId > 0) {
                this.mIconView.setImageResource(this.mIconId);
                return true;
            } else if (this.mIcon != null) {
                this.mIconView.setImageDrawable(this.mIcon);
                return true;
            } else if (this.mIconId != 0) {
                return true;
            } else {
                this.mTitleView.setPadding(this.mIconView.getPaddingLeft(), this.mIconView.getPaddingTop(), this.mIconView.getPaddingRight(), this.mIconView.getPaddingBottom());
                this.mIconView.setVisibility(8);
                return true;
            }
        } else {
            findViewById(R.id.title_template).setVisibility(8);
            this.mIconView.setVisibility(8);
            topPanel.setVisibility(8);
            return false;
        }
    }

    private boolean setupButtons() {
        int whichButtons = 0;
        this.mButtonPositive = (Button) findViewById(R.id.button1);
        this.mButtonPositive.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonPositiveText)) {
            this.mButtonPositive.setVisibility(8);
        } else {
            this.mButtonPositive.setText(this.mButtonPositiveText);
            this.mButtonPositive.setVisibility(0);
            whichButtons = 0 | 1;
        }
        this.mButtonNegative = (Button) findViewById(R.id.button2);
        this.mButtonNegative.setOnClickListener(this.mButtonHandler);
        if (TextUtils.isEmpty(this.mButtonNegativeText)) {
            this.mButtonNegative.setVisibility(8);
        } else {
            this.mButtonNegative.setText(this.mButtonNegativeText);
            this.mButtonNegative.setVisibility(0);
            whichButtons |= 2;
        }
        if (whichButtons == 1) {
            centerButton(this.mButtonPositive);
        } else if (whichButtons == 2) {
            centerButton(this.mButtonNegative);
        }
        if (whichButtons != 0) {
            return true;
        }
        return false;
    }

    private void centerButton(Button button) {
        LayoutParams params = (LayoutParams) button.getLayoutParams();
        params.gravity = 1;
        params.weight = 0.5f;
        button.setLayoutParams(params);
    }

    private void setupContent() {
        this.mProgressBar = (ProgressBar) findViewById(R.id.apps_customize_progress_bar);
        this.mProgressBar.setVisibility(4);
        this.mPagedView = (PagedView) findViewById(R.id.paged_view);
        if (this.mContentWidth > 0 && this.mContentHeight > 0) {
            ViewGroup.LayoutParams params = this.mPagedView.getLayoutParams();
            Log.d(TAG, "pagedDialog viewgroup params " + (params == null) + " null");
            Log.d(TAG, "pagedDialog mContentWidth = " + this.mContentWidth + ", mContentHeight=" + this.mContentHeight);
            if (params == null) {
                params = new ViewGroup.LayoutParams(this.mContentWidth, this.mContentHeight);
            } else {
                params.width = this.mContentWidth;
                params.height = this.mContentHeight;
            }
            this.mPagedView.setLayoutParams(params);
        }
        this.mPageIndicator = (PageIndicator) findViewById(R.id.page_indicator);
        if (this.mAdapter != null) {
            this.mPagedView.setAdapter(this.mAdapter);
            this.mPagedView.setOnPageChangeListener(this);
            this.mAdapter.registerDataSetObserver(new DataSetObserver() {
                private void updatePageIndicator() {
                    int count = PagedDialog.this.mAdapter.getCount();
                    int visibility = PagedDialog.this.mPageIndicator.getVisibility();
                    if (visibility != 0 && count > 0) {
                        PagedDialog.this.mPageIndicator.setDotCount(count);
                        PagedDialog.this.mPageIndicator.setActiveDot(PagedDialog.this.mPagedView.getCurrentPage());
                        PagedDialog.this.mPageIndicator.setVisibility(0);
                    } else if (visibility == 0 && count <= 0) {
                        PagedDialog.this.mPageIndicator.setVisibility(8);
                    }
                }

                public void onChanged() {
                    updatePageIndicator();
                }

                public void onInvalidated() {
                    updatePageIndicator();
                }
            });
            if (this.mAdapter.getCount() > 0) {
                this.mPageIndicator.setDotCount(this.mAdapter.getCount());
                this.mPageIndicator.setActiveDot(this.mPagedView.getCurrentPage());
                return;
            }
            this.mPageIndicator.setVisibility(4);
        }
    }

    public void onPageChanged(PagedView pagedView, int previousPage, int newPage) {
        this.mPageIndicator.setActiveDot(newPage);
    }

    public void onStartTracking(PagedView pagedView) {
    }

    public void onStopTracking(PagedView pagedView) {
    }

    private boolean handleKeyEvent(int keyCode, KeyEvent event) {
        if (event.getAction() != 0 || this.mPagedView == null) {
            return false;
        }
        if (this.mPagedView.hasFocus()) {
            return this.mPagedView.onKeyDown(keyCode, event);
        }
        if (event.getKeyCode() == 19) {
            return this.mPagedView.requestFocus(130);
        }
        return false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.mTitleText = title;
        if (this.mTitleView != null) {
            this.mTitleView.setText(title);
        }
    }

    public PagedAdapter getAdapter() {
        return this.mAdapter;
    }

    public void setAdapter(PagedAdapter adapter) {
        this.mAdapter = adapter;
    }

    public void setContentLayout(int width, int height) {
        this.mContentWidth = width;
        this.mContentHeight = height;
    }

    public void setButton(int whichButton, CharSequence text, Message msg) {
        setButton(whichButton, text, null, msg);
    }

    /* access modifiers changed from: private */
    public void setButton(int whichButton, CharSequence text, DialogInterface.OnClickListener listener, Message msg) {
        if (msg == null && listener != null) {
            msg = this.mHandler.obtainMessage(whichButton, listener);
        }
        switch (whichButton) {
            case -2:
                this.mButtonNegativeText = text;
                this.mButtonNegativeMessage = msg;
                return;
            case PageIndicator.NO_ACTIVE_DOT /*-1*/:
                this.mButtonPositiveText = text;
                this.mButtonPositiveMessage = msg;
                return;
            default:
                throw new IllegalArgumentException("Button does not exist");
        }
    }

    /* access modifiers changed from: private */
    public void setIcon(int resId) {
        this.mIconId = resId;
        if (this.mIconView == null) {
            return;
        }
        if (resId > 0) {
            this.mIconView.setImageResource(resId);
        } else if (resId == 0) {
            this.mIconView.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public void setIcon(Drawable icon) {
        this.mIcon = icon;
        if (this.mIconView != null && icon != null) {
            this.mIconView.setImageDrawable(icon);
        }
    }
}
