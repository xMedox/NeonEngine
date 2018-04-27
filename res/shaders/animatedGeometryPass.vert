#version 330

layout(location = 0) in vec3 position;
layout(location = 1) in vec2 texCoord;
layout(location = 2) in vec3 normal;
layout(location = 3) in vec3 tangent;
layout(location = 4) in ivec3 jointIndices;
layout(location = 5) in vec3 weights;

const int MAX_JOINTS = 50;
const int MAX_WEIGHTS = 3;

uniform mat4 M_jointTransforms[MAX_JOINTS];

uniform mat4 T_model;
uniform mat4 T_MVP;

out vec2 texCoord0;
out mat3 tbnMatrix;

void main(){
	vec4 totalLocalPos = vec4(0.0);
	vec4 totalNormal = vec4(0.0);
	vec4 totalTangent = vec4(0.0);
	
	for(int i=0;i<MAX_WEIGHTS;i++){
		mat4 jointTransform = M_jointTransforms[jointIndices[i]];
		vec4 posePosition = jointTransform * vec4(position, 1.0);
		totalLocalPos += posePosition * weights[i];
		
		vec4 worldNormal = jointTransform * vec4(normal, 0.0);
		totalNormal += worldNormal * weights[i];
		
		vec4 worldTangent = jointTransform * vec4(tangent, 0.0);
		worldTangent += worldTangent * weights[i];
	}
	
	gl_Position = T_MVP * totalLocalPos;
	texCoord0 = texCoord;
	
	vec3 n = normalize((T_model * totalNormal).xyz);
	vec3 t = normalize((T_model * totalTangent).xyz);
	t = normalize(t - dot(t, n) * n);
	
	tbnMatrix = mat3(t, cross(t, n), n);
}