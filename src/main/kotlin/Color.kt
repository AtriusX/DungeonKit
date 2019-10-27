class Color(
    val r: Int,
    val g: Int,
    val b: Int,
    val a: Int
) {
    constructor(r: Int, g: Int, b: Int) : this(r, g, b, 1)

    companion object {
        operator fun get(r: Int, g: Int, b: Int): Color =
            Color(r, g, b)

        operator fun get(r: Int, g: Int, b: Int, a: Int): Color =
            Color(r, g, b, a)

        operator fun get(hex: Int): Color =
            if (hex > 0xFFFFFF) {
                throw NumberFormatException(
                    "Improper number format for RGB hex code: $hex (Should be 0xFFFFFF or less)"
                )
            } else {
                val r = ((hex shr 16) and 0xFF)
                val g = ((hex shr  8) and 0xFF)
                val b = ((hex       ) and 0xFF)
                Color(r, g, b)
            }
    }

    operator fun not(): String = "rgba($r, $g, $b, $a)"
}