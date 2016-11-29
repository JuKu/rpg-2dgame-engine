# Java 2D RPG Game Engine + Game

An little Open Source Java RPG in 2D.

![Logo](https://raw.githubusercontent.com/JuKu/rpg-2dgame-engine/master/rpg-2dgame/data/graphic/intro/intro_screen.png)

Current version: 0.0.1 alpha

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
  - Core
  - Game
  - Game Engine
  - GLFW Window System (platform dependent)
  
### Open issues
  - reimplement class Color, use Off Heap memory
  
### Window System

The RPG 2D Game Engine only supports 1 window on primary monitor.
GLFW library is used to create window and get OpenGL context.

How to create an window:
```java
//you have to initialize GLFW first (only once)
GLFWUtils.init();

//create window
IWindow window = new GLFWWindow(1280, 720, "2D RPG", false);
window.create();

//show window
window.setVisible(true);

while (true) {
    //process input
    window.processInput();

    Thread.currentThread().sleep(100);
}
```

IMPORTANT: The window is frozing, if you dont poll window events with window.processInput()!

To set an clear background color, you can use this example:
```java
//you have to initialize GLFW first (only once)
GLFWUtils.init();

//create window
IWindow window = new GLFWWindow(1280, 720, "2D RPG", false);
window.create();

//show window
window.setVisible(true);

window.setExitOnClose(true);

//prepare rendering
window.prepareRendering();

//set clear color
window.setClearColor(0, 0, 0, 0);

while (true) {
    //process input
    window.processInput();

    window.clear();

    window.swap();

    Thread.currentThread().sleep(100);
}
```

Result:
![GLFW Window](https://raw.githubusercontent.com/JuKu/rpg-2dgame-engine/master/rpg-2dgame/docs/images/window.PNG)

### Input Handling

There is an basic way to get all raw inputs from window. You can register 1 ore more key callbacks to get notified, if keyboard events received:
```java
window.addKeyCallback(new AbstractKeyCallback() {
    @Override
    public boolean keyPressed(int key) {
        System.out.println("key pressed: " + key);

        //return true, so other key callbacks are also executed
        return true;
    }

    @Override
    public boolean keyReleased(int key) {
        System.out.println("key released: " + key);

        //return true, so other key callbacks are also executed
        return true;
    }
});
```

IMPORTANT: This only works, if you call window.processInput() in your gameloop.

### Logging

To log, there is an class GameLogger.
Examples:

```java
//initialize game logger
GameLogger.init();

//log
GameLogger.info("GameMain", "app start now.");
```