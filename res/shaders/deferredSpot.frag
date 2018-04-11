#version 330

in vec2 texCoord0;

#include "deferredlighting.glh"

uniform vec3 CM_eyePos;

uniform SpotLight R_spotLight;

vec4 CalcLightingEffect(vec3 diffuse, vec3 normal, vec3 worldPos, float roughnessMap, float metallicMap){
	float spotFactor = dot(normalize(worldPos - R_spotLight.pointLight.position), R_spotLight.direction);
	
	vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
	
	if(spotFactor > R_spotLight.cutoff){
		color = CalcPointLight(R_spotLight.pointLight, diffuse, normal, worldPos, roughnessMap, metallicMap, CM_eyePos) * (1.0 - (1.0 - spotFactor)/(1.0 - R_spotLight.cutoff));
	}
	
	return color;
}

#include "deferredLightingMain.fragh"