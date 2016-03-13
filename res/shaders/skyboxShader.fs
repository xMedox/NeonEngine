#version 330

in vec3 texCoord0;

uniform samplerCube cubeMap;

layout(location = 0) out vec4 outputFS;

void main(){
    outputFS = texture(cubeMap, texCoord0);
}