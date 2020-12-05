package com.example.snake3d_new.openGL.game.model;

public class Model {
    public float[] positions = null;
    public float[] normals = null;
    public float[] colors = null;

    public float[] weights = null;
    public int[] indexes = null;//Для каждой точки по четыре веса и индекса, так как передаем инфу в vec4
    public int bonesAmount = 0;

    public int order;

    private int size;

    public void setSize(int size){
        this.size = size;
    }

    public boolean checkSize(int size) {
        return this.size == size;
    }

    public int getSize() {
        return size;
    }

    public void setBonesAmount(int bonesAmount) {
        this.bonesAmount = bonesAmount;
    }
}
