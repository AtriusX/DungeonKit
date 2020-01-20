package dungeonkit.data.tiles

/**
 * Represents tile data. Can be used in conjunction with a [dungeonkit.data.Grid] to build
 * abstract dungeon map representations.
 */
interface Tile {

    /**
     * @property name Gets the name of the tile.
     */
    val name: String
        get() = "NONE"

    /**
     * @property solid Describes if the tile is solid or not. Typically will be used
     *                 for describing walls.
     */
    val solid: Boolean
        get() = true
}
