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
    private         val roomCount      : IntRange  = 6..25,
    private         val hallwayDistance: Int       = 1,
    private         val retries        : Int       = 25,
    private         val floorTile      : String    = "floor",
    override vararg val modifiers      : Modifier
) : ModifiableStep {
    override val status: String
        get() = "Creating cells..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        if (map.area.h < cellSize.h || map.area.w < cellSize.w)
            throw IllegalStateException("Cannot generate cell grid in map: Too small")
        val viableArea    = map.area - cellSize
        val tile          = tileMap[floorTile].tile
        val start         = Room(viableArea.random(), cellSize, tile)
        val rooms         = mutableListOf(start)
        val roomsToCreate = roomCount.random()
        // Create the decided room amount
        var retry = retries
        while (rooms.size < roomsToCreate) {
            if (retry < 1)
                break
            val room  = rooms.random()
            val index = Random.nextInt(4)
            // Calculate the offset coordinate vector for the generated room
            val offset = if (index % 2 == 0) (index - 2) at 0 else 0 at (index - 1)
            // Look into this later, values given seem to come out weird
            val pos    = room.pos + (offset * cellSize + (offset * (hallwayDistance - 3)))
            if (pos !in viableArea || rooms.any { it.pos == pos }) {
                retry--
                continue
            }
            val newRoom = Room(pos, cellSize, tile)
            // Add rooms to running total and apply to map
            rooms += newRoom
            map   += newRoom.tiles
            // Generate pathway (Not probably the most efficient way, but it should work)
            Path(room.center, newRoom.center).process(map, tileMap)
            retry = retries
        }
        // Make sure the starting room is applied to the map
        return map.also { it += start.tiles }
    }

    companion object : RectCell()
}