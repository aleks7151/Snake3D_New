package com.example.snake3d_new.openGL.game.model;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Класс для получения модели
public class MeshObj {
    public static Model getModel(AssetManager assets, String path){
        List<Float> mesh = new ArrayList<>();
        List<Float> normal = new ArrayList<>();
        List<Float> texture = new ArrayList<>();
        try (Scanner scanner = new Scanner(assets.open(path))){
            readInTry(scanner, mesh, normal, texture);
        } catch (IOException e){
            Log.d("MyLog", e.toString());
        }
        Model model = new Model();
        model.setSize(mesh.size());
        model.positions = new float[model.getSize() * 3];
        model.normals = new float[model.getSize() * 3];
        model.colors = new float[model.getSize() * 3];
        for (int i = 0; i < mesh.size(); i++)
            model.positions[i] = mesh.get(i);
        for (int i = 0; i < normal.size(); i++)
            model.normals[i] = normal.get(i);
        for (int i = 0; i < texture.size(); i++)
            model.colors[i] = texture.get(i);
        return model;
    }

    private static void readInTry(Scanner scanner, List<Float> mesh, List<Float> normal, List<Float> texture) {
        List<Float> points = new ArrayList<>();
        List<Float> firstNormals = new ArrayList<>();
        List<Float> firstTexture = new ArrayList<>();
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

    public static Model getPoint() {
        float[][] point = new float[][]{
                {0, 0, 0},
                {0, 0, 1},
                {0, 0, 0},
        };
        Model model = new Model();
        model.setSize(3);
        model.positions = point[0];
        model.normals = point[1];
        model.colors = point[2];
        return model;
    }
}
