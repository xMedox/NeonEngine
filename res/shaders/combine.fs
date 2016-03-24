#version 330

in vec2 texCoord0;

uniform sampler2D R0_displayTexture;
uniform sampler2D R_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	outputFS = texture(R0_displayTexture, texCoord0) + texture(R_filterTexture, texCoord0);
}