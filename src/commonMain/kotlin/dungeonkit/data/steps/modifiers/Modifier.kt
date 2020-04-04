package dungeonkit.data.steps.modifiers

import dungeonkit.DungeonProcess

/**
 * Designates a generation modifier. Any class that extends this interface may be used as
 * a modifier in the generator. Modifiers **should not** however implement this interface
 * directly. Classes should instead implement extensions of this interface. The purpose of
 * this is to enable data-specific use cases within different steps.
 */
interface Modifier : DungeonProcess {

    /**
     * @property status Describes what the modifier is doing. Can be used to relay information
     *                  back to the user.
     */
    override val status: String
        get() = "Modifying generation..."
}
