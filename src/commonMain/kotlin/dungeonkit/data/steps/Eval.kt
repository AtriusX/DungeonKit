package dungeonkit.data.steps

import dungeonkit.*
import dungeonkit.data.Coordinate
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap

/**
 * @warn This step is unsafe for use on native platforms, please avoid using it
 *       for this purpose.
 *
 * This allows for simple custom code evaluations to be done within the step chain.
 * Evaluations can be done over a selected area noted between two [coordinates][Coordinate].
 *
 * @property pos1 The top-left corner of the evaluation region.
 * @property pos2 The bottom-right corner of the evaluation region.
 * @property eval The function to run for the entire grid, this method passes the position
 *                and tile value as parameters for consideration. The result is allowed to
 *                return null as a way of telling the system to skip this tile.
 * @constructor   Constructs a new simple code evaluator.
 */
class Eval(
    private val pos1: Coordinate = 0.pos,
    private val pos2: Coordinate = Int.MAX_VALUE.pos,
    private val eval: (Int, Int, Tile) -> Tile?
) : Step {
    override val status: String
        get() = "Evaluating expression..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        // Skip this step completely since it seems to cause problems with kotlin native
        if (platform == "Native") {
            Log.info("Skipped evaluation: Not supported on Native")
            return map
        }
        val (w, h) = map.area
        // Loop over the entire grid (this does a lot to ensure the range is always valid)
        for (x in minClamp(pos1.x, pos2.x, w) until maxClamp(pos1.x, pos2.x, w))
        for (y in minClamp(pos1.y, pos2.y, h) until maxClamp(pos1.y, pos2.y, h))
            // Assign all tiles according to this evaluation, or skip if null
            map[x, y] = eval(x, y, map[x, y]) ?: continue
        return map
    }
}