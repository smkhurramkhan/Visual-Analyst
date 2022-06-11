package com.visualanalyst

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import java.io.IOException

class MainGalleryActivity : AppCompatActivity() {
    private var btnPhoto: ImageButton? = null
    private var photo: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var Labels: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        setContentView(R.layout.activity_gallery_main)
        btnPhoto = findViewById(R.id.btnPhoto)
        photo = findViewById(R.id.photo)
        progressBar = findViewById(R.id.progressBar)
        Labels = findViewById(R.id.Labels)
        btnPhoto?.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_CODE_PICK)
        }
    }

    private fun labelImage(uri: Uri?) {
        try {
            val image = FirebaseVisionImage.fromFilePath(this, uri!!)
            val labeler = FirebaseVision.getInstance().onDeviceImageLabeler
            progressBar!!.visibility = View.VISIBLE
            labeler.processImage(image).addOnSuccessListener { firebaseVisionImageLabels ->
                if (firebaseVisionImageLabels.isEmpty()) {
                    Labels?.text = "No tags detected"
                } else {
                    val sb = StringBuilder(
                        """
    Recognized tags: 
    
    """.trimIndent()
                    )
                    for (i in 1..firebaseVisionImageLabels.size) {
                        sb.append(
                            """
    $i. ${firebaseVisionImageLabels[i - 1].text}
    
    """.trimIndent()
                        )
                    }
                    Labels!!.text = sb.toString()
                    progressBar!!.visibility = View.GONE
                    photo!!.setImageURI(uri)
                }
            }
                .addOnFailureListener {
                    Toast.makeText(
                        this@MainGalleryActivity,
                        "Failed to recognize image", Toast.LENGTH_SHORT
                    ).show()
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_PICK) {
            val uri = data!!.data
            labelImage(uri)
        }
    }

    companion object {
        private const val REQUEST_CODE_PICK = 0
    }
}