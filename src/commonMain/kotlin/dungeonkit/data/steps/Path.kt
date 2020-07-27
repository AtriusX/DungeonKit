package dungeonkit.data.steps

import dungeonkit.DungeonKit.random
import dungeonkit.data.Coordinate
import dungeonkit.data.Direction
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import kotlin.math.max
import kotlin.math.min

/**
 * This system generates a pathway between two points using recursion. A level of randomness
 * can be specified to allow the pathway to make the generated pathway more interesting.
 *
 * @property start      The pathway's start position.
 * @property end        The pathway's end position.
 * @property maxRange   The max distance the walker is allowed to step at any point.
 * @property randomness The amount of randomness applied to this pathway. Increasing this value
 *                      allows for more interesting path generation.
 * @property pathTile   The name of the tile used as the path base. This tile will be retrieved
 *                      from the dungeon's provided [TileMap].
 * @constructor         Constructs a new recursive path generator.
 */
class Path(
    private val start     : Coordinate,
    private val end       : Coordinate,
    private val maxRange  : Int    = 25,
    private val randomness: Double = 0.0,
    private val pathTile  : String = "floor"
) : Step {
    override val status: String
        get() = "Creating path..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>) = map.also {
        val walker = Walker(map, tileMap, start)
        val pos    = walker.position
        while (pos != end && pos !in end.relatives) {
            val direction = Direction.cardinals.run {
                if (random.nextDouble() > randomness) minByOrNull { pos.relative(it) distance end }!! else random()
            }
            if (walker.facing?.opposite == direction)
                continue
            val step = min((1..max(1, maxRange)).random(), pos.distance(end).toInt())
            if (pos.relative(direction, step) in map)
                walker.walk(direction, step)
        }
    }

    private inner class Walker(
        val map     : Grid<Tile>,
        val tileMap : TileMap<*>,
        val position: Coordinate
    ) {
        var facing: Direction? = null
            private set

        tailrec fun walk(
            direction: Direction,
            steps    : Int
        ): Unit = if (steps < 1) facing = direction else {
            val (x, y) = position.move(direction)
            map[x, y]  = tileMap[pathTile].tile
            walk(direction, steps - 1)
        }
    }
}
