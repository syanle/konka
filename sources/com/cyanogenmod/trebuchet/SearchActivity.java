package com.cyanogenmod.trebuchet;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.cyanogenmod.trebuchet.RocketLauncher.Board.FlyingIcon;
import com.konka.ios7launcher.R;
import com.tencent.stat.common.StatConstants;
import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends Activity {
    private static final int HOTWORDS_QUERY_END = 1;
    private static final int HOTWORDS_QUERY_FAILED = 0;
    private static final int HOTWORDS_QUERY_TOTAL = 5;
    private static final int HOTWORDS_ROWS_TOTAL = 2;
    private static final int HOTWORDS_TOTAL = 20;
    private static final String HOTWORDS_URL = "content://com.konka.cloudsearch.search.SearchProvider/query/hotsearch/*";
    private static final int SUGGESTWORDS_QUERY_END = 2;
    private static final String SUGGESTWORDS_URL = "content://com.konka.cloudsearch.search.SearchProvider/query/suggest/";
    private static final String TAG = "SearchActivity";
    /* access modifiers changed from: private */
    public int currHotWordsWidth = 0;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    SearchActivity.this.refreshHotWordsTextView.setVisibility(0);
                    SearchActivity.this.searchHotWordsHintTextView.setText(SearchActivity.this.getResources().getString(R.string.hotwords_got_failed));
                    return;
                case 1:
                    if (SearchActivity.this.hotwordsCursor == null) {
                        Log.d(SearchActivity.TAG, "cursor is null");
                        if (SearchActivity.this.queryHotWordsCount < 5) {
                            SearchActivity.this.queryHotWords();
                            SearchActivity searchActivity = SearchActivity.this;
                            searchActivity.queryHotWordsCount = searchActivity.queryHotWordsCount + 1;
                            return;
                        }
                        SearchActivity.this.refreshHotWordsTextView.setVisibility(0);
                        SearchActivity.this.searchHotWordsHintTextView.setText(SearchActivity.this.getResources().getString(R.string.hotwords_got_failed));
                        return;
                    } else if (SearchActivity.this.hotwordsCursor.getCount() != 0) {
                        SearchActivity.this.searchHotWordsHintLayout.setVisibility(8);
                        TableRow row = new TableRow(SearchActivity.this);
                        SearchActivity.this.searchHotwordsLayout.addView(row, new LayoutParams(-1, -2));
                        int i = 0;
                        while (i < 20 && i < SearchActivity.this.hotwordsCursor.getCount()) {
                            SearchActivity.this.hotwordsCursor.moveToPosition(i);
                            String word = SearchActivity.this.hotwordsCursor.getString(1);
                            Log.d(SearchActivity.TAG, "hotwords:" + word);
                            int wordWidth = ((int) SearchActivity.this.measureText(word, (int) SearchActivity.this.getResources().getDimension(R.dimen.hotword_text_size))) + ((int) SearchActivity.this.getResources().getDimension(R.dimen.hotword_textview_padding_left)) + ((int) SearchActivity.this.getResources().getDimension(R.dimen.hotword_textview_padding_right));
                            SearchActivity searchActivity2 = SearchActivity.this;
                            searchActivity2.currHotWordsWidth = searchActivity2.currHotWordsWidth + wordWidth;
                            Log.d(SearchActivity.TAG, "\ncurrHotWordsWidth:" + SearchActivity.this.currHotWordsWidth + ";hotWordsLayoutWidth:" + SearchActivity.this.hotWordsLayoutWidth);
                            int paddingRight = (int) SearchActivity.this.getResources().getDimension(R.dimen.hotword_textview_padding_right);
                            if (SearchActivity.this.currHotWordsWidth > SearchActivity.this.hotWordsLayoutWidth) {
                                if (SearchActivity.this.currHotWordsWidth <= SearchActivity.this.hotWordsLayoutWidth + paddingRight) {
                                    paddingRight = 0;
                                } else {
                                    SearchActivity.this.currHotWordsWidth = wordWidth;
                                    SearchActivity searchActivity3 = SearchActivity.this;
                                    searchActivity3.hotWordsRowCount = searchActivity3.hotWordsRowCount + 1;
                                    if (SearchActivity.this.hotWordsRowCount < 2) {
                                        row = new TableRow(SearchActivity.this);
                                        SearchActivity.this.searchHotwordsLayout.addView(row, new LayoutParams(-2, -2));
                                    } else {
                                        return;
                                    }
                                }
                            }
                            TextView hotwordTextView = new TextView(SearchActivity.this);
                            hotwordTextView.setText(word);
                            hotwordTextView.setTextSize(0, SearchActivity.this.getResources().getDimension(R.dimen.hotword_text_size));
                            hotwordTextView.setTextColor(SearchActivity.this.getResources().getColorStateList(R.drawable.search_hotwords_text_color));
                            hotwordTextView.setGravity(17);
                            hotwordTextView.setVisibility(0);
                            hotwordTextView.setFocusable(true);
                            hotwordTextView.setPadding((int) SearchActivity.this.getResources().getDimension(R.dimen.hotword_textview_padding_left), (int) SearchActivity.this.getResources().getDimension(R.dimen.hotword_textview_padding_top), paddingRight, (int) SearchActivity.this.getResources().getDimension(R.dimen.hotword_textview_padding_bottom));
                            hotwordTextView.setSingleLine(true);
                            hotwordTextView.setEllipsize(TruncateAt.MARQUEE);
                            hotwordTextView.setOnClickListener(new OnClickListener() {
                                public void onClick(View arg0) {
                                    SearchActivity.this.searchWord(((TextView) arg0).getText().toString());
                                }
                            });
                            row.addView(hotwordTextView, new TableRow.LayoutParams(-2, -2));
                            i++;
                        }
                        return;
                    } else {
                        Log.d(SearchActivity.TAG, "hotwords is none");
                        SearchActivity.this.refreshHotWordsTextView.setVisibility(0);
                        SearchActivity.this.searchHotWordsHintTextView.setText(SearchActivity.this.getResources().getString(R.string.hotwords_got_failed));
                        return;
                    }
                case 2:
                    if (msg.arg1 == SearchActivity.this.querySuggestWordTag && SearchActivity.this.searchWordEditText.hasFocus()) {
                        Log.d(SearchActivity.TAG, "call this");
                        if (SearchActivity.this.mSearchSuggestPopupWindow != null) {
                            if (SearchActivity.this.keyWord.equals(StatConstants.MTA_COOPERATION_TAG) || SearchActivity.this.suggestwordsCursor == null || SearchActivity.this.suggestwordsCursor.getCount() == 0) {
                                SearchActivity.this.mSearchSuggestPopupWindow.dismiss();
                                Log.d(SearchActivity.TAG, "dismiss sussgest window");
                                return;
                            }
                            SearchActivity.this.mSearchSuggestPopupWindow.refreshSuggestWord(SearchActivity.this.suggestwordsCursor, SearchActivity.this.keyWord.length());
                            return;
                        } else if (!SearchActivity.this.keyWord.equals(StatConstants.MTA_COOPERATION_TAG) && SearchActivity.this.suggestwordsCursor != null && SearchActivity.this.suggestwordsCursor.getCount() > 0) {
                            Log.d(SearchActivity.TAG, "show sussgest window");
                            SearchActivity.this.mSearchSuggestPopupWindow = new SearchSuggestPopupWindow(SearchActivity.this);
                            SearchActivity.this.mSearchSuggestPopupWindow.refreshSuggestWord(SearchActivity.this.suggestwordsCursor, SearchActivity.this.keyWord.length());
                            SearchActivity.this.mSearchSuggestPopupWindow.showAsDropDown(SearchActivity.this.searchWordEditText, -((int) SearchActivity.this.getResources().getDimension(R.dimen.suggest_window_shadow_width)), -((int) SearchActivity.this.getResources().getDimension(R.dimen.suggest_popupwindow_y_offset)));
                            return;
                        } else if (SearchActivity.this.suggestwordsCursor == null) {
                            Log.d(SearchActivity.TAG, "suggestwordsCursor is null");
                            return;
                        } else if (SearchActivity.this.suggestwordsCursor.getCount() == 0) {
                            Log.d(SearchActivity.TAG, "suggestwordsCursor is empty");
                            return;
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean hasSelectedSuggestWord = false;
    /* access modifiers changed from: private */
    public int hotWordsLayoutWidth = 0;
    /* access modifiers changed from: private */
    public int hotWordsRowCount = 0;
    /* access modifiers changed from: private */
    public Cursor hotwordsCursor;
    /* access modifiers changed from: private */
    public boolean isSearchIconVisiable = true;
    /* access modifiers changed from: private */
    public String keyWord = null;
    /* access modifiers changed from: private */
    public SearchSuggestPopupWindow mSearchSuggestPopupWindow = null;
    /* access modifiers changed from: private */
    public int queryHotWordsCount = 0;
    /* access modifiers changed from: private */
    public int querySuggestWordTag = 0;
    /* access modifiers changed from: private */
    public TextView refreshHotWordsTextView;
    /* access modifiers changed from: private */
    public LinearLayout searchHotWordsHintLayout;
    /* access modifiers changed from: private */
    public TextView searchHotWordsHintTextView;
    /* access modifiers changed from: private */
    public LinearLayout searchHotwordsLayout;
    private Button searchWordButton;
    /* access modifiers changed from: private */
    public EditText searchWordEditText;
    /* access modifiers changed from: private */
    public Cursor suggestwordsCursor;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_popupwindow);
        findView();
        initHotWords();
        this.hotWordsLayoutWidth = ((int) getResources().getDimension(R.dimen.search_layout_width)) + ((int) getResources().getDimension(R.dimen.search_button_margin_left)) + ((int) getResources().getDimension(R.dimen.search_button_width));
        this.isSearchIconVisiable = true;
    }

    private void findView() {
        this.searchHotwordsLayout = (LinearLayout) findViewById(R.id.search_hotwords_layout);
        this.searchWordEditText = (EditText) findViewById(R.id.search_word_edittext);
        this.searchWordButton = (Button) findViewById(R.id.search_word_button);
        this.searchHotWordsHintLayout = (LinearLayout) findViewById(R.id.search_hotwords_hint_layout);
        this.searchHotWordsHintTextView = (TextView) findViewById(R.id.search_hotwords_hint_textview);
        this.refreshHotWordsTextView = (TextView) findViewById(R.id.refresh_hotwords_textview);
        this.searchWordEditText.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                Log.d(SearchActivity.TAG, "call onTextChanged:" + arg0.toString());
                if (SearchActivity.this.searchWordEditText.getText().toString().equals(StatConstants.MTA_COOPERATION_TAG)) {
                    SearchActivity.this.isSearchIconVisiable = true;
                    SearchActivity.this.searchWordEditText.setPadding((int) SearchActivity.this.getResources().getDimension(R.dimen.search_edittext_with_icon_padding_left), 0, 0, 0);
                    Drawable drawable = SearchActivity.this.getResources().getDrawable(R.drawable.search_icon);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    SearchActivity.this.searchWordEditText.setCompoundDrawables(drawable, null, null, null);
                } else if (SearchActivity.this.isSearchIconVisiable) {
                    SearchActivity.this.isSearchIconVisiable = false;
                    SearchActivity.this.searchWordEditText.setCompoundDrawables(null, null, null, null);
                    SearchActivity.this.searchWordEditText.setPadding((int) SearchActivity.this.getResources().getDimension(R.dimen.search_edittext_without_icon_padding_left), 0, 0, 0);
                }
            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
                SearchActivity.this.keyWord = arg0.toString();
                Log.d(SearchActivity.TAG, "call afterTextChanged:" + arg0.toString());
                if (!SearchActivity.this.keyWord.equals(StatConstants.MTA_COOPERATION_TAG)) {
                    new Thread() {
                        public void run() {
                            SearchActivity access$0 = SearchActivity.this;
                            int tag = access$0.querySuggestWordTag + 1;
                            access$0.querySuggestWordTag = tag;
                            try {
                                SearchActivity.this.suggestwordsCursor = SearchActivity.this.getContentResolver().query(Uri.parse(new StringBuilder(SearchActivity.SUGGESTWORDS_URL).append(Uri.encode(SearchActivity.this.keyWord)).toString()), null, null, null, null);
                                if (SearchActivity.this.checkSuggestItSelf(SearchActivity.this.suggestwordsCursor, SearchActivity.this.keyWord)) {
                                    SearchActivity.this.suggestwordsCursor = null;
                                }
                                Message msg = SearchActivity.this.handler.obtainMessage();
                                msg.what = 2;
                                msg.arg1 = tag;
                                SearchActivity.this.handler.sendMessage(msg);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.d(SearchActivity.TAG, "call cloudsearch interface error");
                            }
                        }
                    }.start();
                    return;
                }
                Message msg = SearchActivity.this.handler.obtainMessage();
                msg.what = 2;
                SearchActivity searchActivity = SearchActivity.this;
                int access$15 = searchActivity.querySuggestWordTag + 1;
                searchActivity.querySuggestWordTag = access$15;
                msg.arg1 = access$15;
                SearchActivity.this.handler.sendMessage(msg);
            }
        });
        this.searchWordEditText.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case 19:
                        Log.d(SearchActivity.TAG, "press key up vid:" + v.getId());
                        if (event.getAction() == 0 && SearchActivity.this.mSearchSuggestPopupWindow != null && SearchActivity.this.mSearchSuggestPopupWindow.isShowing()) {
                            Log.d(SearchActivity.TAG, "request up focus");
                            SearchActivity.this.mSearchSuggestPopupWindow.requestUpFocus();
                            return true;
                        }
                    case 20:
                        Log.d(SearchActivity.TAG, "press key down vid:" + v.getId());
                        if (event.getAction() == 0 && SearchActivity.this.mSearchSuggestPopupWindow != null && SearchActivity.this.mSearchSuggestPopupWindow.isShowing()) {
                            Log.d(SearchActivity.TAG, "request down focus");
                            SearchActivity.this.mSearchSuggestPopupWindow.requestDownFocus();
                            return true;
                        }
                    case 21:
                        Log.d(SearchActivity.TAG, "press key left vid:" + v.getId());
                        return false;
                }
                return false;
            }
        });
        this.searchWordEditText.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (SearchActivity.this.hasSelectedSuggestWord) {
                    SearchActivity.this.hasSelectedSuggestWord = false;
                    return;
                }
                if (SearchActivity.this.mSearchSuggestPopupWindow != null && SearchActivity.this.mSearchSuggestPopupWindow.isShowing()) {
                    String word = SearchActivity.this.mSearchSuggestPopupWindow.getSelectedSuggestWord();
                    if (word != null) {
                        SearchActivity.this.searchWordEditText.setText(word);
                        SearchActivity.this.searchWordEditText.setSelection(word.length());
                        SearchActivity.this.mSearchSuggestPopupWindow.dismiss();
                        SearchActivity.this.hasSelectedSuggestWord = true;
                        return;
                    }
                }
                Log.d(SearchActivity.TAG, "call inputmethod");
                ((InputMethodManager) SearchActivity.this.getSystemService("input_method")).showSoftInput(v, 0);
            }
        });
        this.searchWordEditText.setOnEditorActionListener(new OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case 3:
                        SearchActivity.this.searchWord(SearchActivity.this.searchWordEditText.getText().toString());
                        return true;
                    default:
                        return false;
                }
            }
        });
        this.searchWordEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus && SearchActivity.this.mSearchSuggestPopupWindow != null && SearchActivity.this.mSearchSuggestPopupWindow.isShowing()) {
                    SearchActivity.this.mSearchSuggestPopupWindow.dismiss();
                }
            }
        });
        this.searchWordButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String word = SearchActivity.this.searchWordEditText.getText().toString();
                if (word.equals(StatConstants.MTA_COOPERATION_TAG)) {
                    SearchActivity.this.searchWordEditText.requestFocus();
                } else {
                    SearchActivity.this.searchWord(word);
                }
            }
        });
        this.refreshHotWordsTextView.setFocusable(true);
        this.refreshHotWordsTextView.setFocusableInTouchMode(true);
        this.refreshHotWordsTextView.getPaint().setFlags(8);
        this.refreshHotWordsTextView.getPaint().setAntiAlias(true);
        this.refreshHotWordsTextView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                SearchActivity.this.initHotWords();
            }
        });
    }

    /* access modifiers changed from: private */
    public void searchWord(String word) {
        Intent intent = new Intent("com.konka.cloudsearch.fromwidget");
        intent.setClassName("com.konka.cloudsearch", "com.konka.cloudsearch.SearchBoxActivity");
        intent.putExtra("query", word);
        startActivity(intent);
    }

    private List<String> getSuggestWords() {
        List<String> wordsList = new ArrayList<>();
        if (this.suggestwordsCursor == null || this.suggestwordsCursor.getCount() == 0) {
            Log.d(TAG, "get suggest words failed");
            return null;
        } else if (!this.suggestwordsCursor.moveToFirst()) {
            return wordsList;
        } else {
            do {
                wordsList.add(this.suggestwordsCursor.getString(1));
                Log.d(TAG, "words:" + this.suggestwordsCursor.getString(1));
            } while (this.suggestwordsCursor.moveToNext());
            return wordsList;
        }
    }

    /* access modifiers changed from: private */
    public void initHotWords() {
        this.queryHotWordsCount = 0;
        this.searchHotWordsHintLayout.setVisibility(0);
        this.searchHotWordsHintTextView.setVisibility(0);
        this.refreshHotWordsTextView.setVisibility(8);
        this.searchHotWordsHintTextView.setText(getResources().getString(R.string.hotwords_getting));
        queryHotWords();
    }

    /* access modifiers changed from: private */
    public void queryHotWords() {
        new Thread() {
            public void run() {
                SearchActivity.this.hotwordsCursor = null;
                try {
                    SearchActivity.this.hotwordsCursor = SearchActivity.this.getContentResolver().query(Uri.parse(SearchActivity.HOTWORDS_URL), null, null, null, null);
                    SearchActivity.this.handler.sendEmptyMessage(1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(SearchActivity.TAG, "call cloudsearch interface error");
                    SearchActivity.this.handler.sendEmptyMessage(0);
                }
            }
        }.start();
    }

    public void clearSuggestWordWindow() {
        this.mSearchSuggestPopupWindow = null;
    }

    /* access modifiers changed from: private */
    public boolean checkSuggestItSelf(Cursor cursor, String word) {
        if (cursor == null || cursor.getCount() != 1 || !cursor.moveToFirst() || !cursor.getString(1).equals(word)) {
            return false;
        }
        return true;
    }

    public void setSelectedSuggestWord(String word) {
        this.searchWordEditText.setText(word);
        this.searchWordEditText.setSelection(word.length());
    }

    private Rect getTextBounds(String text, int size) {
        Rect rect = new Rect();
        Paint paint = new Paint();
        paint.setTextSize((float) size);
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect;
    }

    /* access modifiers changed from: private */
    public float measureText(String text, int size) {
        if (text == null || size <= 0) {
            return FlyingIcon.ANGULAR_VMIN;
        }
        Paint paint = new Paint();
        paint.setTextSize((float) size);
        return paint.measureText(text);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (this.mSearchSuggestPopupWindow == null || !this.mSearchSuggestPopupWindow.isShowing()) {
                    finish();
                } else {
                    this.mSearchSuggestPopupWindow.dismiss();
                }
                return true;
            default:
                Log.d(TAG, "keycode:" + keyCode);
                return super.onKeyDown(keyCode, event);
        }
    }
}
