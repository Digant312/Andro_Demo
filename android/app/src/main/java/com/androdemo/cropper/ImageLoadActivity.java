package com.androdemo.cropper;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androdemo.R;


public class ImageLoadActivity extends AppCompatActivity
{

    Button btnSelectImage;

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
                Toast.makeText(ImageLoadActivity.this, "Diglo doffo", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
