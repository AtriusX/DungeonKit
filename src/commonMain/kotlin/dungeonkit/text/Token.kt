package dungeonkit.text

internal class Token private constructor(
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