#version 330

#include "forwardlighting.fragh"

#include "forwardLighting.glh"

uniform vec3 C_eyePos;
uniform float roughness;
uniform float metallic;

uniform SpotLight R_spotLight;

vec4 CalcLightingEffect(vec3 diffuse, vec3 normal, vec3 worldPos, float roughnessMap, float metallicMap){
	float spotFactor = dot(normalize(worldPos - R_spotLight.pointLight.position), R_spotLight.direction);
	
	vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
	
	if(spotFactor > R_spotLight.cutoff){
		color = CalcPointLight(R_spotLight.pointLight, diffuse, normal, worldPos, roughnessMap, metallicMap, roughness, metallic, C_eyePos) * (1.0 - (1.0 - spotFactor)/(1.0 - R_spotLight.cutoff));
	}
	
	return color;
}

#include "forwardLightingMain.fragh"