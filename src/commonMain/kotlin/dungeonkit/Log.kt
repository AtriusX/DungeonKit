package dungeonkit

const val prefix: String = "[dK]"

expect object Log {

    fun info(message: Any?)

    fun warn(message: Any?)

    fun error(message: Any?)
}