package com.example.snake3d_new.openGL.game.logik;

import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.example.snake3d_new.openGL.game.logik.snakeAndFood.DrawFood;
import com.example.snake3d_new.openGL.game.logik.snakeAndFood.DrawSnake;
import com.example.snake3d_new.openGL.game.logik.snakeAndFood.SnakeBackend;
import com.example.snake3d_new.openGL.game.model.Bone;
import com.example.snake3d_new.openGL.game.model.Model;
import com.example.snake3d_new.openGL.game.utils.MatrixEnum;

import java.util.Arrays;
import java.util.Map;

import static android.opengl.GLES20.GL_BACK;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_FRONT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glCullFace;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES30.glBindVertexArray;
import static com.example.snake3d_new.openGL.game.logik.InitGL.PLANE;
import static com.example.snake3d_new.openGL.game.logik.InitGL.POINT;
import static com.example.snake3d_new.openGL.game.logik.InitGL.TEST_MODEL;
import static com.example.snake3d_new.openGL.game.logik.WhatProgram.eProgramId;
import static com.example.snake3d_new.openGL.game.logik.WhatProgram.eProgramShadow;
import static com.example.snake3d_new.openGL.game.utils.Constants.AMOUNT_X;
import static com.example.snake3d_new.openGL.game.utils.Constants.AMOUNT_Y;
import static com.example.snake3d_new.openGL.game.utils.Constants.X_BEGIN;
import static com.example.snake3d_new.openGL.game.utils.Constants.Y_BEGIN;
import static com.example.snake3d_new.openGL.game.utils.Constants.Z_BEGIN;
import static com.example.snake3d_new.openGL.game.utils.Constants.freedom;
import static com.example.snake3d_new.openGL.game.utils.Constants.howMuchFreedom;
import static com.example.snake3d_new.openGL.game.utils.MatrixEnum.rotate;
import static com.example.snake3d_new.openGL.game.utils.MatrixEnum.scale;
import static com.example.snake3d_new.openGL.game.utils.Order.getBegin;
import static com.example.snake3d_new.openGL.game.utils.Order.getCount;
import static com.example.snake3d_new.openGL.game.utils.MatrixEnum.translate;

public class Draw {
    private InitGL initGL;
    private DrawSnake drawSnake;
    private DrawFood drawFood;

    public Draw(InitGL initGL){
        this.initGL = initGL;
        drawFood = new DrawFood(this);
        SnakeBackend snakeBackend = new SnakeBackend(drawFood);
        drawSnake = new DrawSnake(this, snakeBackend);
    }

    public void translateM(double x, double y, double z){
        Matrix.translateM(initGL.translateMatrix, 0, (float) x, (float) y, (float) z);
    }

    public void rotateM(double angle, double x, double y, double z){
        Matrix.rotateM(initGL.rotateMatrix, 0, (float) angle, (float) x, (float) y, (float) z);
    }

    public void scaleM(double x, double y, double z){
        Matrix.scaleM(initGL.scaleMatrix, 0, (float) x, (float) y, (float) z);
    }

    public void bindMatrix(){
        Matrix.multiplyMM(initGL.modelMatrix, 0, initGL.translateMatrix, 0, initGL.rotateMatrix, 0);
        Matrix.multiplyMM(initGL.modelMatrix, 0, initGL.modelMatrix, 0, initGL.scaleMatrix, 0);
        if (initGL.program == eProgramId)
            glUniformMatrix4fv(initGL.locationModelMatrix, 1, false, initGL.modelMatrix, 0);
        else if (initGL.program == eProgramShadow)
            glUniformMatrix4fv(initGL.locationModelMatrixShadow, 1, false, initGL.modelMatrix, 0);
    }

    public void setIdentityM(MatrixEnum... matrixes){
        for (MatrixEnum matrix : matrixes) {
            if (matrix.getI() == 0)
                Matrix.setIdentityM(initGL.translateMatrix, 0);
            else if (matrix.getI() == 1)
                Matrix.setIdentityM(initGL.rotateMatrix, 0);
            else if (matrix.getI() == 2)
                Matrix.setIdentityM(initGL.scaleMatrix, 0);
        }
    }

    public void drawTriangles(Model model){
        glDrawArrays(GL_TRIANGLES, getBegin(model), getCount(model));
    }

    public void drawLines(Model model){
        glDrawArrays(GL_LINES, getBegin(model), getCount(model));
    }

    public void drawPoints(Model model){
        glDrawArrays(GL_POINTS, getBegin(model), getCount(model));
    }

    public void setColor(float r, float g, float b){
        if (initGL.program != eProgramId)
            return;
        glUniform4f(initGL.locationColor, r, g, b, 1);
    }

    public void touchEvent(MotionEvent event, float width, float height) {
        drawSnake.touchEvent(event, width, height);
    }

    public void drawScene(){
        glUseProgram(initGL.programShadow);
        glCullFace(GL_FRONT);
        glBindFramebuffer(GL_FRAMEBUFFER, initGL.shadowFBO.get(0));
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        initGL.program = eProgramShadow;
        glBindVertexArray(initGL.vaoShadow.get(0));
//        drawSnake.draw(initGL.program);
//        drawFood.draw();
        glCullFace(GL_BACK);

        glUseProgram(initGL.programId);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, initGL.shadowMap.get(0));
        initGL.program = eProgramId;
        glBindVertexArray(initGL.vaoMain.get(0));
//        drawSnake.draw(initGL.program);
//        drawFood.draw();
//
//        drawPlane();
//        drawFreedom();

