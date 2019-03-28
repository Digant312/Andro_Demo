package com.androdemo.cropper

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
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
        mContext=this
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

}