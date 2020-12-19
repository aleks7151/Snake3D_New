package com.example.snake3d_new.openGL.game.dae;

import android.util.Log;

import com.example.snake3d_new.openGL.game.model.Bone;
import com.example.snake3d_new.openGL.game.model.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.snake3d_new.openGL.game.utils.Constants.STRIDE_COLOR;
import static com.example.snake3d_new.openGL.game.utils.Constants.STRIDE_INDEX;
import static com.example.snake3d_new.openGL.game.utils.Constants.STRIDE_MATRIX;
import static com.example.snake3d_new.openGL.game.utils.Constants.STRIDE_NORMAL;
import static com.example.snake3d_new.openGL.game.utils.Constants.STRIDE_ORDER;
import static com.example.snake3d_new.openGL.game.utils.Constants.STRIDE_POSITION;
import static com.example.snake3d_new.openGL.game.utils.Constants.STRIDE_WEIGHT;

public class ReadDataDae {
    private List<float[]> position = null;
    private List<float[]> normal = null;
    private List<float[]> color = null;
    private List<int[]> orderMesh = null;

    public List<float[]> weight = null;
    public List<int[]> index = null;

    private int pointsAmount;
    private int finalPointsAmount;
    public int bonesAmount = 0;
    private Map <String, Bone> mapBones = new HashMap<>();

    private Model model;

    public ReadDataDae(Model model) {
        this.model = model;
    }


    void getGeometries(List<String> listGeometries) {
        position = new ArrayList<>();
        normal = new ArrayList<>();
        color = new ArrayList<>();//Использоваться не будет, но это, возможно, пока
        orderMesh = new ArrayList<>();
        String stringPosition = listGeometries.get(0);
        String stringNormal = listGeometries.get(1);
        String stringColor = listGeometries.get(2);//Использоваться не будет, но это, возможно, пока
        String stringOrder = listGeometries.get(3);

        UtilsDae.toFloatArrayFromString(position, stringPosition, STRIDE_POSITION);
        UtilsDae.toFloatArrayFromString(normal, stringNormal, STRIDE_NORMAL);
        UtilsDae.toFloatArrayFromString(color, stringColor, STRIDE_COLOR);
        UtilsDae.toIntArrayFromString(orderMesh, stringOrder, STRIDE_ORDER);

        pointsAmount = position.size();
        finalPointsAmount = orderMesh.size();
    }

    void getControllers(List<String> listControllers, Map<String, float[]> beginBoneMatrix, Map<String, List<String>> childParentBone, String rootBoneName) {
        List<float[]> boneInvertMatrix = new ArrayList<>();
        List<Float> listWeight = new ArrayList<>();
        List<Integer> amountOfBone = new ArrayList<>();
        List<int[][]> jointWeight = new ArrayList<>();
        List<String> nameBones = new ArrayList<>();

        String stringNameBone = listControllers.get(0);
        String stringBoneMatrixBegin = listControllers.get(1);
        String stringWeights= listControllers.get(2);
        String stringAmountOfBones = listControllers.get(3);
        String stringJointsWeights = listControllers.get(4);

        UtilsDae.toStringFromString(nameBones, stringNameBone);
        UtilsDae.toFloatArrayFromString(boneInvertMatrix, stringBoneMatrixBegin, STRIDE_MATRIX);
        UtilsDae.toFloatFromString(listWeight, stringWeights);
        UtilsDae.toIntFromString(amountOfBone, stringAmountOfBones);
        UtilsDae.toIntArrayFromStringAndAmountBones(jointWeight, stringJointsWeights, amountOfBone);

        mapBones = new HashMap<>();
        for (int i = 0; i < nameBones.size(); i++)
            mapBones.put(nameBones.get(i), new Bone(nameBones.get(i), boneInvertMatrix.get(i), i));
        model.rootBone = mapBones.get(rootBoneName);
        for (Map.Entry<String, Bone> entry : mapBones.entrySet()) {
            Bone bone = entry.getValue();
            String nameBone = entry.getKey();
            bone.setBeginBoneMatrix(beginBoneMatrix.get(nameBone));
            if (childParentBone.get(nameBone) == null)
                bone.childs = null;
            else {
                bone.childs = new ArrayList<>();
                for (String nameChilds : childParentBone.get(nameBone))
                    bone.childs.add(mapBones.get(nameChilds));
            }
        }

        if (pointsAmount != amountOfBone.size())
            Log.d("MyLog", "Не совпадает количество точек в geometries и controllers. Size: " + pointsAmount + "  newSie: " + amountOfBone.size());
        else
            Log.d("sss", "Все совпадает, не парься");
        bonesAmount = nameBones.size();

        index = new ArrayList<>();
        weight = new ArrayList<>();
        for (int i = 0; i < pointsAmount; i++) {
            int[][] array = jointWeight.get(i);
            int[] indexArray = new int[4];
            float[] weightArray = new float[4];
            for (int k = 0; k < STRIDE_INDEX/*Потому что vec4*/; k++) {
                if (k < array[0].length)
                    indexArray[k] = array[0][k];
                else
                    indexArray[k] = -1;
                if (k < array[1].length)
                    weightArray[k] = listWeight.get(array[1][k]);
                else
                    weightArray[k] = -1;
            }
            index.add(indexArray);
            weight.add(weightArray);
        }
    }

