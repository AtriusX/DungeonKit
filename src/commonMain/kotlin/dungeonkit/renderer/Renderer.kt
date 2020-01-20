package dungeonkit.renderer

import dungeonkit.DungeonProcess
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

/**
 * This interface describes classes that are responsible for translating dungeon data to
 * a specified output medium. Due to the nature of render targets, these may need to be
 * provided on a platform-to-platform basis; and might not exist in some instances (ex:
 * The JS Canvas API). Classes that implement this interface should *not* also implement
 * [Step][dungeonkit.data.steps.Step] or [ModifiableStep][dungeonkit.data.steps.ModifiableStep].
 * Data passed through this interface is immutable and cannot be returned. Implementers of
 * this interface allow support for a single subset of [TileMaps][TileMap] in order to
 * preserve the map's type.
 *
 * @see PermissiveRenderer For an abstraction that allows for support of multiple
 *      [TileMap] subsets.
 *
 * @param    T   The supertype of the [TileMap] used for the base.
 * @property map The supported [PermissiveRenderer] instance.
 * @constructor  Constructs a new strict renderer, supporting only one type.
 */
interface Renderer<T : TileMap<*>> : DungeonProcess {

    override val status: String
        get() = "Rendering..."

    /**
     * Renders the [map] using the provided [tileMap] bindings. Access to the
     * [TileMaps][TileMap] type is preserved due to the scoped nature of this
     * class.
     *
     * @param map     The map data to render for this target.
     * @param tileMap The [TileMap] binding to use for rendering the map data.
     */
    fun render(map: Grid<Tile>, tileMap: T)
}