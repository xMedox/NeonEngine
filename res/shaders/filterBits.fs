#version 330

in vec2 texCoord0;

uniform sampler2D R_filterTexture;

const float Resolution = 4.0f;
const float Saturation = 1.5f;
const float MosaicSize = 8.0f;

layout(location = 0) out vec4 outputFS;

void main(){
    vec2 mosaicInSize = textureSize(R_filterTexture, 0) / MosaicSize;
	
    vec4 baseTexel = texture(R_filterTexture, texCoord0 - fract(texCoord0 * mosaicInSize) / mosaicInSize);
	
    vec3 fractTexel = baseTexel.rgb - fract(baseTexel.rgb * Resolution) / Resolution;
    float luma = dot(fractTexel, vec3(0.3f, 0.59f, 0.11f));
    baseTexel.rgb = luma + (fractTexel - luma) * Saturation;
    baseTexel.a = 1.0f;
	
    outputFS = vec4(baseTexel);
}
