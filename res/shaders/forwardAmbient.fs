#version 330

in vec2 texCoord0;

uniform vec3 R_ambient;
uniform sampler2D diffuseMap;
uniform sampler2D emissiveMap;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

void main(){
	vec4 diffuse = texture(diffuseMap, texCoord0);
	
	if(diffuse.a >= 0.5f){
		vec4 emissive = texture(emissiveMap, texCoord0);
		
		vec4 outvar = diffuse * clamp((vec4(R_ambient, 1) + vec4(emissive.r, emissive.r, emissive.r, 1)), 0, 1);
		
		outputFS = outvar;
		
		if(dot(emissive.r, 0.8*2) > 1.0){
			outputBloom = outvar;
		}else{
			outputBloom = vec4(0, 0, 0, 0);
		}
	}else{
		discard;
	}
}