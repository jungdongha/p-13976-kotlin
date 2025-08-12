package com.back.repository
import com.back.WiseSaying
import java.io.File

class WiseSayingRepository {
    private val dbDir = "db/wiseSaying"

    init {
        File(dbDir).mkdirs()
    }

    fun findById(id: Int): WiseSaying? {
        val file = File("$dbDir/$id.json")
        if (!file.exists()) {
            return null
        }

        val jsonStr = file.readText()
        val idMatch = """"id": (\d+)""".toRegex().find(jsonStr)
        val contentMatch = """"content": "([^"]*)"""".toRegex().find(jsonStr)
        val authorMatch = """"author": "([^"]*)"""".toRegex().find(jsonStr)

        val foundId = idMatch?.groupValues?.get(1)?.toInt() ?: return null
        val content = contentMatch?.groupValues?.get(1) ?: ""
        val author = authorMatch?.groupValues?.get(1) ?: ""

        return WiseSaying(foundId, content, author)
    }

    fun findAll(): List<WiseSaying> {
        val wiseSayings = mutableListOf<WiseSaying>()
        val dbDirFile = File(dbDir)
        val jsonFiles = dbDirFile.listFiles { _, name -> name.endsWith(".json") } ?: emptyArray()

        for (file in jsonFiles) {
            val id = file.nameWithoutExtension.toIntOrNull() ?: continue
            findById(id)?.let { wiseSayings.add(it) }
        }
        return wiseSayings
    }

    fun save(wiseSaying: WiseSaying) {
        File("$dbDir/${wiseSaying.id}.json").writeText(wiseSaying.toJson())
    }

    fun delete(id: Int) {
        File("$dbDir/$id.json").delete()
    }

    fun getLastId(): Int {
        val lastIdFile = File("$dbDir/lastId.txt")
        return if (lastIdFile.exists()) {
            lastIdFile.readText().toIntOrNull() ?: 0
        } else {
            0
        }
    }

    fun saveLastId(id: Int) {
        File("$dbDir/lastId.txt").writeText(id.toString())
    }

    fun saveAllToJson(wiseSayings: List<WiseSaying>) {
        val jsonStr = wiseSayings
            .map { it.toJson() }
            .joinToString(",\n")
        val finalJson = "[\n$jsonStr\n]"
        File("data.json").writeText(finalJson)
    }
}
