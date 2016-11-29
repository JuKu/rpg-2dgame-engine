# rpg-2dgame-engine

An little Open Source Java RPG in 2D, mainly for educational purposes.

![Logo](https://raw.githubusercontent.com/JuKu/rpg-2dgame-engine/master/rpg-2dgame/data/graphic/intro/intro_screen.png)

## Java 2D RPG Game Engine

Game Engine is based on Java 2D Renderer Prototype: https://github.com/JuKu/java-2drenderer-prototyp
Its only supporting OpenGL 3.2+, Vulkan support isnt planned. 

Contributors:
  - JuKu (jukusoft.com)
  
## Requirements
  - Java 8
  - OpenGL 3.2+
  - Windows, Linux (only 64 Bit) and Mac OSX
  
## Documentation

### Modules
  - Game
  - Game Engine
  - GLFW Window System (platform dependent)

### Logging

To log, there is an class GameLogger.
Examples:

```java
//initialize game logger
GameLogger.init();

//log
GameLogger.info("GameMain", "app start now.");
```