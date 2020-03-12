package dungeonkit.data.tiles.binding

import dungeonkit.data.tiles.SimpleTile
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.Tiles

/**
 * A simple tile map binding tiles to [Characters][Char]. The primary use-case for
 * this tile map is for text-based renderers like console windows.
 */
object SimpleCharTileMap : TileMap<Char> {

    override val tiles: Set<TileBinding<Tile, Char>> = setOf(
        Tiles.WALL  bind '#',
        Tiles.FLOOR bind ' ',
        Tiles.EXIT  bind '.'
    )

    override val default: TileBinding<Tile, Char> = this["wall"]
}