package dungeonkit.data.steps

import dungeonkit.data.Dimension
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

/**
 * Runs a block of code on the existing map. Unlike [Eval] this block is only executed
 * once and doesn't return a type. The map can be modified using the block but must be
 * done so directly.
 *
 * @property block The function to execute on the entire map.
 * @constructor    Runs a function on the entire map.
 */
class Run (
    private val block: Grid<Tile>.(dim: Dimension) -> Unit
) : Step {
    override val status: String
        get() = "Running..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> = map.also {
        it.block(map.area)
    }
}