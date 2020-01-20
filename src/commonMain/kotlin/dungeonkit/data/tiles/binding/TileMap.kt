package dungeonkit.data.tiles.binding

import dungeonkit.data.tiles.Tile

/**
 * This interface represents a simple tilemap containing a pair of [TileBinding] instances.
 * This interface can be implemented directly or extended by other interfaces if more data
 * is required.
 *
 * @param D The data type bound to the [Tile] instances in this tile map. Data types must
 *          be consistent in order to ensure ease-of-use.
 */
interface TileMap<D> {

    /**
     * @property default The default value used in tile maps. It's recommended to use this
     *                   value as the default assignment in [Grid][dungeonkit.data.Grid]
     *                   instances.
     */
    val default: TileBinding<Tile, D>

    /**
     * @property primary The primary tile binding. This should signify the most important
     *                   tile in your dungeons. Typically used for floor tiles.
     */
    val primary: TileBinding<Tile, D>

    /**
     * @property secondary The secondary tile binding. This tile should represent the
     *                     second-most important tile. The use of this binding may
     *                     vary between generators.
     */
    val secondary: TileBinding<Tile, D>

    /**
     * Retrieves the proper tile mapping for the requested tile.
     *
     * @param tile The [Tile] used for finding the proper binding.
     * @return     The [TileBinding] related to this [Tile].
     */
    operator fun get(tile: Tile) = arrayOf(default, primary, secondary)
        .first { it.tile == tile }
}