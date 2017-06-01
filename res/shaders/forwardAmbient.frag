#version 330

in vec2 texCoord0;
in vec3 worldPos0;
in mat3 tbnMatrix;

uniform sampler2D diffuseMap;
uniform sampler2D normalMap;
uniform sampler2D roughnessMap;
uniform sampler2D metallicMap;
uniform sampler2D emissiveMap;

uniform samplerCube R_irradianceMap;
uniform samplerCube R_prefilterMap;
uniform sampler2D R_brdfLUT;

uniform vec3 C_eyePos;
uniform float roughness;
uniform float metallic;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

vec3 fresnelSchlickRoughness(float cosTheta, vec3 F0, float roughness){
    return F0 + (max(vec3(1.0 - roughness), F0) - F0) * pow(1.0 - cosTheta, 5.0);
}   

void main(){
	vec4 diffuse = texture(diffuseMap, texCoord0);
	
	if(diffuse.a >= 0.5){
		vec3 color = pow(diffuse.rgb, vec3(2.2));
		
		float rough = texture(roughnessMap, texCoord0).x + roughness;
		float metal = texture(metallicMap, texCoord0).x + metallic;
		
		vec3 normal = normalize(tbnMatrix * (255.0/128.0 * texture(normalMap, texCoord0).xyz - 1.0));
		vec3 V = normalize(C_eyePos - worldPos0);
		vec3 R = reflect(-V, normal);
		
		vec3 F0 = vec3(0.04);
		//F0 = mix(F0, diffuse.rgb, metal);
		F0 = mix(F0, color, metal);
		
		vec3 F = fresnelSchlickRoughness(max(dot(normal, V), 0.0), F0, rough);
		
		vec3 kS = F;
		vec3 kD = 1.0 - kS;
		kD *= 1.0 - metal;
		
		vec3 irradiance = texture(R_irradianceMap, normal).rgb;
		//vec3 diffuseUsed = irradiance * diffuse.rgb;
		vec3 diffuseUsed = irradiance * color;
		
		
		
		const float MAX_REFLECTION_LOD = 4.0;
		vec3 prefilteredColor = textureLod(R_prefilterMap, R, rough * MAX_REFLECTION_LOD).rgb;
		vec2 brdf = texture(R_brdfLUT, vec2(max(dot(normal, V), 0.0), rough)).rg;
		vec3 specular = prefilteredColor * (F * brdf.x + brdf.y);
		
		
		
		vec3 ambient = kD * diffuseUsed + specular;
		
		
		vec4 emissive = texture(emissiveMap, texCoord0);
		
		outputFS = vec4(ambient, 1.0) + diffuse * vec4(emissive.r, emissive.r, emissive.r, 1.0);
		
		//if(dot(emissive.r, 0.8*2.0) > 1.0){
		if(emissive.r > 0){
			outputBloom = diffuse * vec4(emissive.r, emissive.r, emissive.r, 1.0);
		}else{
			outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
		}
	}else{
		discard;
	}
}