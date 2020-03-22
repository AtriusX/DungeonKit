package dungeonkit.text

object Title {
    private val defaults = arrayOf(
        "%4na, The %a %no of %3na",
        "%a %no"
    )

    fun generate(format: String = defaults.random()) =
        format.split(" ").map {
            Token.parse(it)?.run {
                when(token) {
                    "na" -> Selector.NAME(count)
                    "no" -> Selector.NOUN()
                    "a"  -> Selector.ADJECTIVE()
                    else -> null
                }?.build()
            } ?: it
        }.joinToString(" ")

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

    private sealed class Selector(
        vararg val data: String
    ) {
        open fun build() = data.random()

        class NAME(
            private val    count: Int,
                    vararg data : String
        ) : Selector(
            "se", "tu", "xa", "eiq", "aw", "ret", "wer", "xun", "op",
            "slb", "ijv", "oj", "jo", "owd", "ih", "ou", "qin", "mo", "ku",
            "ur", "io", "xe", *data
        ) {
            override fun build() = Array(count) { data.random() }.joinToString("")
        }

        class NOUN(
            vararg data: String
        ) : Selector(
            "cavern", "fields", "keep", "passageway", "hall", "lake", "hive", "dimension", *data
        )

        class ADJECTIVE(
            vararg data: String
        ) : Selector(
            "hidden", "serene", "wandering", "pleasant", "white", "violent", *data
        )
    }
}
