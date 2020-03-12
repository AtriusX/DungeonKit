package dungeonkit.data.steps

import dungeonkit.DungeonKit.random
import dungeonkit.data.Coordinate
import dungeonkit.data.Direction
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

/**
 * This system generates a pathway between two points using recursion. A level of randomness
 * can be specified to allow the pathway to make the generated pathway more interesting.
 *
 * @property start      The pathway's start position.
 * @property end        The pathway's end position.
 * @property randomness The amount of randomness applied to this pathway. Increasing this value
 *                      allows for more interesting path generation.
 * @constructor         Constructs a new recursive path generator.
 */
class Path(
    private val start     : Coordinate,
    private val end       : Coordinate,
    private val randomness: Double = 0.0,
    private val pathTile  : String = "floor"
) : Step {
    override val status: String
        get() = "Creating path..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        // Ensure the coordinates are on the map
        if (start !in map || end !in map)
            throw IllegalStateException("Coordinates must be within the map!")
        // Generate the pathway
        return walk(map, tileMap, start, end)
    }

    /**
     * Generates a pathway between two points. This is done recursively and incorporates
     * a specified amount of randomness within the generation.
     *
     * @param map     The map to apply the pathway to.
     * @param tileMap The [TileMap] supplied.
     * @param start   The pathway's start position.
     * @param end     The pathway's end position.
     */
    private tailrec fun walk(
        map  : Grid<Tile>, tileMap: TileMap<*>,
        start: Coordinate, end    : Coordinate
    ): Grid<Tile> {
        map[start.x, start.y] = tileMap[pathTile].tile
        val pos = Direction.values()
            .map    { start relative it }
            .filter { it == end || it in map }
            .minBy  { (it distance end) + random.nextDouble() * randomness }!!
        return if (pos == end) map else walk(map, tileMap, pos, end)
    }
}