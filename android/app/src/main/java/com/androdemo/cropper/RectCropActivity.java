package com.androdemo.cropper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.androdemo.R;
import com.androdemo.constants.Constant;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.ByteArrayOutputStream;


public class RectCropActivity extends BaseActivity implements View.OnTouchListener
{

    // We can be in one of these 3 states
    // static final int NONE = 0;
    // static final int DRAG = 1;
    // static final int ZOOM = 2;
    // // These matrices will be used to move and zoom image
    // Matrix matrix = new Matrix();
    // Matrix initialMatrix = null;
    // Matrix savedMatrix = new Matrix();
    // int mode = NONE;

    // PinchZoom Imageview variables
    // Remember some things for zooming
    // PointF start = new PointF();
    // PointF mid = new PointF();
    // float oldDist = 1f;
    // String savedItemClicked;
    // float matrixX = 0; // X coordinate of matrix inside the ImageView
    // float matrixY = 0; // Y coordinate of matrix inside the ImageView
    // float width = 0; // width of drawable
    // float height = 0; // height of drawable
    // float currentZoom = 0.2f;

    // private String imagePath = "";
    // private ImageView profilePicture, overlapView;
    // private AppCompatSeekBar seekZoomController;
    // private CardView btnCancel, btnDone;
    // private LinearLayout layCropper;
    // private String TAG = "RectCropActivity";
    // private Context mContext;
    // private float dx; // postTranslate X distance
    // private float dy; // postTranslate Y distance
    // private float old_dx = 0; // postTranslate X distance
    // private float old_dy = 0; // postTranslate Y distance
    // private float defaultValue = 0.2f;

    // private boolean valueSet = false;
    // private float[] matrixValues = new float[9];
    // private float[] matrixTest = new float[9];
    // private Bitmap croppedBitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect_crop);
    }
}
