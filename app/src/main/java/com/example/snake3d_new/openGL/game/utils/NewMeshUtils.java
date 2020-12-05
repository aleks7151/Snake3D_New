package com.example.snake3d_new.openGL.game.utils;

import android.content.res.AssetManager;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class NewMeshUtils {
    public static float[][] getModel(AssetManager assets, String path){
        List<String> listGeometries = new ArrayList<>();
        List<String> listControllers = new ArrayList<>();
        List<String> listAnimations = new ArrayList<>();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(assets.open(path));

            findData(document.getElementsByTagName("library_geometries").item(0).getChildNodes(),
                    listGeometries, new String[]{"float_array", "p"});

            findData(document.getElementsByTagName("library_controllers").item(0).getChildNodes(),
                    listControllers, new String[]{"float_array", "vcount", "v"});

            findData(document.getElementsByTagName("library_animations").item(0).getChildNodes(),
                    listAnimations, new String[]{"float_array", "Name_array"});
        }
        catch (Exception e){
            Log.d("MyLog", "Ooops\n" + e);
        }
        return (getMesh(listGeometries));
    }

    private static float[][] getMesh(List<String> listGeometries) {
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

        float[][] mesh = new float[3][order.size() * 3];
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
            System.arraycopy(point, 0, mesh[0], i * 3, point.length);
            System.arraycopy(normal, 0, mesh[1], i * 3, normal.length);
            System.arraycopy(color, 0, mesh[2], i * 3, color.length);
        }
        return mesh;
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

    private static void findData(NodeList childs, List<String> list, String[] strings) {
        for (int i = 0; i < childs.getLength(); i++){
            boolean isHit = false;
            for (String str : strings)
                if (childs.item(i).getNodeName().equals(str)){
                    isHit = true;
                    break;
                }
            if (isHit)
                list.add(childs.item(i).getTextContent());
            else if (childs.item(i).hasChildNodes())
                findData(childs.item(i).getChildNodes(), list, strings);
        }
    }
}
