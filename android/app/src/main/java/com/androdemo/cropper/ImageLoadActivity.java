package com.androdemo.cropper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.androdemo.R;
import com.androdemo.constants.Constant;
import com.bumptech.glide.Glide;
import com.facebook.react.ReactActivity;


public class ImageLoadActivity extends ReactActivity
{

    Button btnSelectImage;
    String imagePath="";
    ImageView profilePicture;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loading);

        init();
        getImagePath();
    }

    private void getImagePath(){
        if(getIntent().hasExtra(Constant.kIMAGE_PATH)){
            imagePath = getIntent().getStringExtra(Constant.kIMAGE_PATH);

            if(imagePath!=null){
                Log.v("Verbose","Setting path : "+imagePath);

                Glide.with(this)
                     .load(imagePath)
                     .into(profilePicture);
            }else{
                Log.v("Error","Image path is null");
            }
        }

    }

    private void init()
    {
        btnSelectImage = findViewById(R.id.btnSelectImage);
        profilePicture = findViewById(R.id.profilePicture);

        btnSelectImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra(Constant.kKEY, "Path to cropped image");
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
