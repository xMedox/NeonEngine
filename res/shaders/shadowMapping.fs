#version 330

in vec2 texCoord0;

uniform sampler2D diffuse;

layout(location = 0) out vec4 outputFS;

void main(){
	if(texture(diffuse, texCoord0).a >= 0.5){
		float depth = gl_FragCoord.z;

		float dx = dFdx(depth);
		float dy = dFdy(depth);

		outputFS = vec4(depth, depth * depth + 0.25f * (dx * dx + dy * dy), 0.0f, 0.0f);
	}else{
		discard;
	}
}