#version 330

#include "forwardlighting.fragh"

#include "lighting.glh"

uniform vec3 C_eyePos;
uniform float specularIntensity;
uniform float specularPower;

uniform SpotLight R_spotLight;

vec4 CalcLightingEffect(vec3 normal, vec3 worldPos, float specular){
    float spotFactor = dot(normalize(worldPos - R_spotLight.pointLight.position), R_spotLight.direction);
    
    vec4 color = vec4(0.0, 0.0, 0.0, 0.0);
    
    if(spotFactor > R_spotLight.cutoff){
        color = CalcPointLight(R_spotLight.pointLight, normal, worldPos, specular, specularIntensity, specularPower, C_eyePos) * (1.0 - (1.0 - spotFactor)/(1.0 - R_spotLight.cutoff));
    }
    
    return color;
}

#include "lightingMain.fragh"