#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;
layout(location = 4) in ivec4 jointIndices;
layout(location = 5) in vec4 weights;

const int MAX_WEIGHTS = 4;

uniform mat4 S_jointTransforms[MAX_JOINTS];

uniform mat4 T_MVP;

out vec2 texCoord0;
out vec4 position0;

void main(){
	//vec4 totalLocalPos = vec4(0.0);
	mat4 mat = mat4(0.0);
	
	for(int i=0;i<MAX_WEIGHTS;i++){
		mat4 jointTransform = S_jointTransforms[jointIndices[i]];
		
		mat += jointTransform * weights[i];
		
		//vec4 posePosition = jointTransform * vec4(position, 1.0);
		//totalLocalPos += posePosition * weights[i];
	}
	
	vec4 pos = T_MVP * (vec4(position, 1.0) * mat);
	
	gl_Position = pos;
	texCoord0 = texCoord;
	position0 = pos;
}