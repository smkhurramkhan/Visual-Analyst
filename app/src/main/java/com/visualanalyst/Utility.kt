package com.visualanalyst

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

object Utility {
    var Name: String? = null
    var Number: String? = null
    var path = "http://circularbyte.com/theapi/"
    var UtilityName = "5cb3648821368.png"
    var bitmap: Bitmap? = null
    var uri: Uri? = null
    fun stringToBitmap(base64: String): Bitmap? {
        if (base64.isEmpty()) return null
        val imageBytes =
            Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    fun bitmapToString(bmp1: Bitmap?): String {
        if (bmp1 == null) return ""
        val bmp = Bitmap.createScaledBitmap(bmp1, 250, 250, false)
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 90, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }
}