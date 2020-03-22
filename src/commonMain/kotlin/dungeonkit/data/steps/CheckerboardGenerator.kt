package dungeonkit.data.steps

import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

/**
 * Generates a checkerboard across the entire [TileMap].
 *
 * @param checkerTile The name of the tile used for generating our
 *                    map's checkerboard.
 * @param invert      Determines whether the checkerboard will start
 *                    on either the odd or even tiles.
 */
class CheckerboardGenerator(
    private val checkerTile: String = "floor",
    private val invert     : Boolean = false
) : Step {
    override val status: String
        get() = "Creating checkerboard..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>) = map.also {
        for(y in 0 until map.area.h) {
            // If the row is even and not inverted begin at the first row index
            for(x in (if (y % 2 == 0 != invert) 0 else 1) until map.area.w step 2) {
                map[x,y] = tileMap[checkerTile].tile
            }
        }
    }
}
