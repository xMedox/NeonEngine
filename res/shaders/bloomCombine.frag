#version 330

in vec2 texCoord0;

//uniform sampler2D R0_displayTexture;
//uniform sampler2D R1_displayTexture;
uniform sampler2D R_filterTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	//float gamma = 1.0;
	//vec4 color = texture(R0_displayTexture, texCoord0) + texture(R_filterTexture, texCoord0);
	
	//if(texture(R1_displayTexture, texCoord0) == vec4(0.0, 0.0, 0.0, 0.0)){
		//outputFS = vec4(pow(color.rgb, vec3(1.0/gamma)), color.a);
		//outputFS = texture(R0_displayTexture, texCoord0) + texture(R_filterTexture, texCoord0);
		outputFS = texture(R_filterTexture, texCoord0);
	//}else{
	//	outputFS = texture(R0_displayTexture, texCoord0);
	//}
}