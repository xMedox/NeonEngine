#version 330

in vec2 texCoord0;

uniform sampler2D R0_displayTexture;
//uniform sampler2D R1_displayTexture;
uniform sampler2D R_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	//if(texture(R1_displayTexture, texCoord0) == vec4(0, 0, 0, 0)){
		outputFS = texture(R0_displayTexture, texCoord0) + texture(R_filterTexture, texCoord0);
	//}else{
	//	outputFS = texture(R0_displayTexture, texCoord0);
	//}
}