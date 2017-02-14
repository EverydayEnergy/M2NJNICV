package tw.com.everydayenergy.m2njnicv;

import android.app.Activity;
import android.app.ActivityManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.ImageView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

/**
 * Created by ctang on 2017/2/5.
 * * http://ibuzzlog.blogspot.tw/2012/08/how-to-use-camera-in-android.html
 * * http://ibuzzlog.blogspot.tw/2012/08/how-to-do-real-time-image-processing-in.html
 */

public class CameraPreview implements SurfaceHolder.Callback, Camera.PreviewCallback {

    //private int PreviewSizeWidth;
    //private int PreviewSizeHeight;

    private static String TAG="CameraPreview";
    private SurfaceHolder mSurfHolder;
    //private Camera mCamera;

    private Camera mCamera = null;
    private ImageView MyCameraPreview = null;
    private Bitmap bitmap = null;
    private int[] pixels = null;
    private byte[] FrameData = null;
    private int imageFormat;
    private int PreviewSizeWidth;
    private int PreviewSizeHeight;
    private boolean bProcessing = false;
    private boolean bHorizontal = true;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    public CameraPreview(int PreviewlayoutWidth, int PreviewlayoutHeight)
    {
        PreviewSizeWidth = PreviewlayoutWidth;
        PreviewSizeHeight = PreviewlayoutHeight;
    }

    public CameraPreview(int PreviewlayoutWidth, int PreviewlayoutHeight, ImageView CameraPreview)
    {
        //PreviewSizeWidth = PreviewlayoutWidth;
        //PreviewSizeHeight = PreviewlayoutHeight;
        MyCameraPreview = CameraPreview;
        setNewSize(PreviewlayoutWidth, PreviewlayoutHeight);
        //if(PreviewSizeWidth >= PreviewSizeHeight) {
        //    bHorizontal = true;
        //}
        //else {
        //    bHorizontal = false;
        //}
        //bitmap = Bitmap.createBitmap(PreviewSizeWidth, PreviewSizeHeight, Bitmap.Config.ARGB_8888);
        //pixels = new int[PreviewSizeWidth * PreviewSizeHeight];
    }
    public void setNewSize(final int w, final int h){
        PreviewSizeWidth = w;
        PreviewSizeHeight = h;
        if(w >= h) {
            bHorizontal = true;
        }
        else {
            bHorizontal = false;
        }
        if(bitmap != null){
            bitmap = null;
        }
        bitmap = Bitmap.createBitmap(PreviewSizeWidth, PreviewSizeHeight, Bitmap.Config.ARGB_8888);
        if(pixels != null) {
            pixels = null;
        }
        pixels = new int[PreviewSizeWidth * PreviewSizeHeight];
    }
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // At preview mode, the frame data will push to here.
        if (imageFormat == ImageFormat.NV21)
        {
            //We only accept the NV21(YUV420) format.
            if ( !bProcessing )
            {
                FrameData = data;
                mHandler.post(DoImageProcessing);
            }
        }
    }

    public void onPause()
    {
        mCamera.stopPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Camera.open();
        try
        {
            // If did not set the SurfaceHolder, the preview area will be black.
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(this);
        }
        catch (IOException e)
        {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        Camera.Parameters parameters;
        mSurfHolder = holder;

        parameters = mCamera.getParameters();
        // Set the camera preview size
        //parameters.setPreviewSize(PreviewSizeWidth, PreviewSizeHeight);
        parameters.setPreviewSize(width, height);
        // Set the take picture size, you can set the large size of the camera supported.
        //parameters.setPictureSize(PreviewSizeWidth, PreviewSizeHeight);
        parameters.setPictureSize(width, height);

        // Turn on the camera flash.
        /*
        String NowFlashMode = parameters.getFlashMode();
        if ( NowFlashMode != null )
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        // Set the auto-focus.
        String NowFocusMode = parameters.getFocusMode ();
        if ( NowFocusMode != null )
            parameters.setFocusMode("auto");*/

        imageFormat = parameters.getPreviewFormat();

        mCamera.setParameters(parameters);

        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    private boolean TakePicture;
    private String NowPictureFileName;
    // Take picture interface
    public void CameraTakePicture(String FileName)
    {
        TakePicture = true;
        NowPictureFileName = FileName;
        mCamera.autoFocus(myAutoFocusCallback);
    }

    // Set auto-focus interface
    public void CameraStartAutoFocus()
    {
        TakePicture = false;
        mCamera.autoFocus(myAutoFocusCallback);
    }

    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback()
    {
        public void onAutoFocus(boolean arg0, Camera NowCamera)
        {
            if ( TakePicture )
            {
                NowCamera.stopPreview();//fixed for Samsung S2
                NowCamera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
                TakePicture = false;
            }
        }
    };

    Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback()
    {
        public void onShutter()
        {
            // Just do nothing.
        }
    };

    Camera.PictureCallback rawPictureCallback = new Camera.PictureCallback()
    {
        public void onPictureTaken(byte[] arg0, Camera arg1)
        {
            // Just do nothing.
        }
    };

    Camera.PictureCallback jpegPictureCallback = new Camera.PictureCallback()
    {
        public void onPictureTaken(byte[] data, Camera camera)
        {
            // Save the picture.
            try {
                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0,data.length);
                FileOutputStream out = new FileOutputStream(NowPictureFileName);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    };

    public void setCameraDisplayOrientation(Activity activity) {//, int cameraId, android.hardware.Camera camera) {

        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();

        //Camera.Parameters parameters = mCamera.getParameters();
        //parameters.
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            //Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Log.d(TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        android.hardware.Camera.getCameraInfo(cameraId, info);

        //Activity activity;

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        mCamera.setDisplayOrientation(result);
    }

    //
    // Native JNI
    //
    public native boolean ImageProcessing(int width, int height, byte[] NV21FrameData, int [] pixels);

    static
    {
        System.loadLibrary("gnustl_shared");
        System.loadLibrary("opencv_java3");
        System.loadLibrary("jnicv-process");
    }

    private Runnable DoImageProcessing = new Runnable()
    {
        public void run()
        {
            Log.i(TAG, "DoImageProcessing():");
            bProcessing = true;
            ImageProcessing(PreviewSizeWidth, PreviewSizeHeight, FrameData, pixels);

            bitmap.setPixels(pixels, 0, PreviewSizeWidth, 0, 0, PreviewSizeWidth, PreviewSizeHeight);
            MyCameraPreview.setImageBitmap(bitmap);
            bProcessing = false;
        }
    };
}
