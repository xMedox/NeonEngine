#version 330

in vec2 texCoord0;
flat in int tid0;
in vec3 color0;

uniform vec3 R_ambient;

uniform sampler2D T_textures[32];

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

void main(){
	vec4 diffuse = texture(T_textures[tid0], texCoord0);
	
	if(diffuse.a >= 0.5){
		outputFS = diffuse * clamp((vec4(R_ambient, 1.0) + color0.z), 0.0, 1.0);
		outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
	}else{
		discard;
	}
}