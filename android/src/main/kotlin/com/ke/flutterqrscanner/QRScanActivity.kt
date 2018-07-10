package com.ke.flutterqrscanner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout

import com.google.zxing.Result

import me.dm7.barcodescanner.zxing.ZXingScannerView

class QRScanActivity : Activity(), ZXingScannerView.ResultHandler {


    private var mScannerView: ZXingScannerView? = null


    private val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qracan)


        mScannerView = findViewById(R.id.scanner_view)

        val linearLayout = findViewById<LinearLayout>(R.id.ll_title)
        linearLayout.setPadding(0, statusBarHeight, 0, 0)

    }

    public override fun onResume() {
        super.onResume()
        mScannerView!!.setResultHandler(this) // Register ourselves as a handler for scan results.
        mScannerView!!.startCamera()          // Start camera on resume
    }

    public override fun onPause() {
        super.onPause()
        mScannerView!!.stopCamera()           // Stop camera on pause
    }

    override fun handleResult(rawResult: Result) {
        returnResult(rawResult.text)


    }


    private fun returnResult(result: String) {
        val intent = Intent()
        intent.putExtra(EXTRA_SCAN_RESULT, result)
        setResult(Activity.RESULT_OK, intent)
        finish()

    }

    fun back(view: View) {
        onBackPressed()
    }

    fun toggleFlash(view: View) {
        view.isSelected = !view.isSelected
        val open = view.isSelected
        mScannerView!!.flash = open
    }

    fun myQRCode(view: View) {
        returnResult("my_qr_code")
    }

    fun manualEnter(view: View) {
        returnResult("manual_input")
    }

    companion object {
        internal const val EXTRA_SCAN_RESULT = "EXTRA_SCAN_RESULT"
    }
}

