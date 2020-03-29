<p align="center"><img src="./res/DungeonKit.png" alt="DungeonKit Logo"></p>

DungeonKit is a multiplatform library written in [Kotlin](https://github.com/jetbrains/kotlin). 
The purpose of the library is to enable a simple and easy way of creating floors for rougelike
dungeon generators.

### How it works:

The generator is built off the back of several different systems. The map is generated using the 
`Generator` API, which is largely usable across all platforms without many extra implementations
The map is then fed into the `Renderer` API, which requires more dependency on specific platforms.
Both sides of the library are tied together through the use of binding maps.

#### Example code:
Below we have a simple implementation that generates a map to the console using the BSP Algorithm 
with a bit of added padding.
```kotlin
DungeonKit.create(dimension = 50.dim, tileMap = SimpleCharTileMap) // Create the base dungeon
    .steps(BinarySplit, Trim(2)) // Generate a binary split partition map and crop it to fit
    .render(ConsoleRenderer) // Render our map out to the console window
```

## Note
This project is still in development, and as such things are likely to continue changing for a while.
Contributions are also welcome!