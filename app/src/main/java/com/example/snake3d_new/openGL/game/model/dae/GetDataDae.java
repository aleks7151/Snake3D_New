package com.example.snake3d_new.openGL.game.model.dae;

import android.content.res.AssetManager;
import android.util.Log;

import com.example.snake3d_new.openGL.game.model.Bone;
import com.example.snake3d_new.openGL.game.model.Model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class GetDataDae {
    public static Model getModel(AssetManager assets, String path){
        List<String> listGeometries = new ArrayList<>();
        List<String> listControllers = new ArrayList<>();
        List<String> listAnimations = new ArrayList<>();
        Node nodeGeometries = null;
        Node nodeControllers = null;
        Node nodeAnimations = null;
        String rootBoneName = "";
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(assets.open(path));

            nodeGeometries = document.getElementsByTagName("library_geometries").item(0);
            findData(nodeGeometries.getChildNodes(),
                    listGeometries, new String[]{"float_array", "p"});

            nodeControllers = document.getElementsByTagName("library_controllers").item(0);
            if (nodeControllers != null) {
                findData(nodeControllers.getChildNodes(),
                        listControllers, new String[]{"Name_array", "float_array", "vcount", "v"});

                beginBoneMatrix = new HashMap<>();
                childParentBone = new HashMap<>();
                Node root = document.getElementsByTagName("node").item(1);
                rootBoneName = parseNameForVisualScene(root.getAttributes().item(0).getTextContent());
                childParentBone.put(rootBoneName, null);
                findTreeBones(root);
            }

            nodeAnimations = document.getElementsByTagName("animation").item(0);
            if (nodeAnimations != null) {
                nameBones = new ArrayList<>();
                NodeList animationChilds = nodeAnimations.getChildNodes();
                for (int i = 0 ; i < animationChilds.getLength(); i++) {
                    if (!animationChilds.item(i).getNodeName().equals("animation"))
                        continue;
                    needAttributes = true;
                    findData(animationChilds.item(i).getChildNodes(),
                            listAnimations, new String[]{"float_array", "Name_array"});
                    if (needAttributes)
                        Log.d("MyLog", "Что-то странное, needAttributes после цикла true, а должен быть false");
                }
            }
        }
        catch (Exception e){
            Log.d("MyLog", "Ошибка при получении xml\n" + e);
        }
        Model model = new Model();
        ReadDataDae readDataDae = new ReadDataDae(model);
        readDataDae.getGeometries(listGeometries);
        if (nodeControllers != null)
            readDataDae.getControllers(listControllers, beginBoneMatrix, childParentBone, rootBoneName);
        if (nodeAnimations != null)
            readDataDae.getAnimations(listAnimations, nameBones);
        readDataDae.setFinalData();
        return model;
    }

    private static boolean needAttributes = false;
    private static List<String> nameBones = null;

    private static void findData(NodeList childs, List<String> list, String[] strings) {
        for (int i = 0; i < childs.getLength(); i++){
            boolean isHit = false;
            for (String str : strings)
                if (childs.item(i).getNodeName().equals(str)){
                    isHit = true;
                    break;
                }
            if (isHit) {
                list.add(childs.item(i).getTextContent());
                if (needAttributes) {
                    needAttributes = false;
                    nameBones.add(parseNameForAnimation(childs.item(i).getAttributes().item(0).getTextContent()));
                }
            }
            else if (childs.item(i).hasChildNodes())
                findData(childs.item(i).getChildNodes(), list, strings);
        }
    }

    private static Map<String, List<String>> childParentBone;
    private static Map<String, float[]> beginBoneMatrix;

    private static void findTreeBones(Node node) {
        String nameParrent = parseNameForVisualScene(node.getAttributes().item(0).getTextContent());
        List<String> stringList = null;
        for (int i = 0; i < node.getChildNodes().getLength(); i++){
            Node child = node.getChildNodes().item(i);
            if (child.getNodeName().equals("matrix")){
                if (beginBoneMatrix.get(nameParrent) != null)
                    Log.d("MyLog", "Странная хуйня, начальная матрица кости дважды попалась");
                List<Float> list = new ArrayList<>();
                UtilsDae.toFloatFromString(list, child.getTextContent());
                float[] array = new float[list.size()];
                if (list.size() != 16)
                    Log.d("MyLog", "Размер начальной матрицы не 16, хмхмхмхм....");
                for (int k = 0; k < list.size(); k++)
                    array[k] = list.get(k);
                beginBoneMatrix.put(nameParrent, array);
            }
            else if (child.getNodeName().equals("node")){
                String nameChild = parseNameForVisualScene(child.getAttributes().item(0).getTextContent());
                if (childParentBone.get(nameChild) != null)
                    Log.d("MyLog", "У кости " + nameChild + " два родителя. Что?");
                if (stringList == null)
                    stringList = new ArrayList<>();
                stringList.add(nameChild);
                findTreeBones(child);
            }
        }
        childParentBone.put(nameParrent, stringList);
    }

    private static String parseNameForVisualScene(String textContent) {
        return textContent.substring(textContent.indexOf("_") + 1);
    }

    private static String parseNameForAnimation(String textContent) {
        return textContent.substring(
                textContent.indexOf("Action") + "Action".length() + 1,
                textContent.indexOf("pose_matrix") - 1
        );
    }

    public static Model getPoint() {
        Model model = new Model();
        new ReadDataDae(model).getPoint();
        return model;
    }
}
