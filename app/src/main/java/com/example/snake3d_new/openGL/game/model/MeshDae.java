package com.example.snake3d_new.openGL.game.model;

import android.content.res.AssetManager;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MeshDae {
    public static Model getModel(AssetManager assets, String path){
        List<String> listGeometries = new ArrayList<>();
        List<String> listControllers = new ArrayList<>();
        List<String> listAnimations = new ArrayList<>();
        Node nodeGeometries = null;
        Node nodeControllers = null;
        Node nodeAnimations = null;
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(assets.open(path));

            nodeGeometries = document.getElementsByTagName("library_geometries").item(0);
            findData(nodeGeometries.getChildNodes(),
                    listGeometries, new String[]{"float_array", "p"});

            nodeControllers = document.getElementsByTagName("library_controllers").item(0);
            if (nodeControllers != null) {
                findData(nodeControllers.getChildNodes(),
                        listControllers, new String[]{"float_array", "vcount", "v"});
            }

            nodeAnimations = document.getElementsByTagName("library_animations").item(0);
            if (nodeAnimations != null) {
                findData(nodeAnimations.getChildNodes(),
                        listAnimations, new String[]{"float_array", "Name_array"});
            }
        }
        catch (Exception e){
            Log.d("MyLog", "Ooops\n" + e);
        }
        Model model = new Model();
        Dae.getGeometries(model, listGeometries);
        if (nodeControllers != null)
            Dae.getControllers(model, listControllers);
        if (nodeAnimations != null)
            Dae.getAnimations(model, listAnimations);
        return model;
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
