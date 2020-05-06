package com.cyanogenmod.trebuchet;

import android.content.Context;
import android.database.Cursor;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.konka.ios7launcher.R;

public class SearchSuggestPopupWindow extends PopupWindow {
    private static final int SUGGESTWORDS_TOTAL = 8;
    private static final String TAG = "SearchSuggestPopupWindow";
    private int focusPosition = -1;
    /* access modifiers changed from: private */
    public Context mContext;
    private LinearLayout searchSuggestLayout;
    private EditText searchWordEditText;
    private View suggestView;
    private int suggestWordsSize = 0;

    public SearchSuggestPopupWindow(Context context) {
        super(context);
        this.mContext = context;
        this.suggestView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.search_suggest_popupwindow, null);
        setContentView(this.suggestView);
        findView();
        setWidth((((int) context.getResources().getDimension(R.dimen.suggest_window_shadow_width)) * 2) + ((int) context.getResources().getDimension(R.dimen.search_layout_width)));
        setHeight(-2);
        setFocusable(false);
        setTouchable(true);
        getBackground().setAlpha(0);
        this.suggestView.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case 4:
                        SearchSuggestPopupWindow.this.dismiss();
                        break;
                }
                return true;
            }
        });
    }

    private void findView() {
        this.searchSuggestLayout = (LinearLayout) this.suggestView.findViewById(R.id.search_suggest_layout);
    }

    public void refreshSuggestWord(Cursor cursor, int keyLength) {
        if (cursor == null) {
            Log.d(TAG, "suggestcursor is null");
        } else if (cursor.getCount() != 0) {
            this.suggestWordsSize = cursor.getCount();
            this.focusPosition = -1;
            if (this.suggestWordsSize > 8) {
                this.suggestWordsSize = 8;
            }
            this.searchSuggestLayout.removeAllViews();
            this.searchSuggestLayout.requestLayout();
            this.searchSuggestLayout.invalidate();
            for (int i = 0; i < this.suggestWordsSize; i++) {
                cursor.moveToPosition(i);
                String word = cursor.getString(1);
                Log.d(TAG, "suggest word:" + word);
                TextView suggestTextView = new TextView(this.mContext);
                suggestTextView.setTextSize(0, this.mContext.getResources().getDimension(R.dimen.suggest_text_size));
                if (word.length() <= keyLength) {
                    suggestTextView.setTextColor(this.mContext.getResources().getColor(R.color.suggest_keyword_color));
                    suggestTextView.setText(word);
                } else {
                    SpannableStringBuilder style = new SpannableStringBuilder(word);
                    style.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.suggest_keyword_color)), 0, keyLength, 33);
                    style.setSpan(new ForegroundColorSpan(this.mContext.getResources().getColor(R.color.suggest_normal_color)), keyLength, word.length(), 33);
                    suggestTextView.setText(style);
                }
                suggestTextView.setBackgroundResource(R.drawable.search_suggest_focused);
                suggestTextView.getBackground().setAlpha(0);
                suggestTextView.setFocusable(false);
                suggestTextView.setPadding((int) this.mContext.getResources().getDimension(R.dimen.suggest_text_padding_left), (int) this.mContext.getResources().getDimension(R.dimen.suggest_text_padding_top), (int) this.mContext.getResources().getDimension(R.dimen.suggest_text_padding_right), (int) this.mContext.getResources().getDimension(R.dimen.suggest_text_padding_bottom));
                suggestTextView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        ((SearchActivity) SearchSuggestPopupWindow.this.mContext).setSelectedSuggestWord(((TextView) v).getText().toString());
                    }
                });
                suggestTextView.setOnHoverListener(new OnHoverListener() {
                    public boolean onHover(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case 9:
                                v.getBackground().setAlpha(255);
                                return true;
                            case 10:
                                v.getBackground().setAlpha(0);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                this.searchSuggestLayout.addView(suggestTextView, new LayoutParams(-1, -2));
            }
        } else {
            Log.d(TAG, "suggest words is none");
        }
    }

    public void requestDownFocus() {
        if (this.focusPosition >= 0 && this.focusPosition < this.suggestWordsSize - 1) {
            this.searchSuggestLayout.getChildAt(this.focusPosition).getBackground().setAlpha(0);
        }
        this.focusPosition++;
        if (this.focusPosition >= this.suggestWordsSize) {
            this.focusPosition = this.suggestWordsSize - 1;
        } else {
            this.searchSuggestLayout.getChildAt(this.focusPosition).getBackground().setAlpha(255);
        }
    }

    public boolean requestUpFocus() {
        if (this.focusPosition >= 0) {
            this.searchSuggestLayout.getChildAt(this.focusPosition).getBackground().setAlpha(0);
        }
        this.focusPosition--;
        if (this.focusPosition < 0) {
            this.focusPosition = -1;
            return false;
        }
        this.searchSuggestLayout.getChildAt(this.focusPosition).getBackground().setAlpha(255);
        return true;
    }

    public String getSelectedSuggestWord() {
        if (this.focusPosition < 0 || this.focusPosition >= this.suggestWordsSize) {
            return null;
        }
        return ((TextView) this.searchSuggestLayout.getChildAt(this.focusPosition)).getText().toString();
    }

    public void dismiss() {
        super.dismiss();
        ((SearchActivity) this.mContext).clearSuggestWordWindow();
    }
}
