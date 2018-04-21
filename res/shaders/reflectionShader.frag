#version 330

in vec2 texCoord0;

uniform sampler2D R_finalTexture;

//uniform sampler2D R0_filterTexture;
uniform sampler2D R1_filterTexture;
uniform sampler2D R2_filterTexture;
uniform sampler2D R3_filterTexture;

uniform mat4 TM_projMatrixInv;
uniform mat4 TM_viewMatrixInv;
uniform mat4 TM_projMatrix;
//uniform mat4 TM_viewMatrix;

const float step = 0.1;
const float minRayStep = 0.1;
const float maxSteps = 30;
const int numBinarySearchSteps = 5;
const float reflectionSpecularFalloffExponent = 3.0;

layout(location = 0) out vec4 outputFS;
layout(location = 1) out vec4 outputBloom;

#define Scale vec3(.8, .8, .8)
#define K 19.19

vec4 RayMarch(vec3 dir, inout vec3 hitCoord, out float dDepth);

vec3 PositionFromDepth(float depth, vec2 coords);

vec3 BinarySearch(inout vec3 dir, inout vec3 hitCoord, inout float dDepth);
 
vec4 RayCast(vec3 dir, inout vec3 hitCoord, out float dDepth);

vec3 fresnelSchlick(float cosTheta, vec3 F0);

vec3 hash(vec3 a);

void main(){
	float Metallic = texture(R2_filterTexture, texCoord0).y;
	float spec = 1;
	
	if(Metallic < 0.01)
        discard;
	
	//vec3 albedo = texture(R0_filterTexture, texCoord0).rgb;
	
	vec3 viewNormal = vec3(texture(R1_filterTexture, texCoord0) * TM_viewMatrixInv);
	
	vec3 viewPos = PositionFromDepth(texture(R3_filterTexture, texCoord0).x, texCoord0);
	
	//vec3 F0 = vec3(0.04);
	//F0 = mix(F0, albedo, Metallic);
	
	//vec3 Fresnel = fresnelSchlick(max(dot(normalize(viewNormal), normalize(viewPos)), 0.0), F0);
	
	vec3 reflected = normalize(reflect(normalize(viewPos), normalize(viewNormal)));
	
	vec3 hitPos = viewPos;
    float dDepth;
 
    //vec3 wp = vec3(vec4(viewPos, 1.0) * TM_viewMatrixInv);
    //vec3 jitt = mix(vec3(0.0), vec3(hash(wp)), spec);
    vec4 coords = RayMarch((/*vec3(jitt) + */reflected * max(minRayStep, -viewPos.z)), hitPos, dDepth);
 
 
    vec2 dCoords = smoothstep(0.2, 0.6, abs(vec2(0.5, 0.5) - coords.xy));
 
 
    float screenEdgefactor = clamp(1.0 - (dCoords.x + dCoords.y), 0.0, 1.0);

    float ReflectionMultiplier = pow(Metallic, reflectionSpecularFalloffExponent) * 
                screenEdgefactor * 
                -reflected.z;
				
	vec3 SSR = texture(R_finalTexture, coords.xy).rgb * clamp(ReflectionMultiplier, 0.0, 0.9)/* * Fresnel*/;  

    outputFS = vec4(SSR/**0.0001*/, 1);
	outputBloom = vec4(0.0, 0.0, 0.0, 1.0);
}

vec3 PositionFromDepth(float depth, vec2 coords) {
    float z = depth * 2.0 - 1.0;

    vec4 clipSpacePosition = vec4(coords * 2.0 - 1.0, z, 1.0);
    vec4 viewSpacePosition = TM_projMatrixInv * clipSpacePosition;

    // Perspective division
     viewSpacePosition /= viewSpacePosition.w;
	
    return viewSpacePosition.xyz;
}

vec3 BinarySearch(inout vec3 dir, inout vec3 hitCoord, inout float dDepth)
{
    float depth;

    vec4 projectedCoord;
 
    for(int i = 0; i < numBinarySearchSteps; i++)
    {

        projectedCoord = TM_projMatrix * vec4(hitCoord, 1.0);
        projectedCoord.xy /= projectedCoord.w;
        projectedCoord.xy = projectedCoord.xy * 0.5 + 0.5;
 
        depth = PositionFromDepth(texture(R3_filterTexture, projectedCoord.xy).x, projectedCoord.xy).z;

 
        dDepth = hitCoord.z - depth;

        dir *= 0.5;
        if(dDepth > 0.0)
            hitCoord += dir;
        else
            hitCoord -= dir;    
    }

        projectedCoord = TM_projMatrix * vec4(hitCoord, 1.0);
        projectedCoord.xy /= projectedCoord.w;
        projectedCoord.xy = projectedCoord.xy * 0.5 + 0.5;
 
    return vec3(projectedCoord.xy, depth);
}

vec4 RayMarch(vec3 dir, inout vec3 hitCoord, out float dDepth)
{

    dir *= step;
 
 
    float depth;
    int steps;
    vec4 projectedCoord;

 
    for(int i = 0; i < maxSteps; i++)
    {
        hitCoord += dir;
 
        projectedCoord = TM_projMatrix * vec4(hitCoord, 1.0);
        projectedCoord.xy /= projectedCoord.w;
        projectedCoord.xy = projectedCoord.xy * 0.5 + 0.5;
 
        depth = PositionFromDepth(texture(R3_filterTexture, projectedCoord.xy).x, projectedCoord.xy).z;
        if(depth > 1000.0)
            continue;
 
        dDepth = hitCoord.z - depth;

        if((dir.z - dDepth) < 1.2)
        {
            if(dDepth <= 0.0)
            {   
                vec4 Result;
                Result = vec4(BinarySearch(dir, hitCoord, dDepth), 1.0);

                return Result;
            }
        }
        
        steps++;
    }
 
    
    return vec4(projectedCoord.xy, depth, 0.0);
}

vec3 fresnelSchlick(float cosTheta, vec3 F0)
{
    return F0 + (1.0 - F0) * pow(1.0 - cosTheta, 5.0);
}


vec3 hash(vec3 a)
{
    a = fract(a * Scale);
    a += dot(a, a.yxz + K);
    return fract((a.xxy + a.yxx)*a.zyx);
}