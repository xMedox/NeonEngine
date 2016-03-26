#version 330

#include "forwardlighting.fsh"

#include "lighting.glh"

uniform vec3 C_eyePos;
uniform float specularIntensity;
uniform float specularPower;

uniform PointLight R_pointLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos, float specular){
	return CalcPointLight(R_pointLight, normal, worldPos, specular,
	                      specularIntensity, specularPower, C_eyePos);
}

#include "lightingMain.fsh"