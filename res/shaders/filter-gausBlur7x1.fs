#version 330

in vec2 texCoord0;

uniform vec3 R_blurScale;
uniform sampler2D R_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	outputFS = texture(R_filterTexture, texCoord0 + (vec2(-3.0f) * R_blurScale.xy)) * (1.0f/64.0f) +
	texture(R_filterTexture, texCoord0 + (vec2(-2.0f) * R_blurScale.xy)) * (6.0f/64.0f) +
	texture(R_filterTexture, texCoord0 + (vec2(-1.0f) * R_blurScale.xy)) * (15.0f/64.0f) +
	texture(R_filterTexture, texCoord0 + (vec2(0.0f) * R_blurScale.xy))  * (20.0f/64.0f) +
	texture(R_filterTexture, texCoord0 + (vec2(1.0f) * R_blurScale.xy))  * (15.0f/64.0f) +
	texture(R_filterTexture, texCoord0 + (vec2(2.0f) * R_blurScale.xy))  * (6.0f/64.0f) +
	texture(R_filterTexture, texCoord0 + (vec2(3.0f) * R_blurScale.xy))  * (1.0f/64.0f);
}