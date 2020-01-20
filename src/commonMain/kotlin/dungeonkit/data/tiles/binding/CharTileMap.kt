package dungeonkit.data.tiles.binding

import dungeonkit.data.tiles.SimpleTile
import dungeonkit.data.tiles.Tiles

/**
 * A simple tile map binding tiles to [Characters][Char]. The primary use-case for
 * this tile map is for text-based renderers like console windows.
 */
open class CharTileMap : TileMap<Char> {

    override val default: TileBinding<SimpleTile, Char> =
        Tiles.WALL bind '#'

    override val primary: TileBinding<SimpleTile, Char> =
        Tiles.FLOOR bind ' '

    override val secondary: TileBinding<SimpleTile, Char> =
        Tiles.EXIT bind '.'
}