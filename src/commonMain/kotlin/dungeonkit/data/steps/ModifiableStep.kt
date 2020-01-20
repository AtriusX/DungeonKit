package dungeonkit.data.steps

import dungeonkit.data.steps.modifiers.Modifier

/**
 * Indicates that a step can be modified with other internal steps. This could allow
 * for more fine-tuned control of the generator system.
 */
interface ModifiableStep : Step {

    /**
     * @property modifiers A list of modifiers provided for the step. The user is allowed
     *                     to provide their own step-specific modifier implementations in
     *                     order to provide specialized steps.
     */
    val modifiers: Array<out Modifier>?
}
