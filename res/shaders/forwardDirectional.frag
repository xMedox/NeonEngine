#version 330

#include "forwardlighting.fragh"

#include "lighting.glh"

uniform vec3 C_eyePos;
uniform float specularIntensity;
uniform float specularPower;

uniform DirectionalLight R_directionalLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos, float specular){
	return CalcLight(R_directionalLight.base, -R_directionalLight.direction, normal, worldPos, specular, specularIntensity, specularPower, C_eyePos);
}

#include "lightingMain.fragh"