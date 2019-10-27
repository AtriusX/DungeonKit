package renderers

import Color
import Dungeon
import TileMap
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

class CanvasPixelRenderer(
    private val canvas: HTMLCanvasElement
) : Renderer {
    private val ctx = canvas.getContext("2d") as CanvasRenderingContext2D

    override fun render(dungeon: Dungeon, tiles: TileMap) {
        canvas.height = dungeon.height
        canvas.width  = dungeon.width

//        val background = Color[0x0B55A7]
        val foreground = Color[0xFF66FF]

        for (y in 0 until tiles.height)
            for (x in 0 until tiles.width) {
                ctx.point(x, y, if (tiles[x, y] == 1) foreground else continue)
            }
    }
}

fun CanvasRenderingContext2D.point(x: Int, y: Int, c: Color) {
    this.fillStyle = !c
    this.fillRect(x, y, 1, 1)
}

fun CanvasRenderingContext2D.fillRect(x: Int, y: Int, w: Int, h: Int) {
    this.fillRect(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())
}
