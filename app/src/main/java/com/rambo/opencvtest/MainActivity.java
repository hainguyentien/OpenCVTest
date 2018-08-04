package com.rambo.opencvtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {


    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean              mIsJavaCamera = true;
    private MenuItem mItemSwitchCamera = null;

    private Mat mRgba, mGray, mHsv;

    private Spinner spnFilter;

    private ArrayList<String> ArrFilter;

    private static final int CAMERA_REQUEST_CODE = 1;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }

        setContentView(R.layout.activity_main);

        mOpenCvCameraView = findViewById(R.id.camera_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disableCamera();
    }

    private void disableCamera() {
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        mHsv = new Mat(height, width, CvType.CV_8UC3);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }


    @Override
    public Mat onCameraFrame(final CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        final int[] position = new int[1];
        //get each frame from camera
        mRgba = inputFrame.rgba();
//        mGray = inputFrame.gray();
//
//        /**********HSV conversion**************/
//        //convert mat rgb to mat hsv
//        Imgproc.cvtColor(mRgba, mHsv, Imgproc.COLOR_RGB2HSV);
//
//        //find scalar sum of hsv
//        Scalar mColorHsv = Core.sumElems(mHsv);
//
//        int pointCount = 320*240;
//
//
//        //convert each pixel
//        for (int i = 0; i < mColorHsv.val.length; i++) {
//            mColorHsv.val[i] /= pointCount;
//        }
//
//        //convert hsv scalar to rgb scalar
//        Scalar mColorRgb = convertScalarHsv2Rgba(mColorHsv);
//
//    /*Log.d("intensity", "Color: #" + String.format("%02X", (int)mColorHsv.val[0])
//            + String.format("%02X", (int)mColorHsv.val[1])
//            + String.format("%02X", (int)mColorHsv.val[2]) );*/
//        //print scalar value
//        Log.d("intensity", "R:"+ String.valueOf(mColorRgb.val[0])+" G:"+String.valueOf(mColorRgb.val[1])+" B:"+String.valueOf(mColorRgb.val[2]));
//
//        /*Convert to YUV*/
//
//        int R = (int) mColorRgb.val[0];
//        int G = (int) mColorRgb.val[1];
//        int B = (int) mColorRgb.val[2];
//
//        int Y = (int) (R *  .299000 + G *  .587000 + B *  .114000);
//        int U = (int) (R * -.168736 + G * -.331264 + B *  .500000 + 128);
//        int V = (int) (R *  .500000 + G * -.418688 + B * -.081312 + 128);
//
//        //int I = (R+G+B)/3;
//
//
//        //Log.d("intensity", "I: "+String.valueOf(I));
//        Log.d("intensity", "Y:"+ String.valueOf(Y)+" U:"+String.valueOf(U)+" V:"+String.valueOf(V));
//
////        salt(mGray.getNativeObjAddr(), 5000);
////        return mGray;
////        blur(mRgba.getNativeObjAddr());

        DrawOnFrame(mRgba.getNativeObjAddr());


        return mRgba;
    }

    private Scalar convertScalarHsv2Rgba(Scalar mColorHsv) {
        Mat pointMatRgba = new Mat();
        Mat pointMatHsv = new Mat(1, 1, CvType.CV_8UC3, mColorHsv);
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB);

        return new Scalar(pointMatRgba.get(0, 0));
    }

    public native void salt(long matAddrGray, int nbrElem);

    public native void blur(long matAddrRgb);

    public native void EdgeDetection(long matAddrGray);

    public native void Histogram(long matAddrGray);

    public native void DrawOnFrame(long matAddrRgba);
}
