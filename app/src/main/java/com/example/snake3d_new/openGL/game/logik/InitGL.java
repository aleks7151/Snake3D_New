package com.example.snake3d_new.openGL.game.logik;

import android.content.res.AssetManager;
import android.opengl.Matrix;

import com.example.snake3d_new.openGL.game.model.dae.GetDataDae;
import com.example.snake3d_new.openGL.game.model.Model;
import com.example.snake3d_new.openGL.game.utils.Order;
import com.example.snake3d_new.openGL.game.utils.ShaderUtils;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static android.opengl.GLES10.GL_FOG;
import static android.opengl.GLES10.glGenTextures;
import static android.opengl.GLES20.GL_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_DEPTH_ATTACHMENT;
import static android.opengl.GLES20.GL_DEPTH_COMPONENT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_INT;
import static android.opengl.GLES20.GL_NEAREST;
import static android.opengl.GLES20.GL_NONE;
import static android.opengl.GLES20.GL_STATIC_DRAW;
import static android.opengl.GLES20.GL_TEXTURE0;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glActiveTexture;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glBufferData;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glGenBuffers;
import static android.opengl.GLES20.glGenFramebuffers;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameteri;
import static android.opengl.GLES20.glUniform1i;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES30.glBindVertexArray;
import static android.opengl.GLES30.glGenVertexArrays;
import static android.opengl.GLES30.glReadBuffer;
import static com.example.snake3d_new.openGL.game.utils.Constants.AMOUNT_X;
import static com.example.snake3d_new.openGL.game.utils.Constants.AMOUNT_Y;
import static com.example.snake3d_new.openGL.game.utils.Constants.BOTTOM;
import static com.example.snake3d_new.openGL.game.utils.Constants.EYE_Z;
import static com.example.snake3d_new.openGL.game.utils.Constants.FAR;
import static com.example.snake3d_new.openGL.game.utils.Constants.LEFT;
import static com.example.snake3d_new.openGL.game.utils.Constants.NEAR;
import static com.example.snake3d_new.openGL.game.utils.Constants.RIGHT;
import static com.example.snake3d_new.openGL.game.utils.Constants.TOP;

public class InitGL {
    private static final String PATH_SHADERS_MAIN = "shaders/main/";
    private static final String PATH_SHADERS_SHADOW = "shaders/shadow/";

    private int width;
    private int height;

    public int programId;
    public int programShadow;
    public WhatProgram program;

    private FloatBuffer vertexData;
    private FloatBuffer vertexNormal;
    private FloatBuffer vertexTexture;
    private FloatBuffer vertexWeight;
    private IntBuffer vertexIndex;

    private AssetManager assets;

    private float[] projectionMatrix = new float[16];
    private float[] viewMatrix = new float[16];
    public float[] translateMatrix = new float[16];
    public float[] rotateMatrix = new float[16];
    public float[] scaleMatrix = new float[16];
    public float[] modelMatrix = new float[16];
    public float[] normalMatrix = new float[16];

    private float[] projectionMatrixShadow = new float[16];
    private float[] viewMatrixShadow = new float[16];

    public int locationModelMatrix;
    public int locationNormalMatrix;
    public int locationColor;

    public int locationModelMatrixShadow;

    public IntBuffer vaoMain = IntBuffer.allocate(1);
    public IntBuffer vaoShadow = IntBuffer.allocate(1);

    public IntBuffer shadowFBO = IntBuffer.allocate(1);
    public IntBuffer shadowMap = IntBuffer.allocate(1);

    public static Model KUB;
    public static Model PLANE;
    public static Model POINT;
    public static Model TEST_MODEL;

    public InitGL(int width, int height){
        this.width = width;
        this.height = height;
    }

    public void init(AssetManager assets) {
        glEnable(GL_DEPTH_TEST);
        glClearColor(0f, 0f, 0f, 0f);
        glViewport(0, 0, width, height);

        this.assets = assets;
        programId = ShaderUtils.getProgram(assets, PATH_SHADERS_MAIN + "vertexMain.glsl",
                PATH_SHADERS_MAIN + "fragmentMain.glsl");
        programShadow = ShaderUtils.getProgram(assets, PATH_SHADERS_SHADOW + "vertexShadow.glsl",
                PATH_SHADERS_SHADOW + "fragmentShadow.glsl");

        Matrix.setIdentityM(translateMatrix, 0);
        Matrix.setIdentityM(rotateMatrix, 0);
        Matrix.setIdentityM(scaleMatrix, 0);
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.setIdentityM(normalMatrix, 0);

        createProjectionMatrix();
        createViewMatrix();

        createProjectionMatrixShadow();
        createViewMatrixShadow();

        prepareData();

        createBuffers();

        bindDataProgramId();
        bindDataProgramShadow();
        glUseProgram(programId);
    }

