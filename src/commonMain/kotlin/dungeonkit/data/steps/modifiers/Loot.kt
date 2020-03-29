package dungeonkit.data.steps.modifiers

import dungeonkit.DungeonKit.random
import dungeonkit.data.Coordinate
import dungeonkit.data.Grid
import dungeonkit.data.Room
import dungeonkit.data.steps.Step
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import kotlin.math.max

/**
 * Generates loot across a dungeon using a loot table. The items can be generated and
 * set either based on room and later will support generation over the entire map.
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
) : Step, RoomModifier {
    override val status: String
        get() = "Looting rooms..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>) = map.also {
        val count = random.nextInt(maxLoot)
        dropLoot(map, tileMap, Array(count) {
            // Select items from the available tiles
            map.shuffled().random().key
        }.toList())
    }

    override fun modify(map: Grid<Tile>, tileMap: TileMap<*>, rooms: List<Room>) = map.also {
        // Get the coordinates for each room
        rooms.flatMap { r ->
            // Convert the number of items per room to a concrete representation
            val count = r.area.area % max(maxLoot, 1)
            Array(count) { r.pos + r.area.random() }.toList()
        }.also {
            dropLoot(map, tileMap, it)
        }
    }

    private fun dropLoot(map: Grid<Tile>, tileMap: TileMap<*>, positions: List<Coordinate>) {
        val items = lootTable.map { tileMap[it].tile }
        // Drop items across the map
        positions.forEach { map[it.x, it.y] = items.random() }
    }

    companion object : Loot()
}
