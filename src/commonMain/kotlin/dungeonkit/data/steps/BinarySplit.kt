package dungeonkit.data.steps

import dungeonkit.DungeonKit.random
import dungeonkit.data.*
import dungeonkit.data.steps.modifiers.Modifier
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import dungeonkit.plus
import kotlin.math.max
import kotlin.math.min

/**
 * An implementation of the BSP (Binary Split Partition) algorithm. This algorithm on
 * it's own will split the map into partitions and generate rooms within those partitions.
 * The use of [Modifier] classes will be needed for improving the generator's output.
 *
 * @property splitDepth The number of times we split the map's cells (This will result in
 *                      a doubling of cells for each level of depth).
 * @property padding    The amount of padding to apply to each room in the map. This prevents
 *                      rooms from generating too close to the edge of a [Partition].
 * @property modifiers  The [Modifier] classes applied to this generator. These classes have
 *                      access to more data than [Step] classes do.
 * @constructor         Creates a binary split generator.
 *
 * TODO: Room generation is pretty off right now, look into this later
 */
open class BinarySplit(
    private  val splitDepth: Int              = 3,
    private  val padding   : Int              = 5,
    override val modifiers : Array<Modifier>? = null
) : ModifiableStep {
    override val status: String
        get() = "Generating binary partitions..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        partition(splitDepth, listOf(Partition(map.area)))
            .run { map     { it.makeRoom(padding, tileMap["floor"].tile) } }
            .run { forEach { map += it.tiles                             } }
        return map
    }

    /**
     * Recursively partitions a list of partitions into smaller partitions.
     *
     * @param depth      The number of times to split the partitions.
     * @param partitions The list of partitions to split.
     * @return           The list of split partitions.
     */
    private tailrec fun partition(
        depth     : Int,
        partitions: List<Partition>
    ): List<Partition> = if (depth < 1) partitions else partition(
        depth - 1, partitions.flatMap {
            it.split(random.nextBoolean(), random.nextDouble(0.2, 1.0))
        }
    )

    /**
     * Internal representation of a partition within this generator. [Coordinate] and
     * [Dimension] classes are not directly used due to the heavy reliance on  raw values.
     *
     * @property x  The (x) coordinate of the partition.
     * @property y  The (y) coordinate of the partition.
     * @property w  The width of the partition.
     * @property h  The height of the partition.
     * @constructor Creates a new partition at the given position and size.
     */
    private inner class Partition(
        private val x: Int, private val y: Int,
        private val w: Int, private val h: Int
    ) {

        /**
         * Creates a simple partition residing over the entirety of a [Dimension].
         *
         * @property dimension The size of the partition.
         * @constructor        Creates a new grid matching the dimension's size.
         */
        constructor(dimension: Dimension) : this(0, 0, dimension.w, dimension.h)

        /**
         * Gets the x/y coordinate pair as a [Coordinate] object.
         */
        private val position: Coordinate
            get() = x at y

        /**
         * Gets the height and width as a [Dimension] object.
         */
        private val dimension: Dimension
            get() = w by h

        /**
         * Splits the partition into smaller ones. The split is center-biased and can be reigned in
         * or out using the [space] parameter.
         *
         * @param vertical Determines if this split will be done vertically or horizontally.
         * @param space    Determines how far from the center the split is permitted to go.
         *                 Can be anywhere from 0 (no range) to 1 (full range).
         * @return         The fully split partition.
         */
        fun split(vertical: Boolean, space: Double): List<Partition> {
            // Gets the split position, this position is center-biased
            fun pos(len: Int) = with(len / 2) {
                val bound = (this * space).toInt() / 2
                this + if (bound == 0) 0 else random.nextInt(min(-bound, bound), max(-bound, bound))
            }
            // Split the partition in two depending on direction
            return if (vertical) with(pos(h)) { Partition(x, y, w, this) + Partition(x, y + this, w, h - this) }
            else                 with(pos(w)) { Partition(x, y, this, h) + Partition(x + this, y, w - this, h) }
        }

        /**
         * Makes a room within the partition, with a specified amount of padding.
         *
         * @param pad The amount of padding to apply to the room.
         * @return    The generated room.
         */
        fun makeRoom(pad: Int, tile: Tile): Room {
            // Get the next random dimension for this partition, padding included
            val area = random.nextDim(dimension, pad)
            // Get a coordinate in the top-left quadrant of the partition and create a room
            return Room(position + (dimension - area).random(), area, tile)
        }
    }

    /**
     * Provides a default implementation for us to use.
     */
    companion object : BinarySplit()
}
