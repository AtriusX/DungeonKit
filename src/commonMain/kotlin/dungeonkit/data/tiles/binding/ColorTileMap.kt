package dungeonkit.data.tiles.binding

import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.Tiles

typealias Color = Int

/**
 * A simple tile binding to convert tiles into colors. This can be used in the context of
 * drawable outputs such as the Javascript Canvas API.
 */
open class ColorTileMap : TileMap<Color> {

    override val default: TileBinding<Tile, Color>
        get() = Tiles.WALL bind  0xFFFFFF

    override val primary: TileBinding<Tile, Color>
        get() = Tiles.FLOOR bind 0x000000

    override val secondary: TileBinding<Tile, Color>
        get() = TODO()
}