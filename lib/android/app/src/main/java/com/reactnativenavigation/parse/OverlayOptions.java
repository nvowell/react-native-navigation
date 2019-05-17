package com.reactnativenavigation.parse;

import com.reactnativenavigation.parse.params.Bool;
import com.reactnativenavigation.parse.params.NullBool;
import com.reactnativenavigation.parse.params.Number;
import com.reactnativenavigation.parse.params.NullNumber;
import com.reactnativenavigation.parse.parsers.BoolParser;
import com.reactnativenavigation.parse.parsers.NumberParser;

import org.json.JSONObject;

public class OverlayOptions {
    public Bool interceptTouchOutside = new NullBool();
    public Bool touchActive = new NullBool();

    public Number top = new NullNumber();
    public Number bottom = new NullNumber();
    public Number left = new NullNumber();
    public Number right = new NullNumber();

    public static OverlayOptions parse(JSONObject json) {
        OverlayOptions options = new OverlayOptions();
        if (json == null) return options;

        options.interceptTouchOutside = BoolParser.parse(json, "interceptTouchOutside");
        options.touchActive = BoolParser.parse(json, "touchActive");

        options.top = NumberParser.parse(json, "top");
        options.bottom = NumberParser.parse(json, "bottom");
        options.left = NumberParser.parse(json, "left");
        options.right = NumberParser.parse(json, "right");

        return options;
    }
}
