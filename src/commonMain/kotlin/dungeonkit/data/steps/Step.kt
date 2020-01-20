package dungeonkit.data.steps

import dungeonkit.DungeonProcess
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

/**
 * This interface describes classes that can be used to generate new dungeon data,
 * or modify existing data. Steps implementing this interface are not likely *modifiable*.
 * To allow for further manipulation of steps, look at implementing [ModifiableStep] instead.
 */
interface Step : DungeonProcess {

    override val status: String
        get() = "Generating..."

    /**
     * Takes the input grid and processes it into another grid which will be either output
     * to the user or fed to the next step in the chain. Any step can be the first in line,
     * an empty grid will be supplied in the event no step precedes this one.
     *
     * @param map The input map to process.
     * @return    The processed map.
     */
    fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile>
}
