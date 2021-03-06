# NeonEngine

## Changelog:

**Rendering:**
- The bloom effect is now resolution independent
- Removed the unneeded vertices of the skybox
- The Engine now uses physically based rendering and hdr
- Added the changeRenderBufferSize, generateMipmap and bindAsRenderTarget method that takes in a mip level to the CubeMap class
- The Texture class now supports hdr files
- All textures now get loaded by the stb_image library(Makes texture loading faster)
- VSync is now disabled by default
- Added the emissive value to the Material class
- You can now set the exposure of a camera
- The Engine now uses a deferred renderer

**Physics:**
- Added the getRestitution method to the Collider class
- Added a new constructor to the BoxCollider and CylinderCollider classes that takes in 3 floats
- Increased the update rate of the Physics Engine to 240 updates per second
- You can now get and set the update rate of the Physics Engine with the changeUpdaterate and getUpdaterate methods

**Other:**
- The ALC context now gets cleared before destroying it
- Added the cleanUp method to the Sound class
- The throwError method now gets used by all catch statements
- Fixed a bug where pointLights weren't affecting objects
- Added the DIRECTIONAL_LIGHT, POINT_LIGHT and SPOT_LIGHT values to the BaseLight
- Added the PERSPECTIVE_MODE and ORTHOGRAPHIC_MODE values to the CameraBase
- Fixed a bug that caused a crash when loading a low resolution Texture while the Texture Quality is lower
- Added the getCurrentYear, getCurrentMonth, getCurrentDay, getCurrentHour, getCurrentMinute and getCurrentSecond methods to the Time class
- The x-Axis rotation of the LookComponent is now properly limited
- Renamed the inputKey method to getInputKey, the inputKeyDown method to getInputKeyDown and the inputKeyUp method to getInputKeyUp
- You can now set the InputKeys and the speed of the PlayerComponent
- The Skybox class now only takes in one file
- The skyboxes are now stored in a separate skyboxes folder
- Added the requestAttention method in the Window class
- Added the pause, rewind, isPlaying, isPaused, getTimeOffset and setTimeOffset methods to the Sound class
- The physics components now support groups and masks
- Added the setRotation, setInnerAngle, setOuterAngle and the setOuterGain method to the Sound class