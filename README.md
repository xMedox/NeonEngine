# NeonEngine

## Changelog:

**Rendering:**
- Added the ability to render to a CubeMap
- Renamed shadowMapGenerator shaders to shadowMapping
- Renamed filterGausBlur7x1 shader to filterGausBlur

**Physics:**
- Fixed physics simulation slowing down at lower frame rates

**Other:**
- Moved image functions from the Util class into the new ImageUtil class
- Changed CoreEngines init function to take in the game object first
- Renamed CoreEngine class to NeonEngine

## Todo:

**Rendering:**
- **PointLight Shadows**
- **Rewrite font rendering and loading**
- Remove the black line of DirectionalLight Shadows
- HDR

**Physics:**
- **Masking**

**Other:**
- **Animation Support**
- Add a text input system(copy, paste, delete, return, mark and cut)
- Locked mouse area
- Controller support
