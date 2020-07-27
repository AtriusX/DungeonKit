package dungeonkit.renderer

import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.SimpleColorTileMap
import kotlinx.browser.document
import org.w3c.dom.get
import org.w3c.dom.CanvasRenderingContext2D as Context
import org.w3c.dom.HTMLCanvasElement as Canvas

/**
 * This renderer that renders the dungeon [Grid] object to the [document]. This [PermissiveRenderer]
 * only exists for use within the Kotlin/JS, Javascript and Typescript ecosystems.
 *
 * @param    canvasId   The node id used to retrieve the [Canvas] element.
 * @property autoResize Whether or not to resize the canvas on render.
 * @constructor         Constructs a new canvas renderer.
 *
 * @throws IllegalStateException If the document doesn't provide a canvas element, the
 *         provided id doesn't exist, or the requested element isn't a canvas.
 */
class CanvasRenderer(
                canvasId  : String? = null,
    private val autoResize: Boolean = false
) : Renderer<SimpleColorTileMap> {
    private val canvas : Canvas
    private val context: Context

    init {
        // Get the element from the document
        val element = document.run {
            if (canvasId != null) getElementById(canvasId)
            else                  getElementsByClassName("canvas")[0]
        }
        // Ensure the element exists and it's a canvas, otherwise throw an error
        if (element != undefined && element is Canvas) element.run {
            context = getContext("2d") as Context
            canvas  = this
        } else throw IllegalStateException("Couldn't find a canvas element!")
    }

    /**
     * Renders the map to a [Canvas] element. Currently this implementation only
     * supports [SimpleColorTileMap], though later may support an image tile binding.
     */
    override fun render(map: Grid<Tile>, tileMap: SimpleColorTileMap) {
        if (autoResize) canvas.apply {
            width  = map.area.w; height = map.area.h
            context.clearRect(0.0, 0.0, width.toDouble(), height.toDouble())
        }
        // Render each tile to the canvas
        for ((pos, tile) in map) {
            context.fillStyle = tileMap[tile].data
            context.fillRect(pos.x.toDouble(), pos.y.toDouble(), 1.0, 1.0)
        }
    }
}