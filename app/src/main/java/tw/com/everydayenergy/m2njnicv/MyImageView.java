package tw.com.everydayenergy.m2njnicv;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by ctang on 2017/2/11.
 */

public class MyImageView extends ImageView {
    public MyImageView(Context context) {
        super(context);
    }

    private OnImageViewSizeChanged sizeCallback = null;


    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w == 0 || h == 0) {
            return;
        }
        else{
            if(sizeCallback != null) {
                //sizeCallback.invoke(this, w, h);
            }
        }

    }

    public void setOnImageViewSizeChanged(OnImageViewSizeChanged _callback){
        this.sizeCallback = _callback;

        if (getWidth() != 0 && getHeight() != 0) {
            _callback.invoke(this, getWidth(), getHeight());
        }
    }

    public interface OnImageViewSizeChanged{
        public void invoke(ImageView v, int w, int h);
    }
}
