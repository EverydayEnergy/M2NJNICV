package tw.com.everydayenergy.m2njnicv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.SurfaceHolder;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ctang on 2017/2/5.
 * * http://ibuzzlog.blogspot.tw/2012/08/how-to-use-camera-in-android.html
 */

public class CameraPreview implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private int PreviewSizeWidth;
    private int PreviewSizeHeight;

    private SurfaceHolder mSurfHolder;
    private Camera mCamera;

    public CameraPreview(int PreviewlayoutWidth, int PreviewlayoutHeight)
    {
        PreviewSizeWidth = PreviewlayoutWidth;
        PreviewSizeHeight = PreviewlayoutHeight;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

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
        parameters.setPreviewSize(PreviewSizeWidth, PreviewSizeHeight);
        // Set the take picture size, you can set the large size of the camera supported.
        parameters.setPictureSize(PreviewSizeWidth, PreviewSizeHeight);

        // Turn on the camera flash.
        String NowFlashMode = parameters.getFlashMode();
        if ( NowFlashMode != null )
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_ON);
        // Set the auto-focus.
        String NowFocusMode = parameters.getFocusMode ();
        if ( NowFocusMode != null )
            parameters.setFocusMode("auto");

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
        public void onPictureTaken(byte[] data, Camera arg1)
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
}
