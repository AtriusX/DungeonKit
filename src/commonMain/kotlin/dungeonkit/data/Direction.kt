package dungeonkit.data

import dungeonkit.DungeonKit

/**
 * A representation of the four main cardinal directions.
 */
enum class Direction(val rel: Coordinate) {

    /**
     * The northwestern coordinate mask, adding this to another coordinate retrieves the
     * relative northwest [Coordinate].
     */
    NORTHWEST(-1 at -1),

    /**
     * The northern coordinate mask, adding this to another coordinate retrieves the
     * relative north [Coordinate].
     */
    NORTH(0 at -1),

    /**
     * The northeastern coordinate mask, adding this to another coordinate retrieves the
     * relative northeast [Coordinate].
     */
    NORTHEAST(1 at -1),

    /**
     * The eastern coordinate mask, adding this to another coordinate retrieves the
     * relative east [Coordinate].
     */
    EAST(1 at 0),

    /**
     * The southeastern coordinate mask, adding this to another coordinate retrieves the
     * relative southeast [Coordinate].
     */
    SOUTHEAST(1 at 1),

    /**
     * The southern coordinate mask, adding this to another coordinate retrieves the
     * relative south [Coordinate].
     */
    SOUTH(0 at 1),

    /**
     * The southwestern coordinate mask, adding this to another coordinate retrieves the
     * relative southwest [Coordinate].
     */
    SOUTHWEST(-1 at 1),

    /**
     * The western coordinate mask, adding this to another coordinate retrieves the
     * relative west [Coordinate].
     */
    WEST(-1 at 0);

    /**
     * @property opposite Retrieves the direction opposite of this one. Ex. Returns [SOUTH]
     *                    if the current direction is [NORTH].
     */
    val opposite: Direction
        get() = values()[(values().indexOf(this) + 4) % 8]

    companion object {

        /**
         * @property RANDOM Returns a randomly selected direction.
         */
        val RANDOM
            get() = values().random()

        /**
         * @property cardinals An array of all cardinal directions.
         */
        val cardinals =
            arrayOf(NORTH, EAST, SOUTH, WEST)

        /**
         * @property ordinals An array of all ordinal directions.
         */
        val ordinals =
            arrayOf(NORTHWEST, NORTHEAST, SOUTHEAST, SOUTHWEST)

        /**
         * Allows this enum to be referenced as an iterator.
         */
        operator fun iterator(): Iterator<Direction> = values().iterator()
    }
}