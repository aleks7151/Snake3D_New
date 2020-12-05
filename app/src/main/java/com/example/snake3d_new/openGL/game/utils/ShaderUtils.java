package com.example.snake3d_new.openGL.game.utils;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

//Класс для загрузки чтения и компиляции шейдеров в программу
public class ShaderUtils {
    private static String readShader(AssetManager assetManager, String file) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = null;
            try {
                InputStream inputStream = assetManager.open(file);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                    stringBuilder.append("\n");
                }
            } finally {
                if (bufferedReader != null)
                    bufferedReader.close();
            }
        } catch (IOException e) {
            Log.d("EXCEPTION((", e + "");
        } catch (Resources.NotFoundException e) {
            Log.d("EXCEPTION((", e + "");
        }
        return stringBuilder.toString();
    }

    public static int getProgram(AssetManager assetManager, String vertexName, String fragmentName){
        String vertexStr = readShader(assetManager, vertexName);
        String fragmentStr = readShader(assetManager, fragmentName);
        int vertexId = loadShader(GLES20.GL_VERTEX_SHADER, vertexStr);
        int fragmentId = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentStr);
        int programId = GLES20.glCreateProgram();
        GLES30.glAttachShader(programId, vertexId);
        GLES30.glAttachShader(programId, fragmentId);
        GLES30.glLinkProgram(programId);
        GLES30.glDeleteShader(vertexId);
        GLES30.glDeleteShader(fragmentId);
        return programId;
    }

    private static int loadShader(int type, String shaderCode) {
        int shader = GLES20.glCreateShader(type);
        GLES30.glShaderSource(shader, shaderCode);
        GLES30.glCompileShader(shader);
        return shader;
    }
}
