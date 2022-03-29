package com.visualanalyst

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {

    private var flagimg = 0
    private var mCropImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        flagimg = -1

    }

    fun fromGallery(view: View?) {
        val photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, SELECT_PICTURE) // method 1, get from gallery
        flagimg = 0
    }

    fun fromCamera(view: View?) {
        /* Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST); // get from camera*/
        flagimg = 1
        startActivity(Intent(this@MainActivity, CameraActivity::class.java))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (flagimg == 1) {
            if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
                val photo = data!!.extras!!["data"] as Bitmap?
                Utility.bitmap = photo
                mCropImageUri = data.extras!!["data"] as Uri?
                if (CropImage.isReadExternalStoragePermissionsRequired(this, mCropImageUri!!)) {
                    // request permissions and handle the result in onRequestPermissionsResult()
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                } else {
                    // no permissions required or already grunted, can start crop image activity
                    startCropImageActivity(mCropImageUri)
                }
            }
        } // handle result of CropImageActivity
        else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
//                ((Button) findViewById(R.id.open_camera)).setImageURI(result.getUri());
                // Toast.makeText(this, "Cropping successful, Sample: " + result.getUri(), Toast.LENGTH_LONG).show();
                Utility.uri = result.uri
                val intent = Intent(this@MainActivity, UploadImage::class.java)
                startActivity(intent)
                finish()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG).show()
            }
        } // end of if
        else {
            if (resultCode == RESULT_OK) {
                try {
                    mCropImageUri = data!!.data
                    //                    CropImage.activity(imageUri);
                    startCropImageActivity(mCropImageUri)
                    val imageStream = contentResolver.openInputStream(
                        mCropImageUri!!
                    )
                    val selectedImage = BitmapFactory.decodeStream(imageStream)
                    Utility.bitmap = selectedImage
                    if (CropImage.isReadExternalStoragePermissionsRequired(this, mCropImageUri!!)) {
                        // request permissions and handle the result in onRequestPermissionsResult()
                        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
                    } else {
                        // no permissions required or already grunted, can start crop image activity
                        startCropImageActivity(mCropImageUri)
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(this@MainActivity, "Something went wrong", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(this@MainActivity, "You haven't picked Image", Toast.LENGTH_LONG)
                    .show()
            }
        } // end of else
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (mCropImageUri != null && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri)
        } else {
            Toast.makeText(
                this,
                "Cancelling, required permissions are not granted",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun startCropImageActivity(imageUri: Uri?) {
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .start(this)
    }



    override fun finish() {
        super.finish()
        overridePendingTransitionExit()
    }

    override fun startActivity(intent: Intent) {
        super.startActivity(intent)
        overridePendingTransitionEnter()

//        overridePendingTransitionExit();
    }

    protected fun overridePendingTransitionEnter() {
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    /**
     * Overrides the pending Activity transition by performing the "Exit" animation.
     */
    protected fun overridePendingTransitionExit() {
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
    }

    override fun onBackPressed() {
//        super.onBackPressed();
        AlertDialog.Builder(this@MainActivity).setTitle("Alert").setMessage("Do you want to Exit?")
            .setPositiveButton("Yes") { dialog, which -> finish() }
            .setNegativeButton("No") { dialog, which -> dialog.dismiss() }.show()
    }

    companion object {
        private const val SELECT_PICTURE = 1
        private const val CAMERA_REQUEST = 1888
        private val thebitmap: Bitmap? = null
    }
}