#version 330

in vec2 texCoord0;

uniform sampler2D R4_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	outputFS = texture(R4_filterTexture, texCoord0);
}