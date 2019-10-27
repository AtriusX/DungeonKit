package generators

import kotlin.math.abs

class Dimension(
    val w: Int,
    val h: Int
)

typealias P = Point

class Point(
    val x: Int,
    val y: Int
) {
    operator fun rangeTo(p: Point): Line =
        Line(this, p)

    operator fun times(p: Point): Dimension =
        Dimension(abs(this.x - p.x), abs(this.y - p.y))

    companion object {
        operator fun get(x: Int, y: Int) =
            Point(x, y)
    }
}

class Line(
    val a: Point,
    val b: Point
) {
    val dim: Dimension
        get() = Dimension(abs(a.x - b.x), abs(a.y - b.y))
}

val Int.d: Dimension
    get() = this by this

infix fun Int.by(other: Int): Dimension =
    Dimension(this, other)
