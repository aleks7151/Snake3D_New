package com.example.snake3d_new.openGL.game.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dae {
    static void getGeometries(Model model, List<String> listGeometries) {
        model.position = new ArrayList<>();
        model.normal = new ArrayList<>();
        model.color = new ArrayList<>();//Использоваться не будет, но это, возможно, пока
        model.orderMesh = new ArrayList<>();
        String stringPosition = listGeometries.get(0);
        String stringNormal = listGeometries.get(1);
        String stringColor = listGeometries.get(2);//Использоваться не будет, но это, возможно, пока
        String stringOrder = listGeometries.get(3);

        toFloatArrayFromString(model.position, stringPosition, model.STRIDE_POSITION);
        toFloatArrayFromString(model.normal, stringNormal, model.STRIDE_NORMAL);
        toFloatArrayFromString(model.color, stringColor, model.STRIDE_COLOR);
        toIntArrayFromString(model.orderMesh, stringOrder, model.STRIDE_ORDER);

        model.setPointsAmount(model.orderMesh.size());
    }

    static void getControllers(Model model, List<String> listControllers) {
        model.boneMatrixBegin = new ArrayList<>();
        model.weight = new ArrayList<>();
        model.amountOfBone = new ArrayList<>();
        model.jointWeight = new ArrayList<>();
        String stringBoneMatrixBegin = listControllers.get(0);
        String stringWeights= listControllers.get(1);
        String stringAmountOfBones = listControllers.get(2);
        String stringJointsWeights = listControllers.get(3);

        toFloatArrayFromString(model.boneMatrixBegin, stringBoneMatrixBegin, model.STRIDE_BONE_MATRIX_BEGIN);
        toFloatFromString(model.weight, stringWeights);
        toIntFromString(model.amountOfBone, stringAmountOfBones);
        toIntArrayFromStringAndAmountBones(model.jointWeight, stringJointsWeights, model.amountOfBone);

        if (!model.checkPointsAmount(model.amountOfBone.size()))
            Log.d("MyLog", "Не совпадает количество точек в geometries и controllers. Size: " + model.getPointsAmount() + "  newSie: " + model.amountOfBone.size());
        model.setBonesAmount(model.boneMatrixBegin.size());
    }

    public static void getAnimations(Model model, List<String> listAnimations) {
    }

    private static void toIntFromString(List<Integer> list, String str) {
        try (Scanner scanner = new Scanner(str)){
            while (scanner.hasNext()) {
                list.add(Integer.parseInt(scanner.next()));
            }
        }
        catch (Exception e){
            Log.d("MyLog", "Fucck\n" + e);
        }
    }

    private static void toFloatFromString(List<Float> list, String str) {
        try (Scanner scanner = new Scanner(str)){
            while (scanner.hasNext()) {
                list.add(Float.parseFloat(scanner.next()));
            }
        }
        catch (Exception e){
            Log.d("MyLog", "Fucck\n" + e);
        }
    }

    private static void toIntArrayFromString(List<int[]> list, String str, int stride) {
        try (Scanner scanner = new Scanner(str)){
            int iteration = 0;
            int[] array = new int[stride];
            while (scanner.hasNext()) {
                array[iteration] = Integer.parseInt(scanner.next());
                iteration++;
                if (iteration == stride){
                    iteration = 0;
                    list.add(array);
                    array = new int[stride];
                }
            }
        }
        catch (Exception e){
            Log.d("MyLog", "Fucck\n" + e);
        }
    }

    private static void toFloatArrayFromString(List<float[]> list, String str, int stride) {
        try (Scanner scanner = new Scanner(str)){
            int iteration = 0;
            float[] array = new float[stride];
            while (scanner.hasNext()) {
                array[iteration] = Float.parseFloat(scanner.next());
                iteration++;
                if (iteration == stride){
                    iteration = 0;
                    list.add(array);
                    array = new float[stride];
                }
            }
        }
        catch (Exception e){
            Log.d("MyLog", "Fucck\n" + e);
        }
    }

    private static void toIntArrayFromStringAndAmountBones(List<int[][]> list, String str, List<Integer> amountOfBones) {
        try (Scanner scanner = new Scanner(str)){
            for (int amount : amountOfBones){
                int[][] array = new int[2][amount];
                for (int i = 0; i < amount * 2; i++){
                    if (i % 2 == 0)
                        array[0][i / 2] = Integer.parseInt(scanner.next());
                    else
                        array[1][i / 2] = Integer.parseInt(scanner.next());
                }
                list.add(array);
            }
        }
        catch (Exception e){
            Log.d("MyLog", "Fucck\n" + e);
        }
    }
}
