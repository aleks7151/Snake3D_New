package com.example.snake3d_new.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.snake3d_new.R;
import com.example.snake3d_new.openGL.game.Render;

public class GameActivity extends AppCompatActivity {
    private Render render;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.FullscreenThemeBlack);

        render = new Render(getApplicationContext());
        setContentView(render);
    }

    @Override
    protected void onPause() {
        super.onPause();
        render.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        render.onResume();
    }
}