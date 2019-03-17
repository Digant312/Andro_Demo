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
import android.util.Log;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
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
import com.facebook.react.ReactActivity;


public class ImageLoadActivity extends ReactActivity implements View.OnTouchListener
{

    // We can be in one of these 3 states
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();
    int mode = NONE;

    // PinchZoom Imageview variables
    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String savedItemClicked;
    private String imagePath = "";
    private ImageView profilePicture, overlapView, imgViewCropped;
    private AppCompatSeekBar seekZoomController;
    private CardView btnCancel, btnDone;
    private LinearLayout layCropper;
    private String TAG = "ImageLoadActivity";
    private Context mContext;

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
        mContext = this;

        profilePicture = findViewById(R.id.profilePicture);
        overlapView = findViewById(R.id.overlapView);
        seekZoomController = findViewById(R.id.seekZoomController);
        btnCancel = findViewById(R.id.btnCancel);
        btnDone = findViewById(R.id.btnDone);
        layCropper = findViewById(R.id.layCropper);
        imgViewCropped = findViewById(R.id.imgViewCropped);

        profilePicture.setOnTouchListener(this);

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                cropImage();
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
                     .listener(new RequestListener<Drawable>()
                     {
                         @Override
                         public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource)
                         {
                             return false;
                         }

                         @Override
                         public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource,
                                                        boolean isFirstResource)
                         {
                             Log.d(TAG, resource.getIntrinsicWidth() + " " + resource.getBounds().height());

                             /*new Handler().postDelayed(new Runnable()
                             {
                                 @Override
                                 public void run()
                                 {
                                     profilePicture.setScaleX(mContext.getResources().getDimension(R.dimen._10sdp));
                                     profilePicture.setScaleY(mContext.getResources().getDimension(R.dimen._10sdp));
                                     Log.d(TAG + "X", profilePicture.getScaleX() + " " + mContext.getResources().getDimension(R.dimen._100sdp));
                                     Log.d(TAG + "Y", profilePicture.getScaleY() + " " + mContext.getResources().getDimension(R.dimen._100sdp));
                                 }
                             }, 500);*/

                             return false;
                         }
                     })
                     .into(profilePicture);
            }
            else
            {
                Log.v("Error", "Image path is null");
            }
        }
    }

    private void cropImage()
    {

        Bitmap bitmap = Bitmap.createBitmap(layCropper.getWidth(), layCropper.getHeight(), Bitmap.Config.ARGB_8888);
        int[] locationOfWindow = new int[2];

        layCropper.getLocationInWindow(locationOfWindow);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            PixelCopy.request(getWindow(),
                              new Rect(locationOfWindow[0], locationOfWindow[1],
                                       locationOfWindow[0] + layCropper.getWidth(), locationOfWindow[1] + layCropper.getHeight())
                , bitmap, new PixelCopy.OnPixelCopyFinishedListener()
                {
                    @Override
                    public void onPixelCopyFinished(int copyResult)
                    {

                    }
                }, new Handler());
        }
        else
        {
            layCropper.buildDrawingCache();
            bitmap = layCropper.getDrawingCache();
        }

        Glide.with(this)
             .load(getclip(bitmap))
             .into(imgViewCropped);
    }

    public static Bitmap getclip(Bitmap bitmap)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                                            bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                          bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        ImageView view = (ImageView) v;
        dumpEvent(event);

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK)
        {
            case MotionEvent.ACTION_DOWN:
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG");
                mode = DRAG;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                Log.d(TAG, "oldDist=" + oldDist);
                if (oldDist > 10f)
                {
                    savedMatrix.set(matrix);
                    midPoint(mid, event);
                    mode = ZOOM;
                    Log.d(TAG, "mode=ZOOM");
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                mode = NONE;
                Log.d(TAG, "mode=NONE");
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == DRAG)
                {
                    // ...
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY()
                                                                 - start.y);
                }
                else if (mode == ZOOM)
                {
                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 10f)
                    {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                }
                break;
        }

        view.setImageMatrix(matrix);
        return true;
    }

    private void dumpEvent(MotionEvent event)
    {
        String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE",
                          "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
            || actionCode == MotionEvent.ACTION_POINTER_UP)
        {
            sb.append("(pid ").append(
                action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }
        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++)
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
            {
                sb.append(";");
            }
        }
        sb.append("]");
        Log.d(TAG, sb.toString());
    }

    /** Determine the space between the first two fingers */
    private float spacing(MotionEvent event)
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /** Calculate the mid point of the first two fingers */
    private void midPoint(PointF point, MotionEvent event)
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }
}
