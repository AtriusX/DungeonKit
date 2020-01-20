package dungeonkit.data.tiles.binding

import dungeonkit.data.tiles.Tile

/**
 * Represents a [Tile] binding for use in [TileMap] instances. This information is used
 * for telling the rendering portion of the library how to render the stored tile. Creating
 * instances of this class *should be* done using the [bind] method.
 *
 * @property tile The tile for this binding. Used on the generator side of the library.
 * @property data The information related to this tile. Will be used for telling the rendering
 *                engine how to render the bound tile.
 * @constructor   Constructs a new tile binding. Using this directly is not recommended.
 */
data class TileBinding<out T : Tile, D> internal constructor(
    val tile: T, val data: D
)

/**
 * Creates a new [TileBinding] object using the specified data.
 *
 * @receiver   The tile used as the base for this binding.
 * @param data The data to be bound to the base tile.
 * @return     A new tile binding consisting of a [Tile] and [data].
 */
infix fun <T : Tile, D> T.bind(data: D) =
    TileBinding(this, data)
