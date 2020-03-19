package dungeonkit

actual object Log {

    actual fun info(message: Any?) =
        console.info("$prefix $message")

    actual fun warn(message: Any?) =
        console.warn("$prefix $message")

    actual fun error(message: Any?) =
        console.error("$prefix $message")
}