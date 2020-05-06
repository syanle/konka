package net.londatiga.android;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.konka.ios7launcher.R;
import java.util.ArrayList;
import java.util.List;

public class QuickAction extends ArrowedPopupWindow implements OnKeyListener {
    public static final int HORIZONTAL = 0;
    private static final String TAG = "QuickAction";
    public static final int VERTICAL = 1;
    private List<ActionItem> actionItems;
    private int mChildPos;
    /* access modifiers changed from: private */
    public boolean mDidAction;
    private int mInsertPos;
    /* access modifiers changed from: private */
    public OnActionItemClickListener mItemClickListener;
    private int mOrientation;

    public interface OnActionItemClickListener {
        void onItemClick(QuickAction quickAction, int i, int i2);
    }

    public QuickAction(Context context) {
        this(context, 1);
    }

    public QuickAction(Context context, int orientation) {
        this(context, orientation, 5);
    }

    public QuickAction(Context context, int orientation, int animStyle) {
        super(context, animStyle);
        this.actionItems = new ArrayList();
        if (orientation == 0) {
            setContentView((int) R.layout.quickaction_popup_horizontal);
        } else {
            setContentView((int) R.layout.quickaction_popup_vertical);
        }
        this.mOrientation = orientation;
        this.mChildPos = 0;
    }

    public ActionItem getActionItem(int index) {
        return (ActionItem) this.actionItems.get(index);
    }

    public void setOnActionItemClickListener(OnActionItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void addActionItem(ActionItem action) {
        addActionItem(action, 0, 0);
    }

    public void addActionItem(ActionItem action, int backgroundRes) {
        addActionItem(action, backgroundRes, 0);
    }

    public void addActionItem(ActionItem action, int backgroundRes, int textColorRes) {
        View actionView;
        this.actionItems.add(action);
        String title = action.getTitle();
        Drawable icon = action.getIcon();
        if (this.mOrientation == 0) {
            actionView = this.mInflater.inflate(R.layout.quickaction_item_horizontal, null);
        } else {
            actionView = this.mInflater.inflate(R.layout.quickaction_item_vertical, null);
        }
        TextView text = (TextView) actionView;
        if (backgroundRes != 0) {
            text.setBackgroundResource(backgroundRes);
        }
        if (textColorRes != 0) {
            try {
                text.setTextColor(getContext().getResources().getColorStateList(textColorRes));
            } catch (NotFoundException e) {
                Log.d(TAG, "resorting to constant color!");
                text.setTextColor(textColorRes);
            }
        }
        if (icon != null) {
            Drawable drawable = this.mOrientation == 1 ? icon : null;
            if (this.mOrientation != 0) {
                icon = null;
            }
            text.setCompoundDrawablesWithIntrinsicBounds(drawable, icon, null, null);
        }
        if (title != null) {
            text.setText(title);
        } else {
            text.setVisibility(8);
        }
        final int pos = this.mChildPos;
        final int actionId = action.getActionId();
        actionView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (QuickAction.this.mItemClickListener != null) {
                    QuickAction.this.mItemClickListener.onItemClick(QuickAction.this, pos, actionId);
                }
                if (!QuickAction.this.getActionItem(pos).isSticky()) {
                    QuickAction.this.mDidAction = true;
                    QuickAction.this.dismiss();
                }
            }
        });
        actionView.setOnKeyListener(this);
        actionView.setId(actionId);
        actionView.setFocusable(true);
        actionView.setClickable(true);
        if (this.mOrientation == 0 && this.mChildPos != 0) {
            View separator = this.mInflater.inflate(R.layout.quickaction_horiz_separator, null);
            separator.setLayoutParams(new LayoutParams(-2, -1));
            separator.setPadding(5, 0, 5, 0);
            this.mContentView.addView(separator, this.mInsertPos);
            this.mInsertPos++;
        }
        this.mContentView.addView(actionView, this.mInsertPos);
        this.mChildPos++;
        this.mInsertPos++;
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == 1) {
            return v.onKeyUp(keyCode, event);
        }
        if (keyCode != 82) {
            return v.onKeyDown(keyCode, event);
        }
        dismiss();
        return true;
    }

    public void onDismiss() {
        if (!this.mDidAction && this.mDismissListener != null) {
            this.mDismissListener.onDismiss();
        }
    }

    public boolean setActionItemAccessible(int id, boolean accessible) {
        View actionItem = this.mContentView.findViewById(id);
        if (actionItem == null) {
            return false;
        }
        actionItem.setFocusable(accessible);
        actionItem.setClickable(accessible);
        actionItem.setEnabled(accessible);
        return true;
    }
}
