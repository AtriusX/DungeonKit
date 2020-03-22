package dungeonkit.text

object Title {
    // Provided default formatters
    private val defaults = arrayOf(
        "%4na, The %a %no of %3na",
        "%a %no"
    )

    fun generate(format: String = defaults.random()) =
        format.split(" ").joinToString(" ") {
            Token.parse(it)?.run {
                when (token) {
                    "na" -> Selector.NAME(count)
                    "no" -> Selector.NOUN()
                    "a"  -> Selector.ADJECTIVE()
                    "r"  -> Selector.RANDOM(count)
                    else -> null
                }?.build()
            } ?: it
        }

    private data class Token(
        val count: Int,
        val token: String
    ) {
        companion object {
            fun parse(input: String): Token? {
                // Match the sections within the input if it matches
                val groups = "(?:%)([0-9]*)([a-z]+)".toRegex()
                    .find(input)?.groupValues ?: return null
                return Token(groups[1].toIntOrNull() ?: 1, groups[2])
            }
        }
    }
}
