# Java 2D RPG Game Engine + Game

An little Open Source Java RPG in 2D.

![Logo](https://raw.githubusercontent.com/JuKu/rpg-2dgame-engine/master/rpg-2dgame/data/graphic/intro/intro_screen.png)

Current version: 0.0.1 alpha

## Java 2D RPG Game Engine

Game Engine is based on Java 2D Renderer Prototype: https://github.com/JuKu/java-2drenderer-prototyp
Its only supporting OpenGL 3.2+, Vulkan support isnt planned. 

Source Code Contributors:
  - JuKu (jukusoft.com)

This game uses free or open source graphics!
Please read LICENSE file for source code and each tilesets has an extra LICENSE file.
  
## Requirements
  - Java 8
  - OpenGL 3.2+
  - Windows, Linux (only 64 Bit) and Mac OSX
  
## Documentation

### Used Libraries
  - LWJGL 3.1.0
  - GLFW
  - JSON
  - ini4j
  - log4j

### Modules
  - Core
  - Game
  - Game Engine
  - GLFW Window System (platform dependent)
  
### Open issues
  - reimplement class Color, use Off Heap memory
  - use GameLogger instead of Logger.getRootLogger() from log4j
  - implement GameConfig
  - add caching to FontTexture
  
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

### Game State System

The game state has also an game state system (optional).
To use this system, you have to extends SimpleGameStateApp<GameState> in your application class.
In onInitGame(GameStateManager<GameState> stateManager) you have to register your game states and push (activate) them.

example:
```java
public class MyGameApp extends SimpleGameStateApp<GameState> {

    public MyGameApp (boolean useMultiThreading, int fixedFPS, int fixedUPS, boolean vSync) {
        super(useMultiThreading, fixedFPS, fixedUPS, vSync);
    }

    @Override
    protected void onInitGame(GameStateManager<GameState> stateManager) {
        //create and register new intro game state
        IntroGameState intro = new IntroGameState();
        stateManager.addGameState("intro", intro);

        //push game state to activate game state --> set intro to active game state
        try {
            stateManager.pushGameState("intro");
        } catch (GameStateNotFoundException e) {
            e.printStackTrace();
        }
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

An example for IntroGameState:
```java
public class IntroGameState extends BasicGameState {

    @Override
    public <T extends GameState> void onInit(GameStateManager<T> gameStateManager, GameApp app) {
        Logger.getRootLogger().info("GameState4::onInit().");
    }

    @Override
    public void update(GameApp app, double delta) {
        //update
    }

    @Override
    public void render (GameApp app) {
        //clear window
        getWindow().clear();

        //check, if window was resized
        if (getWindow().wasResized()) {
            //reset viewport
            getWindow().setViewPort(0, 0, getWindow().getWidth(), getWindow().getHeight());

            //reset resized flag
            getWindow().setResizedFlag(false);
        }
    }

}
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