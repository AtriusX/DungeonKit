package dungeonkit.data.steps

import dungeonkit.data.Coordinate
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

private typealias Method =
        (region: RegionDetect.Region, map: Grid<Tile>, tileMap: TileMap<*>) -> Unit

open class RegionDetect(
    private inline val method: Method = { _, _, _ -> }
) : Step {

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        val clone = map.copy()
        val regions = mutableListOf<Region>()
        while (!clone.isEmpty()) {
            // Pick a random position and scan the surrounding area
            Scanner(clone.random().key).scan(map, tileMap).also {
                // Remove all the tiles in the region from the clone and add the region to the region list
                clone.remove(*it.positions.toTypedArray())
                if (regions.isNotEmpty()) {
                    regions.last().apply {
                        next = it
                        it.previous = this
                    }
                }
                regions.add(it)
            }
        }

        for (region in regions)
            method(region, map, tileMap)
        return map
    }

    private inner class Scanner(
        val position: Coordinate
    ) {
        fun scan(map: Grid<Tile>, tileMap: TileMap<*>): Region {
            // Tracks all positions
            val positions = mutableSetOf(position)
            // Tracks current iteration set
            var current   = mutableSetOf(position)
            var next: MutableSet<Coordinate>
            while (current.isNotEmpty()) {
                next = mutableSetOf()
                for (pos in current) {
                    for (rel in pos.relatives) {
                        // Add the tile if it isn't the default and hasn't already been visited
                        if (map[rel] != tileMap.default.tile && rel !in positions) {
                            positions.add(rel)
                            next.add(rel)
                        }
                    }
                }
                current = next
            }
            return Region(positions)
        }

    }

    data class Region(
        val positions: Set<Coordinate>,
        var previous : Region? = null,
        var next     : Region? = null
    )

    companion object : RegionDetect()
}