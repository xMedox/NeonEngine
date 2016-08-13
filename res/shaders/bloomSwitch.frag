#version 330

in vec2 texCoord0;

uniform sampler2D R1_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	outputFS = texture(R1_filterTexture, texCoord0);
}