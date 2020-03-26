package dungeonkit.text

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

    class RANDOM(
        count: Int,
        chars: Array<Char> = ('a'..'z').toList().toTypedArray()
    ) : Selector(
        Array(count) { chars.random() }.joinToString("")
    )
}