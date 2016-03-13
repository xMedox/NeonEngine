#version 330

in vec2 texCoord0;

uniform sampler2D R_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	vec4 normalColor = texture(R_filterTexture, texCoord0);
    float gray = 0.299f * normalColor.r + 0.587f * normalColor.g + 0.114f * normalColor.b;
    outputFS = vec4(gray, gray, gray, normalColor.a);
}