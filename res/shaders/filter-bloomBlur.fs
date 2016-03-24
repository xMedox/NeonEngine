#version 330

in vec2 texCoord0;

uniform vec3 R_blurScale;
uniform sampler2D R0_filterTexture;
uniform sampler2D R1_filterTexture;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

void main(){
	vec4 color = vec4(0.0f);
	
	color += texture(R1_filterTexture, texCoord0 + (vec2(-3.0f) * R_blurScale.xy)) * (1.0f/64.0f);
	color += texture(R1_filterTexture, texCoord0 + (vec2(-2.0f) * R_blurScale.xy)) * (6.0f/64.0f);
	color += texture(R1_filterTexture, texCoord0 + (vec2(-1.0f) * R_blurScale.xy)) * (15.0f/64.0f);
	color += texture(R1_filterTexture, texCoord0 + (vec2(0.0f) * R_blurScale.xy))  * (20.0f/64.0f);
	color += texture(R1_filterTexture, texCoord0 + (vec2(1.0f) * R_blurScale.xy))  * (15.0f/64.0f);
	color += texture(R1_filterTexture, texCoord0 + (vec2(2.0f) * R_blurScale.xy))  * (6.0f/64.0f);
	color += texture(R1_filterTexture, texCoord0 + (vec2(3.0f) * R_blurScale.xy))  * (1.0f/64.0f);
	
	outputFS = texture(R0_filterTexture, texCoord0);
	outputBloom = color;
}