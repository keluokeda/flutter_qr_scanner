package com.ke.flutterqrscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QRScanActivity extends Activity implements ZXingScannerView.ResultHandler {
    static final String EXTRA_SCAN_RESULT = "EXTRA_SCAN_RESULT";



    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qracan);


        mScannerView = findViewById(R.id.scanner_view);

        LinearLayout linearLayout = findViewById(R.id.ll_title);
        linearLayout.setPadding(0, getStatusBarHeight(), 0, 0);

    }


    protected int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {
        returnResult(rawResult.getText());



    }


    private void returnResult(String result){
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SCAN_RESULT, result);
        setResult(RESULT_OK, intent);
        finish();

    }

    public void back(View view) {
        onBackPressed();
    }

    public void toggleFlash(View view) {
        view.setSelected(!view.isSelected());
        boolean open = view.isSelected();
        mScannerView.setFlash(open);
    }

    public void myQRCode(View view) {
        returnResult("my_qr_code");
    }

    public void manualEnter(View view) {
        returnResult("manual_enter");
    }
}

