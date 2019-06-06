package com.example.ximalaya.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.example.ximalaya.R;

@SuppressLint("AppCompatCustomView")
public class LoadingView extends ImageView {

    private int rotateDegree = 0;

    private boolean mNeedRotate = false;


    public LoadingView(Context context) {
        this(context,null);
    }

    public LoadingView(Context context, @NonNull AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingView(Context context, @NonNull AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading2);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(new Runnable() {
            @Override
            public void run() {
                rotateDegree += 30;
                rotateDegree = rotateDegree <= 360 ? rotateDegree : 0;
                invalidate();
                if(mNeedRotate) {
                    postDelayed(this,100);
                }
            }
        });
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mNeedRotate = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        canvas.rotate(rotateDegree,getWidth()/2,getHeight()/2);
        super.onDraw(canvas);
    }
}
