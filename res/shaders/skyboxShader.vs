#version 330

layout(location = 0) in vec3 position;

uniform mat4 T_MVP;

out vec3 texCoord0;

void main(){
	gl_Position = T_MVP * vec4(position, 1.0f); 
	texCoord0 = position;
}