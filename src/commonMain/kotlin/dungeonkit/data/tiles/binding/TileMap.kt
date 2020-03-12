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
     * @property tiles The collection of tiles stored in this tilemap. This property should by
     *                 accessed via either of the [get] methods.
     */
    val tiles: Set<TileBinding<Tile, D>>

    /**
     * @property default The default value used in tile maps. It's recommended to use this
     *                   value as the default assignment in [Grid][dungeonkit.data.Grid]
     *                   instances.
     */
    val default: TileBinding<Tile, D>

    /**
     * Retrieves the proper tile mapping for the requested tile.
     *
     * @param tile The [Tile] used for finding the proper binding.
     * @return     The [TileBinding] related to this [Tile].
     */
    operator fun get(tile: Tile) = tiles
        .first { tile == it.tile }

    /**
     * Retrieves the proper tile mapping for the requested name.
     *
     * @param name The name used for finding the proper binding.
     * @return     The [TileBinding] related to this [Tile].
     */
    operator fun get(name: String) = tiles
        .first { name.equals(it.tile.name, true) }
}