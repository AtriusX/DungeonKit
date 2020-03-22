package dungeonkit.data.steps

import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

class CheckerboardGenerator(
    private val checkerTile: String = "floor",
    private val invert     : Boolean = false
) : Step {

    override val status: String
        get() = "Creating checkerboard..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>) = map.also {

        for(y in 0 until map.area.h) {
            for(x in (if(y % 2 == 0 != invert) 0 else 1) until map.area.w step 2) {
                map[x,y] = tileMap[checkerTile].tile
            }
        }
    }
}