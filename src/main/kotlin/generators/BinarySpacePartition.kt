package generators

import Color
import Tile
import TileMap
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import renderers.fillRect
import kotlin.browser.document
import kotlin.random.Random.Default.nextInt

typealias SplitGroup =
        ArrayList<BinarySpacePartition.Partition>

class BinarySpacePartition(
    private val depth  : Int,
    private val padding: Int
) : Generator {
    override fun generate(height: Int, width: Int, default: Tile): TileMap {
        val tiles  = TileMap(height, width, default)
        val parts  = split(Partition(width, height), depth)
        val canvas = document.getElementById("dungeon") as HTMLCanvasElement
        val ctx    = canvas.getContext("2d") as CanvasRenderingContext2D

        ctx.apply {
            for (p in parts) {
                fillStyle = !Color[nextInt(0xFFFFFF)]
                fillRect(p.x, p.y, p.w, p.h)
//                fillRect(p.centerX - 5, p.centerY - 5, 10, 10)
                console.log(p)
                p.makeRoom(tiles, 8, 20, this)
            }
        }

        return tiles
    }

    private fun split(partition: Partition, levels: Int): SplitGroup =
        partition.split(padding).also {
            if (levels > 0) return it
                .map    { p -> split(p, levels - 1) }
                .reduce { a, b -> a.also { a += b } }
        }

    /**
     * Partitions are 2-dimensional areas that exist inside of a tilemap. The X and Y coordinates are
     * RELATIVE to their position in the tilemap (they do not necessarily exist at point [0, 0].)
     */
    data class Partition(
        val x : Int,
        val y : Int,
        val w : Int,
        val h : Int
    ) {
        val cornerX = x + w
        val cornerY = y + h
        val centerX = x + w / 2
        val centerY = y + h / 2

        constructor(w: Int, h: Int) : this(0, 0, w, h)

        fun split(padding: Int): SplitGroup {
            val axis  = w > h
            val split = paddedSplit(if (axis) w else h, padding)
            return if (axis)
                Partition(x, y, split, h) and Partition(x + split, y, w - split, h)
            else
                Partition(x, y, w, split) and Partition(x, y + split, w, h - split)
        }

        fun makeRoom(tiles: TileMap, padding: Int, minSize: Int, ctx: CanvasRenderingContext2D) = try {
            val x = nextInt(x + padding, centerX - minSize)
            val y = nextInt(y + padding, centerY - minSize)
            val h = 50
            val w = 50

            ctx.fillStyle = !Color[0xFFFFFF]
            ctx.fillRect(x, y, w, h)
            tiles[x, y, w, h] = 1
        } catch (e: IllegalArgumentException) {}

        private fun paddedSplit(input: Int, padding: Int): Int {
            val top = input - padding
            return if (padding > top)
                nextInt(top, padding + 1)
            else
                nextInt(padding, top + 1)
        }

        private infix fun and(partition: Partition): SplitGroup =
            arrayListOf(this, partition)
    }
}