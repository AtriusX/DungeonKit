package dungeonkit.data.steps

import dungeonkit.DungeonKit.random
import dungeonkit.Log
import dungeonkit.data.Dimension
import dungeonkit.data.Grid
import dungeonkit.data.Room
import dungeonkit.data.nextDim
import dungeonkit.data.steps.modifiers.Modifier
import dungeonkit.data.steps.modifiers.RoomModifier
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import dungeonkit.dim

/**
 * A Cell-Tree algorithm implementation. This algorithm is designed to generate rooms that
 * are connected together using [paths][Path]. This algorithm will generate layouts similar
 * to [BinarySplit], however the algorithm doesn't use any partition modeling, and rooms
 * size is constrained within a specified range.
 *
 * @property minRoomSize      The min permitted room dimensions.
 * @property maxRoomSize      The max permitted room dimensions.
 * @property maxRooms         The max number of rooms permitted within this generation step.
 * @property maxRange         The maximum number of steps a path can take at any given time.
 * @property randomness       The amount of path randomness allowed in the generation of the
 *                            dungeon's layout.
 * @property allowRoomOverlap Determines whether or not the generator is allowed to place rooms
 *                            where a tile has already been placed.
 * @property floor            The name of the floor tile used for this generator. All tiles
 *                            generated in this generator will use this tile.
 * @property modifiers        The modifiers that can be applied to this class, currently support
 *                            only exists for [RoomModifier] instances.
 * @constructor               Creates a cell-tree generator.
 */
open class CellTree(
    private         val minRoomSize     : Dimension = 5.dim,
    private         val maxRoomSize     : Dimension = 11.dim,
    private         val maxRooms        : Int       = 10,
    private         val maxRange        : Int       = 10,
    private         val randomness      : Double    = 0.0,
    private         val allowRoomOverlap: Boolean   = false,
    private         val floor           : String    = "floor",
    override vararg val modifiers       : Modifier
) : Step, ModifiableStep {
    override val status: String
        get() = "Generating cell tree..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>) = map.also {
        val floor = tileMap[floor].tile
        val rooms = arrayListOf<Room>()
        var count = 0
        while (count < maxRooms) {
            // Calculate the room size and position
            val size = random.nextDim(minRoomSize, maxRoomSize)
            val pos  = (map.area - size).random(padding = 1)
            val room = Room(pos, size, floor)
            // Make sure the rooms are allowed to overlap, check for collisions if they aren't
            if (allowRoomOverlap || !rooms.any { room in it }) {
                map += room.tiles
                // Connect the previous room to the current one
                if (rooms.isNotEmpty()) Path(
                    room.center, rooms.last().center, maxRange, randomness, floor.name
                ).process(map, tileMap)
                rooms.add(room)
                count++
            }
        }
        // Apply modifiers
        for (m in modifiers) {
            when (m) {
                is RoomModifier -> m.modify(map, tileMap, rooms)
                else            -> Log.warn("${m::class.simpleName} is not supported by this generator!")
            }
        }
    }

    companion object : CellTree()
}