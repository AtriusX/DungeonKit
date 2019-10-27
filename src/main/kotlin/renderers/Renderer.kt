package renderers

import Dungeon
import TileMap

interface Renderer {

    /**
     * Renders a dungeon out to the renderer's implemented context.
     *
     * @param dungeon The dungeon to render.
     */
    fun render(dungeon: Dungeon, tiles: TileMap)
}