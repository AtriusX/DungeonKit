package dungeonkit

actual object Log {
    actual fun info(message: Any?)  = Unit
    actual fun warn(message: Any?)  = Unit
    actual fun error(message: Any?) = Unit
}