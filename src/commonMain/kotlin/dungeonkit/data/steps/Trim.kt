package dungeonkit.data.steps

import dungeonkit.data.Grid
import dungeonkit.data.at
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import dungeonkit.dim
import dungeonkit.pos

/**
 * Trims map data to the populated frame of the grid. This is especially useful for
 * making sure any generated data is always the focus and large amounts of unneeded
 * whitespace is ignored.
 *
 * @property padding The amount of [Tiles][Tile] to surround the grid in (prevents
 *                   the map's contents from touching the edge if enabled.)
 * @constructor      Constructs a new grid trimmer.
 */
open class Trim(
    private val padding: Int = 3
) : Step {
    override val status: String
        get() = "Trimming..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        val (w, h) = map.area
        val keys = map.map { it.key }
        // Get the min and max bounds for the map
        val low  = (keys.minByOrNull { it.x }?.x ?: 0) at (keys.minByOrNull { it.y }?.y ?: 0)
        val high = (keys.maxByOrNull { it.x }?.x ?: w) at (keys.maxByOrNull { it.y }?.y ?: h)
        // Calculate the area with padding
        val area = (high - low + 1.pos + (padding * 2).pos).toDim()
        // Create a new grid of the proper size and populate it
        return Grid(area, tileMap.default.tile).also {
            map.forEach { (pos, tile) ->
                val rel = pos - low + padding.dim
                it[rel.x, rel.y] = tile
            }
        }
    }

    /**
     * Provides a default implementation for us to use.
     */
    companion object : Trim()
}