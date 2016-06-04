#include "sampling.glh"

uniform sampler2D diffuseMap;
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
	//vec4 diffuse = texture(diffuseMap, texCoord0);
	//vec4 lighting = CalcLightingEffect(normalize(tbnMatrix * (255.0f/128.0f * texture(normalMap, texCoord0).xyz - 1)), worldPos0, texture(specMap, texCoord0).x) * CalcShadowAmount(R_shadowMap, shadowMapCoords0);
	
    outputFS = texture(diffuseMap, texCoord0) * CalcLightingEffect(normalize(tbnMatrix * (255.0f/128.0f * texture(normalMap, texCoord0).xyz - 1)), worldPos0, texture(specMap, texCoord0).x) * CalcShadowAmount(R_shadowMap, shadowMapCoords0);
	
	//float brightness = dot(lighting.rgb, vec3(0.25, 0.25, 0.25));
	
	//if(brightness > 1.0){		
		//outputBloom = (diffuse * lighting)*vec4(0.5, 0.5, 0.5, 1);
	//}else{		
		outputBloom = vec4(0, 0, 0, 0);
	//}
}