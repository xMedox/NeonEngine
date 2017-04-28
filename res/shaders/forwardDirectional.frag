#version 330

#include "forwardlighting.fragh"

#include "lighting.glh"

uniform vec3 C_eyePos;
uniform float roughness;
uniform float metallic;

uniform DirectionalLight R_directionalLight;

vec4 CalcLightingEffect(vec3 diffuse, vec3 normal, vec3 worldPos, float roughnessMap, float metallicMap){
	return CalcLight(R_directionalLight.base, -R_directionalLight.direction, diffuse, normal, worldPos, roughnessMap, metallicMap, roughness, metallic, C_eyePos);
}

#include "lightingMain.fragh"