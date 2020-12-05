package com.example.snake3d_new.openGL.game.logik.snakeAndFood;

import android.util.Log;

import com.example.snake3d_new.openGL.game.logik.Draw;
import com.example.snake3d_new.openGL.game.logik.WhatProgram;
import com.example.snake3d_new.openGL.game.logik.snakeAndFood.utils.Coord;
import com.example.snake3d_new.openGL.game.utils.MatrixEnum;

import static com.example.snake3d_new.openGL.game.utils.Constants.X_BEGIN;
import static com.example.snake3d_new.openGL.game.utils.Constants.Y_BEGIN;
import static com.example.snake3d_new.openGL.game.utils.Constants.Z_BEGIN;
import static com.example.snake3d_new.openGL.game.utils.Constants.freedom;
import static com.example.snake3d_new.openGL.game.utils.Constants.howMuchFreedom;
import static com.example.snake3d_new.openGL.game.utils.MatrixEnum.translate;
import static com.example.snake3d_new.openGL.game.utils.Order.KUB;

public class DrawFood {
    public Coord food;
    private Draw draw;

    public DrawFood(Draw draw){
        this.draw = draw;
    }

    public void createFood() {
        int random = (int) Math.round((howMuchFreedom - 1) * Math.random());
        boolean myBreak = false;
        for (int i = 0; i < freedom.length; i++){
            for (int k = 0; k < freedom[i].length; k++){
                if (freedom[i][k]) {
                    if (random == 0){
                        freedom[i][k] = false;
                        howMuchFreedom--;
                        food = new Coord(i, k, 0);
                        myBreak = true;
                        break;
                    }
                    random--;
                }
            }
            if (myBreak)
                break;
        }
    }

    public void draw(){
        draw.setColor(0, 0.2f, 0.8f);
        draw.translateM(X_BEGIN, Y_BEGIN, Z_BEGIN);
        draw.translateM(food.getX(), food.getY(), food.getZ());
        draw.bindMatrix();

        draw.drawTriangles(KUB);

        draw.setIdentityM(translate);
        draw.bindMatrix();
    }
}
