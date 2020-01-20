package dungeonkit.data.steps.modifiers

import dungeonkit.data.Grid
import dungeonkit.data.Room
import dungeonkit.data.tiles.Tile

/**
 * This interface describes a grid modifier that requests [Room] context. The data provided
 * in this context allows us to manipulate rooms directly in the generator rather than needing
 * to rely on potentially slower sequential steps.
 */
interface RoomModifier : Modifier {

    /**
     * Modifies a step that provides [Room] data for it's map [Grid].
     *
     * @param map   The [Grid] data provided by the [dungeonkit.data.steps.ModifiableStep].
     * @param rooms The [Room] data provided for the [Grid].
     * @return      The modified map data.
     */
    fun modify(map: Grid<Tile>, rooms: List<Room>): Grid<Tile>
}