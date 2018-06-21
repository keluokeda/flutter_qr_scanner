package com.ke.flutterqrscanner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import com.tbruyelle.rxpermissions2.RxPermissions
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import io.flutter.plugin.common.PluginRegistry.Registrar
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.io.ByteArrayOutputStream
import java.util.*


class FlutterQrScannerPlugin(private val activity: Activity) : MethodCallHandler, PluginRegistry.ActivityResultListener {
    private var result: Result? = null

    private val hints: EnumMap<EncodeHintType, Any> = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)

    init {
        hints[EncodeHintType.CHARACTER_SET] = "utf-8"
        hints[EncodeHintType.MARGIN] = 0
        hints[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H
    }

    companion object {
        @JvmStatic
        fun registerWith(registrar: Registrar) {
            val channel = MethodChannel(registrar.messenger(), "github.com/keluokeda/scanner")
            val plugin = FlutterQrScannerPlugin(registrar.activity())

            channel.setMethodCallHandler(plugin)

            registrar.addActivityResultListener(plugin)
        }
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        when {
            call.method == "scan" -> {
                val rxPermissions = RxPermissions(activity)

                rxPermissions.request(Manifest.permission.CAMERA).subscribe({
                    if (it) {
                        this.result = result
                        val intent = Intent(activity, QRScanActivity::class.java)
                        activity.startActivityForResult(intent, 2222)
                    } else {
                        result.success("")
                    }
                }, { result.success("") })


            }
            call.method == "createQRImageData" -> {
                val map = call.arguments as Map<*, *>
                val content = map["content"] as String
                val size = map["size"] as Int
                Observable.just(Pair(content, size)).observeOn(Schedulers.io())
                        .map { encodeAsBitmap(it.first, it.second) }
                        .map {
                            val byteArrayOutputStream = ByteArrayOutputStream()
                            it.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                            return@map byteArrayOutputStream.toByteArray()
                        }
                        .subscribe { result.success(it) }

            }
            else -> result.notImplemented()
        }
    }


    private fun encodeAsBitmap(str: String, size: Int): Bitmap {
        val result: BitMatrix =
                MultiFormatWriter().encode(str,
                        BarcodeFormat.QR_CODE, size, size, hints)


        val w = result.width
        val h = result.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = if (result.get(x, y)) Color.BLACK else Color.WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, size, 0, 0, w, h)
        return bitmap
    }


    override fun onActivityResult(code: Int, resultCode: Int, data: Intent?): Boolean {
        if (code == 2222 && result != null) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                val content = data.getStringExtra(QRScanActivity.EXTRA_SCAN_RESULT)
                result?.success(content)
            } else {

                result?.success("")
            }

            return true
        }

        //false表示别的也可以处理
        return false
    }


}
