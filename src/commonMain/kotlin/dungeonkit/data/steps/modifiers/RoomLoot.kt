package dungeonkit.data.steps.modifiers

import dungeonkit.data.Grid
import dungeonkit.data.Room
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import kotlin.math.min

/**
 * Generates loot across a dungeon using a loot table. The items can be generated and
 * set either based on room and later will support generation over the entire map.
 *
 * TODO: Add support for overall map generation.
 *
 * @property maxLoot   The maximum number of items that can be generated in each room.
 * @property lootTable The names of all items within a loot table. These are used for
 *                     determining which items will be generated and distributed evenly.
 *
 * TODO: Add support for weighting item selection.
 *
 * @constructor        Creates a new Loot generator.
 */
open class Loot(
    private        val maxLoot  : Int = 10,
    private vararg val lootTable: String
) : RoomModifier {
    override val status: String
        get() = "Looting rooms..."

    override fun modify(map: Grid<Tile>, tileMap: TileMap<*>, rooms: List<Room>) = map.also {
        val items     = lootTable.map { tileMap[it].tile }
        // Get the coordinates for each room
        val positions = rooms.flatMap { r ->
            // Convert the number of items per room to a concrete representation
            val count = r.area.area % min(maxLoot, 1)
            Array(count) { r.pos + r.area.random() }.toList()
        }
        // Drop items across the map
        positions.forEach {
            map[it.x, it.y] = items.random()
        }
    }

    companion object : Loot()
}