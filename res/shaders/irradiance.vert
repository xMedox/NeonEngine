#version 330

layout(location = 0) in vec3 position;

uniform mat4 T_MVP;

out vec3 texCoord0;

void main(){
	vec3 pos = vec3(position.x*-1, position.y, position.z);
	
	gl_Position = T_MVP * vec4(position, 1.0);
	gl_Position.z = gl_Position.w - 0.00001;
	texCoord0 = pos;
}