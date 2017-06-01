#version 330

in vec2 texCoord0;

uniform sampler2D R_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	vec3 color = texture(R_filterTexture, texCoord0).rgb;
	
	//color = color / (color + vec3(1.0));
	color = vec3(1.0) - exp(-color * 1.0);
	
	color = pow(color, vec3(1.0/2.2));
	
	outputFS = vec4(color, 1.0);
}