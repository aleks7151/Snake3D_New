package com.example.snake3d_new.openGL.game.utils;

import com.example.snake3d_new.openGL.game.model.Model;

import java.util.List;

//Класс для удобного вызова glDrawArrays
public class Order {
    public static List<int[]> orderList;

    public static int getBegin(Model model){
        return orderList.get(model.order)[0];
    }

    public static int getCount(Model model){
        return orderList.get(model.order)[1];
    }
}
