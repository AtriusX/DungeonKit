package dungeonkit.data.tiles.binding

import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.Tiles

typealias Color = Int

/**
 * A simple tile binding to convert tiles into colors. This can be used in the context of
 * drawable outputs such as the Javascript Canvas API.
 */
object SimpleColorTileMap : TileMap<Color> {

    override val tiles: Set<TileBinding<Tile, Color>> = setOf(
        Tiles.WALL  bind 0x000000,
        Tiles.FLOOR bind 0xDDDDDD,
        Tiles.EXIT  bind 0xAAAAAA
    )

    override val default: TileBinding<Tile, Color> = this["wall"]
}