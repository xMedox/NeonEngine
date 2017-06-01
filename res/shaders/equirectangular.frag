#version 330

in vec3 texCoord0;

uniform sampler2D X_filterTexture;

layout(location = 0) out vec4 outputFS;

const vec2 invAtan = vec2(0.1591, 0.3183);
vec2 SampleSphericalMap(vec3 v){
    vec2 uv = vec2(atan(v.z, v.x), asin(v.y));
    uv *= invAtan;
    uv += 0.5;
    return uv;
}

void main(){		
    vec2 uv = SampleSphericalMap(normalize(texCoord0));
    vec3 color = texture(X_filterTexture, uv).rgb;
    
    outputFS = vec4(color, 1.0);
}