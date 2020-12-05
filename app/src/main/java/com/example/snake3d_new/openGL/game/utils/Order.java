package com.example.snake3d_new.openGL.game.utils;

import java.util.List;

//Класс для удобного вызова glDrawArrays
public class Order {
    public static List<int[]> orderList;
    public static int KUB;
    public static int PLANE;
    public static int POINT;
    public static int TEST;

    public static int getBegin(int object){
        return orderList.get(object)[0];
    }

    public static int getCount(int object){
        return orderList.get(object)[1];
    }
}
