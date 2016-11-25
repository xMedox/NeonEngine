#version 330

in vec2 texCoord0;

uniform sampler2D diffuseMap;

layout(location = 0) out vec4 outputFS;

void main(){
	if(texture(diffuseMap, texCoord0).a >= 0.5){
		float depth = gl_FragCoord.z;
		
		float dx = dFdx(depth);
		float dy = dFdy(depth);
		
		outputFS = vec4(depth, depth * depth + 0.25 * (dx * dx + dy * dy), 0.0, 0.0);
	}else{
		discard;
	}
}