package tw.com.everydayenergy.m2njnicv;

//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.app.Activity;
import android.app.ActivityManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
//import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
//import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
//import android.widget.ImageView;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity {//AppCompatActivity { //implements View.OnClickListener {

    private static String TAG="MainActivity";
    private Activity mActivity = null;
    //private Button btnProc;
    //private ImageView imageView;
    //private Bitmap bmp;

    //public static native int[] grayProc(int[] pixels, int w, int h);

    //static {
    //    System.loadLibrary("jnicv-process");
    //}

    private CameraPreview camPreview = null;
    private FrameLayout mainLayout;
    //private LinearLayout mainLayout;

    //private CameraPreview camPreview;
    private MyImageView MyCameraPreview = null;
    private SurfaceView camView = null;
    private SurfaceHolder camHolder = null;
    //private FrameLayout mainLayout;
    private int PreviewSizeWidth = 0; //640;
    private int PreviewSizeHeight= 0; //480;

    //private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //btnProc = (Button) findViewById(R.id.btn_gray_process);
        //imageView = (ImageView) findViewById(R.id.image_view);
        //bmp = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        //imageView.setImageBitmap(bmp);
        //btnProc.setOnClickListener(this);
        //Set this APK Full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Set this APK no title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //
        // Create my camera preview
        //
        //MyCameraPreview = new ImageView(this);
        MyCameraPreview = (MyImageView) findViewById(R.id.image_view);
        mActivity = this;
        camView = (SurfaceView) findViewById(R.id.surface_view);
        camHolder = camView.getHolder();

        MyCameraPreview.setOnImageViewSizeChanged(new MyImageView.OnImageViewSizeChanged() {
            @Override
            public void invoke(ImageView v, final int w, final int h) {
                // Do whatever you want with w and h which are non zero values ...
                Log.i(TAG, "SizeChanged:W="+w+":H="+h);

                if(camPreview == null) {
                    Log.i(TAG, "new camPreview");
                    PreviewSizeHeight = h;
                    PreviewSizeWidth = w;
                    camPreview = new CameraPreview(PreviewSizeWidth, PreviewSizeHeight, MyCameraPreview, mActivity);
                    camHolder.addCallback(camPreview);
                    camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                    //camPreview.setCameraDisplayOrientation(mActivity);
                }
                else {
                    if((h != PreviewSizeHeight) || (w != PreviewSizeWidth)) {
                        PreviewSizeHeight = h;
                        PreviewSizeWidth = w;
                        //camPreview.setNewSize(PreviewSizeWidth, PreviewSizeHeight);
                        //camPreview.setCameraDisplayOrientation(mActivity);
                    }
                }
            }
        });
        //MyCameraPreview.setMinimumHeight(PreviewSizeHeight);
        //PreviewSizeHeight = MyCameraPreview.getMeasuredHeight();
        //MyCameraPreview.setMinimumWidth(PreviewSizeWidth);
        //PreviewSizeWidth = MyCameraPreview.getMeasuredWidth();
        //SurfaceView camView = new SurfaceView(this);
        //camView.setMinimumHeight(PreviewSizeHeight);
        //camView.setMinimumWidth(PreviewSizeWidth);

        //camPreview = new CameraPreview(PreviewSizeWidth, PreviewSizeHeight, MyCameraPreview);

        //camHolder.addCallback(camPreview);
        //camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        mainLayout = (FrameLayout) findViewById(R.id.activity_main);
        //mainLayout = (LinearLayout) findViewById(R.id.activity_main);
        //mainLayout.addView(camView, new FrameLayout.LayoutParams(PreviewSizeWidth, PreviewSizeHeight));
        //mainLayout.addView(MyCameraPreview, new FrameLayout.LayoutParams(PreviewSizeWidth, PreviewSizeHeight));

        //SurfaceView camView = new SurfaceView(this);
        //SurfaceHolder camHolder = camView.getHolder();
        //camPreview = new CameraPreview(640, 480);

        //camHolder.addCallback(camPreview);
        //camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        //mainLayout = (FrameLayout) findViewById(R.id.frameLayout1);
        //mainLayout.addView(camView, new FrameLayout.LayoutParams(640, 480));
    }

    //@Override
    /*public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            int X = (int)event.getX();
            if ( X >= 640 )
                mHandler.postDelayed(TakePicture, 300);
            else
                camPreview.CameraStartAutoFocus();
        }
        return true;
    };*/
    /*
    @Override
    public void onClick(View v) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int[] pixels = new int[w*h];
        bmp.getPixels(pixels, 0, w, 0, 0, w, h);
        int[] resultInt = grayProc(pixels, w, h);
        Bitmap resultImg = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        resultImg.setPixels(resultInt, 0, w, 0, 0, w, h);
        imageView.setImageBitmap(resultImg);
    }*/
    @Override
    public void onResume(){

        super.onResume();
    }

    protected void onPause()
    {
        if ( camPreview != null)
            camPreview.onPause();
        super.onPause();
    }
    /*private Runnable TakePicture = new Runnable()
    {
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        String MyDirectory_path = extStorageDirectory;
        String PictureFileName;
        public void run()
        {
            File file = new File(MyDirectory_path);
            if (!file.exists())
                file.mkdirs();
            PictureFileName = MyDirectory_path + "/MyPicture.jpg";
            camPreview.CameraTakePicture(PictureFileName);
        }
    };*/
}
