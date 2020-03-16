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
open class Denoise internal constructor(
    private val threshold       : Int     = 3,
    private val primary         : String  = "floor"
) : Step {

    /**
     * @constructor Allows for the setting of the injection of an alternate primary
     *              tile. Constructors are also provided for each of the alternate
     *              denoising presets.
     */
    constructor(primary: String) : this(3, primary)

    override val status: String
        get() = "Denoising..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        val default = tileMap.default.tile
        val primary = tileMap[primary].tile
        val empties = HashSet<Coordinate>()

        // Loop over all directions for all tiles on the map
        for (d in Direction.cardinals) {
            // Add all default tiles to the set that border non-default tiles
            empties += map
                .map    { it.key.relative(d) }
                .filter {
                    it in map && map[it.x, it.y] == default
                }
        }
        // Remove any tiles that border other default tiles

        empties.removeAll {
            it in map && neighbors(map, it) <= threshold && map[it.x, it.y] == default
        }

        // Replace all single tiles with floor tiles
        empties.forEach { map[it.x, it.y] = primary }
        return map
    }

    private fun neighbors(map: Grid<Tile>, coordinate: Coordinate): Int {
        var count = 0
        for (d in Direction.cardinals) {
            val (x, y) = coordinate
            val rel    = coordinate.relative(d)
            if (map[x, y] != map[rel.x, rel.y])
                count++
        }
        return count
    }

    /**
     * Provides a default implementation for us to use.
     */
    companion object : Denoise()
}

/**
 * An extremely limited denoising preset which has the effect of creating
 * very busy rooms and caverns; the opposite intent of the algorithm.
 */
open class Mangle(primary: String = "floor") : Denoise(4, primary) {
    companion object : Mangle()
}

/**
 * An alternate denoising preset which further smooths out sections
 * of the grid. More denoising might be required to remove new islands
 * that pop up.
 */
open class Smoothing(primary: String = "floor") : Denoise(2, primary) {
    companion object : Smoothing()
}

/**
 * An alternate denoising preset which smooths further beyond the [Smoothing]
 * preset. More denoising might be required to remove new islands that pop up.
 */
open class SuperSmoothing(primary: String = "floor") : Denoise(1, primary) {
    companion object : SuperSmoothing()
}

/**
 * An alternate denoising preset which opens up massive caverns throughout
 * the grid structure.
 */
open class Carve(primary: String = "floor") : Denoise(0, primary) {
    companion object : Carve()
}