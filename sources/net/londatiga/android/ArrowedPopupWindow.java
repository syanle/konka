package net.londatiga.android;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.konka.ios7launcher.R;

public class ArrowedPopupWindow extends PopupWindows implements android.widget.PopupWindow.OnDismissListener {
    public static final int ANIM_AUTO = 5;
    public static final int ANIM_GROW_FROM_CENTER = 3;
    public static final int ANIM_GROW_FROM_LEFT = 1;
    public static final int ANIM_GROW_FROM_RIGHT = 2;
    public static final int ANIM_REFLECT = 4;
    private static final String TAG = "ArrowedPopupWindow";
    private int mAnimStyle;
    private ImageView mArrowDown;
    private ImageView mArrowUp;
    private FrameLayout mContainer;
    protected ViewGroup mContentView;
    protected OnDismissListener mDismissListener;
    private int mRootWidth = 0;

    public interface OnDismissListener {
        void onDismiss();
    }

    public ArrowedPopupWindow(Context context, int animStyle) {
        super(context);
        this.mAnimStyle = animStyle;
    }

    private void initContentView(View content, LayoutParams params) {
        super.setContentView((int) R.layout.arrowed_popup_window);
        View rootView = super.getContentView();
        this.mArrowDown = (ImageView) rootView.findViewById(R.id.arrow_down);
        this.mArrowUp = (ImageView) rootView.findViewById(R.id.arrow_up);
        this.mContainer = (FrameLayout) rootView.findViewById(R.id.container);
        if (this.mContentView != null) {
            this.mContainer.removeView(this.mContentView);
        }
        if (params != null) {
            content.setLayoutParams(params);
        }
        this.mContainer.addView(content);
        this.mContentView = (ViewGroup) content;
    }

    public void setContentView(int viewGroupResId) {
        initContentView((ViewGroup) this.mInflater.inflate(viewGroupResId, null), null);
    }

    public void setContentView(int viewGroupResId, LayoutParams params) {
        initContentView((ViewGroup) this.mInflater.inflate(viewGroupResId, null), params);
    }

    public void setContentView(View content) {
        initContentView(content, null);
    }

    public void setContentView(View content, LayoutParams params) {
        initContentView(content, params);
    }

    public void setContainerAlignParentRight() {
        ((RelativeLayout.LayoutParams) this.mContainer.getLayoutParams()).addRule(11);
    }

    public void setAnimStyle(int mAnimStyle2) {
        this.mAnimStyle = mAnimStyle2;
    }

    public void show(View anchor) {
        int widthSize;
        int heightSize;
        int xPos;
        int arrowPos;
        int yPos;
        preShow();
        View rootView = this.mRootView;
        LayoutParams params = rootView.getLayoutParams();
        if (params != null) {
            widthSize = params.width;
            heightSize = params.height;
        } else {
            widthSize = 0;
            heightSize = 0;
        }
        rootView.measure(MeasureSpec.makeMeasureSpec(widthSize, 0), MeasureSpec.makeMeasureSpec(heightSize, 0));
        int rootHeight = rootView.getMeasuredHeight();
        if (this.mRootWidth == 0) {
            this.mRootWidth = rootView.getMeasuredWidth();
        }
        int screenWidth = this.mWindowManager.getDefaultDisplay().getWidth();
        int screenHeight = this.mWindowManager.getDefaultDisplay().getHeight();
        int[] location = new int[2];
        anchor.getLocationOnScreen(location);
        Rect anchorRect = new Rect(location[0], location[1], location[0] + anchor.getWidth(), location[1] + anchor.getHeight());
        if (this instanceof QuickAction) {
            if (anchorRect.left + (this.mRootWidth / 2) > screenWidth) {
                xPos = (screenWidth - this.mRootWidth) - 1;
                if (xPos < 0) {
                    xPos = 0;
                }
                arrowPos = anchorRect.centerX() - xPos;
            } else {
                xPos = Math.max(anchorRect.centerX() - (this.mRootWidth / 2), 0);
                arrowPos = anchorRect.centerX() - xPos;
            }
        } else if (anchorRect.left + (this.mRootWidth / 4) > screenWidth) {
            int xPos2 = (screenWidth - this.mRootWidth) - 1;
            if (xPos2 < 0) {
                xPos2 = 0;
            }
            arrowPos = anchorRect.centerX() - xPos;
        } else {
            xPos = Math.max(anchorRect.centerX() - ((this.mRootWidth * 3) / 4), 0);
            arrowPos = anchorRect.centerX() - xPos;
        }
        int dyTop = anchorRect.top;
        int dyBottom = screenHeight - anchorRect.bottom;
        boolean onTop = dyTop > dyBottom;
        if (!onTop) {
            yPos = anchorRect.bottom;
            if (rootHeight > dyBottom) {
                this.mContainer.getLayoutParams().height = dyBottom;
            }
        } else if (rootHeight > dyTop) {
            yPos = 15;
            this.mContainer.getLayoutParams().height = dyTop - anchor.getHeight();
        } else {
            yPos = anchorRect.top - rootHeight;
        }
        setAnimationStyle(screenWidth, anchorRect.centerX(), onTop);
        showArrow(onTop ? R.id.arrow_down : R.id.arrow_up, arrowPos);
        setMeasureSpecs(MeasureSpec.makeMeasureSpec(Math.min(this.mRootWidth, screenWidth), 1073741824), MeasureSpec.makeMeasureSpec(Math.min(rootHeight, screenHeight), 1073741824));
        this.mWindow.showAtLocation(anchor, 0, xPos, yPos);
        this.mContainer.requestFocus();
    }

