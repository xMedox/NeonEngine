# NeonEngine

## Changelog:

**Rendering:**
- Added the ability to add filters
- Fixed shadow mapping on some machines
- Fixed a lighting bug on some machines
- The batchrenderer now uses sampler arrays
- Fixed the batchrenderer on some machines
- Fixed the font rendering

**Physics:**
- The engine now uses the LibGDX Bullet wrapper(results in better simulation quality and performance)
- Renamed the add- and removeObject methods to add- and removeCollider in the PhysicsEngine
- Renamed the collider classes to have the Collider prefix
- Fixed the StaticPlaneCollider class
- Fixed the setUpAxis method in the CharacterController class
- The Collider and the Constraint class are now abstract classes
- Added collision filtering

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
- Added the mouseToRay method in the Util class
- Moved the 2d components to the new components2D package
- Added some noise creation methods and the lerp method to the Util class
- Added the new Cursor class that gets used by the Window
- Self made models now calculate their radius
- Added the isMouseInsideWindow method in the Input class
- You can now change the inputKey of the ui elements
- The button and checkbox now work like a normal button and checkbox
- Updated the error code
- The standard font color of the text boxes is now black
- You can now change the window resizability after creating the window
- Renamed setStartDimensions to setStartSize in the Window class
- You can now change the font style
- Added error logging
- The engine gets cleaned up if an error occurs
- The unused rendering textures are getting cleaned up now
- Added the cleanUp method in all OpenGL objects