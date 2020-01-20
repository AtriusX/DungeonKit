package dungeonkit

/**
 * Base for dungeon generation process. Any class that extends this interface may be
 * used to process dungeon information.
 */
interface DungeonProcess {

    /**
     * @property status Describes what this process is doing. Can be used to relay
     *                  information back to the user.
     */
    val status: String
}