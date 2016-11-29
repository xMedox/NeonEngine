#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;
layout(location = 2) in int tid;

uniform mat4 T_MVP;

out vec2 texCoord0;
flat out int tid0;
out vec4 position0;

void main(){
	vec4 pos = T_MVP * vec4(position, 1.0);
	
	gl_Position = pos;
	texCoord0 = texCoord;
	
	tid0 = tid;
	position0 = pos;
}