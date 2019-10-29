import generators.Dimension

typealias Tile = Int

// REVIEW efficiency of processing later, perhaps convert to an
// instruction stack or render in a coroutine of some kind if possible.
class TileMap(
    val height : Int,
    val width  : Int,
    val default: Tile
) {
    private val tiles = Array(height) {
        Array(width) { default }
    }

    operator fun get(x: Int, y: Int): Tile {
        return tiles[y][x]
    }

    operator fun set(x: Int, y: Int, tile: Tile) {
        if (x < width && y < height)
            tiles[y][x] = tile
    }

    operator fun set(x: Int, y: Int, w: Int, h: Int, tile: Tile) {
        for (j in y until y + h)
            for (i in x until x + w)
                set(i, j, tile)
    }

    operator fun set(x: Int, y: Int, d: Dimension, tile: Tile) {
        set(x, y, d.w, d.h, tile)
    }

    fun forEach(block: (x: Int, y: Int) -> Tile) {
        for (y in 0 until height)
            for (x in 0 until width)
                this[x, y] = block(x, y)
    }

    companion object {
        operator fun get(height: Int, width: Int): TileMap =
            TileMap(height, width, 0)
    }
}