package dungeonkit.data.tiles

/**
 * A simple [Tile] implementation that describes a plain, boring tile.
 *
 * @property name  The name of this tile.
 * @property solid If the tile is solid or not, used for describing walls.
 * @constructor    Creates a new [SimpleTile].
 */
data class SimpleTile(
    override val name : String,
    override val solid: Boolean
) : Tile
