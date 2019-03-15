package com.androdemo.cropper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.androdemo.R;
import com.facebook.react.ReactActivity;


public class ImageLoadActivity extends ReactActivity
{

    Button btnSelectImage;
    public static String kKEY = "Key";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loading);

        init();
    }

    private void init()
    {
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSelectImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra("Key","Path to cropped image");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
