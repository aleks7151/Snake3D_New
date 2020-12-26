#version 300 es
layout (location = 0) in vec4 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 texture;
layout (location = 3) in vec4 weight;
layout (location = 4) in ivec4 index;

uniform mat4 projectionViewMatrix;
uniform mat4 projectionViewMatrixShadow;
uniform mat4 modelMatrix;
uniform mat4 boneModelMatrix[50];
uniform mat4 boneNormalMatrix[50];
uniform bool needBone;

out vec4 positionVariable;
out vec3 normalVariable;
out vec4 shadowPosition;
out vec2 textureVariable;

void main(){
    textureVariable = texture;
    bool check = false;
    mat4 animModelMatrix = mat4(0.0);
    mat4 animNormalMatrix = mat4(0.0);
    if (needBone){
        for (int i = 0; i < 4; i++){
            if (index[i] >= 0){
                check = true;
                animModelMatrix += weight[i] * boneModelMatrix[index[i]];
                animNormalMatrix += weight[i] * boneNormalMatrix[index[i]];
            }
        }
    }
    vec4 modelPosition;
    if (check){
        modelPosition = position * animModelMatrix;
        modelPosition = modelMatrix * modelPosition;
        vec3 tempNormal = normal * mat3(animNormalMatrix);
        normalVariable = mat3(modelMatrix) * tempNormal;
    }
    else {
        modelPosition = modelMatrix * position;
        normalVariable = mat3(modelMatrix) * normal;
    }

    shadowPosition = projectionViewMatrixShadow * modelPosition;

    positionVariable = modelPosition;
    gl_Position = projectionViewMatrix * modelPosition;
//    gl_Position = projectionViewMatrixShadow * modelPosition;//Для отображении вида с камеры для теней

    gl_PointSize = 5.0;
}