package com.example.snake3d_new.openGL.game.model;

import java.util.Arrays;
import java.util.List;

public class Bone {
    private String name = null;
    private float[] invertMatrix = null;
    private float[] beginBoneMatrix = null;
    public List<Bone> childs = null;

    private List<Double> time = null;
    private List<float[]> animMatrix = null;

    private float[] matrixNow = null;

    private int indexBone;

    public Bone(String name, float[] invertMatrix, int indexBone){
        this.name = name;
        this.invertMatrix = invertMatrix;
        this.indexBone = indexBone;
    }

    public String getName() {
        return name;
    }

    public float[] getInvertMatrix() {
        return invertMatrix;
    }

    public void setTime(List<Double> time) {
        this.time = time;
    }

    public void setAnimMatrix(List<float[]> animMatrix) {
        this.animMatrix = animMatrix;
    }

    public List<Double> getTime() {
        return time;
    }

    public List<float[]> getAnimMatrix() {
        return animMatrix;
    }

    public void setBeginBoneMatrix(float[] beginBoneMatrix) {
        this.beginBoneMatrix = beginBoneMatrix;
    }

    public float[] getBeginBoneMatrix() {
        return beginBoneMatrix;
    }

    public void setMatrixNow(float[] matrixNow) {
        this.matrixNow = matrixNow;
    }

    public float[] getMatrixNow() {
        return matrixNow;
    }

    public int getIndexBone() {
        return indexBone;
    }

    @Override
    public String toString() {
        StringBuilder namesChilds = new StringBuilder();
        if (childs != null){
            for (Bone boneChild : childs){
                namesChilds.append(boneChild.getName()).append("   ");
            }
        }
        else {
            namesChilds.append("null");
        }
        return "Bone{" +
                "\nname='" + name + "'" + "\n" +
//                ", invertMatrix=" + Arrays.toString(invertMatrix) + "\n" +
//                ", beginBoneMatrix=" + Arrays.toString(beginBoneMatrix) + "\n" +
                ", childs=" + namesChilds.toString() + "\n" +
//                ", time=" + time + "\n" +
//                ", animMatrix=" + animMatrix + "\n" +
//                ", needUpdate=" + needUpdate + "\n" +
//                ", matrixNow=" + Arrays.toString(matrixNow) + "\n" +
                ", indexBone=" + indexBone + "\n" +
                '}';
    }
}
