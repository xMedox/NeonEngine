#version 330

in vec2 texCoord0;

uniform sampler2D R3_filterTexture;

uniform mat4 T_projMatrixInv;
uniform mat4 T_viewMatrixInv;

layout(location = 0) out vec4 outputFS;

vec3 worldPosFromDepth(){
	float depth = texture(R3_filterTexture, texCoord0).x;
	
	float z = depth * 2.0 - 1.0;
	
    vec4 clipSpacePosition = vec4(texCoord0 * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = T_projMatrixInv * clipSpacePosition;
	
    viewSpacePosition /= viewSpacePosition.w;
	
    vec4 worldSpacePosition = T_viewMatrixInv * viewSpacePosition;
	
    return worldSpacePosition.xyz;
}

void main(){
	outputFS = vec4(worldPosFromDepth(), 1.0);
}