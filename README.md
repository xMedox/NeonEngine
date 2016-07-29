# NeonEngine

## Changelog:

**Physics:**
- The engine now uses the LibGDX Bullet wrapper(better performance and simulation quality)
- Fixed the StaticPlane
- Renamed the add- and removeObject methods to add- and removeCollider in the PhysicsEngine
- Renamed the colliders to have Collider at the end
- Fixed the setUpAxis method in the CharacterController class

**Other:**
- Renamed the setOrientation method in the SoundEngine class to setRotation
- Renamed fileExists and directoryExists to doesFileExist and doesDirectoryExist
- Fixed spelling of the createDirectory method
- The camera bases have their own files now
- Moved the light classes to the new lighting package

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