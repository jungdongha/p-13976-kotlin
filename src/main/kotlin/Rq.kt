package com.back

class Rq(command: String) {
    val action: String
    private val paramsMap: Map<String, String>

    init {
        val commandBits = command.split("?", limit = 2)
        action = commandBits[0].trim()

        if (commandBits.size > 1) {
            val queryString = commandBits[1]
            paramsMap = queryString.split("&").associate {
                val (key, value) = it.split("=", limit = 2)
                key to value
            }
        } else {
            paramsMap = emptyMap()
        }
    }

    fun getParamValueAsInt(name: String, defaultValue: Int): Int {
        return paramsMap[name]?.toIntOrNull() ?: defaultValue
    }

    fun getParamValue(name: String, defaultValue: String): String {
        return paramsMap[name] ?: defaultValue
    }
}
