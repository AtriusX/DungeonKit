package dungeonkit.data

import dungeonkit.DungeonKit.random
import dungeonkit.dim
import kotlin.math.min
import kotlin.random.Random

/**
 * Represents a spacial area of the specified width and height. These are used internally
 * for [Grid] objects to determine their size.
 *
 * @property w  The width of the area.
 * @property h  The height of the area.
 * @constructor Creates a new dimension with the specified width and height.
 */
data class Dimension(
    var w: Int,
    var h: Int
) {

    /**
     * @property center Gets the center [Coordinate] for this dimension.
     */
    val center: Coordinate
        get() = w / 2 at h / 2

    /**
     * @property area Gets the total area in squares for this dimension.
     */
    val area: Int
        get() = w * h

    /**
     * @property largestSquare Returns the square root of the largest possible
     *                         square in this dimension.
     */
    val largestSquare: Int
        get() = min(w, h)

    /**
     * Calculates a random coordinate within the bounds of this dimension.
     *
     * @return The randomly calculated coordinate.
     */
    fun random(padding: Int = 0) =
        (if (w > 0) random.nextInt(w) else 0).coerceIn(padding, w - padding) at
        (if (h > 0) random.nextInt(h) else 0).coerceIn(padding, h - padding)

    /**
     * Compounds the values of the dimensions into a new larger dimension. This can be used to add base
     * values to the dimension.
     *
     * @param other The dimension to compound.
     * @return      A new dimension with the compounded value.
     */
    operator fun plus(other: Dimension) = Dimension(
        w + other.w, h + other.h
    )

    /**
     * Deducts the value of the other dimension from this one into a new dimension. This can be used to remove
     * base values from the dimension.
     *
     * @param other The dimension to deduct.
     * @return      A new dimension with the deducted value.
     */
    operator fun minus(other: Dimension) = Dimension(
        w - other.w, h - other.h
    )

    /**
     * Checks if a coordinate is contained within this dimension's bounds.
     *
     * @param coordinate The coordinate to check.
     * @return           True if the coordinate exists within this dimension's bounds.
     */
    operator fun contains(coordinate: Coordinate) = coordinate.run {
        x in 0 until w && y in 0 until h
    }
}

/**
 * Helper function for easily creating a dimension with the specified bounds.
 *
 * @receiver    The width of the dimension.
 * @param other The height of the dimension.
 * @return      A new dimension with the specified bounds.
 */
infix fun Int.by(other: Int) = Dimension(this, other)

/**
 * Generates a dimension between 0x0 and the upper bound [to].
 *
 * @receiver Our driving [Random] instance.
 * @param to The dimension provided as the upper bound.
 * @return   The randomly sized dimension.
 */
fun Random.nextDim(to: Dimension) =
    nextDim(0.dim, to)

/**
 * Generates a dimension between the lower bound [from] and the
 * upper bound [to].
 *
 * @receiver   Our driving [Random] instance.
 * @param from The dimension provided as the lower bound.
 * @param to   The dimension provided as the upper bound.
 * @return     The randomly sized dimension.
 */
fun Random.nextDim(from: Dimension, to: Dimension) =
    nextInt(from.w, to.w) by nextInt(from.h, to.h)