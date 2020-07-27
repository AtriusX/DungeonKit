package dungeonkit.data

import kotlin.collections.Map.Entry

/**
 * An implementation of a 2D Grid. This class includes support for generics however
 * it's typically used as the base for laying out [Dungeon][dungeonkit.Dungeon] tiles. This
 * is also the base used in map generation. This collection only stores the values
 * that matter; any indexes not explicitly assigned will return the default value.
 *
 * @param    T       The data type held by this collection.
 * @property area    The [Dimension] or width and height of the grid.
 * @property default The default parameter used in this grid. This also determines
 *                   the grid type.
 * @constructor      Creates a new grid of the given size with a default value.
 */
open class Grid<T>(
            val area   : Dimension,
    private val default: T
): Iterable<Entry<Coordinate, T>> {

    var data = HashMap<Coordinate, T>()

    /**
     * Checks if the grid is currently empty.
     *
     * @return True if the grid has no explicitly set tiles.
     */
    fun isEmpty() = data.isEmpty()

    /**
     * Creates a carbon-copy of this grid and it's data.
     *
     * @return A copy of this grid
     */
    fun copy() = Grid(area, default).also {
        it.data(HashMap(data))
    }

    private fun data(data: HashMap<Coordinate, T>) {
        this.data = data
    }

    fun random() =
        data.entries.random()


    fun remove(vararg coordinates: Coordinate) {
        for (pos in coordinates) data.remove(pos)
    }

    /**
     * Retrieves a value from the grid. If a value isn't stored in the grid it
     * will return the default value.
     *
     * @param x The (x) coordinate of the data.
     * @param y The (y) coordinate of the data.
     * @return  The data stored at the specified index.
     *
     * @throws IndexOutOfBoundsException If the requested index is outside the
     *         dimensions of the grid.
     */
    open operator fun get(x: Int, y: Int) = get(x at y)

    /**
     * Retrieves a value from the grid. If a value isn't stored in the grid it
     * will return the default value.
     *
     * @param coordinate The x/y coordinate pair of the data.
     * @return           The data stored at the specified index.
     *
     * @throws IndexOutOfBoundsException If the requested index is outside the
     *         dimensions of the grid.
     */
    open operator fun get(coordinate: Coordinate) = if (coordinate !in this)
        throw IndexOutOfBoundsException() else data[coordinate] ?: default

    /**
     * Sets a value at the specified point in the grid.
     *
     * @param x     The (x) coordinate to set at.
     * @param y     The (y) coordinate to set at.
     * @param value The new value set at the coordinate.
     *
     * @throws IndexOutOfBoundsException If the requested index is outside the
     *         dimensions of this grid.
     */
    open operator fun set(x: Int, y: Int, value: T) {
        this[x at y] = value
    }

    /**
     * Sets a value at the specified point in the grid.
     *
     * @param coordinate The x/y coordinate pair to set at.
     * @param value      The new value set at the coordinate.
     *
     * @throws IndexOutOfBoundsException If the requested index is outside the
     *         dimensions of this grid.
     */
    open operator fun set(coordinate: Coordinate, value: T) = if (coordinate !in this)
        throw IndexOutOfBoundsException() else data[coordinate] = value

    /**
     * Adds the contents of another grid to this one.
     *
     * @param other The grid to append the contents for.
     *
     * @throws IndexOutOfBoundsException If some or all of the data to be appended
     *         falls outside of this grid's bounds.
     */
    operator fun plusAssign(other: Grid<T>) = if (other.data.keys.all(this::contains))
        data.plusAssign(other.data) else throw IndexOutOfBoundsException()

    /**
     * Checks if a coordinate fits within the bounds of this grid.
     *
     * @param coordinate The [Coordinate] to check bounds for.
     * @return           True if the coordinate fits within the bounds of this grid.
     */
    operator fun contains(coordinate: Coordinate) = coordinate.run {
        x in 0 until area.w && y in 0 until area.h
    }

    /**
     * This allows for stream-style transformations to be conducted on this collection.
     *
     * @return An iterator of all entries within this grid.
     */
    override fun iterator() = data.entries.iterator()
}
