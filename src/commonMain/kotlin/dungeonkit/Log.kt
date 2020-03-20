package dungeonkit

const val prefix: String = "[dK]"

/**
 * A multi-platform logging implementation, this is used to incorporate
 * logging across the entire API since kotlin-multiplatform doesn't offer
 * direct support for cross-platform logging.
 */
expect object Log {

    /**
     * Logs an info message to the platform's log output.
     *
     * @param message The message to pass to the logger.
     */
    fun info(message: Any?)

    /**
     * Logs a warning message to the platform's log output.
     *
     * @param message The message to pass to the logger.
     */
    fun warn(message: Any?)

    /**
     * Logs an error message to the platform's log output.
     *
     * @param message The message to pass to the logger.
     */
    fun error(message: Any?)
}

/**
 * This is a workaround method used to support logging on Native since
 * the native library doesn't have access to [kotlin.io]. This can be
 * used within the library for testing purposes, though it should be
 * expected that this function may disappear over time should the
 * workaround no longer be needed.
 *
 * @receiver The object that will be printed.
 */
internal fun Any?.log(): Unit = println(this)