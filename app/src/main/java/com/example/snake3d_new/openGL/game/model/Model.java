package com.example.snake3d_new.openGL.game.model;

import android.util.Log;

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

    public void animate(int programId) {
        if (animation.firstTime) {
            animation.setTime();
            animation.firstTime = false;
        }
        if (animation.needAnimate) {
            animation.currentTime = System.nanoTime() / Math.pow(10, 6);
            if (animation.pendulum) {
                double endAnimation = getEndAnimation(rootBone);
                if (endAnimation == -1)
                    Log.d("Error", "Не может найти анимацию:(");
                animation.animate(rootBone, null, endAnimation - (animation.currentTime - animation.beginTime), programId);
            }
            else
                animation.animate(rootBone, null, animation.currentTime - animation.beginTime, programId);
        }
    }

    private double getEndAnimation(Bone bone) {
        if (bone.getTime() != null)
            return bone.getTime().get(bone.getTime().size() - 1);
        if (bone.childs == null)
            return -1;
        for (Bone child : bone.childs){
            double endAnimation = getEndAnimation(child);
            if (endAnimation != -1)
                return endAnimation;
        }
        return -1;
    }
}
