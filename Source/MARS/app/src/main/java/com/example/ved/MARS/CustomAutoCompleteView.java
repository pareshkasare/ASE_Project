package com.example.ved.MARS;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;

/**
 * Created by Ved on 4/8/2017.
 */

public class CustomAutoCompleteView extends AutoCompleteTextView {
    private ProgressBar mLoadingIndicator;

    private static final int MESSAGE_TEXT_CHANGED = 100;
    private static final int DEFAULT_AUTOCOMPLETE_DELAY = 4000;

    private int mAutoCompleteDelay = DEFAULT_AUTOCOMPLETE_DELAY;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            CustomAutoCompleteView.super.performFiltering((CharSequence) msg.obj, msg.arg1);
        }
    };

    public CustomAutoCompleteView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public CustomAutoCompleteView(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }
    public void setLoadingIndicator(ProgressBar progressBar) {
        mLoadingIndicator = progressBar;
    }
    public void setAutoCompleteDelay(int autoCompleteDelay) {
        mAutoCompleteDelay = autoCompleteDelay;
    }
    @Override
    protected void performFiltering(final CharSequence text, final int keyCode) {
        //String filterText = "";
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
        mHandler.removeMessages(MESSAGE_TEXT_CHANGED);
        System.out.println("delay:"+mAutoCompleteDelay);
        mHandler.sendMessageDelayed(mHandler.obtainMessage(MESSAGE_TEXT_CHANGED, text), mAutoCompleteDelay);
        //super.performFiltering(filterText, keyCode);
    }
    /**
     * After a selection, capture the new value and append to the existing
     * text
     */
    @Override
    public void onFilterComplete(int count) {
        if (mLoadingIndicator != null) {
            mLoadingIndicator.setVisibility(View.GONE);
        }
        super.onFilterComplete(count);
    }
    @Override
    protected void replaceText(final CharSequence text) {
        super.replaceText(text);
    }

}