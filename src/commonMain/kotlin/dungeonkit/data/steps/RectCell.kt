package dungeonkit.data.steps

import dungeonkit.data.Dimension
import dungeonkit.data.Grid
import dungeonkit.data.Room
import dungeonkit.data.at
import dungeonkit.data.steps.modifiers.Modifier
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import dungeonkit.dim
import kotlin.random.Random

open class RectCell(
    private         val cellSize       : Dimension = 5.dim,
    private         val roomCount      : IntRange  = 6..10,
    private         val hallwayDistance: Int       = 1,
    private         val floorTile      : String    = "floor",
    override vararg val modifiers      : Modifier
) : ModifiableStep {

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        if (map.area.h < cellSize.h || map.area.w < cellSize.w)
            throw IllegalStateException("Cannot generate cell grid in map: Too small")
        val viableArea    = map.area - cellSize
        val tile          = tileMap[floorTile].tile
        val rooms         = mutableListOf(Room(viableArea.random(), cellSize, tile))
        val roomsToCreate = roomCount.random()
        // Create the decided room amount
        while (rooms.size < roomsToCreate) {
            val room  = rooms.random()
            val index = Random.nextInt(4)
            // Calculate the offset coordinate vector for the generated room
            val offset = if (index % 2 == 0) (index - 2) at 0 else 0 at (index - 1)
            val pos    = room.pos + (offset * cellSize + hallwayDistance)
            if (pos !in viableArea)
                continue
            val newRoom = Room(pos, cellSize, tile)
            // Add rooms to running total and apply to map
            rooms += newRoom
            map   += newRoom
            // Generate pathway (Not probably the most efficient way, but it should work)
            Path(room.center, newRoom.center).process(map, tileMap)
        }
        return map
    }

    companion object : RectCell()
}