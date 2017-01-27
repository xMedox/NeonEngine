# NeonEngine

## Changelog:

**Rendering:**
- The bloom effect is now resolution independent

**Other:**
- The ALC context now gets cleared before destroying it
- Added the cleanUp method to the Sound class
- The throwError method now gets used by all catch statements
- Fixed a bug where pointLights weren't affecting objects
- Added the DIRECTIONAL_LIGHT, POINT_LIGHT and SPOT_LIGHT values to the BaseLight
- Added the PERSPECTIVE_MODE and ORTHOGRAPHIC_MODE values to the CameraBase