#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;
layout(location = 4) in ivec3 jointIndices;
layout(location = 5) in vec3 weights;

const int MAX_JOINTS = 50;
const int MAX_WEIGHTS = 3;

uniform mat4 M_jointTransforms[MAX_JOINTS];

uniform mat4 T_MVP;

out vec2 texCoord0;
out vec4 position0;

void main(){
	vec4 totalLocalPos = vec4(0.0);
	
	for(int i=0;i<MAX_WEIGHTS;i++){
		mat4 jointTransform = M_jointTransforms[jointIndices[i]];
		vec4 posePosition = jointTransform * vec4(position, 1.0);
		totalLocalPos += posePosition * weights[i];
	}
	
	vec4 pos = T_MVP * totalLocalPos;
	
	gl_Position = pos;
	texCoord0 = texCoord;
	position0 = pos;
}