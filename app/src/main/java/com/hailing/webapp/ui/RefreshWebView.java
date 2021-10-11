package com.hailing.webapp.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.webkit.WebView;

public class RefreshWebView extends WebView {

    private ViewGroup viewGroup;

    public RefreshWebView(Context context) {
        super(context);
    }

    public RefreshWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup getViewGroup() {
        return viewGroup;
    }

    public void setViewGroup(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (this.getScrollY() <= 0) {
                this.scrollTo(0, 1);
            }
        }
        return super.onTouchEvent(event);
    }
}