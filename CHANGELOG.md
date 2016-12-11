# Changelog

## Version 1.0.2 Alpha
  - added support to set an timer for an Runnable (execute Runnable after an fixe time)
  - set VBOs methods are now public in class Mesh
  - resolved some concurrency issues on class Asset - thanks to @noctarius for this tips!
  - marked class Shader as deprecated, use class OpenGLShaderProgram instead!
  - DefaultCamera: added support to detect, if camera was changed (for example position, rotation and so on), so renderer can be optimized and dont have to generate some matrizes every render cycle
  - FloatVertexBufferObject & IntegerVertexBufferObject now supports GL_DYNAMIC_DRAW

Special thanks to noctarius for some tips!

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