# NeonEngine

## Changelog:

**Rendering:**
- Added the ability to set the font size
- The drawString method now takes the color in before the scale
- Now only the red component of the glowMap gets used
- Removed particle bloom
- Renamed glowMap to emissiveMap
- Renamed add... methods in the RenderingEngine to render...
- Added a method in the RenderingEngine to render a mesh
- Minor performance improvements
- Fixed the particles

**Physics:**
- Fixed a collision bug
- Some minor performance improvements
- Removed the hard coded position of physics objects

**Other:**
- Cameras now have a mode value
- Skyboxes aren't components anymore
- Added the toString method to all math classes
- Fixed CubeMaps getting loaded even if the same CubeMap already exists
- Removed OPTION_ENABLE_MIPMAPPING and OPTION_MSAA_MULTIPLIER in the NeonEngine
- Added the getSecond method in the Time class
- You can now get the CharacterController of the PlayerPhysicsComponent

## Features needed for 1.0.0:

**Rendering:**
- PointLight Shadows

**Physics:**
- Masking

**Other:**
- Animation Support

## Ideas and bugs:

**Rendering:**
- Remove the black line of DirectionalLight Shadows
- HDR

**Other:**
- Add a text input system(copy, paste, delete, return, mark and cut)
- Locked mouse area
- Controller support
- Networking