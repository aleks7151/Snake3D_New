package com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood;

import com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood.utils.Coord;
import com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood.utils.Direction;

import java.util.ArrayList;
import java.util.List;

import static com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood.utils.Direction.DOWN;
import static com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood.utils.Direction.LEFT;
import static com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood.utils.Direction.RIGHT;
import static com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood.utils.Direction.UP;
import static com.example.snake3d_new.openGL.game.utils.Constants.AMOUNT_X;
import static com.example.snake3d_new.openGL.game.utils.Constants.AMOUNT_Y;
import static com.example.snake3d_new.openGL.game.utils.Constants.QUANTITY_SNAKE;
import static com.example.snake3d_new.openGL.game.utils.Constants.freedom;
import static com.example.snake3d_new.openGL.game.utils.Constants.howMuchFreedom;

public class SnakeBackend {
    public List<Coord> listSnake = new ArrayList<>();
    public List<Coord> listBefore = new ArrayList<>();
    public List<Boolean> teleportNow = new ArrayList<>();
    public Direction direction = UP;
    public Direction futureDirection = UP;
    public final double BEGIN_SPEED = 0.14;
    private double pixel = 0;
    private DrawFood drawFood;

    public SnakeBackend(DrawFood drawFood){
        this.drawFood = drawFood;
        for (int i = 0; i < QUANTITY_SNAKE; i++){
            int x = (AMOUNT_X - 1) / 2;
            int y = (AMOUNT_Y - 1) / 2 - i;
            listSnake.add(new Coord(x, y, 0));
            listBefore.add(listSnake.get(i).clone());
            teleportNow.add(false);
            freedom[x][y] = false;
            howMuchFreedom--;
            if (i == 0){
                y = (AMOUNT_Y - 1) / 2 + 1;
                freedom[x][y] = false;
                howMuchFreedom--;
            }
        }
        drawFood.createFood();
    }

    public void move(double speedWithKoef){
        double speed;
        if (cretinDouble(pixel + speedWithKoef) > 1)
            speed = cretinDouble(1 - pixel);
        else
            speed = cretinDouble(speedWithKoef);
        for (int i = listSnake.size() - 1; i > 0; i--){
            Coord now = listSnake.get(i);
            Coord before = listBefore.get(i - 1);

            if (now.getX() - before.getX() > 3)
                now.plusX(speed);
            else if (now.getX() - before.getX() < -3)
                now.plusX(-speed);
            else if (now.getY() - before.getY() > 3)
                now.plusY(speed);
            else if (now.getY() - before.getY() < -3)
                now.plusY(-speed);

            else if (now.getX() > before.getX())
                now.plusX(-speed);
            else if (now.getX() < before.getX())
                now.plusX(speed);
            else if (now.getY() > before.getY())
                now.plusY(-speed);
            else if (now.getY() < before.getY())
                now.plusY(speed);
            cretinCoord(now);
            if (!teleportNow.get(i))
                teleportNow.set(i, teleport(now, speed));
        }

        if (direction == RIGHT)
            listSnake.get(0).plusX(speed);
        else if (direction == LEFT)
            listSnake.get(0).plusX(-speed);
        else if (direction == UP)
            listSnake.get(0).plusY(speed);
        else if (direction == DOWN)
            listSnake.get(0).plusY(-speed);
        cretinCoord(listSnake.get(0));
        if (!teleportNow.get(0))
            teleportNow.set(0, teleport(listSnake.get(0), speed));


        checkPixel(speed, speedWithKoef);
    }

    private boolean teleport(Coord coord, double speed) {
        if (coord.getX() > AMOUNT_X - 1d){
            coord.setX(-1d + speed);
        }
        else if (coord.getX() < 0){
            coord.setX(AMOUNT_X - speed);
        }
        else if (coord.getY() > AMOUNT_Y - 1d){
            coord.setY(-1d + speed);
        }
        else if (coord.getY() < 0){
            coord.setY(AMOUNT_Y - speed);
        }
        else {
            return false;
        }
        cretinCoord(coord);
        return true;
    }

    private void checkPixel(double speed, double speedWithKoef) {
        pixel = cretinDouble(pixel + speed);
        if (pixel >= 1){
            pixel = 0;
            direction = futureDirection;
            changeFreedom();
            for (int i = 0; i < listSnake.size(); i++) {
                listBefore.set(i, listSnake.get(i).clone());
                cretinCoord(listBefore.get(i));
                teleportNow.set(i, false);
            }
            if (speedWithKoef > speed)
                move(cretinDouble(speedWithKoef - speed));
        }
    }

    private boolean checkFood() {
        Coord firstSnake = listSnake.get(0);
        if (firstSnake.equals(drawFood.food)){
            drawFood.createFood();
            listSnake.add(
                    listBefore.get(listBefore.size() - 1)
                            .clone()
            );
            listBefore.add(new Coord(0, 0, 0));
            teleportNow.add(false);
            return false;
        }
        return true;
    }

    private void changeFreedom() {
        boolean endQue = checkFood();
        Coord temp;
        int indexX;
        int indexY;

        if (endQue) {
            temp = listBefore.get(listBefore.size() - 1);
            indexX = (int)Math.round(temp.getX());
            indexY = (int)Math.round(temp.getY());
            freedom[indexX][indexY] = true;
        }

        temp = listSnake.get(0);
        indexX = (int)Math.round(temp.getX());
        indexY = (int)Math.round(temp.getY());
        if (direction == UP){
            indexY++;
            if (indexY > AMOUNT_Y - 1)
                indexY = 0;
        }
        else if (direction == DOWN){
            indexY--;
            if (indexY < 0)
                indexY = AMOUNT_Y - 1;
        }
        else if (direction == RIGHT){
            indexX++;
            if (indexX > AMOUNT_X - 1)
                indexX = 0;
        }
        else if (direction == LEFT){
            indexX--;
            if (indexX < 0)
                indexX = AMOUNT_X - 1;
        }
        freedom[indexX][indexY] = false;
    }

    public void cretinCoord(Coord now) {
        //Потому что double неточный. Возможно, в будущем следует перейти на long
        now.setX(cretinDouble(now.getX()));
        now.setY(cretinDouble(now.getY()));
        now.setZ(cretinDouble(now.getZ()));
    }

    public double cretinDouble(double x){
        //Потому что double неточный. Возможно, в будущем следует перейти на long
        return Math.round(x * Math.pow(10, 8)) / Math.pow(10, 8);
    }
    ///Изучить возможность записи анимации в файл, и затем воспроизводить ее. А то та хуйня в прошлом проекте - это пиздец

    ///Закинуть данные скелета на видюху!!!!!!!Срочно переделай умножение на матрцу можели в шейдере! Оно должно идти после костей

    ///криво читается текстура
    ///И, соответсвенно, НОРМАЛЬНУЮ обработку событий
    ///Попробовать реализовать мягкие тени. Сделать разрешение у тени раза в 3 меньше, создать еще две теневые камеры, которые чуть левее/правее, отрендерить три тени, размыть и смешать
    ///Сделать анимацию еды
    ///Чтобы источник света и тени совпадли
    //РЕализовать поворот нормали по нормальной матрице
}
