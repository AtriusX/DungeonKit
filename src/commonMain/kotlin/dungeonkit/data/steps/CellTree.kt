package dungeonkit.data.steps

import dungeonkit.data.Dimension
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import dungeonkit.dim

open class CellTree(
    minRoomSize: Dimension = 5.dim,
    maxRoomSize: Dimension = 9.dim
) : Step {
    private val width  = minRoomSize.w..maxRoomSize.w
    private val height = minRoomSize.h..maxRoomSize.h

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        return map
    }

    companion object : CellTree()
}