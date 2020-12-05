#version 300 es
layout (location = 0) in vec4 position;

uniform mat4 projectionViewMatrixShadow;
uniform mat4 modelMatrix;

void main(){
    vec4 modelPosition = modelMatrix * position;
    gl_Position = projectionViewMatrixShadow * modelPosition;

    gl_PointSize = 5.0;
}