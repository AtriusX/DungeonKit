package dungeonkit.data.steps

import dungeonkit.data.Coordinate
import dungeonkit.data.Direction
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

/**
 * A simple denoising algorithm that removes any default tiles that are surrounded
 * completely by non-default tiles. This enables maps to have more open space by
 * clearing out areas that are littered with single-piece walls.
 */
open class Denoise : Step {

    override val status: String
        get() = "Denoising..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        val default = tileMap.default.tile
        val primary = tileMap.primary.tile
        val empties = HashSet<Coordinate>()
        // Loop over all directions for all tiles on the map
        for (d in Direction) {
            // Add all default tiles to the set that border non-default tiles
            empties += map
                .map    { it.key.relative(d) }
                .filter {
                    it in map && map[it.x, it.y] == default
                }
        }
        // Remove any tiles that border other default tiles
        for (d in Direction) {
            empties.removeAll {
                it.relative(d).run {
                    it in map && map[x, y] == default
                }
            }
        }
        // Replace all single tiles with floor tiles
        empties.forEach { map[it.x, it.y] = primary }
        return map
    }

    /**
     * Provides a default implementation for us to use.
     */
    companion object : Denoise()
}