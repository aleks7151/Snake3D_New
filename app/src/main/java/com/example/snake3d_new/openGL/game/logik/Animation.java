package com.example.snake3d_new.openGL.game.logik;

import android.opengl.Matrix;
import android.util.Log;

import com.example.snake3d_new.openGL.game.model.Bone;

import java.util.Arrays;
import java.util.List;

import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniformMatrix4fv;

public class Animation {
    public static void animate(Bone bone, Bone parent, float time, int program) {
        float[] matrix = new float[16];
        float[] invert = new float[16];
        if (parent == null){

            List<Float> listTime = bone.getTime();
            for (int i = listTime.size() - 1; i >= 0; i--){
                if (time > listTime.get(i)){
                    if (i == listTime.size() - 1){
                        Draw.timeBegin += listTime.get(i);
                        animate(bone, parent, time - Draw.timeBegin, program);//При условии, что у нас один массив времени на все кости, иначе переменная timeBegin должна быть массивом
                        return;
                    }
                    float koef = (time - listTime.get(i)) / (listTime.get(i + 1) - listTime.get(i));
                    float[] mat1 = Arrays.copyOf(bone.getAnimMatrix().get(i), 16);
                    float[] mat2 = Arrays.copyOf(bone.getAnimMatrix().get(i + 1), 16);
                    for (int k = 0; k < 16; k++){
                        matrix[k] = mat1[k] * koef + mat2[k] * (1f - koef);
                    }
                    break;
                }
                else if (time == listTime.get(i)){
                    if (i == listTime.size() - 1){
                        Draw.timeBegin += listTime.get(i);
                    }
                    matrix = Arrays.copyOf(bone.getAnimMatrix().get(i), 16);
                    break;
                }
            }

            Matrix.multiplyMM(invert, 0, bone.getInvertMatrix(), 0, matrix, 0);
        }
        else {
            float[] nu = new float[16];
            List<Float> listTime = bone.getTime();
            for (int i = listTime.size() - 1; i >= 0; i--){
                if (time > listTime.get(i)){
                    if (i == listTime.size() - 1){
                        Log.d("!!!!!", "ТАК НЕ ДОЖЛНО БЫТЬ");
//                        Draw.timeBegin += listTime.get(i);
//                        animate(bone, parent, time - Draw.timeBegin, program);//При условии, что у нас один массив времени на все кости, иначе переменная timeBegin должна быть массивом
//                        return;
                    }
                    float koef = (time - listTime.get(i)) / (listTime.get(i + 1) - listTime.get(i));
                    float[] mat1 = Arrays.copyOf(bone.getAnimMatrix().get(i), 16);
                    float[] mat2 = Arrays.copyOf(bone.getAnimMatrix().get(i + 1), 16);
                    for (int k = 0; k < 16; k++){
                        nu[k] = mat1[k] * koef + mat2[k] * (1f - koef);
                    }
                    break;
                }
                else if (time == listTime.get(i)){
                    if (i == listTime.size() - 1){
                        Draw.timeBegin += listTime.get(i);
                    }
                    nu = Arrays.copyOf(bone.getAnimMatrix().get(i), 16);
                    break;
                }
            }

            Matrix.multiplyMM(matrix, 0, nu, 0, parent.getMatrixNow(), 0);
            Matrix.multiplyMM(invert, 0, bone.getInvertMatrix(), 0, matrix, 0);
        }
        bone.setMatrixNow(matrix);

        float[] forNormal = Arrays.copyOf(invert, 16);
        Matrix.transposeM(forNormal, 0, invert, 0);
        Matrix.invertM(forNormal, 0, forNormal, 0);

        glUniformMatrix4fv(glGetUniformLocation(program, "boneModelMatrix[" + bone.getIndexBone() + ']'), 1, false, invert, 0);
        glUniformMatrix4fv(glGetUniformLocation(program, "boneNormalMatrix[" + bone.getIndexBone() + ']'), 1, false, forNormal, 0);
        if (bone.childs != null){
            for (Bone child : bone.childs)
                animate(child, bone, time, program);
        }
    }
}
