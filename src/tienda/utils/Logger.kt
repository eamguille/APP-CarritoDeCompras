package tienda.utils

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SimpleLogger(private val fileName: String = "app.log") {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val lock = Any()
    private val file = File(fileName).apply { if (!exists()) createNewFile() }

    private fun writeLine(level: String, mensaje: String) {
        val linea = "[${LocalDateTime.now().format(formatter)}] [$level] $mensaje\n"
        synchronized(lock) { file.appendText(linea) }
    }

    fun info(msg: String) = writeLine("INFO", msg)
    fun error(msg: String) = writeLine("ERROR", msg)
}