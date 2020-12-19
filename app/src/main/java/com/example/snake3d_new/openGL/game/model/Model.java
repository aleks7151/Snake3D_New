package com.example.snake3d_new.openGL.game.model;

import com.example.snake3d_new.openGL.game.drawAndInit.Animation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Model {
    public int order;//Для порядковой отрисовки

    public float[] position = null;
    public float[] normal = null;
    public float[] color = null;

    public float[] weight = null;
    public int[] index = null;

    public Bone rootBone = null;
    public Animation animation = null;

    public void animate(Bone rootBone, Bone parent, int programId) {
        if (animation.needAnimate) {
            animation.currentTime = System.nanoTime() / Math.pow(10, 6);
            animation.animate(rootBone, parent, animation.currentTime - animation.beginTime, programId);
        }
    }
}
