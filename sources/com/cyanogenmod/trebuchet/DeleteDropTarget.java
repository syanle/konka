package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;
import com.cyanogenmod.trebuchet.DropTarget.DragObject;
import com.konka.ios7launcher.R;
import com.tencent.stat.common.StatConstants;

public class DeleteDropTarget extends ButtonDropTarget {
    private static int DELETE_ANIMATION_DURATION = 250;
    private static final int MODE_DELETE = 0;
    private static final int MODE_UNINSTALL = 1;
    private Drawable mCurrentDrawable;
    private final Handler mHandler;
    private int mHoverColor;
    private int mMode;
    private ColorStateList mOriginalTextColor;
    private Drawable mRemoveActiveDrawable;
    private Drawable mRemoveNormalDrawable;
    private final Runnable mShowUninstaller;
    private boolean mUninstall;
    private Drawable mUninstallActiveDrawable;
    private Drawable mUninstallNormalDrawable;

    public DeleteDropTarget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DeleteDropTarget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mMode = 0;
        this.mHoverColor = -65536;
        this.mHandler = new Handler();
        this.mShowUninstaller = new Runnable() {
            public void run() {
                DeleteDropTarget.this.performHapticFeedback(0);
                DeleteDropTarget.this.switchToUninstallTarget();
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mOriginalTextColor = getTextColors();
        Resources r = getResources();
        this.mHoverColor = r.getColor(R.color.delete_target_hover_tint);
        this.mHoverPaint.setColorFilter(new PorterDuffColorFilter(this.mHoverColor, Mode.SRC_ATOP));
        this.mUninstallNormalDrawable = r.getDrawable(R.drawable.ic_launcher_trashcan_normal_holo);
        this.mUninstallActiveDrawable = r.getDrawable(R.drawable.ic_launcher_trashcan_active_holo);
        this.mRemoveActiveDrawable = r.getDrawable(R.drawable.ic_launcher_clear_active_holo);
        this.mRemoveNormalDrawable = r.getDrawable(R.drawable.ic_launcher_clear_normal_holo);
        if (getResources().getConfiguration().orientation == 2 && !LauncherApplication.isScreenLarge()) {
            setText(StatConstants.MTA_COOPERATION_TAG);
        }
    }

    private boolean isAllAppsItem(DragSource source, Object info) {
        return isAllAppsApplication(source, info) || isAllAppsWidget(source, info);
    }

    private boolean isAllAppsApplication(DragSource source, Object info) {
        return (source instanceof AppsCustomizeView) && (info instanceof ApplicationInfo);
    }

    private boolean isAllAppsWidget(DragSource source, Object info) {
        return (source instanceof AppsCustomizeView) && (info instanceof PendingAddWidgetInfo);
    }

    private boolean isDragSourceWorkspaceOrFolder(DragSource source) {
        return (source instanceof Workspace) || (source instanceof Folder);
    }

    private boolean isWorkspaceOrFolderApplication(DragSource source, Object info) {
        return isDragSourceWorkspaceOrFolder(source) && (info instanceof ShortcutInfo);
    }

    private boolean isWorkspaceWidget(DragSource source, Object info) {
        return isDragSourceWorkspaceOrFolder(source) && (info instanceof LauncherAppWidgetInfo);
    }

    private boolean isWorkspaceFolder(DragSource source, Object info) {
        return (source instanceof Workspace) && (info instanceof FolderInfo);
    }

    public boolean acceptDrop(DragObject d) {
        boolean accepted = true;
        if (d.dragInfo instanceof ShortcutInfo) {
            accepted = false;
            if (!ShortcutInfo.isUninstallable(this.mContext.getPackageManager(), (ShortcutInfo) d.dragInfo)) {
                Toast.makeText(this.mContext, R.string.uninstall_system_app_text, 0).show();
            } else {
                this.mLauncher.startShortcutUninstallActivity((ShortcutInfo) d.dragInfo);
            }
        }
        return accepted;
    }

    public void onDragStart(DragSource source, Object info, int dragAction) {
        boolean isUninstall = false;
        if (isAllAppsApplication(source, info)) {
            if ((((ApplicationInfo) info).flags & 1) != 0) {
                isUninstall = true;
            }
        } else if (isWorkspaceOrFolderApplication(source, info)) {
            ResolveInfo resolveInfo = getContext().getPackageManager().resolveActivity(((ShortcutInfo) info).intent, 0);
            if (resolveInfo != null && (resolveInfo.activityInfo.applicationInfo.flags & 1) == 0) {
                isUninstall = true;
            }
            setCompoundDrawablesWithIntrinsicBounds(this.mUninstallNormalDrawable, null, null, null);
            this.mCurrentDrawable = getCompoundDrawables()[0];
            this.mMode = 1;
            if (getText().length() > 0) {
                setText(R.string.delete_target_uninstall_label);
            }
        } else if (isWorkspaceFolder(source, info)) {
            setCompoundDrawablesWithIntrinsicBounds(this.mRemoveNormalDrawable, null, null, null);
            this.mCurrentDrawable = getCompoundDrawables()[0];
            this.mMode = 0;
            if (getText().length() > 0) {
                setText(R.string.delete_target_label);
            }
        }
        this.mUninstall = isUninstall;
        this.mActive = true;
        setTextColor(this.mOriginalTextColor);
        ((ViewGroup) getParent()).setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void switchToUninstallTarget() {
        if (this.mUninstall) {
            this.mMode = 1;
            if (getText().length() > 0) {
                setText(R.string.delete_target_uninstall_label);
            }
            setCompoundDrawablesWithIntrinsicBounds(this.mUninstallActiveDrawable, null, null, null);
            this.mCurrentDrawable = getCompoundDrawables()[0];
        }
    }

    public void onDragEnd() {
        super.onDragEnd();
        this.mActive = false;
    }

    public void onDragEnter(DragObject d) {
        super.onDragEnter(d);
        if (this.mMode == 0) {
            setCompoundDrawablesWithIntrinsicBounds(this.mRemoveActiveDrawable, null, null, null);
            this.mCurrentDrawable = getCompoundDrawables()[0];
        } else if (this.mMode == 1) {
            setCompoundDrawablesWithIntrinsicBounds(this.mUninstallActiveDrawable, null, null, null);
            this.mCurrentDrawable = getCompoundDrawables()[0];
        }
        setTextColor(this.mHoverColor);
    }

    public void onDragExit(DragObject d) {
        super.onDragExit(d);
        if (!d.dragComplete) {
            if (this.mMode == 0) {
                if (getText().length() > 0) {
                    if (isAllAppsItem(d.dragSource, d.dragInfo)) {
                        setText(R.string.cancel_target_label);
                    } else {
                        setText(R.string.delete_target_label);
                    }
                }
                setCompoundDrawablesWithIntrinsicBounds(this.mRemoveNormalDrawable, null, null, null);
                this.mCurrentDrawable = getCompoundDrawables()[0];
            } else if (this.mMode == 1) {
                setCompoundDrawablesWithIntrinsicBounds(this.mUninstallNormalDrawable, null, null, null);
                this.mCurrentDrawable = getCompoundDrawables()[0];
            }
            setTextColor(this.mOriginalTextColor);
        }
    }

    private void animateToTrashAndCompleteDrop(DragObject d) {
        DragLayer dragLayer = this.mLauncher.getDragLayer();
        Rect from = new Rect();
        Rect to = new Rect();
        dragLayer.getViewRectRelativeToSelf(d.dragView, from);
        dragLayer.getViewRectRelativeToSelf(this, to);
        int width = this.mCurrentDrawable.getIntrinsicWidth();
        int height = this.mCurrentDrawable.getIntrinsicHeight();
        to.set(to.left + getPaddingLeft(), to.top + getPaddingTop(), to.left + getPaddingLeft() + width, to.bottom);
        to.offset((-(d.dragView.getMeasuredWidth() - width)) / 2, (-(d.dragView.getMeasuredHeight() - height)) / 2);
        this.mSearchDropTargetBar.deferOnDragEnd();
        final DragObject dragObject = d;
        dragLayer.animateView(d.dragView, from, to, 0.1f, 0.1f, DELETE_ANIMATION_DURATION, new DecelerateInterpolator(2.0f), new DecelerateInterpolator(1.5f), new Runnable() {
            public void run() {
                DeleteDropTarget.this.mSearchDropTargetBar.onDragEnd();
                DeleteDropTarget.this.mLauncher.exitSpringLoadedDragMode();
                DeleteDropTarget.this.completeDrop(dragObject);
            }
        }, false);
    }

    /* access modifiers changed from: private */
    public void completeDrop(DragObject d) {
        ItemInfo item = (ItemInfo) d.dragInfo;
        switch (this.mMode) {
            case 0:
                if (isWorkspaceOrFolderApplication(d.dragSource, item)) {
                    LauncherModel.deleteItemFromDatabase(this.mLauncher, item);
                    return;
                } else if (isWorkspaceFolder(d.dragSource, d.dragInfo)) {
                    FolderInfo folderInfo = (FolderInfo) item;
                    this.mLauncher.removeFolder(folderInfo);
                    LauncherModel.deleteFolderContentsFromDatabase(this.mLauncher, folderInfo);
                    return;
                } else if (isWorkspaceWidget(d.dragSource, item)) {
                    this.mLauncher.removeAppWidget((LauncherAppWidgetInfo) item);
                    LauncherModel.deleteItemFromDatabase(this.mLauncher, item);
                    final LauncherAppWidgetInfo launcherAppWidgetInfo = (LauncherAppWidgetInfo) item;
                    final LauncherAppWidgetHost appWidgetHost = this.mLauncher.getAppWidgetHost();
                    if (appWidgetHost != null) {
                        new Thread("deleteAppWidgetId") {
                            public void run() {
                                appWidgetHost.deleteAppWidgetId(launcherAppWidgetInfo.appWidgetId);
                            }
                        }.start();
                        return;
                    }
                    return;
                } else {
                    return;
                }
            case 1:
                if (isAllAppsApplication(d.dragSource, item)) {
                    this.mLauncher.startApplicationUninstallActivity((ApplicationInfo) item);
                    return;
                } else if (isWorkspaceOrFolderApplication(d.dragSource, item)) {
                    this.mLauncher.startShortcutUninstallActivity((ShortcutInfo) item);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    public void onDrop(DragObject d) {
        animateToTrashAndCompleteDrop(d);
    }
}
