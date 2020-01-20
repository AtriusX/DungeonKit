package dungeonkit.data.steps

import dungeonkit.DungeonKit.random
import dungeonkit.data.Direction
import dungeonkit.data.Grid
import dungeonkit.data.tiles.Tile
import dungeonkit.data.tiles.binding.TileMap
import kotlin.math.min

/**
 * This is an implementation for an algorithm that generates data through the use of
 * wanderers. The aim of this algorithm is to facilitate easy creation of more natural
 * looking dungeons through the use of random step tracking.
 *
 * @property wanderers       The number of wanderers to run in this algorithm. Increasing this
 *                           will allow more parallel tunnels to be made. This of course can
 *                           have the effect of creating unconnected areas on the map.
 * @property maxLifetime     The maximum number of [Tiles][Tile] each runner is permitted to
 *                           traverse before being killed. The amount of tiles traversed for
 *                           each runner will be set as a random value between this and half
 *                           this value.
 * @property allowSeparation This determines whether or not this algorithm is permitted to
 *                           allow runners to run on their own. This allows them the ability
 *                           to select start coordinates that are independent of already generated
 *                           tiles. This has the effect of potentially creating sections of
 *                           terrain that are disconnected from one another.
 * @property newTileBias     Tells the wanderers how often to prefer new tiles compared to
 *                           existing ones. This value can range from 0 to 1. Setting this
 *                           to 0 ignores tile status completely, while setting it to 1 will
 *                           always choose new tiles.
 * @property maxRetries      The amount of retry attempts allowed for each step a wanderer takes.
 *                           If the runner is unable to find a new tile after this number of
 *                           attempts, the process ends and the runner is killed.
 * @constructor              Constructs a mindless wanderer algorithm with the applied settings.
 */
open class MindlessWanderer(
    private val wanderers      : Int     = 10,
    private val maxLifetime    : Int     = 100,
    private val allowSeparation: Boolean = false,
    private val newTileBias    : Double  = 0.5,
    private val maxRetries     : Int     = 50
) : Step {
    override val status: String
        get() = "Wandering..."

    override fun process(map: Grid<Tile>, tileMap: TileMap<*>): Grid<Tile> {
        for (i in 0..wanderers) {
            Wanderer(random.nextInt(maxLifetime / 2, maxLifetime))
                .wander(map, tileMap)
        }
        return map
    }

    /**
     * Internal wanderer abstraction. This object stores the current location, lifespan,
     * and life status for each wanderer. A wanderer will continue to run until it is
     * considered dead.
     *
     * @property lifespan The current lifespan of the wanderer.
     * @constructor       Constructs a new wanderer instance.
     */
    private inner class Wanderer(
        private var lifespan: Int
    ) {
        fun wander(map: Grid<Tile>, tileMap: TileMap<*>) {
            // Get the wanderers initial position
            val pos = when {
                allowSeparation -> map.area.random()
                map.isEmpty()   -> map.area.center
                else            -> map.map { it.key }.run { get(random.nextInt(size)) }
            }
            // Start the wanderers lifespan
            repeat(lifespan) {
                // Set the current tile as the primary
                map[pos.x, pos.y] = tileMap.primary.tile
                for (tries in 0..maxRetries) {
                    // Get the direction and the relative coordinate
                    val direction = Direction.RANDOM
                    val rel       = direction.rel + pos
                    // Kill the process if the wanderer fails to move after reaching the max retry count
                    if (tries == maxRetries)
                        return
                    // Retry if the tile isn't in the map, or if it is and the tile bias check fails
                    if (rel !in map
                        || (map[rel.x, rel.y] == tileMap.primary.tile
                        && random.nextDouble() < min(newTileBias, 1.0))
                    ) continue
                    // Move the wanderer
                    pos move direction
                    break
                }
            }
        }
    }

    /**
     * Provides a default implementation for us to use.
     */
    companion object : MindlessWanderer()
}