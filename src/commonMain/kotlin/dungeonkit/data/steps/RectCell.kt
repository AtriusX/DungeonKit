package dungeonkit.data.steps

import dungeonkit.data.Dimension
import dungeonkit.data.Grid
import dungeonkit.data.Room
import dungeonkit.data.steps.modifiers.Modifier
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import dungeonkit.dim
import kotlin.random.Random

open class RectCell(
    private         val cellSize : Dimension = 5.dim,
    private         val roomCount: IntRange  = 6..10,
    private         val floorTile: String    = "floor",
    override vararg val modifiers: Modifier
) : ModifiableStep {

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        if (map.area.h < cellSize.h || map.area.w < cellSize.w)
            throw IllegalStateException("Cannot generate cell grid in map: Too small")

        val viableArea = map.area - cellSize
        val rooms = mutableListOf(Room(viableArea.random(), cellSize, tileMap[floorTile].tile))
        val roomsToCreate = roomCount.random()
        while (rooms.size < roomsToCreate) {
            val room = rooms.random()
            val sides = listOf(room.top, room.left, room.bottom, room.right)
            val index = Random.nextInt(4)
            // TODO: If index is even then it's a top of bottom border, if not then it's
            //       a left or right border. 
        }
        return map
    }

    companion object : RectCell()
}