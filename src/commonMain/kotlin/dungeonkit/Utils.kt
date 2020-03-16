package dungeonkit

import dungeonkit.data.Coordinate
import dungeonkit.data.Dimension
import dungeonkit.data.at
import dungeonkit.data.by
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * Utility for easily creating a list out of two objects. Both objects must be
 * of the same type in order to be assigned properly.
 *
 * @receiver    The first element assigned to the list.
 * @param other The second element assigned to the list.
 * @return      The joined [List] of [T] elements.
 */
operator fun <T> T.plus(other: T) =
    listOf(this, other)

/**
 * Utility function to support exponents for [Int] data, since the standard
 * library only provides support for floating-point numbers.
 *
 * @receiver    The base value.
 * @param other The exponent value.
 * @return      The fully multiplied [Int].
 */
fun Int.pow(other: Int) =
    toDouble().pow(other)

/**
 * @property sq Returns the square of a given [Int].
 */
val Int.sq: Double
    get() = pow(2)

/**
 * @property pos Converts this number into a [Coordinate] at an equal x/y point.
 */
val Int.pos: Coordinate
    get() = this at this

/**
 * @property dim Converts this number into a square [Dimension] of the given size.
 */
val Int.dim: Dimension
    get() = this by this

fun <T : Number> minClamp(a: T, b: T, upper: Int) =
    min(a.toInt(), b.toInt()).coerceIn(0 until upper)

fun <T : Number> maxClamp(a: T, b: T, upper: Int) =
    max(a.toInt(), b.toInt()).coerceIn(0 until upper)


fun Boolean.toInt() =
    if (this) 1 else 0

fun Int.toBoolean() =
    this > 0