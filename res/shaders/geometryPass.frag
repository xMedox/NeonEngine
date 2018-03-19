#version 330

in vec2 texCoord0;
in mat3 tbnMatrix;

uniform sampler2D diffuseMap;
uniform sampler2D emissiveMap;
uniform sampler2D normalMap;
uniform sampler2D roughnessMap;
uniform sampler2D metallicMap;

uniform float roughness;
uniform float metallic;

layout (location = 0) out vec4 out0;
layout (location = 1) out vec4 out1;
layout (location = 2) out vec4 out2;

void main(){
	vec4 diffuse = texture(diffuseMap, texCoord0);
	
	if(diffuse.a >= 0.5){
		vec4 emissive = texture(emissiveMap, texCoord0);
		
		out0 = vec4(diffuse.rgb, emissive.r);
		
		//if(dot(emissive.r, 0.8*2.0) > 1.0){
		//if(emissive.r > 0){
		//	out1 = diffuse * vec4(emissive.r, emissive.r, emissive.r, 1.0);
		//}else{
		//	out1 = vec4(0.0, 0.0, 0.0, 0.0);
		//}
		
		out1 = vec4(normalize(tbnMatrix * (255.0/128.0 * texture(normalMap, texCoord0).xyz - 1.0)), 1.0);
		
		float rough = texture(roughnessMap, texCoord0).x + roughness;
		float metal = texture(metallicMap, texCoord0).x + metallic;
		
		out2 = vec4(rough, metal, 0.0, 1.0);
	}else{
		discard;
	}
}