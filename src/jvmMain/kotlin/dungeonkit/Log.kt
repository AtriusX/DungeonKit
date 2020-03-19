package dungeonkit

import java.util.logging.Logger


actual object Log {
    private val logger: Logger =
        Logger.getLogger(prefix)

    actual fun info(message: Any?) =
        logger.info(message.toString())

    actual fun warn(message: Any?) =
        logger.warning(message.toString())

    actual fun error(message: Any?) =
        logger.severe(message.toString())
}