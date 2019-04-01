// ToastModule.java

package com.androdemo;

import android.content.Intent;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.androdemo.constants.Constant;
import com.androdemo.cropper.BaseActivity;
import com.androdemo.cropper.ImageLoadActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableNativeArray;

import java.util.HashMap;
import java.util.Map;


public class ToastModule extends ReactContextBaseJavaModule {

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";
    public static SparseArray<Promise> mPromises;

    public ToastModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mPromises = new SparseArray<>();
    }

    @Override
    public String getName() {
        return "ToastExample";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void show(String message, int duration, Promise promise) {
        Log.d("ImagePath", message);

        Intent intent = new Intent(getReactApplicationContext(), ImageLoadActivity.class);
        intent.putExtra(Constant.kIMAGE_PATH, message);
        getReactApplicationContext().startActivityForResult(intent, Constant.IMAGE_CROP, null);

        mPromises.put(Constant.IMAGE_CROP, promise);
    }

//    @ReactMethod
//    public void deleteFiles(ReadableNativeArray files) {
//        Log.d("deleteFiles - Array", files.toString());
//
//        for (int i = 0; i < files.size(); i++) {
//            BaseActivity.Companion.deleteFile(files.getString(i));
//        }
//    }

    @ReactMethod
    public void deleteFiles(String files) {
        Log.d("deleteFiles - Single", files.toString());
        BaseActivity.Companion.deleteFile(files.toString());
    }
}