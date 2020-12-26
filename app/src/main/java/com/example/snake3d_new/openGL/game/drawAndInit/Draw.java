package com.example.snake3d_new.openGL.game.drawAndInit;

import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

import com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood.DrawFood;
import com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood.DrawSnake;
import com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood.SnakeBackend;
import com.example.snake3d_new.openGL.game.model.Model;
import com.example.snake3d_new.openGL.game.utils.MatrixEnum;
import com.example.snake3d_new.openGL.game.utils.WhatProgram;

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
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES30.glBindVertexArray;
import static com.example.snake3d_new.openGL.game.drawAndInit.InitGL.PLANE;
import static com.example.snake3d_new.openGL.game.drawAndInit.InitGL.POINT;
import static com.example.snake3d_new.openGL.game.drawAndInit.InitGL.ANIMATE_MODEL;
import static com.example.snake3d_new.openGL.game.utils.WhatProgram.eProgramId;
import static com.example.snake3d_new.openGL.game.utils.WhatProgram.eProgramShadow;
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
    private DrawFence drawFence;

    public Draw(InitGL initGL){
        this.initGL = initGL;
        drawFood = new DrawFood(this);
        drawFence = new DrawFence(this);
        SnakeBackend snakeBackend = new SnakeBackend(drawFood, drawFence);
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
        if (InitGL.PROGRAM_TYPE == eProgramId) {
            glUniformMatrix4fv(initGL.locationModelMatrix, 1, false, initGL.modelMatrix, 0);
            Matrix.invertM(initGL.normalMatrix, 0, initGL.modelMatrix, 0);
            Matrix.transposeM(initGL.normalMatrix, 0, initGL.normalMatrix, 0);
            glUniformMatrix4fv(initGL.locationNormalMatrix, 1, false, initGL.normalMatrix, 0);
        }
        else if (InitGL.PROGRAM_TYPE == eProgramShadow)
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
        if (InitGL.PROGRAM_TYPE != eProgramId)
            return;
        glUniform4f(initGL.locationColor, r, g, b, 1);
    }

    public void setNeedBone(boolean needBone){
        glUniform1i(initGL.locationNeedBone, needBone ? 1 : 0);
    }

    private void setProgram(WhatProgram eProgram) {
        InitGL.PROGRAM_TYPE = eProgram;
        if (eProgram == eProgramShadow)
            InitGL.PROGRAM_INT = initGL.programShadow;
        else if (eProgram == eProgramId)
            InitGL.PROGRAM_INT = initGL.programId;
    }

    public void touchEvent(MotionEvent event, float width, float height) {
        drawSnake.touchEvent(event, width, height);
    }

    public void drawScene(){
        glUseProgram(initGL.programShadow);
        glCullFace(GL_FRONT);
        glBindFramebuffer(GL_FRAMEBUFFER, initGL.shadowFBO.get(0));
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        setProgram(eProgramShadow);
        glBindVertexArray(initGL.vaoShadow.get(0));
        drawSnake.draw(InitGL.PROGRAM_TYPE);
        drawFood.draw();
        glCullFace(GL_BACK);

        glUseProgram(initGL.programId);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, initGL.shadowMap.get(0));
        setProgram(eProgramId);
        glBindVertexArray(initGL.vaoMain.get(0));
        drawSnake.draw(InitGL.PROGRAM_TYPE);
        drawFood.draw();

        drawFence.draw();

        drawPlane();
        drawFreedom();

//        drawTestAnimateModel();
    }
    float angle0 = 0;

    private void drawTestAnimateModel() {
        setNeedBone(true);
        if (firstKostyl){
            firstKostyl = false;
            ANIMATE_MODEL.setNeedUpdate(0);
        }
        ANIMATE_MODEL.animate(initGL.programId, 0);
        setColor(1, 0.3f, 0.3f);
//        rotateM(angle0, 0, 1, 0);
//        rotateM(20, 1, 0, 0);
        rotateM(90, 0, 0, 1);
        rotateM(140, 0, -1, 0);
        rotateM(180, 1, 0, 0);
        rotateM(angle0, 0, 0, 1);
        angle0 += 0.5f;
        translateM(0, 0, 0);
        bindMatrix();
        drawTriangles(ANIMATE_MODEL);
        setIdentityM(translate, rotate, scale);
        bindMatrix();
        setNeedBone(false);
    }
    boolean firstKostyl = true;


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
