## NeonEngine

## Changelog:

Rendering:
- Renamed shaders
- Made it possible to enable bloom after creating the Window
- Optimized updateUniforms function
- Optimized shader2D and forwardParticleAmbient shaders
- Added mesh2DInFrustum function

Other:
- Added functions to change material properties
- Removed MappedValues class
- Fixed addToEngine function in the component classes
- Removed getRootEntity and getRootEntity2D functions
- Made framerate an int

## Todo:

Rendering:
- Ability to render to a CubeMap
- PointLight Shadows
- Remove the black line of DirectionalLight Shadows
- Rewrite Fonts

Physics:
- Masking

Other:
- Add a text input system(copy, paste, delete, return, mark and cut)
- Locked mouse area
- Animation Support
- Controller support
