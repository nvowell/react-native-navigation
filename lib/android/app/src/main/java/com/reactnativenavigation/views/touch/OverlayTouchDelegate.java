package com.reactnativenavigation.views.touch;

import android.graphics.Rect;
import android.support.annotation.VisibleForTesting;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.utils.UiUtils;
import com.reactnativenavigation.viewcontrollers.IReactView;

public class OverlayTouchDelegate {
    private enum TouchLocation {Outside, Inside}
    private final Rect hitRect = new Rect();
    private IReactView reactView;
    private Bool interceptTouchOutside = new NullBool();
    private Bool touchActive = new NullBool();

    private Number left = new NullNumber();
    private Number top = new NullNumber();
    private Number right = new NullNumber();
    private Number bottom = new NullNumber();

    public void setInterceptTouchOutside(Bool interceptTouchOutside) {
        this.interceptTouchOutside = interceptTouchOutside;
    }

    public void setTouchActive(Bool touchActive) {
        this.touchActive = touchActive;
    }

    public void setHitRect(Number left, Number top, Number right, Number bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public OverlayTouchDelegate(IReactView reactView) {
        this.reactView = reactView;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (interceptTouchOutside instanceof NullBool) return false;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                return handleDown(event);
            default:
                reactView.dispatchTouchEventToJs(event);
                return false;
        }
    }

    @VisibleForTesting
    public boolean handleDown(MotionEvent event) {
        TouchLocation location = getTouchLocation(event);
        if (location == TouchLocation.Inside) {
            reactView.dispatchTouchEventToJs(event);
            return false;
        }
        return interceptTouchOutside.isFalseOrUndefined();
    }

    private TouchLocation getTouchLocation(MotionEvent ev) {
        if (touchActive.isFalseOrUndefined()) {
            return TouchLocation.Outside;
        }

        getView((ViewGroup) reactView.asView()).getHitRect(hitRect);

        if (left.hasValue() && top.hasValue() && right.hasValue() && bottom.hasValue()) {
            hitRect.left = left.get();
            hitRect.top = top.get();
            hitRect.right = right.get();
            hitRect.bottom = bottom.get();
        }

        return hitRect.contains((int) ev.getRawX(), (int) ev.getRawY()) ?
                TouchLocation.Inside :
                TouchLocation.Outside;
    }

    private View getView(ViewGroup view) {
        return view.getChildCount() > 0 ? view.getChildAt(0) : view;
    }
}
