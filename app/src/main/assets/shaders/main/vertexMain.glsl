#version 300 es
layout (location = 0) in vec4 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 texture;
layout (location = 3) in vec4 weight;
layout (location = 4) in ivec4 index;

uniform mat4 projectionViewMatrix;
uniform mat4 projectionViewMatrixShadow;
uniform mat4 modelMatrix;
uniform mat4 boneMatrix[50];

out vec4 positionVariable;
out vec3 normalVariable;
out vec4 shadowPosition;
out vec2 textureVariable;

void main(){
    textureVariable = texture;

    vec3 normalModel = mat3(modelMatrix) * normal;
    vec3 normalBone = vec3(0.0);

    vec4 modelPosition = modelMatrix * position;//Пеобразовать обратно без tmp
    vec4 bonePosition = vec4(0.0);
    bool check = false;

    for (int i = 0; i < 4; i++){
        if (index[i] > 0){
            check = true;
            bonePosition += weight[i] * boneMatrix[index[i]] * modelPosition;
            normalBone += (weight[i] * boneMatrix[index[i]] * vec4(normalModel, 1.0)).xyz;
        }
    }
    if (check){
        modelPosition = bonePosition;
        normalVariable = normalBone;
    }
    else {
        normalVariable = normalModel;
    }

    shadowPosition = projectionViewMatrixShadow * modelPosition;

    positionVariable = modelPosition;
    gl_Position = projectionViewMatrix * modelPosition;
//    gl_Position = projectionViewMatrixShadow * modelPosition;//Для отображении вида с камеры для теней

    gl_PointSize = 5.0;
}