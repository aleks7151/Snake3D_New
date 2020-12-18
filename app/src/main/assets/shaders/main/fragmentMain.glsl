#version 300 es
precision mediump float;
in vec4 positionVariable;
in vec3 normalVariable;
in vec4 shadowPosition;
in vec2 textureVariable;

uniform vec4 color;
uniform sampler2D shadowMap;

out vec4 gl_FragColor;

float getShadow(){
    vec2 size = 1.0 / vec2(textureSize(shadowMap, 0));
    vec3 shadowNew = shadowPosition.xyz /    shadowPosition.w;
    shadowNew = (shadowNew + 1.0) / 2.0;

    float shadow = 0.0;
    float distance = texture(shadowMap, shadowNew.xy).r;
    if (shadowNew.z - distance > 0.001)
        shadow = 0.65;
    return shadow;
}

void main(){
    vec3 lightPos = vec3(0.0, 0.0, 30.0);

    vec3 lightVector = normalize(lightPos - positionVariable.xyz);
    float diffuse = max(dot(normalize(normalVariable), lightVector), 0.0);

    float shadow = getShadow();

    gl_FragColor = diffuse * color * (1.0 - shadow);
}