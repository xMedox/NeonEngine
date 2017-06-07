#version 330

in vec2 texCoord0;
flat in int tid0;
in vec3 color0;

uniform sampler2D T_textures[R_MAX_TEXTURE_IMAGE_UNITS];

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

void main(){
	vec4 diffuse = texture(T_textures[tid0], texCoord0);
	
	if(diffuse.a >= 0.5){
		vec3 color = pow(diffuse.rgb, vec3(2.2));
		
		//outputFS = vec4(color, diffuse.a);
		outputFS = vec4(color, 1.0);
		outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
	}else{
		discard;
	}
}