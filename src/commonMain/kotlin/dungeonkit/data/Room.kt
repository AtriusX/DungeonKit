package dungeonkit.data

import dungeonkit.data.tiles.Tile
import kotlin.collections.MutableMap.MutableEntry as Entry

/**
 * A representation of a [dungeonkit.Dungeon] room. This effectively functions as
 * a [Grid] with positional data. This allows rooms to be overlaid on top of existing
 * grid data. This class also takes care to explicitly initialize all values within
 * it's bounds.
 *
 * @param pos The [Coordinate] of this room corresponding to it's position on
 *                   existing [Grid] data.
 * @param dimension  The width and height of this room.
 * @param floor      The floor [Tile] used for this room.
 */
class Room(
    val pos      : Coordinate,
    val dimension: Dimension,
    floor        : Tile
): Grid<Tile>(dimension, floor) {
    init {
        val (w, h) = dimension
        // Ensure the room has space before initializing
        if (w != 0 && h != 0) {
            // Fill in the entire floor explicitly with the floor tile.
            val (begX, begY) = pos
            val (endX, endY) = pos + dimension
            for (x in begX until endX)
                for (y in begY until endY)
                    this[x, y] = floor
        }
    }

    /**
     * @property top Retrieves the coordinates for the top border of the room.
     */
    val top: List<Coordinate>
        get() = border.filter { it.x == 0 }

    /**
     * @property left Retrieves the coordinates for the left border of the room.
     */
    val left: List<Coordinate>
        get() = border.filter { it.y == 0 }

    /**
     * @property right Retrieves the coordinates for the right border of the room.
     */
    val right: List<Coordinate>
        get() = border.filter { it.x == dimension.w - 1 }

    /**
     * @property bottom Retrieves the coordinates for the bottom border of the room.
     */
    val bottom: List<Coordinate>
        get() = border.filter { it.y == dimension.h - 1 }

    /**
     * @property tiles Retrieves the tiles for this room. The data is remapped to the
     *                 proper [Coordinate] before returning (as the data is internally
     *                 based at index 0).
     */
    val tiles: Grid<Tile>
        get() = also {
            forEach { (c, _) -> c.x += pos.x; c.y += pos.y }
        }

    /**
     * @property center The center-point of this room represented as a [Coordinate]
     */
    val center: Coordinate
        get() = area.w / 2 + pos.x at
                area.h / 2 + pos.y

    /**
     * @property border Retrieves a list of coordinates that edge the room.
     */
    val border: List<Coordinate>
        get() = map(Entry<Coordinate, Tile>::key).filter {
            it.x - pos.x !in 1 until area.w || it.y - pos.y !in 1 until area.h
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
        super.get(x - pos.x, y - pos.y)

    /**
     * Sets a tile at the specified point in the room. Parameters are rebased
     * at zero before assignment.
     *
     * @param x     The (x) coordinate to set data at.
     * @param y     The (y) coordinate to set data at.
     * @param value The value to set at the specified point.
     */
    override operator fun set(x: Int, y: Int, value: Tile) =
        super.set(x - pos.x, y - pos.y, value)

    /**
     * Checks if another room overlaps the bounds of this one. This can be used
     * to prevent rooms from overlaying on top of each other.
     *
     * @param other The room instance to check for overlaps.
     * @return      True if the rooms overlap and false otherwise.
     */
    operator fun contains(other: Room): Boolean {
        val (x, y)       = pos
        val (endX, endY) = pos + dimension
        for (dx in x until endX) for (dy in y until endY)
            if (other hasPosition (dx at dy)) return true
        return false
    }

    private infix fun hasPosition(coordinate: Coordinate): Boolean {
        val (x, y)       = pos
        val (endX, endY) = pos + dimension
        return coordinate.x in x..endX && coordinate.y in y..endY
    }
}
