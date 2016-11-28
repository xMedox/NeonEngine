#version 330

in vec3 texCoord0;

uniform samplerCube cubeMap;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

void main(){
	outputFS = texture(cubeMap, texCoord0);
	outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
}