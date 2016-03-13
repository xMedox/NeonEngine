#version 330

in vec2 texCoord0;

uniform sampler2D diffuse;

layout(location = 0) out vec4 outputFS;

void main(){
	outputFS = texture(diffuse, texCoord0);
}