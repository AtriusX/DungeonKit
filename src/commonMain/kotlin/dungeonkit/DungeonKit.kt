package dungeonkit

import dungeonkit.data.Dimension
import dungeonkit.data.by
import dungeonkit.data.tiles.binding.TileMap
import kotlin.properties.Delegates.observable
import kotlin.random.Random
import kotlin.random.Random.Default.nextInt

/**
 * The entry-point for creating dungeons with this library. This object also keeps track
 * of several bit of useful data including the **seed** and global [Random] instance.
 *
 * @author [Atrius][https://github.com/AtriusX]
 *
 * TODO : Compounder Step for merging multiple maps together?
 */
@Suppress("MemberVisibilityCanBePrivate")
object DungeonKit {

    /**
     * @property seed The current dungeon seed for this generator. This property automatically
     *                updates the [Random] instance to reflect the current seed. This value
     *                **cannot** be reassigned outside this object.
     */
    var seed: Int by observable(nextInt()) { _, _, new -> random = Random(new) }
        private set

    /**
     * @property random The current stored [Random] instance. Automatically updated by the seed
     *                  property as it gets updated. This value **cannot** be reassigned outside
     *                  this object.
     */
    var random: Random = Random(seed)
        private set

    init {
        // Log the startup of the library
        Log.info("Launching DungeonKit on target: $platform")
    }

    /**
     * Creates a new dungeon with an optional set of metadata. Use of at least the
     * **name** and **dimension** properties is *greatly* recommended, though not
     * required.
     *
     * @param name      The name of the dungeon.
     * @param dimension The height and width of the dungeon.
     * @param seed      The seed used in generating maps for this dungeon.
     * @return          The newly created [Dungeon] instance.
     */
    fun <T : TileMap<*>> create(
        name     : String    = "Default Dungeon",
        dimension: Dimension = 50 by 30,
        tileMap  : T,
        seed     : Int?      = null
    ) = Dungeon(name, dimension, tileMap).also {
        // Update the seed if it's not null
        if (seed != null) this.seed = seed
    }
}

/**
 * Notates the platform the library is currently loaded on.
 */
expect val platform: String