package generators

import Color
import Percent
import Tile
import TileMap
import i
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import pc
import renderers.fillRect
import kotlin.browser.document
import kotlin.random.Random.Default.nextInt

typealias SplitGroup
        = ArrayList<BinarySpacePartition.Partition>
typealias Ignored
        = Exception

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
                p.makeRoom(tiles, 10, nextInt(25, 75).pc)
                // Generate hallways
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
        private val cornerX = x + w
        private val cornerY = y + h
        private val centerX = x + w / 2
        private val centerY = y + h / 2

        constructor(w: Int, h: Int) : this(0, 0, w, h)

        fun split(padding: Int): SplitGroup {
            val axis  = w > h
            val split = paddedSplit(if (axis) w else h, padding)
            return if (axis)
                Partition(x, y, split, h) and Partition(x + split, y, w - split, h)
            else
                Partition(x, y, w, split) and Partition(x, y + split, w, h - split)
        }

        fun makeRoom(tiles: TileMap, padding: Int = 10, minSize: Percent = 50.pc): Unit = try {
            if (w * minSize + padding * 2 < w &&
                h * minSize + padding * 2 < h
            ) {
                val x = nextInt(x + padding, centerX)
                val y = nextInt(y + padding, centerY)
                val d = nextInt((w * minSize).i, cornerX - x - padding) by
                        nextInt((h * minSize).i, cornerY - y - padding)
                // Push region to tilemap
                tiles[x, y, d] = 1
            } else Unit
        } catch (e: Ignored) {
            makeRoom(tiles, padding, minSize)
        }

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