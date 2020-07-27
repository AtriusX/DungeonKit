package dungeonkit.data.steps.modifiers

import dungeonkit.data.Grid
import dungeonkit.data.Room
import dungeonkit.data.steps.Step
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

/**
 * A simple generator that applies an exit to the given dungeon floor. This keeps
 * things modularized away from other scripts so this doesn't need to generate over
 * top of other generators directly.
 *
 * @property exit The name of the exit tile to apply to the grid.
 * @constructor   Constructs a new exit generator.
 */
class Exit(
    private val exit: String = "exit"
) : Step, RoomModifier {
    override val status: String
        get() = "Dropping exit..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>) = map.also {
        map[map.random().key] = tileMap[exit].tile
    }

    override fun modify(map: Grid<Tile>, tileMap: TileMap<*>, rooms: List<Room>) =
        process(rooms.random().tiles, tileMap)
}