package com.androdemo.cropper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.androdemo.R;
import com.androdemo.constants.Constant;
import com.bumptech.glide.Glide;
import com.facebook.react.ReactActivity;


public class ImageLoadActivity extends ReactActivity
{

    private String imagePath = "";
    private ImageView profilePicture;
    private AppCompatSeekBar seekZoomController;
    private CardView btnCancel,btnDone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_loading);

        init();
        getImagePath();
    }

    private void init()
    {
        profilePicture = findViewById(R.id.profilePicture);
        seekZoomController = findViewById(R.id.seekZoomController);
        btnCancel = findViewById(R.id.btnCancel);
        btnDone = findViewById(R.id.btnDone);

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(ImageLoadActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra(Constant.kKEY, imagePath);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        seekZoomController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                float scale = ((progress / 10.0f) + 1);
                profilePicture.setScaleX(scale);
                profilePicture.setScaleY(scale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }

    private void getImagePath()
    {
        if (getIntent().hasExtra(Constant.kIMAGE_PATH))
        {
            imagePath = getIntent().getStringExtra(Constant.kIMAGE_PATH);

            if (imagePath != null)
            {
                Log.v("Verbose", "Setting path : " + imagePath);

                Glide.with(this)
                     .load(imagePath)
                     .into(profilePicture);
            }
            else
            {
                Log.v("Error", "Image path is null");
            }
        }
    }
}
