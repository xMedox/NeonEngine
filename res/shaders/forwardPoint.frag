#version 330

#include "forwardlighting.fragh"

#include "forwardLighting.glh"

uniform vec3 C_eyePos;
uniform float roughness;
uniform float metallic;

uniform PointLight R_pointLight;

vec4 CalcLightingEffect(vec3 diffuse, vec3 normal, vec3 worldPos, float roughnessMap, float metallicMap){
	return CalcPointLight(R_pointLight, diffuse, normal, worldPos, roughnessMap, metallicMap, roughness, metallic, C_eyePos);
}

#include "forwardLightingMain.fragh"