        testAnimationFunc();
        setColor(1, 0, 0);
        float x = 0.2f;
//        scaleM(x, x, x);
        translateM(0, 0, -15);
        rotateM(ttt, 0, 1, 0);
//        rotateM(ttt, 0, 0, 1);
//        rotateM(ttt, 1, 0, 0);
        ttt += 0.5f;
        bindMatrix();
        drawTriangles(TEST_MODEL);
        setIdentityM(translate, rotate, scale);
        bindMatrix();
    }
    float ttt = 0;

    private void testAnimationFunc() {
        for (Map.Entry<String, Bone> entry : TEST_MODEL.mapBones.entrySet())
            entry.getValue().setNeedUpdate(true);
        for (Map.Entry<String, Bone> entry : TEST_MODEL.mapBones.entrySet()){
            Bone bone = entry.getValue();
            if (bone.getNeedUpdate()){//Если требуется апдейт матрицы
                testRekurs(TEST_MODEL.mapBones, bone);
            }
        }
        for (Map.Entry<String, Bone> entry : TEST_MODEL.mapBones.entrySet()){
//            Matrix.multiplyMM(entry.getValue().getMatrixNow(), 0, entry.getValue().getInvertMatrix(), 0, entry.getValue().getMatrixNow(), 0);
//            Matrix.multiplyMM(entry.getValue().getMatrixNow(), 0, entry.getValue().getMatrixNow(), 0, GLOBAL, 0);

            Matrix.multiplyMM(entry.getValue().getMatrixNow(), 0, entry.getValue().getMatrixNow(), 0, GLOBAL, 0);
            Matrix.multiplyMM(entry.getValue().getMatrixNow(), 0, entry.getValue().getMatrixNow(), 0, entry.getValue().getInvertMatrix(), 0);

            glUniformMatrix4fv(glGetUniformLocation(initGL.programId, "boneMatrix[" + entry.getValue().getIndexBone() +  "]"), 1, false, entry.getValue().getMatrixNow(), 0);
        }
    }

    boolean[] gg = new boolean[]{true, true, true};
    public float[] GLOBAL0 = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, -0.8360187f,
            0, 0, 0, 1,
    };
    public float[] GLOBAL1 = new float[]{
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1,
    };
    public float[] GLOBAL = GLOBAL1;
    int anim = 1;
    private void testRekurs(Map<String, Bone> map, Bone bone){
        if (bone.getParent() == null){//Если рут кость
            float[] matrix = new float[16];
            matrix = Arrays.copyOf(bone.getAnimMatrix().get(anim), 16);
            bone.setMatrixNow(matrix);
            bone.setNeedUpdate(false);
//            if (gg[bone.getIndexBone()]){
//                gg[bone.getIndexBone()] = false;
//                Log.d("Name and index", bone.getName() + "  " + bone.getIndexBone());
//                Log.d("ivertMatrix", Arrays.toString(bone.getInvertMatrix()));
//                Log.d("beginMatrix", Arrays.toString(bone.getBeginBoneMatrix()));
//                Log.d("NowMatrix", Arrays.toString(bone.getMatrixNow()));
//                Log.d("matrix", Arrays.toString(matrix));
//                Log.d("enter", "e");
//            }
        }
        else {
            if (bone.getParent().getNeedUpdate())
                testRekurs(map, bone.getParent());
            float[] matrix = new float[16];
            Matrix.multiplyMM(matrix, 0, bone.getAnimMatrix().get(anim), 0, bone.getParent().getMatrixNow(), 0);
//            Matrix.multiplyMM(matrix, 0, bone.getParent().getMatrixNow(), 0, bone.getAnimMatrix().get(anim), 0);
            bone.setMatrixNow(matrix);
            bone.setNeedUpdate(false);
//            if (gg[bone.getIndexBone()]){
//                gg[bone.getIndexBone()] = false;
//                Log.d("Name and index", bone.getName() + "  " + bone.getIndexBone());
//                Log.d("ivertMatrix", Arrays.toString(bone.getInvertMatrix()));
//                Log.d("beginMatrix", Arrays.toString(bone.getBeginBoneMatrix()));
//                Log.d("parentMatrix", Arrays.toString(bone.getParent().getMatrixNow()));
//                Log.d("NowMatrix", Arrays.toString(bone.getMatrixNow()));
//                Log.d("withoutParentr", Arrays.toString(kaka));
//                Log.d("matrix", Arrays.toString(matrix));
//                Log.d("enter", "e");
//            }
        }
    }

    private void drawPlane() {
        setColor(0.5f, 0.5f, 0.5f);
        scaleM(AMOUNT_X, AMOUNT_Y, 1);
        bindMatrix();
        drawTriangles(PLANE);
        setIdentityM(scale);
        bindMatrix();
    }

    private boolean risFree = false;
    private void drawFreedom(){
        if (!risFree)
            return;
        for (int i = 0; i < freedom.length; i++){
            for (int k = 0; k < freedom[i].length; k++){
                if (freedom[i][k])
                    setColor(0, 1, 0);
                else
                    setColor(1, 0, 0);

                translateM(X_BEGIN, Y_BEGIN, Z_BEGIN + 0.5f);
                translateM(i, k, 0.5f);
                bindMatrix();

                drawPoints(POINT);

                setIdentityM(translate);
                bindMatrix();
            }
        }
        Log.d("howMuchFreedom", "" + howMuchFreedom);
    }
}
