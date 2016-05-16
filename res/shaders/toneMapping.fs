#version 330

in vec2 texCoord0;

uniform float R_gamma;
uniform float R_exposure;
uniform sampler2D R_filterTexture;
//uniform sampler2D R1_displayTexture;

layout(location = 0) out vec4 outputFS;

void main(){
	vec3 hdrColor = texture(R_filterTexture, texCoord0).rgb;
	
    vec3 mapped = vec3(1.0) - exp(-hdrColor * R_exposure);
	
    mapped = pow(mapped, vec3(1.0 / R_gamma));
	
    outputFS = vec4(mapped, 1.0);
}