package com.example.snake3d_new.openGL.game.utils;

//Енамка для матриц, чтобы понимать, что обнулить в setIdentity
public enum MatrixEnum {
    translate(0), rotate(1), scale(2);

    private int i;

    MatrixEnum(int i){
        this.i = i;
    }

    public int getI(){
        return i;
    }
}
