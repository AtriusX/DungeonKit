package dungeonkit.data.steps

import dungeonkit.DungeonKit.random
import dungeonkit.Log
import dungeonkit.data.*
import dungeonkit.data.steps.modifiers.Modifier
import dungeonkit.data.steps.modifiers.RoomModifier
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import dungeonkit.dim
import dungeonkit.plus

/**
 * An implementation of the BSP (Binary Split Partition) algorithm. This algorithm on
 * it's own will split the map into partitions and generate rooms within those partitions.
 *
 * @property depth       The number of times we split the map's cells (This will result in
 *                       a doubling of cells for each level of depth).
 * @property minCellSize The minimum size cells can be when generating partitions in the grid.
 * @property padding     The amount of padding to apply to each room in the map. This prevents
 *                       rooms from generating too close to the edge of a [Partition].
 * @property splitRatio  The max allowable ratio between the width and height of a cell when
 *                       deciding how to split a partition.
 * @property reject      This determines the rejection chance for a cell. If the conditions are
 *                       met for this option, a room will not be generated for the specified cell.
 * @property floor       The name of the [Tile] which will be retrieved from the provided [TileMap].
 *                       This will default to "floor" if no value is substituted.
 * @property modifiers   The modifiers that can be applied to this class, currently support only
 *                       exists for [RoomModifier] instances.
 * @constructor          Creates a binary split generator.
 */
open class BinarySplit(
    private         val depth      : Int    = 4,
    private         val minCellSize: Int    = 10,
    private         val padding    : Int    = 4,
    private         val splitRatio : Double = 1.75,
    private         val reject     : Double = 0.0,
    private         val floor      : String = "floor",
    override vararg val modifiers  : Modifier
) : Step, ModifiableStep {
    override val status: String
        get() = "Generating cells..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>) = map.also {
        val rooms = arrayListOf<Room>()
        // Process the map into partitions and rooms, then store the rooms in the above list
        partition(depth, listOf(Partition(map.area)))
            .map     { if (reject < random.nextDouble()) it.makeRoom(tileMap[floor].tile) else null }
            .forEach { rooms.add(it ?: return@forEach).apply { map += it.tiles }                    }
        // Loop over each room and connect them together
        for (i in 1 until rooms.size) {
            Path(rooms[i - 1].center, rooms[i].center, maxRange = 5, pathTile = floor).process(map, tileMap)
        }
        // Apply modifiers TODO: Find a better way to implement this without code duplication
        for (m in modifiers) {
            when (m) {
                is RoomModifier -> m.modify(map, tileMap, rooms)
                else            -> Log.warn("${m::class.simpleName} is not supported by this generator!")
            }
        }
    }

    private tailrec fun partition(
        depth     : Int,
        partitions: List<Partition>
    ): List<Partition> = if (depth < 1) partitions else partition(
        depth - 1, partitions.flatMap(Partition::split)
    )

    private inner class Partition private constructor(
        val position: Coordinate,
        val size    : Dimension
    ) {
        constructor(size: Dimension) : this(0 at 0, size)

        fun split(): List<Partition> {
            val (x, y)          = position
            val (width, height) = size

            val horizontal = when {
                height > width && width / height >= splitRatio -> false
                width > height && height / width >= splitRatio -> true
                else                                           -> random.nextBoolean()
            }
            val max = (if (horizontal) height else width) - minCellSize
            if (max <= minCellSize)
                return listOf(this)
            val split = random.nextInt(minCellSize, max)
            return if (horizontal)
                Partition(x at y, width by split)  + Partition(x at y + split, width by height - split)
            else
                Partition(x at y, split by height) + Partition(x + split at y, width - split by height)
        }

        fun makeRoom(tile: Tile): Room? {
            // Get the next random dimension for this partition, padding included
            val area = size - padding.dim
            // Further reject the generation if the room is too small
            if (area.largestSquare < padding)
                return null
            // Get a coordinate in the top-left quadrant of the partition and create a room
            return Room(position + (size - area).random(), area, tile)
        }
    }

    companion object : BinarySplit()
}