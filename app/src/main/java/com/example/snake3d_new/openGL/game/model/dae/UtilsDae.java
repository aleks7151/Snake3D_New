package com.example.snake3d_new.openGL.game.model.dae;

import android.util.Log;

import java.util.List;
import java.util.Scanner;

public class UtilsDae {
    static void toStringFromString(List<String> list, String str) {
        try (Scanner scanner = new Scanner(str)){
            while (scanner.hasNext()) {
                list.add(scanner.next());
            }
        }
        catch (Exception e){
            Log.d("MyLog", "Fucck\n" + e);
        }}
        static void toIntFromString(List<Integer> list, String str) {
        try (Scanner scanner = new Scanner(str)){
            while (scanner.hasNext()) {
                list.add(Integer.parseInt(scanner.next()));
            }
        }
        catch (Exception e){
            Log.d("MyLog", "Fucck\n" + e);
        }
    }

    static void toFloatFromString(List<Float> list, String str) {
        try (Scanner scanner = new Scanner(str)){
            while (scanner.hasNext()) {
                list.add(Float.parseFloat(scanner.next()));
            }
        }
        catch (Exception e){
            Log.d("MyLog", "Fucck\n" + e);
        }
    }

    static void toIntArrayFromString(List<int[]> list, String str, int stride) {
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

    static void toFloatArrayFromString(List<float[]> list, String str, int stride) {
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
            Log.d("MyLog"   , "Fucck\n" + e);
        }
    }

    static void toIntArrayFromStringAndAmountBones(List<int[][]> list, String str, List<Integer> amountOfBones) {
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
