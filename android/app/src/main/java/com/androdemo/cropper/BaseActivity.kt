package com.androdemo.cropper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.PixelCopy
import android.widget.ImageView
import android.widget.LinearLayout
import com.androdemo.constants.Constant
import com.facebook.react.ReactActivity
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


abstract class BaseActivity : ReactActivity() {

    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
    }

    fun createDirectory(subDir: String = "") {
        val invoiceDirectory = File(Constant.docPath + subDir)
        if (!invoiceDirectory.exists())
            invoiceDirectory.mkdirs()
    }

    fun saveBitmapToFile(bitmap: Bitmap): Uri {

        var savePath = Constant.docPath

        val filename = generateFileName()
        val dest = File(savePath, filename)

        try {
            val out = FileOutputStream(dest)
            bitmap.compress(Bitmap.CompressFormat.PNG, 70, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Uri.fromFile(dest)
    }

    fun generateFileName(): String {
        val timeStamp = SimpleDateFormat(Constant.yyyyMMdd_HHmmssSSS).format(Date())
        return "PNG_" + timeStamp + "_.png"
    }

    fun cropImage(layCropper: LinearLayout, profilePicture: ImageView): Bitmap {

        var bitmap = Bitmap.createBitmap(layCropper.getWidth(), layCropper.getHeight(), Bitmap.Config.ARGB_8888)
        val locationOfWindow = IntArray(2)

        layCropper.getLocationInWindow(locationOfWindow)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PixelCopy.request(window,
                    Rect(locationOfWindow[0], locationOfWindow[1],
                            locationOfWindow[0] + layCropper.getWidth(), locationOfWindow[1] + layCropper.getHeight()), bitmap, { }, Handler())
        } else {
            profilePicture.isDrawingCacheEnabled = true
            profilePicture.buildDrawingCache()

            bitmap = profilePicture.drawingCache

            val output = Bitmap.createBitmap(layCropper.width,
                    layCropper.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(output)
            val rect = Rect(layCropper.left, layCropper.top, layCropper.right, layCropper.bottom)
            val paint = Paint()
            canvas.drawBitmap(bitmap, rect, Rect(0, 0, layCropper.width, layCropper.height), paint)
            bitmap = output
        }

        return bitmap
    }

}