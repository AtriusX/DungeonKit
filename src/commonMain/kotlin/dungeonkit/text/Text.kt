package dungeonkit.text

object Text {
    private val defaults = arrayOf(
        "%4na, The %a %no of %3na"
    )

    fun generate(format: String = defaults.random()) =
        format.toLowerCase().split(" ").map {
            val (count, token) = Token.parse(it) ?: return@map it
            when(token) {
                "na" -> Selector.NAME(count)
                "no" -> Selector.NOUN()
                "a"  -> Selector.ADJECTIVE()
                else -> null
            }?.build() ?: it
        }.joinToString(" ")

    private data class Token(
        val count: Int,
        val token: String
    ) {
        companion object {
            fun parse(input: String): Token? {
                val groups = "(?:%)([0-9]*)([a-z]+)".toRegex()
                    .find(input)?.groupValues ?: return null
                val count = if (groups[1].isNotEmpty())
                    groups[1].toInt() else 1
                return Token(count, groups[2])
            }
        }
    }
}

sealed class Selector(
    vararg val data: String
) {

    open fun build() =
        data.random()

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