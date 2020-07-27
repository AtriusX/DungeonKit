package dungeonkit.data

import dungeonkit.sq
import kotlin.math.sqrt

/**
 * Represents a coordinate in space. These are used internally for [Grid] objects
 * to allow for easy representation of data.
 *
 * @property x  The (x) position of the coordinate (spans from left to right).
 * @property y  The (y) position of the coordinate (spans from top to bottom).
 * @constructor Creates a new coordinate at the specified position.
 */
data class Coordinate(
    var x: Int,
    var y: Int
) {

    /**
     * @property relatives Gets a list of valid relative coordinates for this position.
     */
    val relatives: List<Coordinate>
        get() = arrayOf(
            relative(Direction.NORTH), relative(Direction.EAST),
            relative(Direction.SOUTH), relative(Direction.WEST)
        ).filter { x >= 0 || y >= 0 }

    /**
     * Converts this coordinate to a [Dimension] of [x] width and [y] height.
     */
    fun toDim() = x by y

    /**
     * Gets the distance between two coordinates.
     *
     * @param other The coordinate to get the distance from.
     * @return      The distance between this and the provided coordinate.
     */
    infix fun distance(other: Coordinate) = sqrt(
        (x - other.x).sq + (y - other.y).sq
    )

    /**
     * Moves this coordinate in the specified direction.
     *
     * @param direction The direction to move in.
     * @return          This coordinate after moving.
     */
    infix fun move(direction: Direction) = move(direction, 1)

    /**
     * Moves this coordinate in the specified direction by the provided number
     * of spaces.
     *
     * @param direction The direction to move in.
     * @param spaces    The number of spaces to move.
     * @return          This coordinate after moving.
     */
    fun move(direction: Direction, spaces: Int) = also {
        x += (direction.rel.x * spaces); y += (direction.rel.y * spaces)
    }

    /**
     * Creates a coordinate relative to this one in the specified direction.
     *
     * @param direction The direction relative to this coordinate.
     * @return          The new coordinate.
     */
    infix fun relative(direction: Direction) = relative(direction, 1)

    /**
     * Creates a coordinate relative to this one in the specified direction at the
     * provided number of spaces.
     *
     * @param d      The direction relative to this coordinate.
     * @param spaces The number of spaces relative to this coordinate.
     * @return       The new coordinate.
     */
    fun relative(d: Direction, spaces: Int) = Coordinate(
        x + d.rel.x * spaces, y + d.rel.y * spaces
    )

    /**
     * Compounds the values of the coordinates into a new larger value. This can be used
     * to add a base value to the coordinate.
     *
     * @param other The other coordinate to compound.
     * @return      A new coordinate at the compounded value.
     */
    operator fun plus(other: Coordinate) = plus(other.x by other.y)

    /**
     * Compounds a dimension into this coordinate. This can be used to find the coordinate for
     * the second-prime (bottom-right) corner in a dimension.
     *
     * @param dimension The dimension to compound.
     * @return          A new coordinate at the compounded value.
     */
    operator fun plus(dimension: Dimension) = Coordinate(
        x + dimension.w, y + dimension.h
    )

    /**
     * Deducts the value of the other coordinate from this one into a new coordinate. This can be used
     * to remove base values from the coordinate.
     *
     * @param other The coordinate to deduct.
     * @return      A new coordinate at the deducted value.
     */
    operator fun minus(other: Coordinate) = Coordinate(
        x - other.x, y - other.y
    )

    /**
     * Determines the direction you'd need to travel to reach the [other] [Coordinate] specified.
     * This can be helpful for providing spacial awareness to generator steps and other objects.
     *
     * @param other The coordinate we are finding the relative direction for.
     * @return      A [Direction] relative to this and [other], or null if the [other] coordinate
     *              shares the same position as this one.
     */
    infix fun directionTo(other: Coordinate): Direction? {
        val lon = (x - other.x).coerceIn(-1..1)
        val lat = (y - other.y).coerceIn(-1..1)
        return Direction.values().find { (x, y) ->
            x == lon && y == lat
        }
    }
}

/**
 * Helper function for easily creating coordinates at a specific position.
 *
 * @receiver    The (x) position of this coordinate.
 * @param other The (y) position of this coordinate.
 * @return      A new coordinate at the specified position.
 */
infix fun Int.at(other: Int) = Coordinate(this, other)
