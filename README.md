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
  - use GameLogger instead of Logger.getRootLogger() from log4j
  
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

while (!window.shouldClose()) {
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

while (!window.shouldClose()) {
    //process input
    window.processInput();

    window.clear();

    window.swap();

    Thread.currentThread().sleep(100);
}
```

Result:
![GLFW Window](https://raw.githubusercontent.com/JuKu/rpg-2dgame-engine/master/rpg-2dgame/docs/images/window.PNG)

### Create an simple application with game states

First, you have to create an class extends SimpleGameStateApp<GameState>, for example:
```java
public class MyGameApp extends SimpleGameStateApp<GameState> {

    public MyGameApp (boolean useMultiThreading, int fixedFPS, int fixedUPS, boolean vSync) {
        super(useMultiThreading, fixedFPS, fixedUPS, vSync);
    }

    @Override
    protected void onInitGame(GameStateManager<GameState> stateManager) {
        //
    }

    @Override
    protected void onCreateWindow(IWindow window) {
        //set window size
        window.setSize(1280, 720);

        //set window title
        window.setTitle("2D RPG");

        //set clear color to black
        window.setClearColor(0, 0, 0, 0);

        //centralize window
        window.center();
    }

}
```

On main class, you have to start the application:
```java
//create new instance of game application with 2 threads for renderer and update thread, unlimited fps rate and 60 updates per second, also vSync isnt enabled.
MyGameApp game = new MyGameApp(true, -1, 60, false);

//initialize game
game.init();

//start game
game.start();
```

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

If you return false, other key callbacks arent called anymore. So your UI can easely catch some input events, which game doesnt know about it.

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