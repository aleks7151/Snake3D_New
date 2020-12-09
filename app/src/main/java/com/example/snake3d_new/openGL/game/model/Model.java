package com.example.snake3d_new.openGL.game.model;

import java.util.Map;

public class Model {
    public int order;//Для порядковой отрисовки

    public float[] position = null;
    public float[] normal = null;
    public float[] color = null;

    public float[] weight = null;
    public int[] index = null;

    public Map<String, Bone> mapBones = null;
}
