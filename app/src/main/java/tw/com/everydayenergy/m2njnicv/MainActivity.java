package tw.com.everydayenergy.m2njnicv;

//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
//import android.view.View;
import android.view.Window;
import android.view.WindowManager;
//import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
//import android.widget.ImageView;

import java.io.File;

public class MainActivity extends AppCompatActivity { //implements View.OnClickListener {

    //private Button btnProc;
    //private ImageView imageView;
    //private Bitmap bmp;

    //public static native int[] grayProc(int[] pixels, int w, int h);

    //static {
    //    System.loadLibrary("jnicv-process");
    //}

    private CameraPreview camPreview;
    private FrameLayout mainLayout;

    //private CameraPreview camPreview;
    private ImageView MyCameraPreview = null;
    //private FrameLayout mainLayout;
    private int PreviewSizeWidth = 640;
    private int PreviewSizeHeight= 480;

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
        MyCameraPreview = new ImageView(this);

        SurfaceView camView = new SurfaceView(this);
        SurfaceHolder camHolder = camView.getHolder();
        camPreview = new CameraPreview(PreviewSizeWidth, PreviewSizeHeight, MyCameraPreview);

        camHolder.addCallback(camPreview);
        camHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        mainLayout = (FrameLayout) findViewById(R.id.frameLayout1);
        mainLayout.addView(camView, new FrameLayout.LayoutParams(PreviewSizeWidth, PreviewSizeHeight));
        mainLayout.addView(MyCameraPreview, new FrameLayout.LayoutParams(PreviewSizeWidth, PreviewSizeHeight));

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
