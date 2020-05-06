package com.konka.avenger.utilities;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ScreenCapture {
    private static final String TAG = "ScreenCapture";
    public boolean bWithBuffer = true;
    Camera mCameraDevice = null;
    MyPictureCallback mMyPictureCallback;
    long time1;
    long time2;

    private final class MyPictureCallback implements PictureCallback {
        MyPictureCallback() {
            Log.d(ScreenCapture.TAG, "==========DWinCamera init!!");
            ScreenCapture.this.mCameraDevice = Camera.open(3);
            Parameters mParameters = ScreenCapture.this.mCameraDevice.getParameters();
            mParameters.setPictureSize(640, 360);
            ScreenCapture.this.mCameraDevice.setParameters(mParameters);
        }

        public void onPictureTaken(byte[] data, Camera arg1) {
            try {
                Log.d(ScreenCapture.TAG, "catch picture data -----------------------");
                Options opts = new Options();
                opts.inPreferredConfig = Config.RGB_565;
                Bitmap mBitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
                Log.d(ScreenCapture.TAG, "start to update the widget background -----------------------");
                if (!mBitmap.isRecycled()) {
                    mBitmap.recycle();
                }
                Log.d(ScreenCapture.TAG, "------------------------>end onPreviewFrame time = " + (System.currentTimeMillis() - ScreenCapture.this.time1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void Snapshot() {
        try {
            Log.d(TAG, "Test dwincatpure start ++++++++===== ");
            this.mMyPictureCallback = new MyPictureCallback();
            this.mCameraDevice.startPreview();
            this.mCameraDevice.takePicture(null, null, this.mMyPictureCallback);
            Thread.sleep(2000);
            Log.d(TAG, "stop dwin camera ++++++++");
            this.mCameraDevice.stopPreview();
            this.mCameraDevice.release();
        } catch (Exception e) {
        }
    }

    private void saveMyBitmap(String bitName, Bitmap mBitmap) {
        File f = new File("/data/data/com.konka.avenger/cache/" + bitName + ".jpg");
        Log.d(TAG, "save picture to path++++++++" + f.getPath());
        try {
            f.createNewFile();
            Log.d(TAG, "createNewFile success");
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
        }
        mBitmap.compress(CompressFormat.JPEG, 100, fOut);
        try {
            fOut.flush();
        } catch (IOException e3) {
            e3.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e4) {
            e4.printStackTrace();
        }
    }
}
