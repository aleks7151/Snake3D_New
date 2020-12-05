package com.example.snake3d_new.openGL.game.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Dae {
    static void getGeometries(Model model, List<String> listGeometries) {
        List<float[]> points = new ArrayList<>();
        List<float[]> normals = new ArrayList<>();
        List<float[]> colors = new ArrayList<>();//Использоваться не будет, но это, возможно, пока
        List<int[]> order = new ArrayList<>();
        String stringPosition = listGeometries.get(0);
        String stringNormal = listGeometries.get(1);
        String stringColor = listGeometries.get(3);//Использоваться не будет, но это, возможно, пока
        String stringOrder = listGeometries.get(3);

        toFloatArrayFromString(points, stringPosition, 3);
        toFloatArrayFromString(normals, stringNormal, 3);
        toFloatArrayFromString(colors, stringColor, 2);
        toIntArrayFromString(order, stringOrder, 3);

        model.setSize(order.size());
        model.positions = new float[model.getSize() * 3];
        model.normals = new float[model.getSize() * 3];
        model.colors = new float[model.getSize() * 3];
        for (int i = 0; i < order.size(); i++){
            float[] point = points.get(
                    order.get(i)[0]
            );
            float[] normal = normals.get(
                    order.get(i)[1]
            );
            float[] color = colors.get(
                    order.get(i)[2]
            );
            System.arraycopy(point, 0, model.positions, i * 3, point.length);
            System.arraycopy(normal, 0, model.normals, i * 3, normal.length);
            System.arraycopy(color, 0, model.colors, i * 3, color.length);
        }
    }

    static void getControllers(Model model, List<String> listControllers) {
        List<float[]> boneMatrixBegin = new ArrayList<>();
        List<Float> weights = new ArrayList<>();
        List<Integer> amountOfBones = new ArrayList<>();
        List<int[][]> jointsWeights = new ArrayList<>();
        String stringBoneMatrixBegin = listControllers.get(0);
        String stringWeights= listControllers.get(1);
        String stringAmountOfBones = listControllers.get(2);
        String stringJointsWeights = listControllers.get(3);

        toFloatArrayFromString(boneMatrixBegin, stringBoneMatrixBegin, 16);
        toFloatFromString(weights, stringWeights);
        toIntFromString(amountOfBones, stringAmountOfBones);
        toIntArrayFromStringAndAmountBones(jointsWeights, stringJointsWeights, amountOfBones);

        if (!model.checkSize(amountOfBones.size()))
            Log.d("MyLog", "Не совпадает количество точек в geometries и controllers. Size: " + model.getSize() + "  newSie: " + amountOfBones.size());
        ///////////////!!!!!!!!!!!Полный рефактор!! Подумай, почему блять
        ///////////////!!!!!!!!!!!Реализовать DrawElements, потому что так удобней!!
        model.setBonesAmount(boneMatrixBegin.size());
        model.indexes = new int[amountOfBones.size() * 4];
        model.weights = new float[amountOfBones.size() * 4];
        for (int i = 0; i < amountOfBones.size(); i++){
            int[][] array = jointsWeights.get(i);
            for (int k = 0; k < 4/*Потому что vec4*/; k++){
                if (k < array[0].length)
                    model.indexes[i * 4 + k] = array[0][k];
                else
                    model.indexes[i * 4 + k] = -1;
                if (k < array[1].length)
                    model.weights[i * 4 + k] = array[1][k];
                else
                    model.weights[i * 4 + k] = -1;
            }
        }
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
