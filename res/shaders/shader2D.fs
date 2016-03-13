#version 330

in vec2 texCoord0;
flat in int tid0;
in vec3 color0;

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
	if(tid0 == -1){
		//if(texCoord0.x <= 0.02f && texCoord0.y <= 0.02f && texCoord0.x >= 0.0f && texCoord0.y >= 0.0f){
		//if(texCoord0.x <= 0.002f || texCoord0.y <= 0.002f || texCoord0.x >= 0.998f || texCoord0.y >= 0.998f){
		//	return;
		//}else{
			outputFS = vec4((texCoord0.y*0.4f-1)*-1*color0.x, (texCoord0.y*0.4f-1)*-1*color0.y, (texCoord0.y*0.4f-1)*-1*color0.z, 1);
		//}
	}else if(tid0 == 0){
		outputFS = texture(T0_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 1){
		outputFS = texture(T1_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 2){
		outputFS = texture(T2_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 3){
		outputFS = texture(T3_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 4){
		outputFS = texture(T4_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 5){
		outputFS = texture(T5_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 6){
		outputFS = texture(T6_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 7){
		outputFS = texture(T7_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 8){
		outputFS = texture(T8_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 9){
		outputFS = texture(T9_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 10){
		outputFS = texture(T10_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 11){
		outputFS = texture(T11_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 12){
		outputFS = texture(T12_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 13){
		outputFS = texture(T13_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 14){
		outputFS = texture(T14_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 15){
		outputFS = texture(T15_texture, texCoord0)*vec4(color0, 1);
	}
	
	if(tid0 == 16){
		outputFS = texture(T16_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 17){
		outputFS = texture(T17_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 18){
		outputFS = texture(T18_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 19){
		outputFS = texture(T19_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 20){
		outputFS = texture(T20_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 21){
		outputFS = texture(T21_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 22){
		outputFS = texture(T22_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 23){
		outputFS = texture(T23_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 24){
		outputFS = texture(T24_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 25){
		outputFS = texture(T25_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 26){
		outputFS = texture(T26_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 27){
		outputFS = texture(T27_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 28){
		outputFS = texture(T28_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 29){
		outputFS = texture(T29_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 30){
		outputFS = texture(T30_texture, texCoord0)*vec4(color0, 1);
	}else if(tid0 == 31){
		outputFS = texture(T31_texture, texCoord0)*vec4(color0, 1);
	}
}