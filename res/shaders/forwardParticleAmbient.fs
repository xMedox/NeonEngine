#version 330

in vec2 texCoord0;
flat in int tid0;
in vec3 color0;

uniform vec3 R_ambient;

uniform sampler2D T0_texture;
uniform sampler2D T1_texture;
uniform sampler2D T2_texture;
uniform sampler2D T3_texture;
uniform sampler2D T4_texture;
uniform sampler2D T5_texture;
uniform sampler2D T6_texture;
uniform sampler2D T7_texture;
uniform sampler2D T8_texture;
uniform sampler2D T9_texture;
uniform sampler2D T10_texture;
uniform sampler2D T11_texture;
uniform sampler2D T12_texture;
uniform sampler2D T13_texture;
uniform sampler2D T14_texture;
uniform sampler2D T15_texture;
uniform sampler2D T16_texture;
uniform sampler2D T17_texture;
uniform sampler2D T18_texture;
uniform sampler2D T19_texture;
uniform sampler2D T20_texture;
uniform sampler2D T21_texture;
uniform sampler2D T22_texture;
uniform sampler2D T23_texture;
uniform sampler2D T24_texture;
uniform sampler2D T25_texture;
uniform sampler2D T26_texture;
uniform sampler2D T27_texture;
uniform sampler2D T28_texture;
uniform sampler2D T29_texture;
uniform sampler2D T30_texture;
uniform sampler2D T31_texture;

layout(location = 0) out vec4 outputFS;

void main(){
	vec4 diffuseMap;
	
	if(tid0 == 0){
		diffuseMap = texture(T0_texture, texCoord0);
	}else if(tid0 == 1){
		diffuseMap = texture(T1_texture, texCoord0);
	}else if(tid0 == 2){
		diffuseMap = texture(T2_texture, texCoord0);
	}else if(tid0 == 3){
		diffuseMap = texture(T3_texture, texCoord0);
	}else if(tid0 == 4){
		diffuseMap = texture(T4_texture, texCoord0);
	}else if(tid0 == 5){
		diffuseMap = texture(T5_texture, texCoord0);
	}else if(tid0 == 6){
		diffuseMap = texture(T6_texture, texCoord0);
	}else if(tid0 == 7){
		diffuseMap = texture(T7_texture, texCoord0);
	}else if(tid0 == 8){
		diffuseMap = texture(T8_texture, texCoord0);
	}else if(tid0 == 9){
		diffuseMap = texture(T9_texture, texCoord0);
	}else if(tid0 == 10){
		diffuseMap = texture(T10_texture, texCoord0);
	}else if(tid0 == 11){
		diffuseMap = texture(T11_texture, texCoord0);
	}else if(tid0 == 12){
		diffuseMap = texture(T12_texture, texCoord0);
	}else if(tid0 == 13){
		diffuseMap = texture(T13_texture, texCoord0);
	}else if(tid0 == 14){
		diffuseMap = texture(T14_texture, texCoord0);
	}else if(tid0 == 15){
		diffuseMap = texture(T15_texture, texCoord0);
	}
	
	if(tid0 == 16){
		diffuseMap = texture(T16_texture, texCoord0);
	}else if(tid0 == 17){
		diffuseMap = texture(T17_texture, texCoord0);
	}else if(tid0 == 18){
		diffuseMap = texture(T18_texture, texCoord0);
	}else if(tid0 == 19){
		diffuseMap = texture(T19_texture, texCoord0);
	}else if(tid0 == 20){
		diffuseMap = texture(T20_texture, texCoord0);
	}else if(tid0 == 21){
		diffuseMap = texture(T21_texture, texCoord0);
	}else if(tid0 == 22){
		diffuseMap = texture(T22_texture, texCoord0);
	}else if(tid0 == 23){
		diffuseMap = texture(T23_texture, texCoord0);
	}else if(tid0 == 24){
		diffuseMap = texture(T24_texture, texCoord0);
	}else if(tid0 == 25){
		diffuseMap = texture(T25_texture, texCoord0);
	}else if(tid0 == 26){
		diffuseMap = texture(T26_texture, texCoord0);
	}else if(tid0 == 27){
		diffuseMap = texture(T27_texture, texCoord0);
	}else if(tid0 == 28){
		diffuseMap = texture(T28_texture, texCoord0);
	}else if(tid0 == 29){
		diffuseMap = texture(T29_texture, texCoord0);
	}else if(tid0 == 30){
		diffuseMap = texture(T30_texture, texCoord0);
	}else if(tid0 == 31){
		diffuseMap = texture(T31_texture, texCoord0);
	}
	
	if(diffuseMap.a >= 0.5f){
		outputFS = diffuseMap * clamp((vec4(R_ambient, 1) + color0.z), 0, 1);
	}else{
		discard;
	}
}