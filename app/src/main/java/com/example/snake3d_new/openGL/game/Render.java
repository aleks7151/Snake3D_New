package com.example.snake3d_new.openGL.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.MotionEvent;

import com.example.snake3d_new.openGL.game.drawAndInit.Draw;
import com.example.snake3d_new.openGL.game.drawAndInit.InitGL;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Render extends GLSurfaceView implements GLSurfaceView.Renderer {
    private Draw draw;
    private Context context;
    private float width;
    private float height;

    public Render(Context context) {
        super(context);
        setEGLContextClientVersion(3);
        setRenderer(this);
        this.context = context;
        Log.d("MyLog", "RenderNew");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("MyLog", "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("MyLog", "onResume");
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d("MyLog", "onSurfaceCreated");
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.width = width;
        this.height = height;
        InitGL initGL = new InitGL(width, height);
        initGL.init(context.getAssets());
        draw = new Draw(initGL);
        Log.d("MyLog", "onSurfaceChanged  Width " + width + "  Height " + height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        draw.drawScene();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        draw.touchEvent(event, width, height);
        return super.onTouchEvent(event);
    }
}
