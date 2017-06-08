#version 330

in vec2 texCoord0;

uniform sampler2D R_filterTexture;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

void main(){
	vec3 color = texture(R_filterTexture, texCoord0).rgb;
	
	outputFS = vec4(color, 1.0);
	
	float brightness = dot(color, vec3(0.2126, 0.7152, 0.0722));
	if(brightness > 1.0){
		outputBloom = vec4(color, 1.0);
	}else{
		outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
	}
}