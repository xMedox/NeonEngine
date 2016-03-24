#version 330

in vec2 texCoord0;

uniform sampler2D R0_filterTexture;
uniform sampler2D R1_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	outputFS = texture(R0_filterTexture, texCoord0) + texture(R1_filterTexture, texCoord0);
}