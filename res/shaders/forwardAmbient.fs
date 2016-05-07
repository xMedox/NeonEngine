#version 330

in vec2 texCoord0;

uniform vec3 R_ambient;
uniform sampler2D diffuse;
uniform sampler2D glowMap;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

void main(){
	vec4 diffuseMap = texture(diffuse, texCoord0);
	
	if(diffuseMap.a >= 0.5f){
		vec4 glow = texture(glowMap, texCoord0);
		
		vec4 outvar = diffuseMap * clamp((vec4(R_ambient, 1) + vec4(glow.r, glow.r, glow.r, 1)), 0, 1);
		
		outputFS = outvar;
		
		if(dot(glow.r, 0.8*2) > 0.0){
			outputBloom = outvar;
		}else{
			outputBloom = vec4(0, 0, 0, 0);
		}
	}else{
		discard;
	}
}