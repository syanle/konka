package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cyanogenmod.trebuchet.CellLayout.LayoutParams;
import com.cyanogenmod.trebuchet.DropTarget.DragObject;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;
import java.util.ArrayList;
import java.util.Iterator;

public class FolderIcon extends LinearLayout implements FolderListener {
    private static final int CONSUMPTION_ANIMATION_DURATION = 100;
    private static final boolean DEBUG = false;
    private static final int DROP_IN_ANIMATION_DURATION = 400;
    private static final int INITIAL_ITEM_ANIMATION_DURATION = 350;
    private static final float INNER_RING_GROWTH_FACTOR = 0.15f;
    private static final float OUTER_RING_GROWTH_FACTOR = 0.3f;
    private static final String TAG = FolderIcon.class.getSimpleName();
    public static Drawable sSharedFolderLeaveBehind = null;
    /* access modifiers changed from: private */
    public static boolean sStaticValuesDirty = true;
    /* access modifiers changed from: private */
    public PreviewItemDrawingParams mAnimParams;
    boolean mAnimating;
    Folder mFolder;
    private BubbleTextView mFolderName;
    FolderRingAnimator mFolderRingAnimator;
    FolderInfo mInfo;
    private Launcher mLauncher;
    private PreviewItemDrawingParams mParams;
    public ImageView mPreviewBackground;
    PreviewComposer mPreviewComposer;

    public static class FolderRingAnimator {
        public static int sPreviewPadding = -1;
        public static int sPreviewSize = -1;
        public static Drawable sSharedInnerRingDrawable = null;
        public static Drawable sSharedOuterRingDrawable = null;
        private ValueAnimator mAcceptAnimator;
        /* access modifiers changed from: private */
        public CellLayout mCellLayout;
        public int mCellX;
        public int mCellY;
        public FolderIcon mFolderIcon = null;
        public float mInnerRingSize;
        private ValueAnimator mNeutralAnimator;
        public float mOuterRingSize;

        public FolderRingAnimator(Launcher launcher, FolderIcon folderIcon) {
            this.mFolderIcon = folderIcon;
            Resources res = launcher.getResources();
            if (FolderIcon.sStaticValuesDirty) {
                sPreviewSize = res.getDimensionPixelSize(R.dimen.folder_preview_size);
                sPreviewPadding = res.getDimensionPixelSize(R.dimen.folder_preview_padding);
                sSharedOuterRingDrawable = res.getDrawable(R.drawable.portal_ring_outer_holo);
                sSharedInnerRingDrawable = res.getDrawable(R.drawable.portal_ring_inner_holo);
                FolderIcon.sSharedFolderLeaveBehind = res.getDrawable(R.drawable.portal_ring_rest);
                FolderIcon.sStaticValuesDirty = false;
            }
        }

