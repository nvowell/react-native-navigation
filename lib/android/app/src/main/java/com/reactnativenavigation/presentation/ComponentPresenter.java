package com.reactnativenavigation.presentation;

import com.reactnativenavigation.parse.Options;
import com.reactnativenavigation.views.ComponentLayout;

public class ComponentPresenter {
    public Options defaultOptions;

    public ComponentPresenter(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void setDefaultOptions(Options defaultOptions) {
        this.defaultOptions = defaultOptions;
    }

    public void applyOptions(ComponentLayout view, Options options) {
        mergeBackgroundColor(view, options);
    }

    public void mergeOptions(ComponentLayout view, Options options) {
        if (options.overlayOptions.interceptTouchOutside.hasValue()) view.setInterceptTouchOutside(options.overlayOptions.interceptTouchOutside);
        if (options.overlayOptions.touchActive.hasValue()) view.setTouchActive(options.overlayOptions.touchActive);
        if (options.overlayOptions.left.hasValue() &&
            options.overlayOptions.top.hasValue() &&
            options.overlayOptions.right.hasValue() &&
            options.overlayOptions.bottom.hasValue()) {
                view.setHitRect(
                    options.overlayOptions.left,
                    options.overlayOptions.top,
                    options.overlayOptions.right,
                    options.overlayOptions.bottom);
        }
        mergeBackgroundColor(view, options);
    }

    private void mergeBackgroundColor(ComponentLayout view, Options options) {
        if (options.layout.componentBackgroundColor.hasValue()) {
            view.setBackgroundColor(options.layout.componentBackgroundColor.get());
        }
    }
}
