#version 330

in vec2 texCoord0;
flat in int tid0;
in vec3 color0;

//uniform vec3 R_ambient;

uniform sampler2D T_textures[R_MAX_TEXTURE_IMAGE_UNITS];

layout (location = 0) out vec4 out0;
layout (location = 1) out vec4 out1;
layout (location = 2) out vec4 out2;
layout (location = 3) out vec4 out3;

void main(){
	vec4 diffuse = texture(T_textures[tid0], texCoord0);
	
	if(diffuse.a >= 0.5){
		//outputFS = diffuse * clamp((vec4(R_ambient, 1.0) + color0.z), 0.0, 1.0);
		//outputBloom = vec4(0.0, 0.0, 0.0, 0.0);
		out0 = diffuse;
		out1 = vec4(0.0, 0.0, 0.0, 0.0);
		out2 = vec4(0.0, 0.0, 0.0, 0.0);
		out3 = vec4(1.0, 1.0, 0.0, 0.0);
	}else{
		discard;
	}
}