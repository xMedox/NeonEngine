#include "sampling.glh"

uniform sampler2D diffuse;
uniform sampler2D normalMap;
uniform sampler2D specMap;

uniform sampler2D R_shadowMap;
uniform float R_shadowVarianceMin;
uniform float R_shadowLightBleedingReduction;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

bool InRange(float val){
	return val >= 0.0f && val <= 1.0f;
}

float CalcShadowAmount(sampler2D shadowMap, vec4 initialShadowMapCoords){
	vec3 shadowMapCoords = (initialShadowMapCoords.xyz/initialShadowMapCoords.w);
	
	if(InRange(shadowMapCoords.z) && InRange(shadowMapCoords.x) && InRange(shadowMapCoords.y)){
		return SampleVarianceShadowMap(shadowMap, shadowMapCoords.xy, shadowMapCoords.z, R_shadowVarianceMin, R_shadowLightBleedingReduction);
	}else{
		return 1.0f;
	}
}

void main(){
    outputFS = texture(diffuse, texCoord0) * CalcLightingEffect(normalize(tbnMatrix * (255.0f/128.0f * texture(normalMap, texCoord0).xyz - 1)), worldPos0, texture(specMap, texCoord0).x) * CalcShadowAmount(R_shadowMap, shadowMapCoords0);
	outputBloom = vec4(0, 0, 0, 0);
}