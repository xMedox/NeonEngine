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
layout(location = 1) out vec4 outputBloom;

void main(){
	vec4 diffuse;
	
	if(tid0 < 10){
		if(tid0 == 0){
			diffuse = texture(T0_texture, texCoord0);
		}else if(tid0 == 1){
			diffuse = texture(T1_texture, texCoord0);
		}else if(tid0 == 2){
			diffuse = texture(T2_texture, texCoord0);
		}else if(tid0 == 3){
			diffuse = texture(T3_texture, texCoord0);
		}else if(tid0 == 4){
			diffuse = texture(T4_texture, texCoord0);
		}else if(tid0 == 5){
			diffuse = texture(T5_texture, texCoord0);
		}else if(tid0 == 6){
			diffuse = texture(T6_texture, texCoord0);
		}else if(tid0 == 7){
			diffuse = texture(T7_texture, texCoord0);
		}else if(tid0 == 8){
			diffuse = texture(T8_texture, texCoord0);
		}else if(tid0 == 9){
			diffuse = texture(T9_texture, texCoord0);
		}
	}else if(tid0 < 20){
		if(tid0 == 10){
			diffuse = texture(T10_texture, texCoord0);
		}else if(tid0 == 11){
			diffuse = texture(T11_texture, texCoord0);
		}else if(tid0 == 12){
			diffuse = texture(T12_texture, texCoord0);
		}else if(tid0 == 13){
			diffuse = texture(T13_texture, texCoord0);
		}else if(tid0 == 14){
			diffuse = texture(T14_texture, texCoord0);
		}else if(tid0 == 15){
			diffuse = texture(T15_texture, texCoord0);
		}else if(tid0 == 16){
			diffuse = texture(T16_texture, texCoord0);
		}else if(tid0 == 17){
			diffuse = texture(T17_texture, texCoord0);
		}else if(tid0 == 18){
			diffuse = texture(T18_texture, texCoord0);
		}else if(tid0 == 19){
			diffuse = texture(T19_texture, texCoord0);
		}
	}else if(tid0 < 30){
		if(tid0 == 20){
			diffuse = texture(T20_texture, texCoord0);
		}else if(tid0 == 21){
			diffuse = texture(T21_texture, texCoord0);
		}else if(tid0 == 22){
			diffuse = texture(T22_texture, texCoord0);
		}else if(tid0 == 23){
			diffuse = texture(T23_texture, texCoord0);
		}else if(tid0 == 24){
			diffuse = texture(T24_texture, texCoord0);
		}else if(tid0 == 25){
			diffuse = texture(T25_texture, texCoord0);
		}else if(tid0 == 26){
			diffuse = texture(T26_texture, texCoord0);
		}else if(tid0 == 27){
			diffuse = texture(T27_texture, texCoord0);
		}else if(tid0 == 28){
			diffuse = texture(T28_texture, texCoord0);
		}else if(tid0 == 29){
			diffuse = texture(T29_texture, texCoord0);
		}
	}else{
		if(tid0 == 30){
			diffuse = texture(T30_texture, texCoord0);
		}else if(tid0 == 31){
			diffuse = texture(T31_texture, texCoord0);
		}
	}
	
	if(diffuse.a >= 0.5){
		outputFS = diffuse * clamp((vec4(R_ambient, 1.0) + color0.z), 0.0, 1.0);
		outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
	}else{
		discard;
	}
}