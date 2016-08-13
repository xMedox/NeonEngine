#version 330

layout(location = 0) in vec3 position;

uniform mat4 T_MVP;

void main(){
    gl_Position = T_MVP * vec4(position, 1.0f);
}