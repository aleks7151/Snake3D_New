package com.example.snake3d_new.openGL.game.logik.snakeAndFood;

import android.view.MotionEvent;

import com.example.snake3d_new.openGL.game.logik.Draw;
import com.example.snake3d_new.openGL.game.logik.WhatProgram;
import com.example.snake3d_new.openGL.game.logik.snakeAndFood.utils.Coord;

import java.util.List;

import static com.example.snake3d_new.openGL.game.logik.InitGL.KUB;
import static com.example.snake3d_new.openGL.game.logik.WhatProgram.eProgramShadow;
import static com.example.snake3d_new.openGL.game.logik.snakeAndFood.utils.Direction.DOWN;
import static com.example.snake3d_new.openGL.game.logik.snakeAndFood.utils.Direction.LEFT;
import static com.example.snake3d_new.openGL.game.logik.snakeAndFood.utils.Direction.RIGHT;
import static com.example.snake3d_new.openGL.game.logik.snakeAndFood.utils.Direction.UP;
import static com.example.snake3d_new.openGL.game.utils.Constants.X_BEGIN;
import static com.example.snake3d_new.openGL.game.utils.Constants.Y_BEGIN;
import static com.example.snake3d_new.openGL.game.utils.Constants.Z_BEGIN;
import static com.example.snake3d_new.openGL.game.utils.MatrixEnum.translate;

public class DrawSnake {
    private Draw draw;
    private SnakeBackend snake;
    private long time = 0;
    private boolean firstIteration = true;
    private final double FPS = 60;
    private final double MILI_SEC;

    public DrawSnake(Draw draw, SnakeBackend snake){
        MILI_SEC = 1000 / FPS;
        this.draw = draw;
        this.snake = snake;
    }

    public void touchEvent(MotionEvent event, float width, float height) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (event.getX() / width > 0.5f){
                if (snake.direction == UP)
                    snake.futureDirection = RIGHT;
                else if (snake.direction == RIGHT)
                    snake.futureDirection = DOWN;
                else if (snake.direction == DOWN)
                    snake.futureDirection = LEFT;
                else if (snake.direction == LEFT)
                    snake.futureDirection = UP;
            }
            else {
                if (snake.direction == UP)
                    snake.futureDirection = LEFT;
                else if (snake.direction == LEFT)
                    snake.futureDirection = DOWN;
                else if (snake.direction == DOWN)
                    snake.futureDirection = RIGHT;
                else if (snake.direction == RIGHT)
                    snake.futureDirection = UP;
            }
        }
    }

    private double getKoef() {
        double koef;
        if (firstIteration){
            koef = 1;
            firstIteration = false;
        }
        else {
            koef = ((System.nanoTime() - time) / Math.pow(10, 6)) / MILI_SEC;
        }
        time = System.nanoTime();
        return koef;
    }

    private void snakeMain(List<Coord> list, boolean before){
        for (int i = 0; i < list.size(); i++){
            if (i == list.size() - 1 && before)
                break;
            Coord coord = list.get(i);
            draw.translateM(X_BEGIN, Y_BEGIN, Z_BEGIN);
            draw.translateM(coord.getX(), coord.getY(), coord.getZ());
            draw.bindMatrix();

            draw.drawTriangles(KUB);

            draw.setIdentityM(translate);
            draw.bindMatrix();
        }
    }

    long rivok = 0;
    boolean quRivok = false;
    public void draw(WhatProgram program){
        if (!quRivok){//Костыль, конечно. После теста тело этого if оставить, остальное нахер
            if (program == eProgramShadow) {
                double koef = getKoef();
                snake.move(snake.cretinDouble(koef * snake.BEGIN_SPEED));
            }
        }
        else if (System.currentTimeMillis() - rivok > 100) {
            snake.move(snake.cretinDouble(snake.BEGIN_SPEED));
            rivok = System.currentTimeMillis();
        }

        draw.setColor(0.6f, 1, 1);
        snakeMain(snake.listSnake, false);
        snakeMain(snake.listBefore, true);
    }
}
