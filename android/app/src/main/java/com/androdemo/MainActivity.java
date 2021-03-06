package com.androdemo;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.androdemo.constants.Constant;
import com.androdemo.cropper.ImageLoadActivity;
import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;


public class MainActivity extends ReactActivity
{

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */

    @Override
    protected String getMainComponentName()
    {
        return "AndroDemo";
    }

    @Override
    @ReactMethod
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
        {
            switch (requestCode)
            {
                case Constant.IMAGE_CROP:

                    if (data != null)
                    {
                        Promise promise = ToastModule.mPromises.get(requestCode);

                        if (promise != null)
                        {
                            WritableMap result = new WritableNativeMap();
                            result.putString(Constant.kAVATAR_IMAGE, data.getStringExtra(Constant.kAVATAR_IMAGE));
                            result.putString(Constant.kPROFILE_IMAGE, data.getStringExtra(Constant.kPROFILE_IMAGE));
                            promise.resolve(result);

                        }
                    }

                    break;
            }
        }
    }
}
