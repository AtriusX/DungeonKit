package dungeonkit.data.tiles

/**
 * A collection of default tiles that can be used within dungeons. These are used within
 * the provided generators for the library.
 */
object Tiles {
    val WALL  = SimpleTile("Wall", true)
    val FLOOR = SimpleTile("Floor", false)
    val EXIT  = SimpleTile("Exit", false)
}