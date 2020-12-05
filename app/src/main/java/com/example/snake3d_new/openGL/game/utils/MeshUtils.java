package com.example.snake3d_new.openGL.game.utils;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Класс для получения модели
public class MeshUtils {
    public static float[][] getModel(AssetManager assets, String path){
        List<Float> mesh = new ArrayList<>();
        List<Float> normal = new ArrayList<>();
        List<Float> texture = new ArrayList<>();
        try {
            Scanner scanner = null;
            try {
                readInTry(scanner, assets.open(path), mesh, normal, texture);
            } finally {
                if (scanner != null)
                    scanner.close();
            }
        } catch (IOException e){
            Log.d("MyLog", e.toString());
        }
        float[][] model = new float[3][mesh.size()];
        for (int i = 0; i < mesh.size(); i++)
            model[0][i] = mesh.get(i);
        for (int i = 0; i < normal.size(); i++)
            model[1][i] = normal.get(i);
        for (int i = 0; i < texture.size(); i++)
            model[2][i] = texture.get(i);
        return model;
    }

    private static void readInTry(Scanner scanner, InputStream inputStream, List<Float> mesh, List<Float> normal, List<Float> texture) {
        List<Float> points = new ArrayList<>();
        List<Float> firstNormals = new ArrayList<>();
        List<Float> firstTexture = new ArrayList<>();
        scanner = new Scanner(inputStream);
        String str = "null";
        while (!str.equals("v"))
            str = scanner.next();
        while (str.equals("v")){
            points.add(Float.parseFloat(scanner.next()));
            float z = Float.parseFloat(scanner.next());
            float y = Float.parseFloat(scanner.next());
            points.add(y);
            points.add(z);
            scanner.nextLine();
            str = scanner.next();
        }
        while (str.equals("vt")){
            float x = Float.parseFloat(scanner.next());
            float y = Float.parseFloat(scanner.next());
            firstTexture.add(x);
            firstTexture.add(y);
            scanner.nextLine();
            str = scanner.next();
        }
        while (str.equals("vn")){
            firstNormals.add(Float.parseFloat(scanner.next()));
            float z = Float.parseFloat(scanner.next());
            float y = Float.parseFloat(scanner.next());
            firstNormals.add(y);
            firstNormals.add(z);
            scanner.nextLine();
            str = scanner.next();
        }
        while (!str.equals("f")) {
            scanner.nextLine();
            str = scanner.next();
        }
        while (str.equals("f")){
            for (int i = 0; i < 3; i++) {
                String[] strings = scanner.next().split("/");
                int indexMesh = (Integer.parseInt(strings[0]) - 1) * 3;
                int indexTexture = (Integer.parseInt(strings[1]) - 1) * 2;
                int indexNormal = (Integer.parseInt(strings[2]) - 1) * 3;
                mesh.add(points.get(indexMesh));
                mesh.add(points.get(indexMesh + 1));
                mesh.add(points.get(indexMesh + 2));
                normal.add(firstNormals.get(indexNormal));
                normal.add(firstNormals.get(indexNormal + 1));
                normal.add(firstNormals.get(indexNormal + 2));
                texture.add(firstTexture.get(indexTexture));
                texture.add(firstTexture.get(indexTexture + 1));
            }
            scanner.nextLine();
            if (!scanner.hasNext())
                break;
            str = scanner.next();
        }
    }

    public static float[][] getPoint() {
        float[][] point = new float[][]{
                {0, 0, 0},
                {0, 0, 1},
                {0, 0, 0},
        };
        return point;
    }
}
