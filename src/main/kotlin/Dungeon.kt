@file:Suppress("unused")

import generators.BinarySpacePartition
import generators.Generator
import org.w3c.dom.HTMLCanvasElement
import renderers.Renderer
import kotlin.browser.document
import kotlin.browser.window

fun main() = println("Loaded Dungeon Kit!")

@JsName("create")
fun create(
    id    : String,
    height: Int = window.innerHeight,
    width : Int = window.innerWidth
): Dungeon {
    val container = document.getElementById(id)
    if (container !is HTMLCanvasElement)
        throw DungeonException("Referenced element is not a canvas!")
    return Dungeon(height, width)
        .withGenerator(BinarySpacePartition(3, 150))
//        .withRenderer(CanvasPixelRenderer(container))
}

class Dungeon(
    val height: Int,
    val width : Int
) {
    var tiles: TileMap? = null

    fun withGenerator(generator: Generator): Dungeon = also {
        tiles = generator.generate(height, width, 0)
    }

    fun withRenderer(renderer: Renderer): Dungeon = also {
        renderer.render(this, tiles!!)
    }
}