    public void getAnimations(List<String> listAnimations, List<String> nameBones) {
        for (int i = 0; i < bonesAmount; i++) {
            List<Double> time = new ArrayList<>();
            List<float[]> boneAnimMatrix = new ArrayList<>();

            String stringTime = listAnimations.get(i * 3);
            String stringBoneMatrix = listAnimations.get(i * 3 + 1);
            String stringTypeInterpoletion = listAnimations.get(i * 3 + 2);

            UtilsDae.toDoubleFromString(time, stringTime);
            UtilsDae.toFloatArrayFromString(boneAnimMatrix, stringBoneMatrix, STRIDE_MATRIX);

            for (int k = 0; k < time.size(); k++)//Переводим в милисекунды
                time.set(k, time.get(k) * 1000);

            Bone bone = mapBones.get(nameBones.get(i));
            if (bone == null)
                Log.d("Еррор", "Проблема при чтении имен");
            bone.setTime(time);
            bone.setAnimMatrix(boneAnimMatrix);
        }
    }

    public void setFinalData(){
        model.position = new float[finalPointsAmount * STRIDE_POSITION];
        model.normal = new float[finalPointsAmount * STRIDE_NORMAL];
        model.color = new float[finalPointsAmount * STRIDE_COLOR];
        model.index = new int[finalPointsAmount * STRIDE_INDEX];
        model.weight = new float[finalPointsAmount * STRIDE_WEIGHT];
        for (int i = 0; i < finalPointsAmount; i++){
            float[] point = position.get(
                    orderMesh.get(i)[0]
            );
            float[] normalArray = normal.get(
                    orderMesh.get(i)[1]
            );
            float[] colorArray = color.get(
                    orderMesh.get(i)[2]
            );
            System.arraycopy(point, 0, model.position, i * STRIDE_POSITION, point.length);
            System.arraycopy(normalArray, 0, model.normal, i * STRIDE_NORMAL, normalArray.length);
            System.arraycopy(colorArray, 0, model.color, i * STRIDE_COLOR, colorArray.length);

            if (index != null && weight != null) {
                int[] indexSmall = index.get(
                        orderMesh.get(i)[0]
                );
                float[] weightSmall = weight.get(
                        orderMesh.get(i)[0]
                );
                System.arraycopy(indexSmall, 0, model.index, i * STRIDE_INDEX, indexSmall.length);
                System.arraycopy(weightSmall, 0, model.weight, i * STRIDE_WEIGHT, weightSmall.length);
            }
            else {
                System.arraycopy(new int[]{-1, -1, -1, -1}, 0, model.index, i * STRIDE_INDEX, STRIDE_INDEX);
                System.arraycopy(new float[]{-1, -1, -1, -1}, 0, model.weight, i * STRIDE_WEIGHT, STRIDE_WEIGHT);
            }
        }
    }

    public void getPoint() {
        position = new ArrayList<>();
        normal = new ArrayList<>();
        color = new ArrayList<>();
        orderMesh = new ArrayList<>();

        position.add(new float[]{0, 0, 0});
        normal.add(new float[]{0, 0, 1});
        color.add(new float[]{0, 0});
        orderMesh.add(new int[]{0, 0, 0});

        pointsAmount = 1;
        finalPointsAmount = 1;
        setFinalData();
    }
}
