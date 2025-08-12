package com.back.service

import com.back.WiseSaying
import com.back.repository.WiseSayingRepository

class WiseSayingService {
    private val wiseSayingRepository = WiseSayingRepository()

    fun write(content: String, author: String): WiseSaying {
        val lastId = wiseSayingRepository.getLastId()
        val newId = lastId + 1
        val wiseSaying = WiseSaying(newId, content, author)

        wiseSayingRepository.save(wiseSaying)
        wiseSayingRepository.saveLastId(newId)

        return wiseSaying
    }

    fun list(keywordType: String, keyword: String): List<WiseSaying> {
        val allWiseSayings = wiseSayingRepository.findAll()

        // 검색어가 있을 경우 필터링 수행
        val filteredWiseSayings = if (keyword.isNotBlank()) {
            when (keywordType) {
                "author" -> allWiseSayings.filter { it.author.contains(keyword) }
                "content" -> allWiseSayings.filter { it.content.contains(keyword) }
                else -> allWiseSayings
            }
        } else {
            allWiseSayings
        }

        return filteredWiseSayings.sortedByDescending { it.id }
    }

    fun findById(id: Int): WiseSaying? {
        return wiseSayingRepository.findById(id)
    }

    fun remove(id: Int): Boolean {
        val wiseSaying = findById(id)
        if (wiseSaying != null) {
            wiseSayingRepository.delete(id)
            return true
        }
        return false
    }

    fun modify(id: Int, newContent: String, newAuthor: String): Boolean {
        val wiseSaying = findById(id)
        if (wiseSaying != null) {
            wiseSaying.content = newContent
            wiseSaying.author = newAuthor
            wiseSayingRepository.save(wiseSaying)
            return true
        }
        return false
    }

    fun build() {
        val wiseSayings = wiseSayingRepository.findAll()
        wiseSayingRepository.saveAllToJson(wiseSayings)
    }
}
