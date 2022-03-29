package com.visualanalyst

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class Crop : AppCompatActivity() {
    private var openCameraBtn: Button? = null
    private var openGalleryBtn: Button? = null

    /**
     * Persist URI image to crop URI if specific permissions are required
     */
    private var mCropImageUri: Uri? = null

    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop)
        openCameraBtn = findViewById(R.id.open_camera)
        openGalleryBtn = findViewById(R.id.open_Gallery)
        openCameraBtn?.setOnClickListener {
            CropImage.startPickImageActivity(
                this@Crop
            )
        }


    }


    @SuppressLint("NewApi", "WrongViewCast")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = CropImage.getPickImageResultUri(this, data)

            // For API >= 23 we need to check specifically cropthat we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri)
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
//                ((Button) findViewById(R.id.open_camera)).setImageURI(result.getUri());
                // Toast.makeText(this, "Cropping successful, Sample: " + result.getUri(), Toast.LENGTH_LONG).show();
                Utility.uri = result.uri
                val intent = Intent(this@Crop, UploadImage::class.java)
                startActivity(intent)
                finish()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (mCropImageUri != null && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri!!)
        } else {
            Toast.makeText(
                this,
                "Cancelling, required permissions are not granted",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Start crop image activity for the given image.
     */
    private fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .start(this)
    }
}