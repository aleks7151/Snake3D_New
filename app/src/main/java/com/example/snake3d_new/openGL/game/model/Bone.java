package com.example.snake3d_new.openGL.game.model;

import java.util.Arrays;
import java.util.List;

public class Bone {
    private String name = null;
    private float[] invertMatrix = null;
    private float[] beginBoneMatrix = null;
    private Bone parent = null;

    private List<Float> time = null;
    private List<float[]> animMatrix = null;


    public Bone(String name, float[] invertMatrix){
        this.name = name;
        this.invertMatrix = invertMatrix;
    }

    public String getName() {
        return name;
    }

    public float[] getInvertMatrix() {
        return invertMatrix;
    }

    public void setTime(List<Float> time) {
        this.time = time;
    }

    public void setAnimMatrix(List<float[]> animMatrix) {
        this.animMatrix = animMatrix;
    }

    public List<Float> getTime() {
        return time;
    }

    public List<float[]> getAnimMatrix() {
        return animMatrix;
    }

    public void setBeginBoneMatrix(float[] beginBoneMatrix) {
        this.beginBoneMatrix = beginBoneMatrix;
    }

    public void setParent(Bone parent) {
        this.parent = parent;
    }

    public float[] getBeginBoneMatrix() {
        return beginBoneMatrix;
    }

    public Bone getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "Bone{" +
                "name='" + name + '\'' +
                ", invertMatrix=" + Arrays.toString(invertMatrix) +
                ", beginBoneMatrix=" + Arrays.toString(beginBoneMatrix) +
                ", time=" + time +
                ", parent=" + (parent == null ? "null" : parent.getName()) +
                '}';
    }
}
