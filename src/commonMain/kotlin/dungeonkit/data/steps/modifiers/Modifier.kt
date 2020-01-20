package dungeonkit.data.steps.modifiers

/**
 * Designates a generation modifier. Any class that extends this interface may be used as
 * a modifier in the generator. Modifiers **should not** however implement this interface
 * directly. Classes should instead implement extensions of this interface. The purpose of
 * this is to enable data-specific use cases within different steps.
 */
interface Modifier {

    /**
     * @property status Describes what the modifier is doing. Can be used to relay information
     *                  back to the user.
     */
    val status: String
        get() = "Modifying generation..."
}
