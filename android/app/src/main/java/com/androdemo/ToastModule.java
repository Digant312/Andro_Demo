// ToastModule.java

package com.androdemo;

import android.content.Intent;
import android.util.SparseArray;
import android.widget.Toast;

import com.androdemo.cropper.ImageLoadActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;


public class ToastModule extends ReactContextBaseJavaModule
{

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    public static final int IMAGE_CROP = 1000;
    public static SparseArray<Promise> mPromises;

    public ToastModule(ReactApplicationContext reactContext)
    {
        super(reactContext);
        mPromises = new SparseArray<>();
    }

    @Override
    public String getName()
    {
        return "ToastExample";
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
        Intent intent = new Intent(getReactApplicationContext(), ImageLoadActivity.class);
        getReactApplicationContext().startActivityForResult(intent, IMAGE_CROP, null);
//        Toast.makeText(getReactApplicationContext(), message, duration).show();

//        promise.resolve("Resolved.");
        mPromises.put(IMAGE_CROP,promise);
        if (promise != null)
        {
            Toast.makeText(getReactApplicationContext(), "Promise is not empty.", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getReactApplicationContext(), "Promise is null.", Toast.LENGTH_SHORT).show();
        }
    }
}