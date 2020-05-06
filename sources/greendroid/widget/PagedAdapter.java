package greendroid.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

public abstract class PagedAdapter implements Adapter {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public abstract int getCount();

    public abstract Object getItem(int i);

    public abstract long getItemId(int i);

    public abstract View getView(int i, View view, ViewGroup viewGroup);

    public boolean hasStableIds() {
        throw new UnsupportedOperationException("hasStableIds(int) is not supported in the context of a SwipeAdapter");
    }

    public final int getItemViewType(int position) {
        throw new UnsupportedOperationException("getItemViewType(int) is not supported in the context of a SwipeAdapter");
    }

    public final int getViewTypeCount() {
        throw new UnsupportedOperationException("getViewTypeCount() is not supported in the context of a SwipeAdapter");
    }

    public final boolean isEmpty() {
        throw new UnsupportedOperationException("isEmpty() is not supported in the context of a SwipeAdapter");
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.unregisterObserver(observer);
    }

    public void notifyDataSetChanged() {
        this.mDataSetObservable.notifyChanged();
    }
}
