package dungeonkit.data.steps

import dungeonkit.DungeonKit.random
import dungeonkit.data.Dimension
import dungeonkit.data.Grid
import dungeonkit.data.nextDim
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import dungeonkit.dim

/**
 * A Cell-Tree algorithm implementation. This algorithm is designed to generate rooms that
 * are connected together using [paths][Path]. This algorithm will generate layouts similar
 * to [BinarySplit], however the algorithm doesn't use any partition modeling, and rooms
 * size is constrained within a specified range.
 *
 * @property minRoomSize  The min permitted room dimensions.
 * @property maxRoomSize  The max permitted room dimensions.
 * @property maxRooms     The max number of rooms permitted within this generation step.
 * @property maxDistance  The max number of tiles a new room can be placed away from the
 *                        previously generated room, this is to prevent rooms from being
 *                        thrown around the map completely at random.
 * @property overlayRooms Determines whether or not the generator is allowed to place rooms
 *                        where a tile has already been placed.
 * @constructor           Creates a cell-tree generator.
 */
open class CellTree(
    private val minRoomSize : Dimension = 5.dim,
    private val maxRoomSize : Dimension = 9.dim,
    private val maxRooms    : Int       = 10,
    private val maxDistance : Int       = 10,
    private val overlayRooms: Boolean   = false
) : Step {
    override val status: String
        get() = "Generating cell tree..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>) = map.also {
        val size = random.nextDim(minRoomSize, maxRoomSize)
    }

    companion object : CellTree()
}