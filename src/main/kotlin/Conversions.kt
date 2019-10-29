import generators.Dimension
import generators.by

typealias Percent
        = Double

val Int.d: Dimension
    get() = this by this

val Number.D: Double
    get() = this.toDouble()

/**
 * Simple conversion to convert any number to an Int
 */
val Number.i: Int
    get() = this.toInt()

/**
 * Converts an int to it's percentage representation.
 */
val Number.pc: Percent
    get() = this.i / 100.0
