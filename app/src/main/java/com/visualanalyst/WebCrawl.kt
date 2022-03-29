package com.visualanalyst

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class WebCrawl : AppCompatActivity() {
    var myWebView: WebView? = null
    val FINGER_RELEASED = 0
    val FINGER_TOUCHED = 1
    val FINGER_DRAGGING = 2
    val FINGER_UNDEFINED = 3
    private var fingerState = FINGER_RELEASED

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_crawl)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        myWebView = findViewById(R.id.webnews)
        myWebView?.settings?.loadsImagesAutomatically = true
        myWebView?.settings?.javaScriptEnabled = true
        myWebView?.scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
        myWebView?.loadUrl("https://images.google.com/searchbyimage?image_url=" + Utility.path + Utility.Name)
        myWebView?.webViewClient = MyBrowser()
        myWebView?.setOnTouchListener(OnTouchListener { view: View?, motionEvent: MotionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> fingerState =
                    if (fingerState == FINGER_RELEASED) FINGER_TOUCHED else FINGER_UNDEFINED
                MotionEvent.ACTION_UP -> if (fingerState != FINGER_DRAGGING) {
                    fingerState = FINGER_RELEASED
                    // Your onClick codes
                    if (myWebView?.title == "Google Search") {
                    } else {
                        AlertDialog.Builder(this@WebCrawl).setTitle("Alert")
                            .setMessage("Are u sure u want to select  :  " + myWebView?.title)
                            .setPositiveButton("Yes") { dialog, which -> myWebView?.loadUrl("https://www.alibaba.com/trade/search?fsb=y&IndexArea=product_en&CatId=&SearchText=" + myWebView?.title) }
                            .setNegativeButton("No") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
                            .show()
                    }
                } else if (fingerState == FINGER_DRAGGING) fingerState =
                    FINGER_RELEASED else fingerState = FINGER_UNDEFINED
                MotionEvent.ACTION_MOVE -> fingerState =
                    if (fingerState == FINGER_TOUCHED || fingerState == FINGER_DRAGGING) FINGER_DRAGGING else FINGER_UNDEFINED
                else -> fingerState = FINGER_UNDEFINED
            }
            false
        })
        // Toast.makeText( WebCrawl.this, "Some Json Exception error occurred -> " +  myWebView.getTitle(), Toast.LENGTH_LONG).show();
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menu.add(0, SELECTTEXT_MENU_ID, 0, "Select Text")
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == SELECTTEXT_MENU_ID) {
            SelectText()
            return true
        }
        return true
    }

    fun SelectText() {
        try {
            val shiftPressEvent = KeyEvent(
                0, 0, KeyEvent.ACTION_DOWN,
                KeyEvent.KEYCODE_SHIFT_LEFT, 0, 0
            )
            shiftPressEvent.dispatch(myWebView)
            Toast.makeText(
                this@WebCrawl,
                "Some Json Exception error occurred -> $shiftPressEvent",
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            throw AssertionError(e)
        }
    }

    companion object {
        private const val SELECTTEXT_MENU_ID = Menu.FIRST
    }
}