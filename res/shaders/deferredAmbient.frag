#version 330

in vec2 texCoord0;

uniform sampler2D R0_filterTexture;
uniform sampler2D R1_filterTexture;
uniform sampler2D R2_filterTexture;
uniform sampler2D R3_filterTexture;

uniform samplerCube R_irradianceMap;
uniform samplerCube R_prefilterMap;
uniform sampler2D R_brdfLUT;

uniform vec3 CM_eyePos;

uniform mat4 TM_projMatrixInv;
uniform mat4 TM_viewMatrixInv;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

vec3 fresnelSchlickRoughness(float cosTheta, vec3 F0, float roughness){
    return F0 + (max(vec3(1.0 - roughness), F0) - F0) * pow(1.0 - cosTheta, 5.0);
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
	vec4 diffuse = texture(R0_filterTexture, texCoord0);
	
	vec3 normal = texture(R1_filterTexture, texCoord0).xyz;
	
	vec2 roughMetal = texture(R2_filterTexture, texCoord0).xy;
	
	if(normal.x != 0.0 || normal.y != 0.0 || normal.z != 0.0){
		vec3 color = pow(diffuse.rgb, vec3(2.2));
		
		vec3 V = normalize(CM_eyePos - worldPosFromDepth());
		vec3 R = reflect(-V, normal);
		
		vec3 F0 = vec3(0.04);
		F0 = mix(F0, color, roughMetal.y);
		
		vec3 F = fresnelSchlickRoughness(max(dot(normal, V), 0.0), F0, roughMetal.x);
		
		vec3 kS = F;
		vec3 kD = 1.0 - kS;
		kD *= 1.0 - roughMetal.y;
		
		vec3 irradiance = texture(R_irradianceMap, normal).rgb;
		vec3 diffuseUsed = irradiance * color;
		
		
		
		const float MAX_REFLECTION_LOD = 4.0;
		vec3 prefilteredColor = textureLod(R_prefilterMap, R, roughMetal.x * MAX_REFLECTION_LOD).rgb;
		vec2 brdf = texture(R_brdfLUT, vec2(max(dot(normal, V), 0.0), roughMetal.x)).rg;
		vec3 specular = prefilteredColor * (F * brdf.x + brdf.y);
		
		
		
		vec3 ambient = kD * diffuseUsed + specular;
		
		
		outputFS = vec4(ambient, 1.0) + vec4(color, 1) * vec4(diffuse.a, diffuse.a, diffuse.a, 1.0);
		
		if(diffuse.a > 0){
			outputBloom = vec4(color, 1) * vec4(diffuse.a, diffuse.a, diffuse.a, 1.0);
		}else{
			outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
		}
	}else{
		if(roughMetal.x == 1 && roughMetal.y == 1){
			outputFS = vec4(pow(diffuse.rgb, vec3(2.2)), 1.0);
			outputBloom = vec4(0.0, 0.0, 0.0, 1.0);
		}else{
			discard;
		}
	}
}