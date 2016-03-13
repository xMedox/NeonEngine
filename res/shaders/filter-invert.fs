#version 330

in vec2 texCoord0;

uniform sampler2D R_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	vec4 color = texture(R_filterTexture, texCoord0);
    outputFS = vec4(1.0f - color.r, 1.0f - color.g, 1.0f - color.b, color.a);
}