#version 330

in vec2 texCoord0;

uniform sampler2D R_filterTexture;
uniform vec3 R_inverseFilterTextureSize;
uniform float R_fxaaSpanMax;
uniform float R_fxaaReduceMin;
uniform float R_fxaaReduceMul;

layout(location = 0) out vec4 outputFS;

void main(){
	vec2 texCoordOffset = R_inverseFilterTextureSize.xy;
	
	vec3 luma = vec3(0.299f, 0.587f, 0.114f);	
	float lumaTL = dot(luma, texture(R_filterTexture, texCoord0.xy + (vec2(-1.0f, -1.0f) * texCoordOffset)).xyz);
	float lumaTR = dot(luma, texture(R_filterTexture, texCoord0.xy + (vec2(1.0f, -1.0f) * texCoordOffset)).xyz);
	float lumaBL = dot(luma, texture(R_filterTexture, texCoord0.xy + (vec2(-1.0f, 1.0f) * texCoordOffset)).xyz);
	float lumaBR = dot(luma, texture(R_filterTexture, texCoord0.xy + (vec2(1.0f, 1.0f) * texCoordOffset)).xyz);
	float lumaM  = dot(luma, texture(R_filterTexture, texCoord0.xy).xyz);

	vec2 dir;
	dir.x = -((lumaTL + lumaTR) - (lumaBL + lumaBR));
	dir.y = ((lumaTL + lumaBL) - (lumaTR + lumaBR));
	
	float inverseDirAdjustment = 1.0f/(min(abs(dir.x), abs(dir.y)) + max((lumaTL + lumaTR + lumaBL + lumaBR) * (R_fxaaReduceMul * 0.25f), R_fxaaReduceMin));
	
	dir = min(vec2(R_fxaaSpanMax, R_fxaaSpanMax), 
		max(vec2(-R_fxaaSpanMax, -R_fxaaSpanMax), dir * inverseDirAdjustment));
	
	dir.x = dir.x * step(1.0f, abs(dir.x));
	dir.y = dir.y * step(1.0f, abs(dir.y));
	
	dir = dir * texCoordOffset;

	vec3 result1 = (1.0f/2.0f) * (
		texture(R_filterTexture, texCoord0.xy + (dir * vec2(1.0f/3.0f - 0.5f))).xyz +
		texture(R_filterTexture, texCoord0.xy + (dir * vec2(2.0f/3.0f - 0.5f))).xyz);

	vec3 result2 = result1 * (1.0f/2.0f) + (1.0f/4.0f) * (
		texture(R_filterTexture, texCoord0.xy + (dir * vec2(0.0f/3.0f - 0.5f))).xyz +
		texture(R_filterTexture, texCoord0.xy + (dir * vec2(3.0f/3.0f - 0.5f))).xyz);

	float lumaResult2 = dot(luma, result2);

	if(lumaResult2 < min(lumaM, min(min(lumaTL, lumaTR), min(lumaBL, lumaBR))) || lumaResult2 > max(lumaM, max(max(lumaTL, lumaTR), max(lumaBL, lumaBR)))){
		outputFS = vec4(result1, 1.0f);
	}else{
		outputFS = vec4(result2, 1.0f);
	}
}