    private void createViewMatrix() {
        float eyeX = 0;
        float eyeY = 0;
        float eyeZ = (NEAR / (TOP - BOTTOM)) * (float)AMOUNT_Y * EYE_Z;
        //Рассчет позиции камеры по соотношению треугольников,
        //чтобы все квадраты по вертикали умещались в экран по вертикали тютелька в тютельку

        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        float upX = 0;
        float upY = 1;
        float upZ = 0;
        Matrix.setLookAtM(viewMatrix, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void createViewMatrixShadow() {
        float eyeX = 15;
        float eyeY = -7;
        float eyeZ = (NEAR / (TOP - BOTTOM)) * (float)AMOUNT_Y * EYE_Z;
        //Рассчет позиции камеры по соотношению треугольников,
        // чтобы все квадраты по вертикали умещались в экран по вертикали тютелька в тютельку

        float centerX = 0;
        float centerY = 0;
        float centerZ = 0;

        float upX = 0;
        float upY = 1;
        float upZ = 0;
        Matrix.setLookAtM(viewMatrixShadow, 0, eyeX, eyeY, eyeZ, centerX, centerY, centerZ, upX, upY, upZ);
    }

    private void createProjectionMatrix() {
        float left = LEFT;
        float right = RIGHT;
        float ratio = (float) width / height;
        left *= ratio;
        right *= ratio;
        Matrix.frustumM(projectionMatrix, 0, left, right, BOTTOM, TOP, NEAR, FAR);
    }

    private void createProjectionMatrixShadow() {
        final float KOFF = 1.03f;
        float left = LEFT * (AMOUNT_X / 2f) * KOFF;
        float right = RIGHT * (AMOUNT_X / 2f) * KOFF;
        float top = TOP * (AMOUNT_Y / 2f) * KOFF;
        float bottom = BOTTOM * (AMOUNT_Y / 2f) * KOFF;
        Matrix.orthoM(projectionMatrixShadow, 0, left, right, bottom, top, NEAR, FAR);
    }

    private void createBuffers() {
        glGenFramebuffers(1, shadowFBO);
        glBindFramebuffer(GL_FRAMEBUFFER, shadowFBO.get(0));

        glActiveTexture(GL_TEXTURE0);
        glGenTextures(1, shadowMap);
        glBindTexture(GL_TEXTURE_2D, shadowMap.get(0));
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, width, height, 0, GL_DEPTH_COMPONENT, GL_UNSIGNED_SHORT, null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, shadowMap.get(0), 0);

        glReadBuffer(GL_NONE);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void vertexAttribPointer(IntBuffer VAO, Buffer buffer, int sizeVec, int order, final int TYPE){
        IntBuffer VBO = IntBuffer.allocate(1);

        glGenBuffers(1, VBO);
        glBindVertexArray(VAO.get(0));
        glBindBuffer(GL_ARRAY_BUFFER, VBO.get());

        buffer.position(0);
        glBufferData(GL_ARRAY_BUFFER, buffer.limit() * 4, buffer, GL_STATIC_DRAW);

        glVertexAttribPointer(order, sizeVec, TYPE, false, sizeVec * 4, 0);
        glEnableVertexAttribArray(order);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    private void bindDataProgramId() {
        glUseProgram(programId);

        glGenVertexArrays(1, vaoMain);
        vertexAttribPointer(vaoMain, vertexData, 3, 0, GL_FLOAT);
        vertexAttribPointer(vaoMain, vertexNormal, 3, 1, GL_FLOAT);
        vertexAttribPointer(vaoMain, vertexTexture, 2, 2, GL_FLOAT);
        vertexAttribPointer(vaoMain, vertexWeight, 4, 3, GL_FLOAT);
        vertexAttribPointer(vaoMain, vertexIndex, 4, 4, GL_INT);

        float[] projectionViewMatrix = new float[16];
        Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        glUniformMatrix4fv(glGetUniformLocation(programId, "projectionViewMatrix"), 1, false, projectionViewMatrix, 0);

        projectionViewMatrix = new float[16];
        Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrixShadow, 0, viewMatrixShadow, 0);
        glUniformMatrix4fv(glGetUniformLocation(programId, "projectionViewMatrixShadow"), 1, false, projectionViewMatrix, 0);

        locationModelMatrix = glGetUniformLocation(programId, "modelMatrix");
        glUniformMatrix4fv(locationModelMatrix, 1, false, modelMatrix, 0);

        locationNormalMatrix = glGetUniformLocation(programId, "normalMatrix");
        glUniformMatrix4fv(locationNormalMatrix, 1, false, normalMatrix, 0);

        locationColor = glGetUniformLocation(programId, "color");
        glUniform4f(locationColor, 1, 1, 1, 1);

        glUniform1i(glGetUniformLocation(programId, "shadowMap"), 0);
    }

    private void bindDataProgramShadow() {
        glUseProgram(programShadow);

        glGenVertexArrays(1, vaoShadow);
        vertexAttribPointer(vaoShadow, vertexData, 3, 0, GL_FLOAT);

        float[] projectionViewMatrix = new float[16];
        Matrix.multiplyMM(projectionViewMatrix, 0, projectionMatrixShadow, 0, viewMatrixShadow, 0);
        glUniformMatrix4fv(glGetUniformLocation(programShadow, "projectionViewMatrixShadow"), 1, false, projectionViewMatrix, 0);

        locationModelMatrixShadow = glGetUniformLocation(programShadow, "modelMatrix");
        glUniformMatrix4fv(locationModelMatrixShadow, 1, false, modelMatrix, 0);
    }

    private void prepareData() {
        KUB = GetDataDae.getModel(assets, "models/kub.dae");
        PLANE = GetDataDae.getModel(assets, "models/plane.dae");
        POINT = GetDataDae.getPoint();
        TEST_MODEL = GetDataDae.getModel(assets, "models/chel.dae");
        float[] normal = getNotPointsFloat(KUB.normal, PLANE.normal, POINT.normal, TEST_MODEL.normal);
        float[] texture = getNotPointsFloat(KUB.color, PLANE.color, POINT.color, TEST_MODEL.color);
        int[] index = getNotPointsInt(KUB.index, PLANE.index, POINT.index, TEST_MODEL.index);
        float[] weight = getNotPointsFloat(KUB.weight, PLANE.weight, POINT.weight, TEST_MODEL.weight);
        float[] mesh = getPoints(KUB.position, PLANE.position, POINT.position, TEST_MODEL.position);
        KUB.order = 0;
        PLANE.order = 1;
        POINT.order = 2;
        TEST_MODEL.order = 3;
        vertexPut(mesh, normal, texture, weight, index);
    }

    private float[] getPoints(float[]... vertices) {
        Order.orderList = new ArrayList<>();
        int size = 0;
        for (float[] vert : vertices)
            size += vert.length;
        float[] meshes = new float[size];
        int i = 0;
        int tempForList = 0;
        for (float[] vert : vertices) {
            for (float digit : vert)
                meshes[i++] = digit;
            int count = vert.length / 3;
            Order.orderList.add(new int[]{tempForList, count});
            tempForList += count;
        }
        return meshes;
    }

    private float[] getNotPointsFloat(float[]... vertices) {
        int size = 0;
        for (float[] vert : vertices)
            size += vert.length;
        float[] colorNormalAndTexture = new float[size];
        int i = 0;
        for (float[] vert : vertices)
            for (float digit : vert)
                colorNormalAndTexture[i++] = digit;
        return colorNormalAndTexture;
    }

    private int[] getNotPointsInt(int[]... vertices) {
        int size = 0;
        for (int[] vert : vertices)
            size += vert.length;
        int[] notPoints = new int[size];
        int i = 0;
        for (int[] vert : vertices)
            for (int digit : vert)
                notPoints[i++] = digit;
        return notPoints;
    }

    private void vertexPut(float[] mesh, float[] normal, float[] texture, float[] weight, int[] index) {
        vertexData = ByteBuffer
                .allocateDirect(mesh.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexData.put(mesh);

        vertexNormal = ByteBuffer
                .allocateDirect(normal.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexNormal.put(normal);

        vertexTexture = ByteBuffer
                .allocateDirect(texture.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexTexture.put(texture);

        vertexWeight = ByteBuffer
                .allocateDirect(weight.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        vertexWeight.put(weight);

        vertexIndex = ByteBuffer
                .allocateDirect(index.length * 4)
                .order(ByteOrder.nativeOrder())
                .asIntBuffer();
        vertexIndex.put(index);
    }
}