        public void animateToAcceptState() {
            if (this.mNeutralAnimator != null) {
                this.mNeutralAnimator.cancel();
            }
            this.mAcceptAnimator = ValueAnimator.ofFloat(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f});
            this.mAcceptAnimator.setDuration(100);
            this.mAcceptAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = ((Float) animation.getAnimatedValue()).floatValue();
                    FolderRingAnimator.this.mOuterRingSize = ((FolderIcon.OUTER_RING_GROWTH_FACTOR * percent) + 1.0f) * ((float) FolderRingAnimator.sPreviewSize);
                    FolderRingAnimator.this.mInnerRingSize = ((FolderIcon.INNER_RING_GROWTH_FACTOR * percent) + 1.0f) * ((float) FolderRingAnimator.sPreviewSize);
                    if (FolderRingAnimator.this.mCellLayout != null) {
                        FolderRingAnimator.this.mCellLayout.invalidate();
                    }
                }
            });
            this.mAcceptAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animation) {
                    if (FolderRingAnimator.this.mFolderIcon != null) {
                        FolderRingAnimator.this.mFolderIcon.mPreviewBackground.setVisibility(4);
                    }
                }
            });
            this.mAcceptAnimator.start();
        }

        public void animateToNaturalState() {
            if (this.mAcceptAnimator != null) {
                this.mAcceptAnimator.cancel();
            }
            this.mNeutralAnimator = ValueAnimator.ofFloat(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f});
            this.mNeutralAnimator.setDuration(100);
            this.mNeutralAnimator.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    float percent = ((Float) animation.getAnimatedValue()).floatValue();
                    FolderRingAnimator.this.mOuterRingSize = (((1.0f - percent) * FolderIcon.OUTER_RING_GROWTH_FACTOR) + 1.0f) * ((float) FolderRingAnimator.sPreviewSize);
                    FolderRingAnimator.this.mInnerRingSize = (((1.0f - percent) * FolderIcon.INNER_RING_GROWTH_FACTOR) + 1.0f) * ((float) FolderRingAnimator.sPreviewSize);
                    if (FolderRingAnimator.this.mCellLayout != null) {
                        FolderRingAnimator.this.mCellLayout.invalidate();
                    }
                }
            });
            this.mNeutralAnimator.addListener(new AnimatorListenerAdapter() {
                public void onAnimationEnd(Animator animation) {
                    if (FolderRingAnimator.this.mCellLayout != null) {
                        FolderRingAnimator.this.mCellLayout.hideFolderAccept(FolderRingAnimator.this);
                    }
                    if (FolderRingAnimator.this.mFolderIcon != null) {
                        FolderRingAnimator.this.mFolderIcon.mPreviewBackground.setVisibility(0);
                    }
                }
            });
            this.mNeutralAnimator.start();
        }

        public void getCell(int[] loc) {
            loc[0] = this.mCellX;
            loc[1] = this.mCellY;
        }

        public void setCell(int x, int y) {
            this.mCellX = x;
            this.mCellY = y;
        }

        public void setCellLayout(CellLayout layout) {
            this.mCellLayout = layout;
        }

        public float getOuterRingSize() {
            return this.mOuterRingSize;
        }

        public float getInnerRingSize() {
            return this.mInnerRingSize;
        }
    }

    public static abstract class PreviewComposer {
        public static final int NUM_ITEMS_IN_PREVIEW = 9;
        public int mAvailableSpaceInPreview;
        public int mIntrinsicIconSize;
        public int mPreviewOffsetX;
        public int mPreviewOffsetY;
        protected int mTotalWidth = -1;

        /* access modifiers changed from: protected */
        public abstract void computePreviewDrawingParamsInner(int i, int i2);

        /* access modifiers changed from: protected */
        public abstract PreviewItemDrawingParams computePreviewItemDrawingParams(int i, PreviewItemDrawingParams previewItemDrawingParams);

        public float getFinalAlpha(int index) {
            return 1.0f;
        }

        public void computePreviewDrawingParams(Drawable d, View target) {
            computePreviewDrawingParams(d.getIntrinsicWidth(), target.getMeasuredWidth());
        }

        public void computePreviewDrawingParams(int drawableSize, int totalSize) {
            if (this.mIntrinsicIconSize != drawableSize || this.mTotalWidth != totalSize) {
                computePreviewDrawingParamsInner(drawableSize, totalSize);
            }
        }
    }

    static class PreviewItemDrawingParams {
        Drawable drawable;
        int overlayAlpha;
        float scale;
        float transX;
        float transY;

        PreviewItemDrawingParams(float transX2, float transY2, float scale2, int overlayAlpha2) {
            this.transX = transX2;
            this.transY = transY2;
            this.scale = scale2;
            this.overlayAlpha = overlayAlpha2;
        }
    }

    public static class StackedPreviewComposer extends PreviewComposer {
        private static final float PERSPECTIVE_SCALE_FACTOR = 0.35f;
        private static final float PERSPECTIVE_SHIFT_FACTOR = 0.24f;
        private float mBaselineIconScale;
        private int mBaselineIconSize;
        private float mMaxPerspectiveShift;

        public float getFinalAlpha(int index) {
            if (index < 9) {
                return 0.5f;
            }
            return FlyingIcon.ANGULAR_VMIN;
        }

        public void computePreviewDrawingParamsInner(int drawableSize, int totalSize) {
            if (this.mIntrinsicIconSize != drawableSize || this.mTotalWidth != totalSize) {
                this.mIntrinsicIconSize = drawableSize;
                this.mTotalWidth = totalSize;
                int previewSize = FolderRingAnimator.sPreviewSize;
                int previewPadding = FolderRingAnimator.sPreviewPadding;
                this.mAvailableSpaceInPreview = previewSize - (previewPadding * 2);
                this.mBaselineIconScale = (1.0f * ((float) ((int) (((float) (this.mAvailableSpaceInPreview / 2)) * 1.8f)))) / ((float) ((int) (((float) this.mIntrinsicIconSize) * 1.24f)));
                this.mBaselineIconSize = (int) (((float) this.mIntrinsicIconSize) * this.mBaselineIconScale);
                this.mMaxPerspectiveShift = ((float) this.mBaselineIconSize) * PERSPECTIVE_SHIFT_FACTOR;
                this.mPreviewOffsetX = (this.mTotalWidth - this.mAvailableSpaceInPreview) / 2;
                this.mPreviewOffsetY = previewPadding;
            }
        }

        /* access modifiers changed from: protected */
        public PreviewItemDrawingParams computePreviewItemDrawingParams(int index, PreviewItemDrawingParams params) {
            float r = (((float) ((9 - Math.min(9, index)) - 1)) * 1.0f) / 8.0f;
            float scale = 1.0f - (PERSPECTIVE_SCALE_FACTOR * (1.0f - r));
            float offset = (1.0f - r) * this.mMaxPerspectiveShift;
            float scaleOffsetCorrection = (1.0f - scale) * ((float) this.mBaselineIconSize);
            float transY = ((float) this.mAvailableSpaceInPreview) - ((offset + (scale * ((float) this.mBaselineIconSize))) + scaleOffsetCorrection);
            float transX = offset + scaleOffsetCorrection;
            float totalScale = this.mBaselineIconScale * scale;
            int overlayAlpha = (int) (80.0f * (1.0f - r));
            if (params == null) {
                return new PreviewItemDrawingParams(transX, transY, totalScale, overlayAlpha);
            }
            params.transX = transX;
            params.transY = transY;
            params.scale = totalScale;
            params.overlayAlpha = overlayAlpha;
            return params;
        }
    }

    public static class TiledPreviewComposer extends PreviewComposer {
        private static final int PREVIEW_CELL_COUNT = ((int) Math.sqrt(9.0d));
        private static final int PREVIEW_ITEM_OVERLAY_ALPHA = 10;
        private static final float PREVIEW_ITEM_SCALE = 0.2f;
        private final int mHeightGap;
        private final int mPaddingLeft;
        private final int mPaddingTop;
        private final int mWidthGap;

        public TiledPreviewComposer(Context context) {
            Resources res = context.getResources();
            this.mPaddingLeft = res.getDimensionPixelSize(R.dimen.folder_preview_item_padding_left);
            this.mPaddingTop = res.getDimensionPixelSize(R.dimen.folder_preview_item_padding_top);
            this.mWidthGap = res.getDimensionPixelSize(R.dimen.folder_preview_item_width_gap);
            this.mHeightGap = res.getDimensionPixelSize(R.dimen.folder_preview_item_height_gap);
        }

        /* access modifiers changed from: protected */
        public void computePreviewDrawingParamsInner(int drawableSize, int totalSize) {
            if (this.mIntrinsicIconSize != drawableSize || this.mTotalWidth != totalSize) {
                this.mIntrinsicIconSize = drawableSize;
                this.mTotalWidth = totalSize;
                int previewPadding = FolderRingAnimator.sPreviewPadding;
                this.mAvailableSpaceInPreview = this.mIntrinsicIconSize;
                this.mPreviewOffsetX = (this.mTotalWidth - this.mAvailableSpaceInPreview) / 2;
                this.mPreviewOffsetY = previewPadding;
            }
        }

        /* access modifiers changed from: protected */
        public PreviewItemDrawingParams computePreviewItemDrawingParams(int index, PreviewItemDrawingParams params) {
            int index2 = Math.min(9, index);
            int previewItemSize = (int) (((float) this.mIntrinsicIconSize) * PREVIEW_ITEM_SCALE);
            float transY = (float) (this.mPaddingTop + ((this.mHeightGap + previewItemSize) * (index2 / PREVIEW_CELL_COUNT)));
            float transX = (float) (this.mPaddingLeft + ((this.mWidthGap + previewItemSize) * (index2 % PREVIEW_CELL_COUNT)));
            if (params == null) {
                return new PreviewItemDrawingParams(transX, transY, PREVIEW_ITEM_SCALE, 10);
            }
            params.transX = transX;
            params.transY = transY;
            params.scale = PREVIEW_ITEM_SCALE;
            params.overlayAlpha = 10;
            return params;
        }
    }

    public FolderIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mFolderRingAnimator = null;
        this.mAnimating = false;
        this.mParams = new PreviewItemDrawingParams(FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, 0);
        this.mAnimParams = new PreviewItemDrawingParams(FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, 0);
        this.mPreviewComposer = new TiledPreviewComposer(context);
    }

    public FolderIcon(Context context) {
        this(context, null);
    }

    public boolean isDropEnabled() {
        return !((Workspace) ((ViewGroup) ((ViewGroup) getParent()).getParent()).getParent()).isSmall();
    }

    static FolderIcon fromXml(int resId, Launcher launcher, ViewGroup group, FolderInfo folderInfo, IconCache iconCache) {
        FolderIcon icon = (FolderIcon) LayoutInflater.from(launcher).inflate(resId, group, false);
        icon.mFolderName = (BubbleTextView) icon.findViewById(R.id.folder_icon_name);
        icon.mFolderName.setFocusable(false);
        icon.mFolderName.setText(folderInfo.title);
        icon.mPreviewBackground = (ImageView) icon.findViewById(R.id.preview_background);
        icon.setTag(folderInfo);
        icon.setOnClickListener(launcher);
        icon.mInfo = folderInfo;
        icon.mLauncher = launcher;
        icon.setContentDescription(String.format(launcher.getString(R.string.folder_name_format), new Object[]{folderInfo.title}));
        Folder folder = Folder.fromXml(launcher);
        folder.setDragController(launcher.getDragController());
        folder.setFolderIcon(icon);
        folder.bind(folderInfo);
        icon.mFolder = folder;
        if (icon.mInfo.contents.size() == 0) {
            icon.mPreviewBackground.setImageResource(R.drawable.com_icon_file_uns);
        } else {
            icon.mPreviewBackground.setImageResource(R.drawable.com_icon_file_s);
        }
        icon.mFolderRingAnimator = new FolderRingAnimator(launcher, icon);
        folderInfo.addListener(icon);
        return icon;
    }

    /* access modifiers changed from: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        this.mFolderName.setSelected(gainFocus);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        sStaticValuesDirty = true;
        return super.onSaveInstanceState();
    }

    private boolean willAcceptItem(ItemInfo item) {
        int itemType = item.itemType;
        if (itemType == 2) {
            boolean canAdd = this.mFolder.checkFolderAdd(((FolderInfo) item).contents.size());
            if (!canAdd && item != this.mInfo) {
                Toast.makeText(getContext(), getContext().getResources().getString(R.string.folder_noroom), 0).show();
            }
            if (!canAdd || item == this.mInfo || this.mInfo.opened) {
                return false;
            }
            return true;
        } else if ((itemType == 0 || itemType == 1 || itemType == 2) && this.mFolder.checkFolderAdd(1) && item != this.mInfo && !this.mInfo.opened) {
            return true;
        } else {
            return false;
        }
    }

    public boolean acceptDrop(Object dragInfo) {
        return willAcceptItem((ItemInfo) dragInfo);
    }

    public void addItem(ShortcutInfo item) {
        this.mInfo.add(item);
        LauncherModel.addOrMoveItemInDatabase(this.mLauncher, item, this.mInfo.id, 0, item.cellX, item.cellY);
    }

    public void onDragEnter(Object dragInfo) {
        if (willAcceptItem((ItemInfo) dragInfo)) {
            LayoutParams lp = (LayoutParams) getLayoutParams();
            CellLayout layout = (CellLayout) getParent().getParent();
            this.mFolderRingAnimator.setCell(lp.cellX, lp.cellY);
            this.mFolderRingAnimator.setCellLayout(layout);
            this.mFolderRingAnimator.animateToAcceptState();
            layout.showFolderAccept(this.mFolderRingAnimator);
        }
    }

    public void onDragOver(Object dragInfo) {
    }

    public void performCreateAnimation(final ShortcutInfo destInfo, View destView, ShortcutInfo srcInfo, View srcView, Rect dstRect, float scaleRelativeToDragLayer, Runnable postAnimationRunnable) {
        Drawable animateDrawable = ((TextView) destView).getCompoundDrawables()[1];
        this.mPreviewComposer.computePreviewDrawingParams(animateDrawable.getIntrinsicWidth(), destView.getMeasuredWidth());
        onDrop(srcInfo, srcView, dstRect, scaleRelativeToDragLayer, 1, postAnimationRunnable);
        animateFirstItem(animateDrawable, INITIAL_ITEM_ANIMATION_DURATION);
        postDelayed(new Runnable() {
            public void run() {
                FolderIcon.this.addItem(destInfo);
            }
        }, 350);
    }

    public void onDragExit(Object dragInfo) {
        this.mFolderRingAnimator.animateToNaturalState();
    }

    private void onDrop(ShortcutInfo item, View animateView, Rect finalRect, float scaleRelativeToDragLayer, int index, Runnable postAnimationRunnable) {
        item.cellX = -1;
        item.cellY = -1;
        if (animateView != null) {
            DragLayer dragLayer = this.mLauncher.getDragLayer();
            Rect from = new Rect();
            dragLayer.getViewRectRelativeToSelf(animateView, from);
            Rect to = finalRect;
            if (to == null) {
                to = new Rect();
                Workspace workspace = this.mLauncher.getWorkspace();
                workspace.setFinalTransitionTransform((CellLayout) getParent().getParent());
                float scaleX = getScaleX();
                float scaleY = getScaleY();
                setScaleX(1.0f);
                setScaleY(1.0f);
                scaleRelativeToDragLayer = dragLayer.getDescendantRectRelativeToSelf(this, to);
                setScaleX(scaleX);
                setScaleY(scaleY);
                workspace.resetTransitionTransform((CellLayout) getParent().getParent());
            }
            int[] center = new int[2];
            float scale = getLocalCenterForIndex(index, center);
            center[0] = Math.round(((float) center[0]) * scaleRelativeToDragLayer);
            center[1] = Math.round(((float) center[1]) * scaleRelativeToDragLayer);
            to.offset(center[0] - (animateView.getMeasuredWidth() / 2), center[1] - (animateView.getMeasuredHeight() / 2));
            dragLayer.animateView(animateView, from, to, this.mPreviewComposer.getFinalAlpha(index), scale * scaleRelativeToDragLayer, 400, new DecelerateInterpolator(2.0f), new AccelerateInterpolator(2.0f), postAnimationRunnable, false);
            final ShortcutInfo shortcutInfo = item;
            postDelayed(new Runnable() {
                public void run() {
                    FolderIcon.this.addItem(shortcutInfo);
                }
            }, 400);
            return;
        }
        addItem(item);
    }

    public void onDrop(DragObject d) {
        ShortcutInfo item;
        if (d.dragInfo instanceof ApplicationInfo) {
            item = ((ApplicationInfo) d.dragInfo).makeShortcut();
        } else if (d.dragInfo instanceof FolderInfo) {
            FolderInfo folder = (FolderInfo) d.dragInfo;
            this.mFolder.notifyDrop();
            Iterator it = folder.contents.iterator();
            while (it.hasNext()) {
                onDrop((ShortcutInfo) it.next(), d.dragView, null, 1.0f, this.mInfo.contents.size(), d.postAnimationRunnable);
            }
            this.mLauncher.removeFolder(folder);
            LauncherModel.deleteItemFromDatabase(this.mLauncher, folder);
            return;
        } else {
            item = (ShortcutInfo) d.dragInfo;
        }
        this.mFolder.notifyDrop();
        onDrop(item, d.dragView, null, 1.0f, this.mInfo.contents.size(), d.postAnimationRunnable);
    }

    public DropTarget getDropTargetDelegate(DragObject d) {
        return null;
    }

    private float getLocalCenterForIndex(int index, int[] center) {
        this.mParams = this.mPreviewComposer.computePreviewItemDrawingParams(index, this.mParams);
        this.mParams.transX += (float) this.mPreviewComposer.mPreviewOffsetX;
        this.mParams.transY += (float) this.mPreviewComposer.mPreviewOffsetY;
        float offsetY = this.mParams.transY + ((this.mParams.scale * ((float) this.mPreviewComposer.mIntrinsicIconSize)) / 2.0f);
        center[0] = Math.round(this.mParams.transX + ((this.mParams.scale * ((float) this.mPreviewComposer.mIntrinsicIconSize)) / 2.0f));
        center[1] = Math.round(offsetY);
        return this.mParams.scale;
    }

    private void drawPreviewItem(Canvas canvas, PreviewItemDrawingParams params) {
        canvas.save();
        canvas.translate(params.transX + ((float) this.mPreviewComposer.mPreviewOffsetX), params.transY + ((float) this.mPreviewComposer.mPreviewOffsetY));
        canvas.scale(params.scale, params.scale);
        Drawable d = params.drawable;
        if (d != null) {
            d.setBounds(0, 0, this.mPreviewComposer.mIntrinsicIconSize, this.mPreviewComposer.mIntrinsicIconSize);
            d.setFilterBitmap(true);
            d.setColorFilter(Color.argb(params.overlayAlpha, 0, 0, 0), Mode.SRC_ATOP);
            d.draw(canvas);
            d.clearColorFilter();
            d.setFilterBitmap(false);
        }
        canvas.restore();
    }

    /* access modifiers changed from: protected */
    public void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (this.mFolder != null) {
            if (this.mFolder.getItemCount() != 0 || this.mAnimating) {
                ArrayList<View> items = this.mFolder.getItemsInReadingOrder(false);
                if (this.mAnimating) {
                    this.mPreviewComposer.computePreviewDrawingParams(this.mAnimParams.drawable, (View) this);
                } else {
                    this.mPreviewComposer.computePreviewDrawingParams(((TextView) items.get(0)).getCompoundDrawables()[1], (View) this);
                }
                int nItemsInPreview = Math.min(items.size(), 9);
                if (!this.mAnimating) {
                    for (int i = nItemsInPreview - 1; i >= 0; i--) {
                        Drawable d = ((TextView) items.get(i)).getCompoundDrawables()[1];
                        this.mParams = this.mPreviewComposer.computePreviewItemDrawingParams(i, this.mParams);
                        this.mParams.drawable = d;
                        drawPreviewItem(canvas, this.mParams);
                    }
                    return;
                }
                drawPreviewItem(canvas, this.mAnimParams);
            }
        }
    }

    private void animateFirstItem(Drawable d, int duration) {
        this.mPreviewComposer.computePreviewDrawingParams(d, (View) this);
        final PreviewItemDrawingParams finalParams = this.mPreviewComposer.computePreviewItemDrawingParams(0, null);
        final float transX0 = (float) ((this.mPreviewComposer.mAvailableSpaceInPreview - d.getIntrinsicWidth()) / 2);
        final float transY0 = (float) ((this.mPreviewComposer.mAvailableSpaceInPreview - d.getIntrinsicHeight()) / 2);
        this.mAnimParams.drawable = d;
        ValueAnimator va = ValueAnimator.ofFloat(new float[]{FlyingIcon.ANGULAR_VMIN, 1.0f});
        va.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = ((Float) animation.getAnimatedValue()).floatValue();
                FolderIcon.this.mAnimParams.transX = transX0 + ((finalParams.transX - transX0) * progress);
                FolderIcon.this.mAnimParams.transY = transY0 + ((finalParams.transY - transY0) * progress);
                FolderIcon.this.mAnimParams.scale = ((finalParams.scale - 1.0f) * progress) + 1.0f;
                FolderIcon.this.invalidate();
            }
        });
        va.addListener(new AnimatorListenerAdapter() {
            public void onAnimationStart(Animator animation) {
                FolderIcon.this.mAnimating = true;
            }

            public void onAnimationEnd(Animator animation) {
                FolderIcon.this.mAnimating = false;
            }
        });
        va.setDuration((long) duration);
        va.start();
    }

    public void setTextVisible(boolean visible) {
        if (visible) {
            this.mFolderName.setVisibility(0);
        } else {
            this.mFolderName.setVisibility(4);
        }
    }

    public boolean getTextVisible() {
        return this.mFolderName.getVisibility() == 0;
    }

    public void onItemsChanged() {
        if (this.mInfo.contents.size() == 0) {
            this.mPreviewBackground.setImageResource(R.drawable.com_icon_file_uns);
        } else {
            this.mPreviewBackground.setImageResource(R.drawable.com_icon_file_s);
        }
        invalidate();
        requestLayout();
    }

    public void onAdd(ShortcutInfo item) {
        invalidate();
        requestLayout();
    }

    public void onRemove(ShortcutInfo item) {
        invalidate();
        requestLayout();
    }

    public void onTitleChanged(CharSequence title) {
        this.mFolderName.setText(title.toString());
        setContentDescription(String.format(this.mContext.getString(R.string.folder_name_format), new Object[]{title}));
    }
}