    private void setAnimationStyle(int screenWidth, int requestedX, boolean onTop) {
        int i;
        int i2 = R.style.Animations_QuickAction_PopUpMenu_Left;
        int i3 = R.style.Animations_QuickAction_PopUpMenu_Center;
        int i4 = R.style.Animations_QuickAction_PopDownMenu_Right;
        int arrowPos = requestedX - (this.mArrowUp.getMeasuredWidth() / 2);
        switch (this.mAnimStyle) {
            case 1:
                PopupWindow popupWindow = this.mWindow;
                if (!onTop) {
                    i2 = 2131623963;
                }
                popupWindow.setAnimationStyle(i2);
                return;
            case 2:
                this.mWindow.setAnimationStyle(onTop ? R.style.Animations_QuickAction_PopUpMenu_Right : 2131623964);
                return;
            case 3:
                this.mWindow.setAnimationStyle(onTop ? 2131623967 : 2131623962);
                return;
            case 4:
                PopupWindow popupWindow2 = this.mWindow;
                if (onTop) {
                    i = R.style.Animations_QuickAction_PopUpMenu_Reflect;
                } else {
                    i = R.style.Animations_QuickAction_PopDownMenu_Reflect;
                }
                popupWindow2.setAnimationStyle(i);
                return;
            case 5:
                if (arrowPos <= screenWidth / 4) {
                    PopupWindow popupWindow3 = this.mWindow;
                    if (!onTop) {
                        i2 = 2131623963;
                    }
                    popupWindow3.setAnimationStyle(i2);
                    return;
                } else if (arrowPos <= screenWidth / 4 || arrowPos >= (screenWidth / 4) * 3) {
                    PopupWindow popupWindow4 = this.mWindow;
                    if (onTop) {
                        i4 = R.style.Animations_QuickAction_PopUpMenu_Right;
                    }
                    popupWindow4.setAnimationStyle(i4);
                    return;
                } else {
                    PopupWindow popupWindow5 = this.mWindow;
                    if (!onTop) {
                        i3 = 2131623962;
                    }
                    popupWindow5.setAnimationStyle(i3);
                    return;
                }
            default:
                return;
        }
    }

    private void showArrow(int whichArrow, int requestedX) {
        View showArrow = whichArrow == R.id.arrow_up ? this.mArrowUp : this.mArrowDown;
        View hideArrow = whichArrow == R.id.arrow_up ? this.mArrowDown : this.mArrowUp;
        int arrowWidth = this.mArrowUp.getMeasuredWidth();
        showArrow.setVisibility(0);
        ((MarginLayoutParams) showArrow.getLayoutParams()).leftMargin = requestedX - (arrowWidth / 2);
        hideArrow.setVisibility(4);
    }

    public void setOnDismissListener(OnDismissListener listener) {
        setOnDismissListener(this);
        this.mDismissListener = listener;
    }

    public void onDismiss() {
        if (this.mDismissListener != null) {
            this.mDismissListener.onDismiss();
        }
    }

    public boolean isShowing() {
        return this.mWindow.isShowing();
    }

    public void dismiss() {
        this.mWindow.dismiss();
    }

    public void update(int width, int height) {
        if (this.mWindow != null && this.mWindow.isShowing() && this.mWindow.getContentView() != null) {
            this.mWindow.update(width, height);
        }
    }
}
