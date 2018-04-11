#version 330

in vec2 texCoord0;

#include "deferredlighting.glh"

uniform vec3 CM_eyePos;

uniform PointLight R_pointLight;

vec4 CalcLightingEffect(vec3 diffuse, vec3 normal, vec3 worldPos, float roughnessMap, float metallicMap){
	return CalcPointLight(R_pointLight, diffuse, normal, worldPos, roughnessMap, metallicMap, CM_eyePos);
}

#include "deferredLightingMain.fragh"