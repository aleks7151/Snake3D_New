package com.example.snake3d_new.openGL.game.model;

import android.opengl.Matrix;
import android.util.Log;

import com.example.snake3d_new.openGL.game.model.Bone;
import com.example.snake3d_new.openGL.game.utils.TypeAnimaion;

import java.util.Arrays;
import java.util.List;

import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class Animation {
    public double currentTime;
    public double beginTime;
    private TypeAnimaion typeAnimaion;
    public boolean needAnimate = false;
    public boolean firstTime = true;
    public boolean pendulum = false;

    public Animation(TypeAnimaion typeAnimaion){
        this.typeAnimaion = typeAnimaion;
    }

    public void setTime(){
        needAnimate = true;
        currentTime = System.nanoTime() / Math.pow(10, 6);
        beginTime = currentTime;
    }

    public void animate(Bone bone, Bone parent, double time, int program) {
        if (!pendulum)
            animateDefault(bone, parent, time, program);
        else
            animatePendulum(bone, parent, time, program);
    }

    private void animateDefault(Bone bone, Bone parent, double time, int program) {
        float[] matrix = new float[16];
        float[] invert = new float[16];
        List<Double> listTime = bone.getTime();
        for (int i = listTime.size() - 1; i >= 0; i--){
            if (time >= listTime.get(i)){
                if (i == listTime.size() - 1){//Если вышли за пределы списка времени
                    if (parent != null)
                        Log.d("!!!!!", "ТАК НЕ ДОЖЛНО БЫТЬ, НУ ИЛИ ИНОГДА");
                    if (typeAnimaion == TypeAnimaion.ONCE){
                        needAnimate = false;
                        return;
                    }
                    else if (typeAnimaion == TypeAnimaion.REPEAT) {
                        beginTime += listTime.get(i);
                        animateDefault(bone, parent, currentTime - beginTime, program);//При условии, что у нас один массив времени на все кости, иначе переменная timeBegin должна быть массивом
                        return;
                    }
                    else if (typeAnimaion == TypeAnimaion.PENDULUM){
                        beginTime += listTime.get(i);
                        pendulum = true;
                        double endAnimation = listTime.get(listTime.size() - 1);
                        animatePendulum(bone, parent, endAnimation - (currentTime - beginTime), program);
                        return;
                    }
                    else if (typeAnimaion == TypeAnimaion.ONCE_AND_BACK){
                        needAnimate = false;
                        animateDefault(bone, parent, 0, program);
                        return;
                    }
                }
                double coefficient = (time - listTime.get(i)) / (listTime.get(i + 1) - listTime.get(i));
                float[] previousFrame = Arrays.copyOf(bone.getAnimMatrix().get(i), 16);
                float[] nextFrame = Arrays.copyOf(bone.getAnimMatrix().get(i + 1), 16);
                for (int k = 0; k < 16; k++)
                    matrix[k] = (float) (previousFrame[k] * (1 - coefficient) + nextFrame[k] * coefficient);
                break;
            }
        }
        multiplyAndDispatch(bone, parent, program, matrix, invert);
        if (bone.childs != null){
            for (Bone child : bone.childs)
                animateDefault(child, bone, time, program);
        }
    }

    private void animatePendulum(Bone bone, Bone parent, double time, int program) {
        float[] matrix = new float[16];
        float[] invert = new float[16];
        List<Double> listTime = bone.getTime();
        for (int i = 0; i < listTime.size(); i++){
            if (time <= listTime.get(i)){
                if (i == 0){//Если вышли за пределы списка времени
                    if (parent != null)
                        Log.d("!!!!!", "ТАК НЕ ДОЖЛНО БЫТЬ, НУ ИЛИ ИНОГДА");
                    if (typeAnimaion == TypeAnimaion.PENDULUM){
                        beginTime += listTime.get(listTime.size() - 1);
                        pendulum = false;
                        animateDefault(bone, parent, currentTime - beginTime, program);
                        return;
                    }
                }
                double coefficient = (time - listTime.get(i)) / (listTime.get(i) - listTime.get(i - 1));
                float[] previousFrame = Arrays.copyOf(bone.getAnimMatrix().get(i - 1), 16);
                float[] nextFrame = Arrays.copyOf(bone.getAnimMatrix().get(i), 16);
                for (int k = 0; k < 16; k++)
                    matrix[k] = (float) (previousFrame[k] * (1 - coefficient) + nextFrame[k] * coefficient);
                break;
            }
        }
        multiplyAndDispatch(bone, parent, program, matrix, invert);
        if (bone.childs != null){
            for (Bone child : bone.childs)
                animatePendulum(child, bone, time, program);
        }
    }

    private void multiplyAndDispatch(Bone bone, Bone parent, int program, float[] matrix, float[] invert){
        if (parent != null)
            Matrix.multiplyMM(matrix, 0, matrix, 0, parent.getMatrixNow(), 0);
        Matrix.multiplyMM(invert, 0, bone.getInvertMatrix(), 0, matrix, 0);
        bone.setMatrixNow(matrix);

        float[] forNormal = Arrays.copyOf(invert, 16);
        Matrix.transposeM(forNormal, 0, invert, 0);
        Matrix.invertM(forNormal, 0, forNormal, 0);

        glUniformMatrix4fv(glGetUniformLocation(program, "boneModelMatrix[" + bone.getIndexBone() + ']'), 1, false, invert, 0);
        glUniformMatrix4fv(glGetUniformLocation(program, "boneNormalMatrix[" + bone.getIndexBone() + ']'), 1, false, forNormal, 0);
    }
}
