package com.visualanalyst

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.visualanalyst.MainActivity

class ObjectsDetected : AppCompatActivity() {
    var optionthreetext: TextView? = null
    var optiontwotext: TextView? = null
    var optiononetext: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_objects_detected)
        optiononetext = findViewById<View>(R.id.optiononetext) as TextView
        optiontwotext = findViewById<View>(R.id.optiontwotext) as TextView
        optionthreetext = findViewById<View>(R.id.optionthreetext) as TextView
        val txt = intent.extras!!.getString("name", "apple")
        val array = txt.split("\n").toTypedArray()
        optiononetext!!.text = array[1] + ""
        optiontwotext!!.text = array[2] + ""
        optionthreetext!!.text = array[3] + ""
        Toast.makeText(this, "T: $txt", Toast.LENGTH_SHORT).show()
    }

    fun optionOne(view: View?) {
        val intent = Intent(this@ObjectsDetected, UploadImage::class.java)
        intent.putExtra("name", optiononetext!!.text.toString() + "")
        startActivity(intent)
        finish()
    }

    fun optionTwo(view: View?) {
        val intent = Intent(this@ObjectsDetected, UploadImage::class.java)
        intent.putExtra("name", optiontwotext!!.text.toString() + "")
        startActivity(intent)
        finish()
    }

    fun optionThree(view: View?) {
        val intent = Intent(this@ObjectsDetected, UploadImage::class.java)
        intent.putExtra("name", optionthreetext!!.text.toString() + "")
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this@ObjectsDetected).setTitle("Alert")
            .setMessage("Do you want to Exit?").setPositiveButton("Yes") { dialog, which ->
                finish()
                startActivity(Intent(this@ObjectsDetected, MainActivity::class.java))
            }
            .setNegativeButton("No") { dialog, which -> dialog.dismiss() }.show()
    }
}