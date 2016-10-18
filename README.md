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
- Physics objects are now getting cleaned up

**Other:**
- Renamed the setOrientation method in the SoundEngine class to setRotation
- Renamed fileExists and directoryExists to doesFileExist and doesDirectoryExist
- Fixed the spelling of the createDirectory method
- The camera bases have their own files now
- Moved the light classes to the new lighting package
- Changed the shader extensions from fs to frag and from vs to vert
- Improved the radius calculation for the models
- Added the licenses of the libraries used
- The glfw window and the fps variables are now private
- Custom meshes and textures are now getting cleaned up after closing the engine
- Added the enable profiling option
- You now have to change the options through methods
- Most options are now booleans
- Added the setPos and setSize methods in the Window class
- The window now gets destroyed before cleaning everything up