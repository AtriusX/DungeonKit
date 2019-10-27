package generators

import Tile
import TileMap

/**
 * Class description for tilemap generators. Properties should
 * be introduced to subclasses of this interface as needed as
 * a way to introduce external parameters to the generator.
 */
interface Generator {

    /**
     * Generates a TileMap of the given height and width; using a default tile.
     *
     * @param height  The height of the generated TileMap.
     * @param width   The width of the generated TileMap.
     * @param default The default tile used in generation.
     */
    fun generate(height: Int, width: Int, default: Tile): TileMap {
        return TileMap(height, width, default)
    }
}