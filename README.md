# NeonEngine

## Changelog:

**Rendering:**
- Added the ability to add filters

**Physics:**
- The engine now uses the LibGDX Bullet wrapper(results in better simulation quality and performance)
- Renamed the add- and removeObject methods to add- and removeCollider in the PhysicsEngine
- Renamed the collider classes to have the Collider prefix
- Fixed the StaticPlaneCollider class
- Fixed the setUpAxis method in the CharacterController class

**Other:**
- Renamed the setOrientation method in the SoundEngine class to setRotation
- Renamed fileExists and directoryExists to doesFileExist and doesDirectoryExist
- Fixed the spelling of the createDirectory method
- The camera bases have their own files now
- Moved the light classes to the new lighting package
- Changed the shader extensions from fs to frag and from vs to vert
- Improved the radius calculation for the models
- Added the licenses of the libraries used
- The glfw window variable is now private
- Meshes and textures are now getting cleaned up after closing the engine

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
- SSAO(Normal-oriented Hemisphere)
- MSAA
- Fix flipped models

**Other:**
- Add a text input system(copy, paste, delete, return, mark and cut)
- Locked mouse area
- Controller support
- Networking