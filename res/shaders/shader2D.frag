#version 330

in vec2 texCoord0;
flat in int tid0;
in vec3 color0;

uniform sampler2D T_textures[32];

layout(location = 0) out vec4 outputFS;

void main(){
	if(tid0 == -1){
		//if(texCoord0.x <= 0.02 && texCoord0.y <= 0.02 && texCoord0.x >= 0.0 && texCoord0.y >= 0.0){
		//if(texCoord0.x <= 0.002 || texCoord0.y <= 0.002 || texCoord0.x >= 0.998 || texCoord0.y >= 0.998){
		//	return;
		//}else{
			outputFS = vec4((texCoord0.y*0.4-1.0)*-1.0*color0.x, (texCoord0.y*0.4-1.0)*-1.0*color0.y, (texCoord0.y*0.4-1.0)*-1.0*color0.z, 1.0);
		//}
	}else{
		outputFS = texture(T_textures[tid0], texCoord0)*vec4(color0, 1.0);
	}
}