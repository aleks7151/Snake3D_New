package com.example.snake3d_new.openGL.game.model;

import java.util.List;

public class Model {
    public final int STRIDE_POSITION = 3;
    public final int STRIDE_NORMAL = 3;
    public final int STRIDE_COLOR = 2;
    public final int STRIDE_ORDER = 3;
    public List<float[]> position = null;
    public List<float[]> normal = null;
    public List<float[]> color = null;
    public List<int[]> orderMesh = null;
    private int pointsAmount;

    public final int STRIDE_BONE_MATRIX_BEGIN = 16;
    public List<float[]> boneMatrixBegin = null;
    public List<Float> weight = null;
    public List<Integer> amountOfBone = null;
    public List<int[][]> jointWeight = null;
    public int bonesAmount = 0;

    public int order;//Для порядковой отрисовки

    public void setPointsAmount(int pointsAmount){
        this.pointsAmount = pointsAmount;
    }

    public boolean checkPointsAmount(int pointsAmount) {
        return this.pointsAmount == pointsAmount;
    }

    public int getPointsAmount() {
        return pointsAmount;
    }

    public void setBonesAmount(int bonesAmount) {
        this.bonesAmount = bonesAmount;
    }

    private final int STRIDE_WEIGHT = 4;
    private final int STRIDE_INDEX = 4;
    public float[] finalPosition = null;
    public float[] finalNormal = null;
    public float[] finalColor = null;
    public float[] finalWeight = null;
    public float[] finalIndex = null;
    public float[] finalBonesMatrix = null;

    public void setFinalData(){
        finalPosition = new float[getPointsAmount() * STRIDE_POSITION];
        finalNormal = new float[getPointsAmount() * STRIDE_NORMAL];
        finalColor = new float[getPointsAmount() * STRIDE_COLOR];
        for (int i = 0; i < pointsAmount; i++){
            float[] point = position.get(
                    orderMesh.get(i)[0]
            );
            float[] normal = this.normal.get(
                    orderMesh.get(i)[1]
            );
            float[] color = this.color.get(
                    orderMesh.get(i)[2]
            );
            System.arraycopy(point, 0, finalPosition, i * STRIDE_POSITION, point.length);
            System.arraycopy(normal, 0, finalNormal, i * STRIDE_NORMAL, normal.length);
            System.arraycopy(color, 0, finalColor, i * STRIDE_COLOR, color.length);

            ///Исправить getPoint исправить здесь

            int[][] array = jointWeight.get(i);
            for (int k = 0; k < STRIDE_INDEX/*Потому что vec4*/; k++){
                if (k < array[0].length)
                    finalIndex[i * STRIDE_INDEX + k] = array[0][k];
                else
                    finalIndex[i * STRIDE_INDEX + k] = -1;
                if (k < array[1].length)
                    finalWeight[i * STRIDE_WEIGHT + k] = array[1][k];
                else
                    finalWeight[i * STRIDE_WEIGHT + k] = -1;
            }
        }
    }
}
