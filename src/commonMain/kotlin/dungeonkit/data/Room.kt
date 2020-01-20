package dungeonkit.data

import dungeonkit.data.tiles.Tile
import kotlin.collections.MutableMap.MutableEntry as Entry

/**
 * A representation of a [dungeonkit.Dungeon] room. This effectively functions as
 * a [Grid] with positional data. This allows rooms to be overlaid on top of existing
 * grid data. This class also takes care to explicitly initialize all values within
 * it's bounds.
 *
 * @param coordinate The [Coordinate] of this room corresponding to it's position on
 *                   existing [Grid] data.
 * @param dimension  The width and height of this room.
 * @param floor      The floor [Tile] used for this room.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Room(
    val coordinate: Coordinate,
        dimension : Dimension,
        floor     : Tile
): Grid<Tile>(dimension, floor) {
    init {
        val (w, h) = dimension
        // Ensure the room has space before initializing
        if (w != 0 && h != 0) {
            // Fill in the entire floor explicitly with the floor tile.
            val (begX, begY) = coordinate
            val (endX, endY) = coordinate + dimension
            for (x in begX until endX)
                for (y in begY until endY)
                    this[x, y] = floor
        }
    }

    /**
     * @property tiles Retrieves the tiles for this room. The data is remapped to the
     *                 proper [Coordinate] before returning (as the data is internally
     *                 based at index 0).
     */
    val tiles: Grid<Tile>
        get() = also {
            forEach { (c, _) -> c.x += coordinate.x; c.y += coordinate.y }
        }
    /**
     * @property center The center-point of this room represented as a [Coordinate]
     */
    val center: Coordinate
        get() = area.w / 2 + coordinate.x at
                area.h / 2 + coordinate.y

    /**
     * Retrieves a list of coordinates that edge the room.
     *
     * @return A [List] of border [Coordinate]s.
     */
    fun border() = this.map(Entry<Coordinate, Tile>::key).filter {
        it.x - coordinate.x !in 1 until area.w || it.y - coordinate.y !in 1 until area.h
    }

    /**
     * Retrieves a tile stored at the specified x/y value. Parameters are rebased
     * at zero before retrieval.
     *
     * @param x The (x) coordinate of the [Tile].
     * @param y The (y) coordinate of the [Tile].
     * @return  The [Tile] stored at the specified x/y point.
     */
    override operator fun get(x: Int, y: Int) =
        super.get(x - coordinate.x, y - coordinate.y)

    /**
     * Sets a tile at the specified point in the room. Parameters are rebased
     * at zero before assignment.
     *
     * @param x     The (x) coordinate to set data at.
     * @param y     The (y) coordinate to set data at.
     * @param value The value to set at the specified point.
     */
    override operator fun set(x: Int, y: Int, value: Tile) =
        super.set(x - coordinate.x, y - coordinate.y, value)
}

//operator fun Grid<in Tile>.plusAssign(room: Room) = room.forEach { (pos, tile) ->
//    this[pos.x + room.coordinate.x, pos.y + room.coordinate.y] = tile
//}