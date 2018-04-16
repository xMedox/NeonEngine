#version 330

in vec2 texCoord0;

#include "deferredlighting.glh"

uniform vec3 C0_eyePos;

uniform mat4 TM_projMatrixInv;
uniform mat4 TM_viewMatrixInv;

const int NR_LIGHTS = 128;
uniform PointLight R_pointLight[NR_LIGHTS];

#include "sampling.glh"

uniform mat4 R0_lightMatrix;

uniform sampler2D R0_filterTexture;
uniform sampler2D R1_filterTexture;
uniform sampler2D R2_filterTexture;
uniform sampler2D R3_filterTexture;

uniform sampler2D R_shadowMap;
uniform float R_shadowVarianceMin;
uniform float R_shadowLightBleedingReduction;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

bool InRange(float val){
	return val >= 0.0 && val <= 1.0;
}

float CalcShadowAmount(sampler2D shadowMap, vec4 initialShadowMapCoords){
	vec3 shadowMapCoords = (initialShadowMapCoords.xyz/initialShadowMapCoords.w);
	
	if(InRange(shadowMapCoords.z) && InRange(shadowMapCoords.x) && InRange(shadowMapCoords.y)){
		return SampleVarianceShadowMap(shadowMap, shadowMapCoords.xy, shadowMapCoords.z, R_shadowVarianceMin, R_shadowLightBleedingReduction);
	}else{
		return 1.0;
	}
}

vec3 worldPosFromDepth(){
	float depth = texture(R3_filterTexture, texCoord0).x;
	
	float z = depth * 2.0 - 1.0;
	
    vec4 clipSpacePosition = vec4(texCoord0 * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = TM_projMatrixInv * clipSpacePosition;
	
    viewSpacePosition /= viewSpacePosition.w;
	
    vec4 worldSpacePosition = TM_viewMatrixInv * viewSpacePosition;
	
    return worldSpacePosition.xyz;
}

void main(){
	//vec4 diffuse = texture(diffuseMap, texCoord0);
	//vec4 lighting = CalcLightingEffect(normalize(tbnMatrix * (255.0/128.0 * texture(normalMap, texCoord0).xyz - 1)), worldPos0, texture(specMap, texCoord0).x) * CalcShadowAmount(R_shadowMap, shadowMapCoords0);
	
	vec4 normal = texture(R1_filterTexture, texCoord0);
	
	vec4 color = vec4(0, 0, 0, 0);
	
	if(normal.x != 0.0 || normal.y != 0.0 || normal.z != 0.0){
		vec3 diffuse = texture(R0_filterTexture, texCoord0).xyz;
		
		vec3 pos = worldPosFromDepth();
		
		float rough = texture(R2_filterTexture, texCoord0).x;
		float metal = texture(R2_filterTexture, texCoord0).y;
		
		float shadow = CalcShadowAmount(R_shadowMap, (R0_lightMatrix * vec4(pos.xyz, 1.0)));
		
		for(int i = 0; i < NR_LIGHTS; ++i){
			color += CalcPointLight(R_pointLight[i], diffuse, normal.xyz, pos.xyz, rough, metal, C0_eyePos) * vec4(shadow, shadow, shadow, shadow);
		}
	}else{
		discard;
	}
	
	outputFS = color;
	
	//float brightness = dot(lighting.rgb, vec3(0.25, 0.25, 0.25));
	
	//if(brightness > 1.0){
		//outputBloom = (diffuse * lighting)*vec4(0.5, 0.5, 0.5, 1.0);
	//}else{		
		outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
	//}
}