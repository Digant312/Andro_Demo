// RectCropModule.java

package com.androdemo;

import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.androdemo.constants.Constant;
import com.androdemo.cropper.RectCropActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;


public class RectCropModule extends ReactContextBaseJavaModule
{

    public static final int IMAGE_CROP = 1000;
    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    public static SparseArray<Promise> mPromises;

    public RectCropModule(ReactApplicationContext reactContext)
    {
        super(reactContext);
        mPromises = new SparseArray<>();
    }

    @Override
    public String getName()
    {
        return "RectCropper";
    }

    @Override
    public Map<String, Object> getConstants()
    {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void show(String message, int duration, Promise promise)
    {
        Log.d("ImagePath", message);

        Intent intent = new Intent(getReactApplicationContext(), RectCropActivity.class);
        intent.putExtra(Constant.kIMAGE_PATH, message);
        getReactApplicationContext().startActivityForResult(intent, RECT_CROP, null);

        mPromises.put(RECT_CROP, promise);
    }
}