package dungeonkit.renderer

import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

/**
 * This interface is an extension of the [Renderer] interface which allows for support of
 * multiple [TileMap] sets. A trade-off of this abstraction would be the erasure of the
 * tile map's type, meaning type casts will be necessary to cover all supported bindings.
 *
 * @see Renderer For a strict abstraction that preserves the type of a subset of [TileMap]
 *      bindings.
 */
interface PermissiveRenderer : Renderer<TileMap<*>> {

    /**
     * @property supportedMaps Lists the supported [TileMaps][TileMap] for this renderer.
     *                         It should be expected that any tile map supplied to this
     *                         renderer that doesn't support will throw an exception.
     */
    val supportedMaps: Array<out TileMap<*>>

    /**
     * Renders the input data to the implementers designated render target. It should be
     * restated that renderers *can vary* in support depending on each platform. Renderers
     * are not required to target all supported platforms.
     *
     * @param map     The map data to render for this target.
     * @param tileMap The [TileMap] binding to use for rendering the map data.
     */
    override fun render(map: Grid<Tile>, tileMap: TileMap<*>)
}