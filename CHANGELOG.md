# Changelog

## Version 1.0.3 Alpha
  - added support for framebuffers
  - added support to rotate Matrix4f
  - improved camera
  - added support to generate view matrix of camera with class TransformationUtils
  - added support to set filter & wrap on texture

## Version 1.0.2 Alpha
  - added support to set an timer for an Runnable (execute Runnable after an fixe time)
  - added support to set an interval timer (execute Runnable every fixed time)
  - moved class GamePlatform to core module instead of game engine, so every sub system can use it
  - set VBOs methods are now public in class Mesh
  - resolved some concurrency issues on class Asset - thanks to @noctarius for this tips!
  - marked class Shader as deprecated, use class OpenGLShaderProgram instead!
  - DefaultCamera: added support to detect, if camera was changed (for example position, rotation and so on), so renderer can be optimized and dont have to generate some matrizes every render cycle
  - FloatVertexBufferObject & IntegerVertexBufferObject now supports GL_DYNAMIC_DRAW
  - OpenGLShaderProgram now also supports Vector4f uniforms
  - added support for Texture Regions (Spritesheets)
  - improved performance in UIRenderer, copied list isnt required anymore, some OpenGL hints in GLFWWindow has solved this issue
  - added support for transparency
  - added support for basic animations (with only 1 layer and 1 spritesheet per animation)

Special thanks to noctarius for some tips about Software Design / Performance and Buschtaube for an OpenGL tip about texture coordinates in texture regions!

## 1.0.1 Alpha Release
  - UIRenderer is ready for your game
  - fixed some problems
  - added start scripts, so user can easely start game app with double click
  - many performance & GC improvements

## 1.0.0 Alpha Release
  - added support to render images
  - fixed some problems
  - added better resource manager, also for gpu
  - if you use an texture more than 1 times, you can use resource manager, so you dont have to upload image more than 1 time to gpu
  - better UI renderer


## 0.0.1 Alpha Release
Features:

  - easy window creation (uses GLFW)
  - render text
  - Game State System
  - UI Renderer
  - all basic classes for rendering (Mesh, Material, Color and so on)