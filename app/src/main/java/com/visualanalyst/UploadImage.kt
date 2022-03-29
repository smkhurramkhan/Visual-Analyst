package com.visualanalyst

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.visualanalyst.MainActivity
import com.visualanalyst.WebCrawl
import org.json.JSONException
import org.json.JSONObject

class UploadImage : AppCompatActivity() {
    var imageView: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_image)
        imageView = findViewById<View>(R.id.imagedetect) as ImageView
        imageView!!.setImageURI(Utility.uri)
    }

    fun detectNow(view: View?) {
        val request: StringRequest = object : StringRequest(
            Method.POST,
            resources.getString(R.string.baseUrl),
            Response.Listener { s ->
                var json: JSONObject? = null // create JSON obj from string

                //Log.e("Resultis", "Image: "+Utility.bitmapToString(((BitmapDrawable)imageView.getDrawable()).getBitmap()));
                Log.e("Resultis", "\n$s ")
                try {
                    json = JSONObject(s)
                    val success = json["success"].toString()
                    val msg = json["msg"].toString()
                    val path = json["path"].toString()
                    Toast.makeText(this@UploadImage, path, Toast.LENGTH_SHORT).show()
                    if (success.equals("1", ignoreCase = true)) {
                        Utility.Name = path
                        val intent = Intent(this@UploadImage, WebCrawl::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@UploadImage, "Error Occurred", Toast.LENGTH_SHORT)
                            .show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(
                        this@UploadImage,
                        "Some Json Exception error occurred -> " + e.message,
                        Toast.LENGTH_LONG
                    ).show()
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { volleyError ->
                Toast.makeText(
                    this@UploadImage,
                    "Some error occurred -> $volleyError",
                    Toast.LENGTH_LONG
                ).show()
            }) {
            override fun getParams(): Map<String, String>? {
                val parameters: MutableMap<String, String> = HashMap()
                parameters["image"] =
                    Utility.bitmapToString((imageView!!.drawable as BitmapDrawable).bitmap)
                parameters["name"] = "eyecheck"
                return parameters
            }
        }
        val rQueue = Volley.newRequestQueue(this@UploadImage)
        rQueue.add(request)
    }

    override fun onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder(this@UploadImage).setTitle("Alert").setMessage("Do you want to Exit?")
            .setPositiveButton("Yes") { dialog, which ->
                finish()
                startActivity(Intent(this@UploadImage, MainActivity::class.java))
            }
            .setNegativeButton("No") { dialog, which -> dialog.dismiss() }.show()
    }
}