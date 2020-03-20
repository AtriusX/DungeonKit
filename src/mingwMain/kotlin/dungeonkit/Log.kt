package dungeonkit

actual object Log {
    actual fun info(message: Any?)  = "[I] $message".log()
    actual fun warn(message: Any?)  = "[W] $message".log()
    actual fun error(message: Any?) = "[E] $message".log()
}