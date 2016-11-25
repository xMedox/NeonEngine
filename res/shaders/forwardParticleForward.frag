#version 330

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

void main(){
	outputFS = vec4(0.0, 0.0, 0.0, 0.0);
	outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
}