package com.example.snake3d_new.openGL.inMain;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class ActivityGL extends GLSurfaceView {

    private int widthContext;
    private int heightContext;

    public ActivityGL(Context context, int width, int height)
    {
        super(context, null);
        setEGLContextClientVersion(2);
        setRenderer(new PartRenderer(context));
        widthContext = width;
        heightContext = height;
    }

    public ActivityGL(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setEGLContextClientVersion(3);
        setRenderer(new PartRenderer(context));
    }
}
