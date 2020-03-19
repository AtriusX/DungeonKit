package dungeonkit

import dungeonkit.data.Dimension
import dungeonkit.data.Grid
import dungeonkit.data.steps.Step
import dungeonkit.data.tiles.binding.TileMap
import dungeonkit.renderer.PermissiveRenderer
import dungeonkit.renderer.Renderer

/**
 * The containing object that stores all dungeon-related data. These cannot be
 * instanced outside the library directly.
 *
 * @param    T         The [TileMap] type used for this render operation.
 * @property name      The name of this dungeon.
 * @property dimension The width and height of this dungeon.
 * @property tileMap   The [TileMap] used for rendering and generating this dungeon.
 * @constructor        Creates a new [Dungeon] instance (Cannot be instantiated directly).
 */
data class Dungeon<T : TileMap<*>> internal constructor(
    val name     : String,
    val dimension: Dimension,
    val tileMap  : T,
    val logging  : Boolean = true
) {
    private var map = Grid(dimension, tileMap.default.tile)

    /**
     * Provides a series of steps that are immediately processed into new map data.
     * This data can be *overdubbed* not *overwritten* by executing this function again.
     *
     * @param steps The steps to apply to this dungeon's generation pipeline.
     * @return      The dungeon after it's processed all steps.
     */
    fun steps(vararg steps: Step) = also {
        steps.forEach {
            map = it.also{ if (logging) Log.info(it.status) }.process(map, tileMap)
        }
    }

    /**
     * Renders the map as it is currently exists. Rendering engines are provided on a
     * per-platform; per-context basis. Some renderers may exist for multiple targets,
     * however it's entirely dependent on the target medium and necessary libraries.
     *
     * @param renderer The renderer used in this operation.
     *
     * @throws UnsupportedOperationException If this [PermissiveRenderer] doesn't offer support
     *         for the supplied [TileMap].
     */
    fun render(renderer: Renderer<T>) {
        if (renderer is PermissiveRenderer && tileMap !in renderer.supportedMaps)
            throw UnsupportedOperationException(
                "${tileMap::class.simpleName} isn't supported by this renderer!"
            )
        renderer.render(map, tileMap)
    }
}
