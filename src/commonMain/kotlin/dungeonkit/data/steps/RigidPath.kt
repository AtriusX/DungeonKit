package dungeonkit.data.steps

import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

class RigidPath : Step {
    override val status: String
        get() = "Creating path..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        return map
    }
}