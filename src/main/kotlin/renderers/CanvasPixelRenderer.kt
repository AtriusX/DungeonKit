package renderers

import Color
import Dungeon
import TileMap
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement

class CanvasPixelRenderer(
    private val canvas: HTMLCanvasElement
) : Renderer {
    private val ctx    = canvas.getContext("2d") as CanvasRenderingContext2D
    private var render = 0

    val foreground = Color[0xFFFFFF]
    override fun render(dungeon: Dungeon, tiles: TileMap) {
//        canvas.height = dungeon.height
//        canvas.width  = dungeon.width
//        val data = ctx.createImageData(dungeon.width.D, dungeon.height.D)
//
//        for (i in 0..data.data.length step 4) {
//            data.data[ i ] = 0xFF.toByte()
//            data.data[i+1] = 0x00.toByte()
//            data.data[i+2] = 0xFF.toByte()
//            data.data[i+3] = 0xFF.toByte()
//        }
        for (y in render until tiles.height)
            for (x in 0 until tiles.width)
                if (tiles[x, y] == 1)
                    ctx.point(x, y, foreground)
//        ctx.putImageData(data, 50.0, 50.0)
    }

//        // This could potentially be converted to an imagedata store,
//        // which is then drawn to the page in one operation
}

fun CanvasRenderingContext2D.point(x: Int, y: Int, c: Color) {
    this.fillStyle = !c
    this.fillRect(x, y, 1, 1)
}

fun CanvasRenderingContext2D.fillRect(x: Int, y: Int, w: Int, h: Int) {
    this.fillRect(x.toDouble(), y.toDouble(), w.toDouble(), h.toDouble())
}
