#version 330

in vec2 texCoord0;

uniform vec3 R_ambient;
uniform sampler2D diffuse;
uniform sampler2D glowMap;

layout(location = 0) out vec4 outputFS;

void main(){	
	vec4 diffuseMap = texture(diffuse, texCoord0);
	
	if(diffuseMap.a >= 0.5f){		
		outputFS = diffuseMap * clamp((vec4(R_ambient, 1) + texture(glowMap, texCoord0)), 0, 1);
	}else{
		discard;
	}
}