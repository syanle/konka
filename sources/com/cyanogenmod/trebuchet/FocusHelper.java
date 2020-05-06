package com.cyanogenmod.trebuchet;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.util.Log;
import android.util.Slog;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import com.cyanogenmod.trebuchet.CellLayout.LayoutParams;
import com.cyanogenmod.trebuchet.RocketLauncher.Board;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;
import com.konka.kkinterface.tv.CommonDesk;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FocusHelper {
    private static final boolean DEBUG = true;
    private static final String TAG = "focushelper";
    private static AnimatorSet mAnimatorSet = new AnimatorSet();
    private static TranslateAnimation mTranslateAnimation = new TranslateAnimation(FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN, FlyingIcon.ANGULAR_VMIN);

    private static TabHost findTabHostParent(View v) {
        ViewParent p = v.getParent();
        while (p != null && !(p instanceof TabHost)) {
            p = p.getParent();
        }
        return (TabHost) p;
    }

    static boolean handleDragViewKeyEvent(View v, int keyCode, KeyEvent e) {
        boolean handleKeyEvent = true;
        Log.d(TAG, "###handleDragViewKeyEvent o");
        if (e.getAction() == 1) {
            handleKeyEvent = false;
        }
        switch (keyCode) {
            case 19:
                if (handleKeyEvent) {
                }
                return true;
            case Board.NUM_ICONS /*20*/:
                if (handleKeyEvent) {
                }
                return true;
            case 21:
                if (handleKeyEvent) {
                }
                return true;
            case 22:
                if (handleKeyEvent) {
                }
                return true;
            default:
                return false;
        }
    }

    static boolean handleAppsCustomizeTabKeyEvent(View v, int keyCode, KeyEvent e) {
        boolean handleKeyEvent = true;
        TabHost tabHost = findTabHostParent(v);
        ViewGroup contents = (ViewGroup) tabHost.findViewById(16908305);
        View appLatest = tabHost.findViewById(R.id.applist_latest);
        View appShop = tabHost.findViewById(R.id.market_button);
        if (e.getAction() == 1) {
            handleKeyEvent = false;
        }
        switch (keyCode) {
            case 19:
                if (!handleKeyEvent) {
                    return false;
                }
                if (v == appLatest || v == appShop) {
                    return true;
                }
                return false;
            case Board.NUM_ICONS /*20*/:
                if (!handleKeyEvent) {
                    return false;
                }
                if (v != appLatest && v != appShop) {
                    return false;
                }
                contents.requestFocus();
                return true;
            case 21:
                if (!handleKeyEvent) {
                    return false;
                }
                if (v == appShop && appLatest.getVisibility() == 0) {
                    appLatest.requestFocus();
                }
                return true;
            case 22:
                if (!handleKeyEvent) {
                    return false;
                }
                if (v == appLatest && appShop.getVisibility() == 0) {
                    appShop.requestFocus();
                }
                return true;
            default:
                return false;
        }
    }

    private static ViewGroup getAppsCustomizePage(ViewGroup container, int index) {
        ViewGroup page = (ViewGroup) ((PagedView) container).getPageAt(index);
        if (page instanceof PagedViewCellLayout) {
            return (ViewGroup) page.getChildAt(0);
        }
        return page;
    }

    static boolean handlePagedViewGridLayoutWidgetKeyEvent(PagedViewWidget w, int keyCode, KeyEvent e) {
        PagedViewGridLayout parent = (PagedViewGridLayout) w.getParent();
        PagedView container = (PagedView) parent.getParent();
        TabHost tabHost = findTabHostParent(container);
        int widgetIndex = parent.indexOfChild(w);
        int widgetCount = parent.getChildCount();
        int pageIndex = container.indexToPage(container.indexOfChild(parent));
        int pageCount = container.getChildCount();
        int cellCountX = parent.getCellCountX();
        int cellCountY = parent.getCellCountY();
        int x = widgetIndex % cellCountX;
        int y = widgetIndex / cellCountX;
        boolean handleKeyEvent = e.getAction() != 1;
        View child = null;
        switch (keyCode) {
            case 19:
                if (handleKeyEvent) {
                    if (y > 0) {
                        View child2 = parent.getChildAt(((y - 1) * cellCountX) + x);
                        if (child2 != null) {
                            child2.requestFocus();
                        }
                    } else {
                        tabHost.findViewById(R.id.applist_latest).requestFocus();
                    }
                }
                return true;
            case Board.NUM_ICONS /*20*/:
                if (handleKeyEvent && y < cellCountY - 1) {
                    View child3 = parent.getChildAt(Math.min(widgetCount - 1, ((y + 1) * cellCountX) + x));
                    if (child3 != null) {
                        child3.requestFocus();
                    }
                }
                return true;
            case 21:
                if (handleKeyEvent) {
                    if (widgetIndex > 0) {
                        parent.getChildAt(widgetIndex - 1).requestFocus();
                    } else if (pageIndex > 0) {
                        ViewGroup newParent = getAppsCustomizePage(container, pageIndex - 1);
                        if (newParent != null) {
                            View child4 = newParent.getChildAt(newParent.getChildCount() - 1);
                            if (child4 != null) {
                                child4.requestFocus();
                            }
                        }
                    }
                }
                return true;
            case 22:
                if (handleKeyEvent) {
                    if (widgetIndex < widgetCount - 1) {
                        parent.getChildAt(widgetIndex + 1).requestFocus();
                    } else if (pageIndex < pageCount - 1) {
                        ViewGroup newParent2 = getAppsCustomizePage(container, pageIndex + 1);
                        if (newParent2 != null) {
                            View child5 = newParent2.getChildAt(0);
                            if (child5 != null) {
                                child5.requestFocus();
                            }
                        }
                    }
                }
                return true;
            case 23:
            case CommonDesk.Cmd_SignalLock /*66*/:
                if (handleKeyEvent) {
                    ((OnClickListener) container).onClick(w);
                }
                return true;
            case 92:
                if (handleKeyEvent) {
                    if (pageIndex > 0) {
                        ViewGroup newParent3 = getAppsCustomizePage(container, pageIndex - 1);
                        if (newParent3 != null) {
                            child = newParent3.getChildAt(0);
                        }
                    } else {
                        child = parent.getChildAt(0);
                    }
                    if (child != null) {
                        child.requestFocus();
                    }
                }
                return true;
            case 93:
                if (handleKeyEvent) {
                    if (pageIndex < pageCount - 1) {
                        ViewGroup newParent4 = getAppsCustomizePage(container, pageIndex + 1);
                        if (newParent4 != null) {
                            child = newParent4.getChildAt(0);
                        }
                    } else {
                        child = parent.getChildAt(widgetCount - 1);
                    }
                    if (child != null) {
                        child.requestFocus();
                    }
                }
                return true;
            case 122:
                if (handleKeyEvent) {
                    View child6 = parent.getChildAt(0);
                    if (child6 != null) {
                        child6.requestFocus();
                    }
                }
                return true;
            case 123:
                if (handleKeyEvent) {
                    parent.getChildAt(widgetCount - 1).requestFocus();
                }
                return true;
            default:
                return false;
        }
    }

    static boolean handleAppsCustomizeKeyEvent(View v, int keyCode, KeyEvent e) {
        ViewGroup parentLayout;
        ViewGroup itemContainer;
        int countX;
        int countY;
        if (v.getParent() instanceof PagedViewCellLayoutChildren) {
            itemContainer = (ViewGroup) v.getParent();
            parentLayout = (ViewGroup) itemContainer.getParent();
            countX = ((PagedViewCellLayout) parentLayout).getCellCountX();
            countY = ((PagedViewCellLayout) parentLayout).getCellCountY();
        } else {
            parentLayout = (ViewGroup) v.getParent();
            itemContainer = parentLayout;
            countX = ((PagedViewGridLayout) parentLayout).getCellCountX();
            countY = ((PagedViewGridLayout) parentLayout).getCellCountY();
        }
        PagedView container = (PagedView) parentLayout.getParent();
        TabHost tabHost = findTabHostParent(container);
        int iconIndex = itemContainer.indexOfChild(v);
        int itemCount = itemContainer.getChildCount();
        int pageIndex = container.indexToPage(container.indexOfChild(parentLayout));
        int pageCount = container.getChildCount();
        int x = iconIndex % countX;
        int y = iconIndex / countX;
        boolean handleKeyEvent = e.getAction() != 1;
        switch (keyCode) {
            case 19:
                if (handleKeyEvent) {
                    if (y > 0) {
                        itemContainer.getChildAt(((y - 1) * countX) + x).requestFocus();
                    } else {
                        tabHost.findViewById(R.id.applist_latest).requestFocus();
                    }
                }
                return true;
            case Board.NUM_ICONS /*20*/:
                if (handleKeyEvent && y < countY - 1) {
                    itemContainer.getChildAt(Math.min(itemCount - 1, ((y + 1) * countX) + x)).requestFocus();
                }
                return true;
            case 21:
                if (handleKeyEvent) {
                    if (iconIndex > 0 && x > 0) {
                        itemContainer.getChildAt(iconIndex - 1).requestFocus();
                    } else if (pageIndex > 0) {
                        ViewGroup newParent = getAppsCustomizePage(container, pageIndex - 1);
                        if (newParent != null) {
                            container.snapToPage(pageIndex - 1);
                            View child = newParent.getChildAt(newParent.getChildCount() - 1);
                            if (child != null) {
                                child.requestFocus();
                            }
                        }
                    }
                }
                return true;
            case 22:
                if (handleKeyEvent) {
                    if (iconIndex < itemCount - 1 && x < countX - 1) {
                        itemContainer.getChildAt(iconIndex + 1).requestFocus();
                    } else if (pageIndex < pageCount - 1) {
                        ViewGroup newParent2 = getAppsCustomizePage(container, pageIndex + 1);
                        if (newParent2 != null) {
                            container.snapToPage(pageIndex + 1);
                            View child2 = newParent2.getChildAt(0);
                            if (child2 != null) {
                                child2.requestFocus();
                            }
                        }
                    }
                }
                return true;
            case 23:
            case CommonDesk.Cmd_SignalLock /*66*/:
                if (handleKeyEvent) {
                    ((OnClickListener) container).onClick(v);
                    Log.d("AppsCustomizePagedView", "the enter key is handled!!!!");
                }
                return true;
            case 92:
                if (handleKeyEvent) {
                    if (pageIndex > 0) {
                        ViewGroup newParent3 = getAppsCustomizePage(container, pageIndex - 1);
                        if (newParent3 != null) {
                            container.snapToPage(pageIndex - 1);
                            View child3 = newParent3.getChildAt(0);
                            if (child3 != null) {
                                child3.requestFocus();
                            }
                        }
                    } else {
                        itemContainer.getChildAt(0).requestFocus();
                    }
                }
                return true;
            case 93:
                if (handleKeyEvent) {
                    if (pageIndex < pageCount - 1) {
                        ViewGroup newParent4 = getAppsCustomizePage(container, pageIndex + 1);
                        if (newParent4 != null) {
                            container.snapToPage(pageIndex + 1);
                            View child4 = newParent4.getChildAt(0);
                            if (child4 != null) {
                                child4.requestFocus();
                            }
                        }
                    } else {
                        itemContainer.getChildAt(itemCount - 1).requestFocus();
                    }
                }
                return true;
            case 122:
                if (handleKeyEvent) {
                    itemContainer.getChildAt(0).requestFocus();
                }
                return true;
            case 123:
                if (handleKeyEvent) {
                    itemContainer.getChildAt(itemCount - 1).requestFocus();
                }
                return true;
            default:
                return false;
        }
    }

    static boolean handleTabKeyEvent(AccessibleTabView v, int keyCode, KeyEvent e) {
        boolean handleKeyEvent = true;
        if (!LauncherApplication.isScreenLarge()) {
            return false;
        }
        FocusOnlyTabWidget parent = (FocusOnlyTabWidget) v.getParent();
        TabHost tabHost = findTabHostParent(parent);
        ViewGroup contents = (ViewGroup) tabHost.findViewById(16908305);
        int tabCount = parent.getTabCount();
        int tabIndex = parent.getChildTabIndex(v);
        if (e.getAction() == 1) {
            handleKeyEvent = false;
        }
        switch (keyCode) {
            case 19:
                return true;
            case Board.NUM_ICONS /*20*/:
                if (handleKeyEvent) {
                    contents.requestFocus();
                }
                return true;
            case 21:
                if (handleKeyEvent && tabIndex > 0) {
                    parent.getChildTabViewAt(tabIndex - 1).requestFocus();
                }
                return true;
            case 22:
                if (handleKeyEvent) {
                    if (tabIndex < tabCount - 1) {
                        parent.getChildTabViewAt(tabIndex + 1).requestFocus();
                    } else if (v.getNextFocusRightId() != -1) {
                        tabHost.findViewById(v.getNextFocusRightId()).requestFocus();
                    }
                }
                return true;
            default:
                return false;
        }
    }

    static boolean handleHotseatButtonKeyEvent(View v, int keyCode, KeyEvent e) {
        CellLayoutChildren parent = (CellLayoutChildren) v.getParent();
        Hotseat hotseat = (Hotseat) ((CellLayout) parent.getParent()).getParent();
        ViewGroup launcher = (ViewGroup) hotseat.getParent();
        Workspace workspace = (Workspace) launcher.findViewById(R.id.workspace);
        ViewGroup viewGroup = (ViewGroup) launcher.findViewById(R.id.paged_view_indicator);
        int buttonIndex = parent.indexOfChild(v);
        int buttonCount = parent.getChildCount();
        int pageIndex = workspace.getCurrentPage();
        boolean handleKeyEvent = e.getAction() != 1;
        ImageView img = (ImageView) launcher.findViewById(R.id.selectbox);
        switch (keyCode) {
            case 19:
                if (!handleKeyEvent) {
                    return false;
                }
                CellLayout workspaceLayout = (CellLayout) workspace.getChildAt(pageIndex);
                View newIcon = getClosestIconFromHotseat(workspaceLayout, workspaceLayout.getChildrenLayout(), hotseat, v);
                if (newIcon == null) {
                    return false;
                }
                newIcon.requestFocus();
                moveSelectboxWithTweenAnimation(v, newIcon, img);
                return true;
            case Board.NUM_ICONS /*20*/:
                return true;
            case 21:
                if (handleKeyEvent && buttonIndex > 0) {
                    View newIcon2 = parent.getChildAt(buttonIndex - 1);
                    newIcon2.requestFocus();
                    moveSelectboxWithTweenAnimation(v, newIcon2, img);
                }
                return true;
            case 22:
                if (handleKeyEvent && buttonIndex < buttonCount - 1) {
                    View newIcon3 = parent.getChildAt(buttonIndex + 1);
                    newIcon3.requestFocus();
                    moveSelectboxWithTweenAnimation(v, newIcon3, img);
                }
                return true;
            default:
                return false;
        }
    }

    private static CellLayoutChildren getCellLayoutChildrenForIndex(ViewGroup container, int i) {
        return (CellLayoutChildren) ((ViewGroup) container.getChildAt(i)).getChildAt(0);
    }

    private static ArrayList<View> getCellLayoutChildrenSortedSpatially(CellLayout layout, ViewGroup parent) {
        final int cellCountX = layout.getCountX();
        int count = parent.getChildCount();
        ArrayList<View> views = new ArrayList<>();
        for (int j = 0; j < count; j++) {
            views.add(parent.getChildAt(j));
        }
        Collections.sort(views, new Comparator<View>() {
            public int compare(View lhs, View rhs) {
                LayoutParams llp = (LayoutParams) lhs.getLayoutParams();
                LayoutParams rlp = (LayoutParams) rhs.getLayoutParams();
                return ((llp.cellY * cellCountX) + llp.cellX) - ((rlp.cellY * cellCountX) + rlp.cellX);
            }
        });
        return views;
    }

    private static View findFocusInHotseat(int cellX, ViewGroup hotseat) {
        CellLayoutChildren cellLayoutChildren = (CellLayoutChildren) ((CellLayout) ((Hotseat) hotseat).getChildAt(0)).getChildAt(0);
        int indexOfHotseat = (((Hotseat) hotseat).getCellLayoutX() * cellX) / 7;
        while (cellLayoutChildren.getChildAt(indexOfHotseat) == null && indexOfHotseat > 0) {
            indexOfHotseat--;
        }
        if (cellLayoutChildren.getChildAt(indexOfHotseat) != null) {
            return cellLayoutChildren.getChildAt(indexOfHotseat);
        }
        return null;
    }

    private static View findIndexOfIcon(ArrayList<View> views, int i, int delta) {
        int count = views.size();
        int newI = i + delta;
        while (newI >= 0 && newI < count) {
            View newV = (View) views.get(newI);
            if ((newV instanceof BubbleTextView) || (newV instanceof FolderIcon) || (newV instanceof LauncherAppWidgetHostView)) {
                return newV;
            }
            newI += delta;
        }
        return null;
    }

    private static View getIconInDirection(CellLayout layout, ViewGroup parent, int i, int delta) {
        return findIndexOfIcon(getCellLayoutChildrenSortedSpatially(layout, parent), i, delta);
    }

    private static View getIconInDirection(CellLayout layout, ViewGroup parent, View v, int delta) {
        ArrayList<View> views = getCellLayoutChildrenSortedSpatially(layout, parent);
        return findIndexOfIcon(views, views.indexOf(v), delta);
    }

    private static View getClosestIconOnLineV(CellLayout layout, ViewGroup parent, View v, int lineDelta) {
        ArrayList<View> views = getCellLayoutChildrenSortedSpatially(layout, parent);
        LayoutParams lp = (LayoutParams) v.getLayoutParams();
        int countX = layout.getCountX();
        int cellCountY = layout.getCountY();
        int row = lineDelta < 0 ? lp.cellY : (lp.cellY + lp.cellVSpan) - 1;
        int newRow = row + lineDelta;
        if (newRow >= 0 && newRow < cellCountY) {
            float closestDistance = Float.MAX_VALUE;
            int closestIndex = -1;
            int curIndex = views.indexOf(v);
            int endIndex = views.size();
            float curCellX = (float) (((double) lp.cellX) + (((double) (lp.cellHSpan - 1)) / 2.0d));
            float curCellY = lineDelta < 0 ? (float) lp.cellY : (float) ((lp.cellY + lp.cellVSpan) - 1);
            for (int index = 0; index < endIndex; index++) {
                if (curIndex != index) {
                    View newV = (View) views.get(index);
                    LayoutParams tmpLp = (LayoutParams) newV.getLayoutParams();
                    boolean satisfiesRow = lineDelta < 0 ? (tmpLp.cellY + tmpLp.cellVSpan) + -1 < row : tmpLp.cellY > row;
                    if (satisfiesRow && ((newV instanceof BubbleTextView) || (newV instanceof FolderIcon) || (newV instanceof LauncherAppWidgetHostView))) {
                        int spanY = lineDelta < 0 ? tmpLp.cellVSpan - 1 : 0;
                        for (int spanX = 0; spanX < tmpLp.cellHSpan; spanX++) {
                            float tmpDistance = (float) Math.sqrt(Math.pow((double) (((float) (tmpLp.cellX + spanX)) - curCellX), 2.0d) + Math.pow((double) (((float) (tmpLp.cellY + spanY)) - curCellY), 2.0d));
                            if (tmpDistance < closestDistance) {
                                closestIndex = index;
                                closestDistance = tmpDistance;
                            }
                        }
                    }
                }
            }
            if (closestIndex > -1) {
                return (View) views.get(closestIndex);
            }
        }
        return null;
    }

    private static View getClosestIconOnLineH(CellLayout layout, ViewGroup parent, View v, int lineDelta) {
        ArrayList<View> views = getCellLayoutChildrenSortedSpatially(layout, parent);
        LayoutParams lp = (LayoutParams) v.getLayoutParams();
        int cellCountX = layout.getCountX();
        int countY = layout.getCountY();
        int col = lineDelta < 0 ? lp.cellX : (lp.cellX + lp.cellHSpan) - 1;
        int newCol = col + lineDelta;
        if (newCol >= 0 && newCol < cellCountX) {
            float closestDistance = Float.MAX_VALUE;
            int closestIndex = -1;
            int curIndex = views.indexOf(v);
            int endIndex = views.size();
            float curCellX = lineDelta < 0 ? (float) lp.cellX : (float) ((lp.cellX + lp.cellHSpan) - 1);
            float curCellY = (float) (((double) lp.cellY) + (((double) (lp.cellVSpan - 1)) / 2.0d));
            for (int index = 0; index < endIndex; index++) {
                if (curIndex != index) {
                    View newV = (View) views.get(index);
                    LayoutParams tmpLp = (LayoutParams) newV.getLayoutParams();
                    boolean satisfiesRow = lineDelta < 0 ? (tmpLp.cellX + tmpLp.cellHSpan) + -1 < col : tmpLp.cellX > col;
                    if (satisfiesRow && ((newV instanceof BubbleTextView) || (newV instanceof FolderIcon) || (newV instanceof LauncherAppWidgetHostView))) {
                        int spanX = lineDelta < 0 ? tmpLp.cellHSpan - 1 : 0;
                        for (int spanY = 0; spanY < tmpLp.cellVSpan; spanY++) {
                            float tmpDistance = (float) Math.sqrt(Math.pow((double) (((float) (tmpLp.cellX + spanX)) - curCellX), 2.0d) + Math.pow((double) (((float) (tmpLp.cellY + spanY)) - curCellY), 2.0d));
                            if (tmpDistance < closestDistance) {
                                closestIndex = index;
                                closestDistance = tmpDistance;
                            }
                        }
                    }
                }
            }
            if (closestIndex > -1) {
                return (View) views.get(closestIndex);
            }
        }
        return null;
    }

    private static View getClosestIconFromHotseat(CellLayout layout, ViewGroup parent, Hotseat hotseat, View v) {
        ArrayList<View> views = getCellLayoutChildrenSortedSpatially(layout, parent);
        LayoutParams lp = (LayoutParams) v.getLayoutParams();
        int cellCountY = layout.getCountY();
        int row = lp.cellY + cellCountY;
        int newRow = row - 1;
        if (newRow >= 0 && newRow < cellCountY) {
            float closestDistance = Float.MAX_VALUE;
            int closestIndex = -1;
            int curIndex = views.indexOf(v);
            int endIndex = views.size();
            float curCellX = (((float) (((double) lp.cellX) + (((double) (lp.cellHSpan - 1)) / 2.0d))) * 7.0f) / 5.0f;
            float curCellY = ((float) lp.cellY) + ((float) cellCountY);
            for (int index = 0; index < endIndex; index++) {
                if (curIndex != index) {
                    View newV = (View) views.get(index);
                    LayoutParams tmpLp = (LayoutParams) newV.getLayoutParams();
                    if (((tmpLp.cellY + tmpLp.cellVSpan) + -1 < row) && ((newV instanceof BubbleTextView) || (newV instanceof FolderIcon) || (newV instanceof LauncherAppWidgetHostView))) {
                        int spanY = tmpLp.cellVSpan - 1;
                        for (int spanX = 0; spanX < tmpLp.cellHSpan; spanX++) {
                            float tmpDistance = (float) Math.sqrt(Math.pow((double) (((float) (tmpLp.cellX + spanX)) - curCellX), 2.0d) + Math.pow((double) (((float) (tmpLp.cellY + spanY)) - curCellY), 2.0d));
                            if (tmpDistance < closestDistance) {
                                closestIndex = index;
                                closestDistance = tmpDistance;
                            }
                        }
                    }
                }
            }
            if (closestIndex > -1) {
                return (View) views.get(closestIndex);
            }
        }
        return null;
    }

    static boolean handleAddGuideKeyEvent(View v, int keyCode, KeyEvent e) {
        CellLayout layout = (CellLayout) v.getParent().getParent();
        CellLayoutChildren parent = layout.getChildrenLayout();
        Workspace workspace = (Workspace) layout.getParent();
        int pageIndex = workspace.indexOfChild(layout);
        int pageCount = workspace.getChildCount();
        boolean handleKeyEvent = e.getAction() != 1;
        switch (keyCode) {
            case 21:
            case 92:
                if (handleKeyEvent) {
                    if (pageIndex > 0) {
                        CellLayoutChildren parent2 = getCellLayoutChildrenForIndex(workspace, pageIndex - 1);
                        View newIcon = null;
                        if (keyCode == 92) {
                            newIcon = getIconInDirection(layout, (ViewGroup) parent2, -1, 1);
                        } else if (keyCode == 21) {
                            newIcon = getIconInDirection(layout, (ViewGroup) parent2, parent2.getChildCount(), -1);
                        }
                        if (newIcon != null) {
                            newIcon.requestFocus();
                        } else {
                            workspace.snapToPage(pageIndex - 1);
                            workspace.getPageAt(pageIndex - 1).requestFocus();
                        }
                    } else {
                        View newIcon2 = getIconInDirection(layout, (ViewGroup) parent, -1, 1);
                        if (newIcon2 != null) {
                            newIcon2.requestFocus();
                        }
                    }
                }
                return true;
            case 22:
            case 93:
                if (handleKeyEvent) {
                    if (pageIndex < pageCount - 1) {
                        View newIcon3 = getIconInDirection(layout, (ViewGroup) getCellLayoutChildrenForIndex(workspace, pageIndex + 1), -1, 1);
                        if (newIcon3 != null) {
                            newIcon3.requestFocus();
                        } else {
                            workspace.snapToPage(pageIndex + 1);
                            workspace.getPageAt(pageIndex + 1).requestFocus();
                        }
                    } else {
                        View newIcon4 = getIconInDirection(layout, (ViewGroup) parent, parent.getChildCount(), -1);
                        if (newIcon4 != null) {
                            newIcon4.requestFocus();
                        }
                    }
                }
                return true;
            default:
                return false;
        }
    }

    static boolean handleIconKeyEvent(View v, int keyCode, KeyEvent e) {
        CellLayoutChildren parent = (CellLayoutChildren) v.getParent();
        CellLayout layout = (CellLayout) parent.getParent();
        Workspace workspace = (Workspace) layout.getParent();
        ViewGroup launcher = (ViewGroup) workspace.getParent();
        ViewGroup tabs = (ViewGroup) launcher.findViewById(R.id.qsb_bar);
        ViewGroup hotseat = (ViewGroup) launcher.findViewById(R.id.hotseat);
        ViewGroup viewGroup = (ViewGroup) launcher.findViewById(R.id.paged_view_indicator);
        int pageIndex = workspace.indexOfChild(layout);
        int pageCount = workspace.getChildCount();
        boolean handleKeyEvent = e.getAction() != 1;
        boolean wasHandled = false;
        ImageView img = (ImageView) launcher.findViewById(R.id.selectbox);
        switch (keyCode) {
            case 4:
                if (!handleKeyEvent || !(v.getTag() instanceof LauncherAppWidgetInfo)) {
                    return false;
                }
                ((LauncherAppWidgetHostView) v).requestParentFocus();
                return true;
            case 19:
                if (!handleKeyEvent) {
                    return false;
                }
                View newIcon = getClosestIconOnLineV(layout, parent, v, -1);
                if (newIcon != null) {
                    newIcon.requestFocus();
                    int[] fromlocation = new int[2];
                    int[] tolocation = new int[2];
                    v.getLocationOnScreen(fromlocation);
                    newIcon.getLocationOnScreen(tolocation);
                    Log.d(TAG, "oldicon:x=" + fromlocation[0] + ";y=" + fromlocation[1]);
                    Log.d(TAG, "newicon:x=" + tolocation[0] + ";y=" + tolocation[1]);
                    moveSelectboxWithTweenAnimation(v, newIcon, img);
                    wasHandled = true;
                } else {
                    tabs.requestFocus();
                }
                Log.d(TAG, "press key up");
                return wasHandled;
            case Board.NUM_ICONS /*20*/:
                if (!handleKeyEvent) {
                    return false;
                }
                View newIcon2 = getClosestIconOnLineV(layout, parent, v, 1);
                if (newIcon2 != null) {
                    newIcon2.requestFocus();
                    int[] fromlocation2 = new int[2];
                    int[] tolocation2 = new int[2];
                    v.getLocationOnScreen(fromlocation2);
                    newIcon2.getLocationOnScreen(tolocation2);
                    Log.d(TAG, "oldicon:x=" + fromlocation2[0] + ";y=" + fromlocation2[1]);
                    Log.d(TAG, "newicon:x=" + tolocation2[0] + ";y=" + tolocation2[1]);
                    moveSelectboxWithTweenAnimation(v, newIcon2, img);
                    wasHandled = true;
                } else if (hotseat != null) {
                    View view = findFocusInHotseat(((LayoutParams) v.getLayoutParams()).cellX, hotseat);
                    if (view != null) {
                        view.requestFocus();
                        moveSelectboxWithTweenAnimation(v, view, img);
                    } else {
                        hotseat.requestFocus();
                    }
                    wasHandled = true;
                }
                Log.d(TAG, "press key down");
                return wasHandled;
            case 21:
                if (handleKeyEvent) {
                    View newIcon3 = getClosestIconOnLineH(layout, parent, v, -1);
                    if (newIcon3 != null) {
                        newIcon3.requestFocus();
                        int[] fromlocation3 = new int[2];
                        int[] tolocation3 = new int[2];
                        v.getLocationOnScreen(fromlocation3);
                        newIcon3.getLocationOnScreen(tolocation3);
                        Log.d(TAG, "oldicon:x=" + fromlocation3[0] + ";y=" + fromlocation3[1]);
                        Log.d(TAG, "newicon:x=" + tolocation3[0] + ";y=" + tolocation3[1]);
                        moveSelectboxWithTweenAnimation(v, newIcon3, img);
                    } else {
                        if (pageIndex == 0) {
                            ((DragLayer) launcher).snapToTVPage();
                        }
                        if (pageIndex > 0) {
                            CellLayoutChildren parent2 = getCellLayoutChildrenForIndex(workspace, pageIndex - 1);
                            View newIcon4 = getIconInDirection(layout, (ViewGroup) parent2, parent2.getChildCount(), -1);
                            if (newIcon4 != null) {
                                newIcon4.requestFocus();
                            } else {
                                workspace.snapToPage(pageIndex - 1);
                                workspace.getPageAt(pageIndex - 1).requestFocus();
                            }
                        }
                    }
                    Log.d(TAG, "press key left");
                }
                return true;
            case 22:
                if (handleKeyEvent) {
                    View newIcon5 = getClosestIconOnLineH(layout, parent, v, 1);
                    if (newIcon5 != null) {
                        newIcon5.requestFocus();
                        int[] fromlocation4 = new int[2];
                        int[] tolocation4 = new int[2];
                        v.getLocationOnScreen(fromlocation4);
                        newIcon5.getLocationOnScreen(tolocation4);
                        Log.d(TAG, "oldicon:x=" + fromlocation4[0] + ";y=" + fromlocation4[1]);
                        Log.d(TAG, "newicon:x=" + tolocation4[0] + ";y=" + tolocation4[1]);
                        moveSelectboxWithTweenAnimation(v, newIcon5, img);
                    } else if (pageIndex < pageCount - 1) {
                        View newIcon6 = getIconInDirection(layout, (ViewGroup) getCellLayoutChildrenForIndex(workspace, pageIndex + 1), -1, 1);
                        if (newIcon6 != null) {
                            newIcon6.requestFocus();
                        } else {
                            workspace.snapToPage(pageIndex + 1);
                            workspace.getPageAt(pageIndex + 1).requestFocus();
                        }
                    }
                    Log.d(TAG, "press key right");
                }
                return true;
            case 23:
            case CommonDesk.Cmd_SignalLock /*66*/:
                if (!handleKeyEvent || !(v.getTag() instanceof LauncherAppWidgetInfo)) {
                    return false;
                }
                Log.d(TAG, "-----------------------key become click");
                LauncherAppWidgetHostView appWidget = (LauncherAppWidgetHostView) v;
                if (!appWidget.requestChildFocus() && appWidget.getChildCount() != 0) {
                    appWidget.getChildAt(0).callOnClick();
                }
                return true;
            case 82:
                if (handleKeyEvent) {
                    CellLayout cellLayout = (CellLayout) v.getParent().getParent();
                    cellLayout.setTagToCellInfoForPoint((int) (((double) (v.getX() + ((float) cellLayout.getPaddingLeft()))) + 0.5d), (int) (((double) (v.getY() + ((float) cellLayout.getPaddingTop()))) + 0.5d));
                    CellInfo cellInfo = cellLayout.getTag();
                    String str = TAG;
                    StringBuilder sb = new StringBuilder("cellInfo");
                    Slog.d(str, sb.append(cellInfo == null ? "==" : "!=").append("NULL").toString());
                    if (cellInfo != null) {
                        ((Workspace) cellLayout.getParent()).popQuickActionMenu(cellInfo);
                        StringBuilder sb2 = new StringBuilder("cell:id=");
                        Log.d("czj-ios", sb2.append(((ItemInfo) cellInfo.cell.getTag()).id).toString());
                    }
                }
                return true;
            case 92:
                if (handleKeyEvent) {
                    if (pageIndex > 0) {
                        View newIcon7 = getIconInDirection(layout, (ViewGroup) getCellLayoutChildrenForIndex(workspace, pageIndex - 1), -1, 1);
                        if (newIcon7 != null) {
                            newIcon7.requestFocus();
                        } else {
                            workspace.snapToPage(pageIndex - 1);
                            workspace.getPageAt(pageIndex - 1).requestFocus();
                        }
                    } else {
                        View newIcon8 = getIconInDirection(layout, (ViewGroup) parent, -1, 1);
                        if (newIcon8 != null) {
                            newIcon8.requestFocus();
                        }
                    }
                }
                return true;
            case 93:
                if (handleKeyEvent) {
                    if (pageIndex < pageCount - 1) {
                        View newIcon9 = getIconInDirection(layout, (ViewGroup) getCellLayoutChildrenForIndex(workspace, pageIndex + 1), -1, 1);
                        if (newIcon9 != null) {
                            newIcon9.requestFocus();
                        } else {
                            workspace.snapToPage(pageIndex + 1);
                            workspace.getPageAt(pageIndex + 1).requestFocus();
                        }
                    } else {
                        View newIcon10 = getIconInDirection(layout, (ViewGroup) parent, parent.getChildCount(), -1);
                        if (newIcon10 != null) {
                            newIcon10.requestFocus();
                        }
                    }
                }
                return true;
            case 122:
                if (handleKeyEvent) {
                    View newIcon11 = getIconInDirection(layout, (ViewGroup) parent, -1, 1);
                    if (newIcon11 != null) {
                        newIcon11.requestFocus();
                        moveSelectboxWithTweenAnimation(v, newIcon11, img);
                    }
                }
                return true;
            case 123:
                if (handleKeyEvent) {
                    View newIcon12 = getIconInDirection(layout, (ViewGroup) parent, parent.getChildCount(), -1);
                    if (newIcon12 != null) {
                        newIcon12.requestFocus();
                        moveSelectboxWithTweenAnimation(v, newIcon12, img);
                    }
                }
                return true;
            default:
                return false;
        }
    }

    private static void moveSelectboxWithPropertyAnimation(View fromView, View toView, ImageView selectboxImageView) {
        if (fromView != null && toView != null) {
            if (fromView == toView) {
                Log.d(TAG, "fromView == toView\n");
                return;
            }
            int[] fromLocation = new int[2];
            fromView.getLocationInWindow(fromLocation);
            int[] toLocation = new int[2];
            toView.getLocationInWindow(toLocation);
            int width = fromView.getWidth();
            int height = fromView.getHeight();
            float fromX = (float) fromLocation[0];
            float fromY = (float) fromLocation[1];
            int width2 = toView.getWidth();
            int height2 = toView.getHeight();
            float toX = (float) toLocation[0];
            float toY = (float) toLocation[1];
            ImageView imageView = selectboxImageView;
            View view = toView;
            View view2 = fromView;
            ObjectAnimator.setFrameDelay(10);
            ObjectAnimator translateXAnimator = ObjectAnimator.ofFloat(selectboxImageView, "x", new float[]{fromX, toX});
            ObjectAnimator translateYAnimator = ObjectAnimator.ofFloat(selectboxImageView, "y", new float[]{fromY, toY});
            if (mAnimatorSet.isRunning()) {
                mAnimatorSet.cancel();
            }
            mAnimatorSet.setDuration(300);
            mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimatorSet.playTogether(new Animator[]{translateXAnimator, translateYAnimator});
            mAnimatorSet.addListener(new AnimatorListener() {
                public void onAnimationStart(Animator animation) {
                }

                public void onAnimationRepeat(Animator animation) {
                }

                public void onAnimationEnd(Animator animation) {
                }

                public void onAnimationCancel(Animator animation) {
                }
            });
            mAnimatorSet.start();
        }
    }

    private static void moveSelectboxWithTweenAnimation(View fromView, View toView, ImageView selectboxImageView) {
        if (fromView != null && toView != null) {
            if (fromView == toView) {
                Log.d(TAG, "fromView == toView\n");
                return;
            }
            int[] fromLocation = new int[2];
            fromView.getLocationInWindow(fromLocation);
            int[] toLocation = new int[2];
            toView.getLocationInWindow(toLocation);
            int width = fromView.getWidth();
            int height = fromView.getHeight();
            int fromX = fromLocation[0];
            int fromY = fromLocation[1];
            int width2 = toView.getWidth();
            int height2 = toView.getHeight();
            int toX = toLocation[0];
            int toY = toLocation[1];
            final View selectboxView = selectboxImageView;
            final View targetView = toView;
            final View sourceView = fromView;
            targetView.getBackground().setAlpha(0);
            sourceView.getBackground().setAlpha(0);
            selectboxView.getBackground().setAlpha(255);
            if (mTranslateAnimation.hasStarted()) {
                mTranslateAnimation.cancel();
            }
            mTranslateAnimation = new TranslateAnimation((float) fromX, (float) toX, (float) fromY, (float) toY);
            mTranslateAnimation.setDuration(200);
            mTranslateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            mTranslateAnimation.setFillAfter(true);
            TranslateAnimation translateAnimation = mTranslateAnimation;
            AnonymousClass3 r0 = new AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    Log.d(FocusHelper.TAG, "onAnimationStart");
                    selectboxView.getBackground().setAlpha(255);
                    targetView.getBackground().setAlpha(0);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    Log.d(FocusHelper.TAG, "onAnimationEnd");
                    sourceView.getBackground().setAlpha(255);
                    targetView.getBackground().setAlpha(255);
                    selectboxView.getBackground().setAlpha(0);
                }
            };
            translateAnimation.setAnimationListener(r0);
            selectboxView.setLayoutParams(new FrameLayout.LayoutParams(toView.getWidth(), toView.getHeight()));
            selectboxView.setAnimation(mTranslateAnimation);
            mTranslateAnimation.start();
        }
    }

    static boolean handleFolderKeyEvent(View v, int keyCode, KeyEvent e) {
        CellLayoutChildren parent = (CellLayoutChildren) v.getParent();
        CellLayout layout = (CellLayout) parent.getParent();
        Folder folder = (Folder) layout.getParent();
        View title = folder.mFolderName;
        boolean handleKeyEvent = e.getAction() != 1;
        ViewGroup layer = (ViewGroup) folder.getParent();
        ImageView img = (ImageView) layer.findViewById(R.id.selectbox);
        layer.bringChildToFront(img);
        switch (keyCode) {
            case 19:
                if (handleKeyEvent) {
                    View newIcon = getClosestIconOnLineV(layout, parent, v, -1);
                    if (newIcon != null) {
                        Log.d(TAG, "folder up to newIcon");
                        newIcon.requestFocus();
                        moveSelectboxWithTweenAnimation(v, newIcon, img);
                    } else {
                        Log.d(TAG, "folder up to title");
                        title.requestFocus();
                        img.setVisibility(8);
                    }
                }
                return true;
            case Board.NUM_ICONS /*20*/:
                if (handleKeyEvent) {
                    View newIcon2 = getClosestIconOnLineV(layout, parent, v, 1);
                    if (newIcon2 != null) {
                        newIcon2.requestFocus();
                        moveSelectboxWithTweenAnimation(v, newIcon2, img);
                    }
                }
                return true;
            case 21:
                if (handleKeyEvent) {
                    View newIcon3 = getClosestIconOnLineH(layout, parent, v, -1);
                    if (newIcon3 != null) {
                        newIcon3.requestFocus();
                        moveSelectboxWithTweenAnimation(v, newIcon3, img);
                    }
                }
                return true;
            case 22:
                if (handleKeyEvent) {
                    View newIcon4 = getClosestIconOnLineH(layout, parent, v, 1);
                    if (newIcon4 != null) {
                        newIcon4.requestFocus();
                        moveSelectboxWithTweenAnimation(v, newIcon4, img);
                    }
                }
                return true;
            case 82:
                if (handleKeyEvent) {
                    CellLayout cellLayout = (CellLayout) v.getParent().getParent();
                    cellLayout.setTagToCellInfoForPoint((int) (v.getX() + ((float) cellLayout.getPaddingLeft())), (int) (v.getY() + ((float) cellLayout.getPaddingTop())));
                    CellInfo cellInfo = cellLayout.getTag();
                    Slog.d(TAG, "cellInfo" + (cellInfo == null ? "==" : "!=") + "NULL");
                    if (cellInfo != null) {
                        folder.popQuickActionMenu(cellInfo);
                    }
                }
                return true;
            case 122:
                if (handleKeyEvent) {
                    View newIcon5 = getIconInDirection(layout, (ViewGroup) parent, -1, 1);
                    if (newIcon5 != null) {
                        newIcon5.requestFocus();
                        moveSelectboxWithTweenAnimation(v, newIcon5, img);
                    }
                }
                return true;
            case 123:
                if (handleKeyEvent) {
                    View newIcon6 = getIconInDirection(layout, (ViewGroup) parent, parent.getChildCount(), -1);
                    if (newIcon6 != null) {
                        newIcon6.requestFocus();
                        moveSelectboxWithTweenAnimation(v, newIcon6, img);
                    }
                }
                return true;
            default:
                return false;
        }
    }
}
