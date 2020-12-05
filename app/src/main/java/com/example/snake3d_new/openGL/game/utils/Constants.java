package com.example.snake3d_new.openGL.game.utils;

import java.util.Arrays;

public class Constants {
    //Соотношение кубиков по вертикали к горизонтали
    public static final float RATIO = 6f / 3f;
    //Сколько кубиков будет вертикально
    public static final int AMOUNT_Y = 30;
    //Сколько кубиков будет горизонтально
    public static final int AMOUNT_X = (int)((float)AMOUNT_Y / RATIO);

    //Для создания projectionMatrix
    public static final float BOTTOM = -1.0f;
    public static final float TOP = 1.0f;
    public static final float LEFT = -1.0f;
    public static final float RIGHT = 1.0f;
    public static final float NEAR = 1.5f;
    public static final float FAR = 100.0f;

    //Для определения границ плоскости, по которой будет ездить змейка
    public static final float K_LEFT = -AMOUNT_X / 2f;
    public static final float K_RIGHT = AMOUNT_X / 2f;
    public static final float K_TOP = AMOUNT_Y / 2f;
    public static final float K_BOTTOM = -AMOUNT_Y / 2f;

    //Начало отсчета для змейки
    public static final float X_BEGIN = K_LEFT + 0.5f;
    public static final float Y_BEGIN = K_BOTTOM + 0.5f;
    public static final float Z_BEGIN = 0.5f;
//    public static final float Z_BEGIN = 0;//Для проверки freedom, потом, конечно убрать

    //Для камеры, в последствии это поле будет пополняться
    public static final float EYE_Z = 1.1f;

    //Количество кубиков змейки изначально
    public static final int QUANTITY_SNAKE = 10;

    //Массив, показывающий, сколько свободных клеточек осталось. А так же переменная, которая делает то же самое
    public static boolean[][] freedom = new boolean[AMOUNT_X][AMOUNT_Y];
    public static int howMuchFreedom = AMOUNT_X * AMOUNT_Y;
    static {
        for (boolean[] booleans : freedom)
            Arrays.fill(booleans, true);
    }
}
