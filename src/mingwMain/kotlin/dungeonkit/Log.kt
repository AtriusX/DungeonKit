package dungeonkit

/**
 * @suppress Native implementation is unable to utilize the [IO][kotlin.io]
 *           package currently. So the native branch can't support logging
 *           through this API. println() should be utilized where possible
 *           instead.
 */
actual object Log {
    actual fun info(message: Any?)  = Unit
    actual fun warn(message: Any?)  = Unit
    actual fun error(message: Any?) = Unit
}