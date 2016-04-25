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

#include "sampling.glh"

uniform sampler2D diffuse;
uniform sampler2D normalMap;
uniform sampler2D specMap;

uniform samplerCube R_shadowMap;
uniform float R_shadowVarianceMin;
uniform float R_shadowLightBleedingReduction;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

float CalcShadowAmount(samplerCube shadowMap, vec4 initialShadowMapCoords, vec3 lightDirection){
	//vec3 shadowMapCoords = (initialShadowMapCoords.xyz/initialShadowMapCoords.w);
	
	return SampleVarianceShadowMapCube(shadowMap, vec3(lightDirection.x, lightDirection.y*-1, lightDirection.z), length(lightDirection)/2, R_shadowVarianceMin, R_shadowLightBleedingReduction+initialShadowMapCoords.w*0.00001f);
}

void main(){
    outputFS = texture(diffuse, texCoord0) * CalcLightingEffect(normalize(tbnMatrix * (255.0f/128.0f * texture(normalMap, texCoord0).xyz - 1)), worldPos0, texture(specMap, texCoord0).x) * CalcShadowAmount(R_shadowMap, shadowMapCoords0, worldPos0 - R_pointLight.position);
	outputBloom = vec4(0, 0, 0, 0);
}