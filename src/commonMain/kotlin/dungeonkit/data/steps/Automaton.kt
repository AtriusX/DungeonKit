package dungeonkit.data.steps

import dungeonkit.DungeonKit.random
import dungeonkit.data.Direction
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

open class Automaton(
    private val generations: Int    = 20,
    private val fillRatio  : Double = 0.25,
    private val threshold  : Int    = 4,
    private val aliveState : String = "floor",
    private val deadState  : String = "wall"
) : Step {
    override val status: String
        get() = "Mutating..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        val (width, height) = map.area
        val alive           = tileMap[aliveState]
        val dead            = tileMap[deadState]
        for (y in 0 until height) for (x in 0 until width)
            if (random.nextDouble() > fillRatio)
                map[x, y] = alive.tile
        return generation(map, alive.tile, dead.tile)
    }

    private fun generation(map: Grid<Tile>, alive: Tile, dead: Tile): Grid<Tile> {
        var current = map.copy()
        for (i in 0 until generations) {
            val temp = current.copy()
            for (tile in current) {
                var count = 0
                for (direction in Direction) {
                    val rel = tile.key.relative(direction)
                    if (rel in current && temp[rel.x, rel.y] == alive)
                        count++
                }
                val (x, y) = tile.key
                current[x, y] = if (count < threshold) dead else alive
            }
            current = temp
        }
        return current
    }

    companion object : Automaton()
}