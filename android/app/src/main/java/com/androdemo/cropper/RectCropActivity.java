package com.androdemo.cropper;

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
    static final int NONE = 0;
    static final int DRAG = 1;
    static final int ZOOM = 2;
    // These matrices will be used to move and zoom image
    Matrix matrix = new Matrix();
    Matrix initialMatrix = null;
    Matrix savedMatrix = new Matrix();
    int mode = NONE;

    // PinchZoom Imageview variables
    // Remember some things for zooming
    PointF start = new PointF();
    PointF mid = new PointF();
    float oldDist = 1f;
    String savedItemClicked;
    float matrixX = 0; // X coordinate of matrix inside the ImageView
    float matrixY = 0; // Y coordinate of matrix inside the ImageView
    float width = 0; // width of drawable
    float height = 0; // height of drawable

    private String imagePath = "";
    private ImageView profilePicture, overlapView;
    private AppCompatSeekBar seekZoomController;
    private CardView btnCancel, btnDone;
    private LinearLayout layCropper;
    private String TAG = "ImageLoadActivity";
    private float dx; // postTranslate X distance
    private float dy; // postTranslate Y distance
    private float old_dx = 0; // postTranslate X distance
    private float old_dy = 0; // postTranslate Y distance
    private float defaultValue = 0.25f;

    private boolean valueSet = false;
    private float[] matrixValues = new float[9];
    private float[] matrixTest = new float[9];
    private Bitmap croppedBitmap = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rect_crop);

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
        layCropper.setDrawingCacheEnabled(true);

        createDirectory("");
        profilePicture.setOnTouchListener(this);

        btnCancel.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.putExtra(Constant.kKEY, imagePath);

                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                croppedBitmap = cropImage(layCropper);

                String resultBitmap;
                if (croppedBitmap != null)
                {
                    resultBitmap = saveBitmapToFile(croppedBitmap).toString();
                    //                    resultBitmap = getBase64ArrayFromBitmap(croppedBitmap);
                }
                else
                {
                    resultBitmap = "null";
                }

                Intent intent = new Intent();
                intent.putExtra(Constant.kPROFILE_IMAGE, resultBitmap);
                intent.putExtra(Constant.kAVATAR_IMAGE, getIntent().getStringExtra(Constant.kAVATAR_IMAGE));

                setResult(RESULT_OK, intent);
                finish();

            }
        });

        seekZoomController.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                if (fromUser)
                {
                    float scale = getScaleFromProgressValue(progress);
                    matrix.getValues(matrixTest);
                    matrix.setScale(scale, scale,
                                    matrixTest[Matrix.MTRANS_X],
                                    matrixTest[Matrix.MTRANS_Y]);

                    profilePicture.setImageMatrix(matrix);

                    matrix.getValues(matrixTest);
                    float postScaleX = ((overlapView.getWidth()/2) - ((scale*profilePicture.getDrawable().getIntrinsicWidth())/2)) * scale ;
                    float postScaleY = ((overlapView.getHeight()/2) - ((scale*profilePicture.getDrawable().getIntrinsicHeight())/2)) *scale;

                    matrix.postTranslate(postScaleX,postScaleY);
                    if(scale == defaultValue){
                        profilePicture.setImageMatrix(initialMatrix);
                    }else{

                        profilePicture.setImageMatrix(matrix);
                    }
                }
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
                             Log.d(TAG, "Matrix : " + profilePicture.getMatrix());

                             new Handler().postDelayed(new Runnable()
                             {
                                 @Override
                                 public void run()
                                 {
                                     Log.v(TAG, "Initail position >>> : " + profilePicture.getWidth() + "--------" +  overlapView.getWidth() + "--------" +  profilePicture.getHeight() + "--------" +  overlapView.getHeight() );

                                     matrix.setScale(defaultValue, defaultValue,
                                                     (overlapView.getWidth()/2) - ((defaultValue*profilePicture.getDrawable().getIntrinsicWidth())/2),
                                                     (overlapView.getHeight()/2) - ((defaultValue*profilePicture.getDrawable().getIntrinsicHeight())/2));


                                     profilePicture.setImageMatrix(matrix);
                                     matrix.getValues(matrixTest);
                                     float postScaleX = ((overlapView.getWidth()/2) - ((defaultValue*profilePicture.getDrawable().getIntrinsicWidth())/2)) * defaultValue ;
                                     float postScaleY = ((overlapView.getHeight()/2) - ((defaultValue*profilePicture.getDrawable().getIntrinsicHeight())/2)) *defaultValue;


                                     matrix.postTranslate(postScaleX,postScaleY);
                                     profilePicture.setImageMatrix(matrix);
                                     if(initialMatrix== null){
                                         Matrix matrix = profilePicture.getImageMatrix();
                                         initialMatrix= matrix;
                                     }
                                 }
                             }, 200);

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


    private String getBase64ArrayFromBitmap(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    private float getScaleFromProgressValue(int scaleValue)
    {
        float progressValue = ((10f + scaleValue) / 50f);
        Log.e("Reverse", progressValue + "");
        return progressValue;
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

                    /*//Customized logic with boundaries.
                    matrix.set(savedMatrix);

                    matrix.getValues(matrixValues);
                    matrixX = matrixValues[2];
                    matrixY = matrixValues[5];
                    width = matrixValues[0] * (((ImageView) view).getDrawable()
                                                                 .getIntrinsicWidth());
                    height = matrixValues[4] * (((ImageView) view).getDrawable()
                                                                  .getIntrinsicHeight());

                    dx = event.getX() - start.x;
                    dy = event.getY() - start.y;

                    if (!valueSet)
                    {
                        old_dx = dx;
                        old_dy = dy;
                        valueSet = !valueSet;
                    }

                    //if image will go outside left bound
                    if (matrixX + dx < 0)
                    {
                        dx = -matrixX;
                    }
                    //if image will go outside right bound
                    if (matrixX + dx + width > view.getWidth())
                    {
                        dx = view.getWidth() - matrixX - width;
                    }
                    //if image will go oustside top bound
                    if (matrixY + dy < 0)
                    {
                        dy = -matrixY;
                    }
                    //if image will go outside bottom bound
                    if (matrixY + dy + height > view.getHeight())
                    {
                        dy = view.getHeight() - matrixY - height;
                    }

                    boolean update = false;
                    //Direction Detection
                    if (dx - old_dx > 0)
                    {
                        update = true;
                        Log.e("Diff", (matrixX + event.getX()) + "/" + layCropper.getLeft());
                        Log.e("DirectionSide", "Right");
                    }
                    else if (dx - old_dx < 0)
                    {
                        update = true;
                        Log.e("DirectionSide", "Left");
                    }
                    else if (dy - old_dy > 0)
                    {
                        update = true;
                        Log.e("Direction", "Down");
                    }
                    else if (dy - old_dy < 0)
                    {
                        update = true;
                        Log.e("Direction", "Up");
                    }

                    if (update)
                    {
                        matrix.postTranslate(dx, dy);
                    }*/

                    // Original logic without boundaries
                    matrix.set(savedMatrix);
                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
                }
                else if (mode == ZOOM)
                {

                    float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 5f)
                    {
                        float scale = newDist / oldDist;
                        if (scale <= 2.2)
                        {
                            matrix.getValues(matrixValues);

                            Log.d("Scale", scale + " SCALE_X" + matrixValues[Matrix.MSCALE_X]);

                            if (matrixValues[Matrix.MSCALE_X] > defaultValue
                                || scale > 1)
                            {
                                matrix.set(savedMatrix);
                                matrix.postScale(scale, scale, mid.x, mid.y);
                                Log.d("Applied Scale", scale + "Mix X&Y : " + mid.x + " / " + mid.y);
                            }

                            matrix.getValues(matrixTest);
                            getProgressFromScaleValue(matrixTest[Matrix.MSCALE_X]);
                        }
                    }

                    //Original logic without scale limit
                    /*float newDist = spacing(event);
                    Log.d(TAG, "newDist=" + newDist);
                    if (newDist > 10f)
                    {
                        matrix.set(savedMatrix);
                        float scale = newDist / oldDist;
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }*/
                }
                break;
        }

        view.setImageMatrix(matrix);
        view.getImageMatrix().getValues(matrixTest);

        old_dx = dx;
        old_dy = dy;

        int[] position = getBitmapOffset(view, false);
        //        int[] positionCropper = getBitmapOffset(imgRef, false);

        Log.d("YYYY", position[0] + "/ " + position[1]);
        //        Log.d("YYYYY", positionCropper[0] + "/ " + positionCropper[1]);
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

    private int getProgressFromScaleValue(float scaleValue)
    {
        int progressValue = Math.round((50 * scaleValue) - 10);
        Log.e("Progress", progressValue + "");
        seekZoomController.setProgress(progressValue);
        return progressValue;
    }

    public int[] getBitmapOffset(ImageView img, Boolean includeLayout)
    {
        int[] offset = new int[2];
        float[] values = new float[9];

        Matrix m = img.getImageMatrix();
        m.getValues(values);

        offset[0] = (int) values[5];
        offset[1] = (int) values[2];

        if (includeLayout)
        {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) img.getLayoutParams();
            int paddingTop = (int) (img.getPaddingTop());
            int paddingLeft = (int) (img.getPaddingLeft());

            offset[0] += paddingTop + lp.topMargin;
            offset[1] += paddingLeft + lp.leftMargin;
        }
        return offset;
    }

    public int[] getLayoutOffset(LinearLayout img, Boolean includeLayout)
    {
        int[] offset = new int[2];
        float[] values = new float[9];

        Matrix m = img.getMatrix();
        m.getValues(values);

        offset[0] = (int) values[5];
        offset[1] = (int) values[2];

        if (includeLayout)
        {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) img.getLayoutParams();
            int paddingTop = (int) (img.getPaddingTop());
            int paddingLeft = (int) (img.getPaddingLeft());

            offset[0] += paddingTop + lp.topMargin;
            offset[1] += paddingLeft + lp.leftMargin;
        }
        return offset;
    }
}
