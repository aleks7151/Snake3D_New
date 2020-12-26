package com.example.snake3d_new.openGL.game.drawAndInit;

import com.example.snake3d_new.openGL.game.drawAndInit.snakeAndFood.utils.Direction;
import com.example.snake3d_new.openGL.game.utils.MatrixEnum;

import java.util.Arrays;

import static com.example.snake3d_new.openGL.game.drawAndInit.InitGL.FENCE;
import static com.example.snake3d_new.openGL.game.utils.Constants.AMOUNT_X;
import static com.example.snake3d_new.openGL.game.utils.Constants.AMOUNT_Y;
import static com.example.snake3d_new.openGL.game.utils.Constants.K_BOTTOM;
import static com.example.snake3d_new.openGL.game.utils.Constants.K_LEFT;
import static com.example.snake3d_new.openGL.game.utils.Constants.K_RIGHT;
import static com.example.snake3d_new.openGL.game.utils.Constants.K_TOP;

public class DrawFence {
    private Draw draw;

    public boolean[] animationToLeft = new boolean[AMOUNT_Y];
    public boolean[] animationToRight = new boolean[AMOUNT_Y];
    public boolean[] animationToBottom = new boolean[AMOUNT_X];
    public boolean[] animationToUp = new boolean[AMOUNT_X];
    {
        Arrays.fill(animationToLeft, false);
        Arrays.fill(animationToRight, false);
        Arrays.fill(animationToBottom, false);
        Arrays.fill(animationToUp, false);
    }

    public DrawFence(Draw draw) {
        this.draw = draw;
    }

    private void draw(int endCycle, boolean[] animation, int shiftAnimationList, Direction direction){
        if (direction == Direction.RIGHT)
            draw.rotateM(180, 0, 0, 1);
        else if (direction == Direction.DOWN)
            draw.rotateM(90, 0, 0, 1);
        else if (direction == Direction.UP)
            draw.rotateM(-90, 0, 0, 1);

        for (int i = 0; i < endCycle; i++){
            if (animation[i]){
                animation[i] = false;
                FENCE.setNeedUpdate(i + shiftAnimationList);
            }
            if (FENCE.animationList.get(i + shiftAnimationList).needAnimate) {
                draw.setNeedBone(true);
                FENCE.animate(InitGL.PROGRAM_INT, i + shiftAnimationList);
            }
            if (direction == Direction.LEFT)
                draw.translateM(K_LEFT, K_BOTTOM + i + 0.5f, 0);
            else if (direction == Direction.RIGHT)
                draw.translateM(K_RIGHT, K_BOTTOM + i + 0.5f, 0);
            else if (direction == Direction.DOWN)
                draw.translateM(K_LEFT + i + 0.5f, K_BOTTOM, 0);
            else if (direction == Direction.UP)
                draw.translateM(K_LEFT + i + 0.5f, K_TOP, 0);
            draw.bindMatrix();
            draw.drawTriangles(FENCE);
            draw.setIdentityM(MatrixEnum.translate);
            draw.setNeedBone(false);
        }
        draw.setIdentityM(MatrixEnum.rotate);
    }

    public void draw(){
        draw(AMOUNT_Y, animationToLeft, 0, Direction.LEFT);
        draw(AMOUNT_Y, animationToRight, AMOUNT_Y, Direction.RIGHT);
        draw(AMOUNT_X, animationToBottom, 2 * AMOUNT_Y, Direction.DOWN);
        draw(AMOUNT_X, animationToUp, 2 * AMOUNT_Y + AMOUNT_X, Direction.UP);
        draw.setIdentityM(MatrixEnum.translate, MatrixEnum.rotate);
        draw.bindMatrix();
    }
}
