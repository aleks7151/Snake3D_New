#version 300 es
layout (location = 0) in vec4 position;
layout (location = 1) in vec3 normal;
layout (location = 2) in vec2 texture;

uniform mat4 projectionViewMatrix;
uniform mat4 projectionViewMatrixShadow;
uniform mat4 modelMatrix;

out vec4 positionVariable;
out vec3 normalVariable;
out vec4 shadowPosition;
out vec2 textureVariable;

void main(){
    normalVariable = mat3(modelMatrix) * normal;
    textureVariable = texture;

    vec4 modelPosition = modelMatrix * position;

    shadowPosition = projectionViewMatrixShadow * modelPosition;

    positionVariable = modelPosition;
    gl_Position = projectionViewMatrix * modelPosition;
//    gl_Position = projectionViewMatrixShadow * modelPosition;

    gl_PointSize = 5.0;
}