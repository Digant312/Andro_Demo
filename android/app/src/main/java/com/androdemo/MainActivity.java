package com.androdemo;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import com.androdemo.cropper.ImageLoadActivity;
import com.facebook.react.ReactActivity;

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "AndroDemo";
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){

                case ToastModule.IMAGE_CROP:

                    if(data!=null){
                        String value = data.getStringExtra(ImageLoadActivity.kKEY);

                        Toast.makeText(this, value!=null? value : "null value returned", Toast.LENGTH_SHORT).show();
                    }

                    break;
            }
        }

    }
}
