#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;
layout(location = 2) in int tid;
layout(location = 3) in vec3 color;

uniform mat4 T_MVP;

out vec2 texCoord0;
flat out int tid0;
out vec3 color0;

void main(){
    gl_Position = T_MVP * vec4(position, 1.0);
    texCoord0 = texCoord; 
	
	tid0 = tid;
	color0 = color;
}