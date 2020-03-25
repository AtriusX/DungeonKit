package dungeonkit.text

object Title {
    // Provided default formatters
    private val defaults = arrayOf(
        "%4na, The %a %no of %3na",
        "%a %no"
    )

    fun generate(format: String = defaults.random()) = format.split(" ").joinToString(" ") {
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
}
